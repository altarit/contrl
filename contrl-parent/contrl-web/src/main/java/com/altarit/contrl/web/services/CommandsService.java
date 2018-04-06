package com.altarit.contrl.web.services;

import java.util.List;

public interface CommandsService {

    void log(String response);

    List<String> getLog();

    void execute(String s);
}
