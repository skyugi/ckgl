package com.lzh.wms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
@SpringBootApplication
@MapperScan(basePackages = {"com.lzh.wms.sys.mapper"})
public class WmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WmsApplication.class, args);
    }

//    @RequestMapping
//    public String hello(){
//        return "hh";
//    }

}
