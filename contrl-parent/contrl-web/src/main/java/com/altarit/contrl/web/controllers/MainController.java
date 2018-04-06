package com.altarit.contrl.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    public static final Logger log = LoggerFactory.getLogger(MainController.class);

    @RequestMapping(value="/", method = RequestMethod.GET)
    public ResponseEntity<String> getHome() {
        return new ResponseEntity<String>("Hello", HttpStatus.OK);
    }
}
