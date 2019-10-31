package com.ebupt.txcy.yellowpagelibbak.service.impl;

import java.util.Date;
import java.util.List;
import com.ebupt.txcy.serviceapi.entity.Yellowpagelibbak;
import com.ebupt.txcy.serviceapi.entity.YellowpagelibbakId;
import com.ebupt.txcy.serviceapi.vo.Pagination;
import com.ebupt.txcy.yellowpagelibbak.config.MyConfig;

import com.ebupt.txcy.yellowpagelibbak.exception.ServiceException;
import com.ebupt.txcy.yellowpagelibbak.repository.YellowpagelibbakRepository;
import com.ebupt.txcy.yellowpagelibbak.service.YellowpagelibbakService;

import com.ebupt.txcy.yellowpagelibbak.utils.CommonUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class YellowpagelibbakServiceImpl implements YellowpagelibbakService {
	@Autowired
	private YellowpagelibbakRepository yellowpagelibbakRepository;
	@Autowired
	private MyConfig myConfig;
	@Override
	public Pagination<Yellowpagelibbak> searchNumberList(String phoneCondition, Integer start, Integer pageSize) {
		List<Yellowpagelibbak>  content = null;
		Pagination<Yellowpagelibbak> pagination = new Pagination<>();
		if(start ==null){
			content = yellowpagelibbakRepository.findAll();
			pagination.setList(content);
			pagination.setCount(content.size());
		}
		else{
			Pageable pageable = PageRequest.of(start, pageSize);
			Specification<Yellowpagelibbak> specification = (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("phoneNumber"), "%" + phoneCondition + "%");
			Page<Yellowpagelibbak> page = yellowpagelibbakRepository.findAll(specification, pageable);
			content = page.getContent();
			pagination.setList(content);
			pagination.setCount(content.size());
		}
		return pagination;
	}

	@Override
	public void delNumber(String phoneNumber, Integer sourceId) {
		YellowpagelibbakId yellowpagelibbakId = new YellowpagelibbakId();
		yellowpagelibbakId.setPhoneNumber(phoneNumber);
		yellowpagelibbakId.setSourceId(sourceId);
		yellowpagelibbakRepository.deleteById(yellowpagelibbakId);
	}
	@Override
	public void delNumber(String phoneNumber) {
		yellowpagelibbakRepository.deleteByPhoneNumber(phoneNumber);

	}
	@Override
	public HSSFWorkbook exportNumbers(String phoneNumber,String timeArea) {
	
		List<Yellowpagelibbak> yellowpagelibbaks = null;
		if(CommonUtils.isNotBlank(phoneNumber)&&CommonUtils.isNotBlank(timeArea)) {
			yellowpagelibbaks = yellowpagelibbakRepository.findByPhoneNumberAndCreateTime(phoneNumber,timeArea);
		}else if(CommonUtils.isNotBlank(phoneNumber)&&CommonUtils.isBlank(timeArea)) {
			yellowpagelibbaks = yellowpagelibbakRepository.findByPhoneNumber(phoneNumber);
		}else if(CommonUtils.isNotBlank(timeArea)&&CommonUtils.isBlank(phoneNumber)) {
			yellowpagelibbaks = yellowpagelibbakRepository.findByCreateTime(timeArea);
		}else {
			yellowpagelibbaks = yellowpagelibbakRepository.findAll();
		}
		return doSetHSSFWorkbook(yellowpagelibbaks);
	
	}

	@Override
	public Yellowpagelibbak searchNumber(String phoneCondition) {
		return yellowpagelibbakRepository.getByPhoneNumber(phoneCondition);
	}

	@Override
	public void addNumber(Yellowpagelibbak yellowpagelibbak) {
		yellowpagelibbakRepository.save(yellowpagelibbak);
	}

	@Override
	public void updateNumber(Yellowpagelibbak yellowpagelibbak) {
		Yellowpagelibbak yellowpagelibbaked = yellowpagelibbakRepository.getByPhoneNumber(yellowpagelibbak.getPhoneNumber());
		if(yellowpagelibbaked == null){
			throw new ServiceException("该号码不存在");
		}
		//更新
		if(yellowpagelibbak.getClassAType()!=null){
			yellowpagelibbaked.setClassAType(yellowpagelibbak.getClassAType());
		}
		if(yellowpagelibbak.getClassBType()!=null){
			yellowpagelibbaked.setClassBType(yellowpagelibbak.getClassBType());
		}
		if(CommonUtils.isNotBlank(yellowpagelibbak.getProfession())){
			yellowpagelibbaked.setProfession(yellowpagelibbak.getProfession());
		}
		if(yellowpagelibbak.getSourceId() !=null){
			yellowpagelibbaked.setSourceId(yellowpagelibbak.getSourceId());
		}
		yellowpagelibbakRepository.save(yellowpagelibbaked);
	}

	@Override
	public void addNumbers(List<Yellowpagelibbak> yellowpagelibbaks) {
		yellowpagelibbaks.forEach(yellowpagelibbak -> {
			if(yellowpagelibbak.getCreateTime()==null){
				yellowpagelibbak.setCreateTime(new Date());
			}
			yellowpagelibbakRepository.save(yellowpagelibbak);
		});
	}

	@Override
	@Transactional
	public void updateNumber(List<Yellowpagelibbak> yellowpagelibbaks) {
		yellowpagelibbaks.forEach(yellowpagelibbak -> {
			updateNumber(yellowpagelibbak);
		});
	}

	@Override
	public void delNumber(List<Yellowpagelibbak> yellowpagelibbaks) {
		yellowpagelibbaks.forEach(yellowpagelibbak -> {
			if(yellowpagelibbak.getSourceId()!=null){
				delNumber(yellowpagelibbak.getPhoneNumber(),yellowpagelibbak.getSourceId());
			}else{
				delNumber(yellowpagelibbak.getPhoneNumber());
			}
		});
	}

	public  HSSFWorkbook doSetHSSFWorkbook(List<Yellowpagelibbak>yellowpagelibbaks ){
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("黄页备份单");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("电话号码");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("号码类型");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("行业归属");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("号码详细描述");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("数据来源");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("创建时间");
		cell.setCellStyle(style);
		if(yellowpagelibbaks!=null){
			for (int i = 0; i < yellowpagelibbaks.size(); i++) {
				Yellowpagelibbak yellowpagelibbak = yellowpagelibbaks.get(i);
				row = sheet.createRow((int) i + 1);
				// 第四步，创建单元格，并设置值
				row.createCell(0).setCellValue(yellowpagelibbak.getPhoneNumber());
				row.createCell(1).setCellValue(yellowpagelibbak.getClassAType());
				row.createCell(2).setCellValue(yellowpagelibbak.getProfession());
				row.createCell(3).setCellValue(yellowpagelibbak.getClassBType());
				row.createCell(4).setCellValue(yellowpagelibbak.getSourceId());
				row.createCell(5).setCellValue(CommonUtils.getFormatedTime(yellowpagelibbak.getCreateTime().getTime()));
			}
		}
		return wb;
	}
}
