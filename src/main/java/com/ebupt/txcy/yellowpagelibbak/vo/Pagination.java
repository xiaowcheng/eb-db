package com.ebupt.txcy.yellowpagelibbak.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Pagination<T> {
    
    private long count;
    
    private List<T> list;
    
}
