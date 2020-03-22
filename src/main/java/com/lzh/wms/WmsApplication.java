package com.lzh.wms;

import com.lzh.wms.system.service.RoleService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lzh
 */
//@RestController
@SpringBootApplication
@EnableProcessApplication
@MapperScan(basePackages = {"com.lzh.wms.*.mapper"})
public class WmsApplication {
    @Autowired
    private static RuntimeService runtimeService;
    @Autowired
    private static RoleService roleService;
    public static void main(String[] args) {
        SpringApplication.run(WmsApplication.class, args);
        System.out.println(runtimeService);
        System.out.println(roleService);
    }

//    @RequestMapping
//    public String hello(){
//        return "hh";
//    }

}
