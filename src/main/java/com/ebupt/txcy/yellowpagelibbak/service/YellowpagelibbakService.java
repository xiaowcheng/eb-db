package com.ebupt.txcy.yellowpagelibbak.service;

import java.util.List;


import com.ebupt.txcy.serviceapi.Entity.Yellowpagelibbak;

import com.ebupt.txcy.serviceapi.vo.Pagination;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public interface YellowpagelibbakService {

	Pagination<Yellowpagelibbak> searchNumberList(String phoneCondition, Integer startStr, Integer page);


	void delNumber(String phoneNumber,Integer sourceId);
	void delNumber(String phoneNumber);
	HSSFWorkbook exportNumbers(String phoneNumber, String timeArea);

	Yellowpagelibbak searchNumber(String phoneCondition);

	void addNumber(Yellowpagelibbak yellowpagelibbak);

	void updateNumber(Yellowpagelibbak yellowpagelibbak);

    void addNumbers(List<Yellowpagelibbak> yellowpagelibbaks);

	void updateNumber(List<Yellowpagelibbak> yellowpagelibbaks);

	void delNumber(List<Yellowpagelibbak> yellowpagelibbaks);
}
