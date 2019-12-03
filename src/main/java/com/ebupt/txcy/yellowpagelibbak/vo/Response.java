package com.ebupt.txcy.yellowpagelibbak.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Response<T> {
    
    private String code;
    
    private String msg;

    private T data;

    //新添加的响应参数,符合条件的数量
    private Long count;
    private String token;
    
	public Response(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
    
    public static Response<Void> ok() {
        return ok(null);
    }

    public static <T> Response<T> ok(T data) {
        return ok(data, null,null);
    }
    
    public static <T> Response<T> ok(T data, String token) {
        ResponseResult result = ResponseResult.OK;
        return new Response<T>(result.getCode(), result.getMsg(), data,null, token);
    }
    public static <T> Response<T> ok(T data, String token,Long count) {
        ResponseResult result = ResponseResult.OK;
        return new Response<T>(result.getCode(), result.getMsg(), data,count, token);
    }
    public static <T> Response<T> ok(T data, Long count) {
        ResponseResult result = ResponseResult.OK;
        return new Response<T>(result.getCode(), result.getMsg(), data,count,null);
    }
    public static <T> Response<T> formatError(String desc) {
    	ResponseResult result = ResponseResult.PARAMETER_ERROR;
        return new Response<T>(result.getCode(), desc);
    }
    
    public static Response<Void> error(String code, String desc) {
        return new Response<Void>(code, desc, null, null,null);
    }

    public static Response<Void> error(String desc) {
        return error(ResponseResult.SERVER_ERROR.getCode(), desc);
    }

    public static Response<Void> error(ResponseResult responseResult) {
        return error(responseResult.getCode(), responseResult.getMsg());
    }


    
    
}
