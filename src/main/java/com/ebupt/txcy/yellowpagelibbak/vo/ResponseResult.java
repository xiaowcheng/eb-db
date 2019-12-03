package com.ebupt.txcy.yellowpagelibbak.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ResponseResult {
    
    OK("0000", "成功"),

	SERVER_ERROR("1000", "服务器内部错误，请稍候再试"),
	
	/**
	 * 平台不存在
	 */
	PLATFORM_NOT_EXISTS("1001", "平台信息不存在"),
	
	/**
	 * 名称重复
	 */
	NAME_REPEAT("2001", "名称重复"),
	
	/**
	 * 用户名或密码错误
	 */
	FAIL_AUTHON("4001", "用户名或密码错误"),
	
	/**
	 * 没有操作权限
	 */
	NO_PERMISSION("4006", "您没有操作权限"),
	
	/**
	 * token校验错误
	 */
	TOKEN_ERROR("4002", "token校验错误"),
	
	/**
	 * 参数安全校验错误
	 */
	SECURITY_ERROR("4003", "参数包含非法字符"),
	
	/**
	 * 原密码错误
	 */
	OLDPW_ERROR("4004", "原密码错误"),
	
	/**
	 * 操作员已存在
	 */
	OPERAOTR_EXISTS("4005", "操作员已存在"),
	
	/**
	 * 参数为空
	 */
	PARAMETER_NULL("5001", "缺少请求参数"),
	
	/**
	 * 参数格式错误
	 */
	PARAMETER_ERROR("5002", "参数格式错误"),
	
	/**
	 * 操作员不存在
	 */
	OPERATOR_NOT_EXISTS("6001", "操作员不存在"),

	/**
	 * 存在敏感词
	 */
	SENSITIVE_WORD_EXISTS("9001", "存在敏感词 ");
    
    private String code;
    
    private String msg;
    
}
