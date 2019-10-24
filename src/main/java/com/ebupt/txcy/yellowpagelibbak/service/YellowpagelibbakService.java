package com.ebupt.txcy.yellowpagelibbak.service;

import java.util.List;


import com.ebupt.txcy.serviceapi.Entity.Yellowpagelibbak;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public interface YellowpagelibbakService {

	List<Yellowpagelibbak> searchNumberList(String phoneCondition, Integer startStr);


	void delNumber(String phoneNumber,Integer sourceId);
	void delNumber(String phoneNumber);
	HSSFWorkbook exportNumbers(String phoneNumber, String timeArea);

	Yellowpagelibbak searchNumber(String phoneCondition);

	void addNumber(Yellowpagelibbak yellowpagelibbak);

	void updateNumber(Yellowpagelibbak yellowpagelibbak);
}
