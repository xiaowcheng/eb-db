package com.ebupt.txcy.yellowpagelibbak.dto;


import com.ebupt.txcy.yellowpagelibbak.entity.Yellowpagelibbak;
import lombok.Data;

import java.util.List;

/**
 * yellowpagelibbak的请求体
 */
@Data
public class YellowpagelibbakRequestBody {
    private List<Yellowpagelibbak> phoneList;
}
