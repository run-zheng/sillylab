package com.sillylab.transaction.template.demo.controller;

import com.sillylab.transaction.template.demo.dao.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestRepository repository;

    @GetMapping("test")
    public void test(){
        repository.test();
    }

}
