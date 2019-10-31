package com.ebupt.txcy.yellowpagelibbak.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.ebupt.txcy.serviceapi.dto.YellowpagelibbakRequestBody;
import com.ebupt.txcy.serviceapi.entity.Yellowpagelibbak;
import com.ebupt.txcy.serviceapi.vo.Pagination;
import com.ebupt.txcy.serviceapi.vo.Response;

import com.ebupt.txcy.yellowpagelibbak.service.YellowpagelibbakService;
import com.ebupt.txcy.yellowpagelibbak.utils.CommonUtils;
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
		String pageStr = input.get("page");
		Integer start = null;
		Integer page =null;
		if(CommonUtils.isNotBlank(startStr)) {
			start = Integer.parseInt(startStr);
		}
		if(CommonUtils.isBlank(phoneCondition)) {
			phoneCondition = "";
		}
		if(CommonUtils.isBlank(pageStr)){
			page = Integer.parseInt(pageStr);
		}
		Pagination<Yellowpagelibbak> pagination = 	yellowpagelibbakService.searchNumberList(phoneCondition,start,page);
		
		return Response.ok(pagination.getList(),pagination.getCount());
	}
	@PostMapping("addNumber")
	public Response addNumber(@RequestBody YellowpagelibbakRequestBody yellowpagelibbakRequestBody) {
		List<Yellowpagelibbak> yellowpagelibbaks = yellowpagelibbakRequestBody.getPhoneList();
		yellowpagelibbaks.forEach(yellowpagelibbak -> {
			if(CommonUtils.isBlank(yellowpagelibbak.getPhoneNumber())) {
				Response.error("缺少phoneNumber参数");
			}
		});
		yellowpagelibbakService.addNumbers(yellowpagelibbaks);
		return Response.ok();
	}
	@PostMapping("updateNumber")
	public Response updateNumber(@RequestBody YellowpagelibbakRequestBody yellowpagelibbakRequestBody) {
		List<Yellowpagelibbak> yellowpagelibbaks = yellowpagelibbakRequestBody.getPhoneList();
		yellowpagelibbaks.forEach(yellowpagelibbak -> {
			if(CommonUtils.isBlank(yellowpagelibbak.getPhoneNumber())) {
				Response.error("缺少phoneNumber参数");
			}
		});

		yellowpagelibbakService.updateNumber(yellowpagelibbaks);
		return Response.ok();
	}
	
	@PostMapping("delNumber")
	public Response delNumber(@RequestBody YellowpagelibbakRequestBody yellowpagelibbakRequestBody) {
		List<Yellowpagelibbak> yellowpagelibbaks = yellowpagelibbakRequestBody.getPhoneList();
		for (Yellowpagelibbak yellowpagelibbak : yellowpagelibbaks) {
			if(yellowpagelibbak.getPhoneNumber()==null){
				return Response.error("缺少phoneNumber参数");
			}
		}
		yellowpagelibbakService.delNumber(yellowpagelibbaks);
		return Response.ok();
	}
	
}
