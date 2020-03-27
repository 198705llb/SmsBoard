package com.warmme.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class SmsBroadController {

    @GetMapping("/smsList")
    public List<String> smsList(){
        return Arrays.asList("ss","AA");
    }
}
