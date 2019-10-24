package com.ebupt.txcy.yellowpagelibbak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
@EntityScan("com.ebupt.txcy.serviceapi.Entity")
@EnableEurekaClient
@SpringBootApplication
public class MicroserviceYellowpagelibbakServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceYellowpagelibbakServerApplication.class, args);
    }

}
