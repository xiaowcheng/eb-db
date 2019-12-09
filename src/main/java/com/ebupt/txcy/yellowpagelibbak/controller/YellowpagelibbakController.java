package com.ebupt.txcy.yellowpagelibbak.controller;


import java.util.List;
import java.util.Map;


import com.ebupt.txcy.yellowpagelibbak.dto.PhoneListResponse;
import com.ebupt.txcy.yellowpagelibbak.dto.YellowpagelibbakRequestBody;
import com.ebupt.txcy.yellowpagelibbak.entity.Yellowpagelibbak;
import com.ebupt.txcy.yellowpagelibbak.service.YellowpagelibbakService;
import com.ebupt.txcy.yellowpagelibbak.utils.CommonUtils;
import com.ebupt.txcy.yellowpagelibbak.utils.Constants;
import com.ebupt.txcy.yellowpagelibbak.vo.Pagination;
import com.ebupt.txcy.yellowpagelibbak.vo.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("yellowpagelibbak/v1/")
@Api(value = "黄页备份库管理接口",tags = {"提供单个查询，分页查询，批量更新，批量添加，批量删除"})
public class YellowpagelibbakController {
	@Autowired
	private YellowpagelibbakService yellowpagelibbakService;

	@ApiOperation(value = "单个查询",notes = "根据号码进行单个查询")
	@ApiImplicitParam(name = "input",value = "{\"phoneCondition\":\"\"}",dataType = "Map",paramType = "body")
	@PostMapping("searchNumber")
	public Response searchNumber(@RequestBody Map<String,String> input) {
		String phoneCondition = input.get("phoneCondition");
		log.info("接收到参数{}",phoneCondition);
		if(CommonUtils.isBlank(phoneCondition)) {
			return Response.error(Constants.PHOONENUMBER_REPSPONSE_PARAM);
		}
		Yellowpagelibbak yellowpagelibbak  = 	yellowpagelibbakService.searchNumber(phoneCondition);
		log.info("单个数据查询完成：{}",yellowpagelibbak);
		return Response.ok(yellowpagelibbak);
	}
	/**
	 * start:0
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "分页查询",notes = "可根据号码进行分页查询")
	@ApiImplicitParam(name = "input",value = "{\"phoneCondition\":\"\",\"page\":\"\",\"start\":\"\"}",dataType = "Map",paramType = "body")
	@PostMapping("searchNumberList")
	public Response searchNumberList(@RequestBody Map<String,String> input) {
		String phoneCondition = input.get("phoneCondition");
		String startStr = input.get("start");
		String pageStr = input.get("page");
		log.info("接收参数（phoneCondition,start,page）：{}，{}，{}",phoneCondition,startStr,pageStr);
		Integer start = null;
		Integer page =null;
		if(CommonUtils.isNotBlank(startStr)) {
			start = Integer.parseInt(startStr);
		}
		if(CommonUtils.isBlank(phoneCondition)) {
			phoneCondition = "";
		}
		if(CommonUtils.isNotBlank(pageStr)){
			page = Integer.parseInt(pageStr);
		}else{
			return Response.error(Constants.PHOONENUMBER_REPSPONSE_PARAM);
		}
		Pagination<Yellowpagelibbak> pagination = 	yellowpagelibbakService.searchNumberList(phoneCondition,start,page);
		log.info("数据处理完成");
		return Response.ok(pagination.getList(),pagination.getCount());
	}
	@PostMapping("addNumber")
	public Response addNumber(@RequestBody YellowpagelibbakRequestBody yellowpagelibbakRequestBody) {
		log.info("添加数据");
		List<Yellowpagelibbak> yellowpagelibbaks = yellowpagelibbakRequestBody.getPhoneList();
		List<PhoneListResponse> phoneListResponses =  yellowpagelibbakService.addNumbers(yellowpagelibbaks);
		log.info("数据处理完成");
		return Response.ok(phoneListResponses);
	}
	@PostMapping("updateNumber")
	public Response updateNumber(@RequestBody YellowpagelibbakRequestBody yellowpagelibbakRequestBody) {
		log.info("更新数据");
		List<Yellowpagelibbak> yellowpagelibbaks = yellowpagelibbakRequestBody.getPhoneList();
		List<PhoneListResponse> phoneListResponses = yellowpagelibbakService.updateNumber(yellowpagelibbaks);
		log.info("数据处理完成");
		return Response.ok(phoneListResponses);
	}
	
	@PostMapping("delNumber")
	public Response delNumber(@RequestBody YellowpagelibbakRequestBody yellowpagelibbakRequestBody) {
		log.info("删除号码");
		List<Yellowpagelibbak> yellowpagelibbaks = yellowpagelibbakRequestBody.getPhoneList();
		List<PhoneListResponse> phoneListResponses = yellowpagelibbakService.delNumber(yellowpagelibbaks);
		log.info("数据处理完成");
		return Response.ok(phoneListResponses);
	}
	
}
