package com.ebupt.txcy.yellowpagelibbak.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
@Component
@Data
@ConfigurationProperties(prefix="myconfig")
public class MyConfig {
   private  Integer pageSize;
}
