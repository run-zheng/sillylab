package com.sillylab.eureka.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DiscoveryClientController {
    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("discoveryClient")
    public String discoveryClient(){
        String services = "Services: " + discoveryClient.getServices()+" service provider";
        log.info(services);
        return services;
    }



}
