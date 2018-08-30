package com.docker.compose;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.connection.Container;
import com.palantir.docker.compose.connection.DockerMachine;
import com.palantir.docker.compose.connection.waiting.HealthCheck;
import com.palantir.docker.compose.execution.Docker;
import com.palantir.docker.compose.execution.DockerExecutable;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MySqlDockerServerTest {

  private static String testResource = "src/main/resources/docker-compose-mysql.yml";

  private static String containerName = "mysql";

  public static DockerComposeRule dockerRule = DockerComposeRule.builder().file(testResource)
      .waitingForService(containerName, toPortsOpen())
      .build();

  public static Container container = dockerRule.dockerCompose().container(containerName);

  static DockerMachine dockerMachine = DockerMachine.localMachine().build();
  static Docker docker = new Docker(DockerExecutable.builder().dockerConfiguration(dockerMachine).build());

  public static HealthCheck<Container> toPortsOpen() {
    return Container::areAllPortsOpen;
  }

  public static void main(String[] args) throws Exception {
    try {
      log.info("docker state is {}",container.state());
      log.info("container name is {}",container.getContainerName());
      //docker.rm(containerName);
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
    //testMsqlStarted();
  }

  public static void testMsqlStarted(){
    try {
      container.up();
      log.info("docker state is {}",container.state());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }


  public static void stop(){
    try {
      container.kill();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
