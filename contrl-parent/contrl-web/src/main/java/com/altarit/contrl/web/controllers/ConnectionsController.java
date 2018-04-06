package com.altarit.contrl.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/connections")
public class ConnectionsController {

    private static final Logger log = LoggerFactory.getLogger(ConnectionsController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getConnections() {
        List<String> connections = new ArrayList<>();
        return new ResponseEntity<List<String>>(connections, HttpStatus.OK);
    }
}
