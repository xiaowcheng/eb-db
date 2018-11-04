package com.ebupt.txcy.yellowpagelibbak.service;

import java.util.List;


import com.ebupt.txcy.serviceapi.dto.PhoneListResponse;
import com.ebupt.txcy.serviceapi.entity.Yellowpagelibbak;

import com.ebupt.txcy.serviceapi.vo.Pagination;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public interface YellowpagelibbakService {

	Pagination<Yellowpagelibbak> searchNumberList(String phoneCondition, Integer startStr, Integer page);


	void delNumber(String phoneNumber,Integer sourceId);
	void delNumber(String phoneNumber);


	Yellowpagelibbak searchNumber(String phoneCondition);


	PhoneListResponse updateNumber(Yellowpagelibbak yellowpagelibbak);

	List<PhoneListResponse>  addNumbers(List<Yellowpagelibbak> yellowpagelibbaks);

	List<PhoneListResponse>  updateNumber(List<Yellowpagelibbak> yellowpagelibbaks);

	List<PhoneListResponse>  delNumber(List<Yellowpagelibbak> yellowpagelibbaks);
}
