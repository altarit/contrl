package com.altarit.contrl.web.controllers;

import com.altarit.contrl.web.services.CommandsService;
import com.altarit.contrl.web.services.ConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/commands")
public class CommandsController {

    private static final Logger log = LoggerFactory.getLogger(CommandsController.class);

    @Autowired
    private CommandsService commandsService;

    @RequestMapping(value ="/", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getLog() {
        List<String> log = commandsService.getLog();
        return new ResponseEntity<>(log, HttpStatus.OK);
    }

    @RequestMapping(value ="/", method = RequestMethod.POST)
    public ResponseEntity<List<String>> execute(@RequestBody String command) {
        commandsService.execute(command);
        List<String> log = commandsService.getLog();
        return new ResponseEntity<>(log, HttpStatus.OK);
    }
}
