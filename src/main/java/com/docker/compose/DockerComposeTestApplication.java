package com.docker.compose;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class DockerComposeTestApplication {

  public static void main(String[] args) {
    SpringApplication.run(DockerComposeTestApplication.class, args);
  }
}
