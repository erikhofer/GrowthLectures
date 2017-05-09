package com.xinra.growthlectures;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.xinra.growthlectures", "asset.pipeline.springboot"})
public class GrowthlecturesApplication {

  public static void main(String[] args) {
    SpringApplication.run(GrowthlecturesApplication.class, args);
  }

}
