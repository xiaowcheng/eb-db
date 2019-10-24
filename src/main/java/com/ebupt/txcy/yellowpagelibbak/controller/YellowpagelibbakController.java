package com.ebupt.txcy.yellowpagelibbak.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ebupt.txcy.serviceapi.Entity.Yellowpagelibbak;
import com.ebupt.txcy.serviceapi.vo.Response;

import com.ebupt.txcy.yellowpagelibbak.service.YellowpagelibbakService;
import com.ebupt.txcy.yellowpagelibbak.utils.CommonUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("yellowpagelibbak/v1/")
public class YellowpagelibbakController {
	@Autowired
	private YellowpagelibbakService yellowpagelibbakService;

	@PostMapping("searchNumber")
	public Response searchNumber(@RequestBody Map<String,String> input) {
		String phoneCondition = input.get("phoneCondition");
		if(CommonUtils.isBlank(phoneCondition)) {
			return Response.error("参数为空");
		}
		Yellowpagelibbak yellowpagelibbak  = 	yellowpagelibbakService.searchNumber(phoneCondition);

		return Response.ok(yellowpagelibbak);
	}
	/**
	 * start:0
	 * @param input
	 * @return
	 */
	@PostMapping("searchNumberList")
	public Response searchNumberList(@RequestBody Map<String,String> input) {
		String phoneCondition = input.get("phoneCondition");
		String startStr = input.get("start");
		Integer start = 0;
		if(CommonUtils.isNotBlank(startStr)) {
			start = Integer.parseInt(startStr);
		}
		if(CommonUtils.isBlank(phoneCondition)) {
			phoneCondition = "";
		}
		List<Yellowpagelibbak> lists = 	yellowpagelibbakService.searchNumberList(phoneCondition,start);
		
		return Response.ok(lists);
	}
	@PostMapping("addNumber")
	public Response addNumber(@RequestBody Map<String,Object> input) {

		String phoneNumber = (String) input.get("phoneNumber");
		Integer sourceId =(Integer) input.get("sourceId");
		String classAType =(String) input.get("classAType");
		String profession =(String) input.get("profession");
		String classBType = (String)input.get("classBType");
		String createTimeStr =(String)input.get("createTime");
		if(CommonUtils.isBlank(phoneNumber)||sourceId==null||CommonUtils.isBlank(classAType)||CommonUtils.isBlank(profession)||CommonUtils.isBlank(classBType)) {
			Response.error("缺少参数");
		}
		Date createTime = null;
		if(createTimeStr!=null){
			createTime = CommonUtils.StrToDatess(createTimeStr);
		}
		if(createTime == null){
			createTime = new Date();
		}
		Yellowpagelibbak yellowpagelibbak = new Yellowpagelibbak();
		try {
			BeanUtils.populate(yellowpagelibbak,input);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		yellowpagelibbakService.addNumber(yellowpagelibbak);
		return Response.ok();
		
	}
	@PostMapping("updateNumber")
	public Response updateNumber(@RequestBody Map<String,Object> input) {
		String phoneNumber = (String) input.get("phoneNumber");
		Integer sourceId =(Integer) input.get("sourceId");
		String classAType =(String) input.get("classAType");
		String profession =(String) input.get("profession");
		String classBType = (String)input.get("classBType");
		if(CommonUtils.isBlank(phoneNumber)) {
			Response.error("缺少phoneNumber参数");
		}
		Yellowpagelibbak yellowpagelibbak = new Yellowpagelibbak();
		try {
			BeanUtils.populate(yellowpagelibbak,input);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		yellowpagelibbakService.updateNumber(yellowpagelibbak);
		return Response.ok();
	}
	
	@PostMapping("delNumber")
	public Response delNumber(@RequestBody Map<String,Object> input) {
		String phoneNumber =(String) input.get("phoneNumber");
		Integer sourceId =(Integer) input.get("sourceId");
		if(CommonUtils.isBlank(phoneNumber)) {
			Response.error("缺少phoneNumber参数");
		}
		if(sourceId==null){
			yellowpagelibbakService.delNumber(phoneNumber);
			return Response.ok();
		}
		yellowpagelibbakService.delNumber(phoneNumber,sourceId);
		return Response.ok();
	}
	@PostMapping("exportNumbers")
	public ResponseEntity<byte[]> exportNumbers(@RequestBody Map<String,String>input) throws IOException {
		String phoneNumber = input.get("phoneNumber");
		String timeArea = input.get("timeArea");
		HSSFWorkbook wb = yellowpagelibbakService.exportNumbers(phoneNumber,timeArea);
		byte[] body = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			wb.write(os);
			body = os.toByteArray();
		} catch (IOException e) {
			// 标记出错原因
		} finally {
			os.close();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition","attachment; filename=\"" + "yellowpagelibbak.xls"+"\"");
		headers.add("Content-Type", "text/html;charset=UTF-8");
		HttpStatus statusCode = HttpStatus.OK;
		ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, statusCode);
		return entity;
	}
	
}
