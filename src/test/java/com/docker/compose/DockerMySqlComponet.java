package com.docker.compose;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.connection.Container;
import com.palantir.docker.compose.connection.DockerMachine;
import com.palantir.docker.compose.connection.waiting.HealthCheck;
import com.palantir.docker.compose.execution.Docker;
import com.palantir.docker.compose.execution.DockerExecutable;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DockerMySqlComponet {

  private static String dockerComposePath = "src/main/resources/docker-compose-mysql.yml";

  private static String containerName = "mysql";

  @Bean
  private DockerComposeRule dockerComposeRule(){
    return DockerComposeRule.builder().file(dockerComposePath)
        .waitingForService(containerName, toPortsOpen())
        .build();
  }

  @Bean
  private Container container(){
    return dockerComposeRule().containers().container(containerName);
  }

  @Bean
  private DockerMachine dockerMachine(){
    return DockerMachine.localMachine().build();
  }

  @Bean
  private Docker docker(){
    return new Docker(DockerExecutable.builder().dockerConfiguration(dockerMachine()).build());
  }

  private static HealthCheck<Container> toPortsOpen() {
    return Container::areAllPortsOpen;
  }

  private Container container = container();
  private Docker docker = docker();

  @PostConstruct
  public void init(){
    start();
  }

  /**
   * mysql docker start
   */
  public void start(){
    try {
      container.up();
      log.info("docker state is {}",container.state());
    } catch (Exception e) {
      log.error(e.getMessage());
      try {
        commandStart();
        log.info("docker try start");
      } catch (Exception e1) {
        log.error("docker start error",e1);
      }
    }
  }

  /**
   * docker rm -f mysql
   */
  public void rm(){
    try {
      docker.rm(containerName);
      log.info("container rm complement ");
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
  }

  public void commandStart() throws Exception{
    String commandStart = "docker start mysql";
    Runtime.getRuntime().exec(commandStart);
  }

  /**
   * 用命令行停止
   * @throws Exception
   */
  public void commandStop() throws Exception{
    String commandStart = "docker stop mysql";
    Runtime.getRuntime().exec(commandStart);
  }

}
