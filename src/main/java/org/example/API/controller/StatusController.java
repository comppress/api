package org.example.API.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    public class Status {
        public String status = "Up Version 1.4";
    }

    @GetMapping("/status")
    public Status getStatus(){
        return new Status();
    }

}
