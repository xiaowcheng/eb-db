package com.ebupt.txcy.yellowpagelibbak.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


import com.ebupt.txcy.yellowpagelibbak.dto.PhoneListResponse;
import com.ebupt.txcy.yellowpagelibbak.entity.Yellowpagelibbak;
import com.ebupt.txcy.yellowpagelibbak.entity.YellowpagelibbakId;
import com.ebupt.txcy.yellowpagelibbak.exception.ServiceException;
import com.ebupt.txcy.yellowpagelibbak.repository.YellowpagelibbakRepository;
import com.ebupt.txcy.yellowpagelibbak.service.YellowpagelibbakService;

import com.ebupt.txcy.yellowpagelibbak.utils.CommonUtils;
import com.ebupt.txcy.yellowpagelibbak.utils.Constants;
import com.ebupt.txcy.yellowpagelibbak.utils.EntityParamValidUtil;
import com.ebupt.txcy.yellowpagelibbak.utils.SqlUtil;
import com.ebupt.txcy.yellowpagelibbak.vo.Pagination;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class YellowpagelibbakServiceImpl implements YellowpagelibbakService {
	@Autowired
	private YellowpagelibbakRepository yellowpagelibbakRepository;
	@Autowired
	private JdbcTemplate jdbcTemplate;
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
			pagination.setCount(page.getTotalElements());
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
	public Yellowpagelibbak searchNumber(String phoneCondition) {
		return yellowpagelibbakRepository.getByPhoneNumber(phoneCondition);
	}


	@Override
	@Transactional
	public PhoneListResponse updateNumber(Yellowpagelibbak yellowpagelibbak) {
		String[] strings = EntityParamValidUtil.notPassParamByincludeField(yellowpagelibbak, new String[]{"sourceId","phoneNumber"});
		if(strings.length>0){
			return  PhoneListResponse.fail(yellowpagelibbak.getPhoneNumber(),Constants.PHOONENUMBER_REPSPONSE_PARAM+Arrays.toString(strings));
		}
		YellowpagelibbakId yellowpagelibbakId = new YellowpagelibbakId();
		BeanUtils.copyProperties(yellowpagelibbak,yellowpagelibbakId);
		Yellowpagelibbak yellowpagelibbaked = yellowpagelibbakRepository.getOne(yellowpagelibbakId);
		if(yellowpagelibbaked == null){
			return PhoneListResponse.fail(yellowpagelibbak.getPhoneNumber(),Constants.PHONENUMBER_NOT_EXIT);
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
		if(yellowpagelibbak.getCreateTime() !=null){
			yellowpagelibbaked.setCreateTime(yellowpagelibbak.getCreateTime());
		}
		yellowpagelibbakRepository.save(yellowpagelibbaked);
		return PhoneListResponse.ok(yellowpagelibbak.getPhoneNumber());
	}
	//对sourceId的，phoneNumber做校验
	public PhoneListResponse addNumber(Yellowpagelibbak yellowpagelibbak){
		String[] strings = EntityParamValidUtil.notPassParamByincludeField(yellowpagelibbak, new String[]{"sourceId","phoneNumber"});
		if(strings.length>0){
			return  PhoneListResponse.fail(yellowpagelibbak.getPhoneNumber(),Constants.PHOONENUMBER_REPSPONSE_PARAM+Arrays.toString(strings));
		}
		yellowpagelibbakRepository.save(yellowpagelibbak);
		return  PhoneListResponse.ok(yellowpagelibbak.getPhoneNumber());
	}
	@Override
	@Transactional
	public List<PhoneListResponse>  addNumbers(List<Yellowpagelibbak> yellowpagelibbaks) {
		List<Yellowpagelibbak> dealList = new ArrayList<>();
		List<PhoneListResponse> phoneListResponses = yellowpagelibbaks.stream().map(yellowpagelibbak -> {
			if(yellowpagelibbak.getCreateTime()==null){
				yellowpagelibbak.setCreateTime(new Date());
			}
			String[] strings = EntityParamValidUtil.validEntityInsertArray(yellowpagelibbak);
			if (strings.length > 0) {
				return PhoneListResponse.fail(yellowpagelibbak.getPhoneNumber(), Constants.PHOONENUMBER_REPSPONSE_PARAM + Arrays.toString(strings));
			}else {
				dealList.add(yellowpagelibbak);
			}
			return PhoneListResponse.ok(yellowpagelibbak.getPhoneNumber());
		}).collect(Collectors.toList());
		SqlUtil.executeInsertBatch(jdbcTemplate,dealList);
		return phoneListResponses;
	}

	@Override
	@Transactional
	public List<PhoneListResponse>  updateNumber(List<Yellowpagelibbak> yellowpagelibbaks) {
		List<Yellowpagelibbak> dealList = new ArrayList<>();
		List<PhoneListResponse> phoneListResponses = yellowpagelibbaks.stream().map(yellowpagelibbak -> {
			String[] strings = EntityParamValidUtil.validEntityUpdateArray(yellowpagelibbak);
			if (strings.length > 0) {
				return PhoneListResponse.fail(yellowpagelibbak.getPhoneNumber(), Constants.PHOONENUMBER_REPSPONSE_PARAM + Arrays.toString(strings));
			}else{
				dealList.add(yellowpagelibbak);
			}
			return PhoneListResponse.ok(yellowpagelibbak.getPhoneNumber());
		}).collect(Collectors.toList());

		SqlUtil.executeUpdateBatch(jdbcTemplate,dealList);
		return phoneListResponses;
	}

	@Override
	@Transactional
	public List<PhoneListResponse>  delNumber(List<Yellowpagelibbak> yellowpagelibbaks) {
		List<PhoneListResponse> phoneListResponses = new ArrayList<>();
		yellowpagelibbaks.forEach(yellowpagelibbak -> {
			if(yellowpagelibbak.getPhoneNumber()==null){
				phoneListResponses.add(PhoneListResponse.fail(yellowpagelibbak.getPhoneNumber(),Constants.PHOONENUMBER_REPSPONSE_PARAM+"[phoneNumber]"));
			   return;
			}
			List<Yellowpagelibbak> byPhoneNumber = yellowpagelibbakRepository.findByPhoneNumber(yellowpagelibbak.getPhoneNumber());
			if(byPhoneNumber.isEmpty()){
				phoneListResponses.add(PhoneListResponse.fail(yellowpagelibbak.getPhoneNumber(),Constants.PHONENUMBER_NOT_EXIT));
				return;
			}
			if(yellowpagelibbak.getSourceId()!=null){
				delNumber(yellowpagelibbak.getPhoneNumber(),yellowpagelibbak.getSourceId());
			}else{
				delNumber(yellowpagelibbak.getPhoneNumber());
			}
			phoneListResponses.add(PhoneListResponse.ok(yellowpagelibbak.getPhoneNumber()));
		});
		return phoneListResponses;
	}
}
