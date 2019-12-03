package com.ebupt.txcy.yellowpagelibbak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
@EnableEurekaClient
@SpringBootApplication
public class MicroserviceYellowpagelibbakServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceYellowpagelibbakServerApplication.class, args);
    }

}
