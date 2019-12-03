package com.ebupt.txcy.yellowpagelibbak.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhoneListResponse {
    private String phonenumber;
    private String code;
    private String errdesc;
    //0成功，1失败
    public static PhoneListResponse ok(String phonenumber){
        return new PhoneListResponse(phonenumber,"0",null);
    }
    public static PhoneListResponse fail(String phonenumber,String errdesc){
        return new PhoneListResponse(phonenumber,"1",errdesc);
    }
}
