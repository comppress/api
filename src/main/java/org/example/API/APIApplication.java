package org.example.API;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
public class APIApplication extends SpringBootServletInitializer {

    Logger logger = LoggerFactory.getLogger(APIApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(APIApplication.class, args);
    }

    @Value( "${personal.settings.time:UTC}" )
    private String timeZone;

    @PostConstruct
    public void init(){
        // Setting Spring Boot SetTimeZone
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
        logger.info("Time: " + new Date());
    }
}
