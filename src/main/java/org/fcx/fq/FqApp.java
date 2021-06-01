package org.fcx.fq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;
@Slf4j
@EnableScheduling
@SpringBootApplication
public class FqApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(FqApp.class,args);
        String[] activeProfiles = ac.getEnvironment().getActiveProfiles();
        log.info("activeProfiles = {}", Arrays.toString(activeProfiles));
    }

}