package com.anji.sp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.anji.sp.mapper")
public class SpAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpAppApplication.class, args);
    }
}
