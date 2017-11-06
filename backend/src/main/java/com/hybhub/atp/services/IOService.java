package com.hybhub.atp.services;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.function.Function;

public interface IOService {

    String PLAYERS_FILE = "players.json";
    String TOURNAMENTS_FILE = "tournaments.json";
    String MATCHES_FILE = "matches.json";
    String RANKINGS_FILE = "rankings%d%02d%02d.json";

    Function<LocalDate, String> FORMAT_RANKINGS_DATE = (d) ->
        String.format(RANKINGS_FILE, d.getYear(), d.getMonthValue(), d.getDayOfMonth());

    ObjectMapper OBJECT_MAPPER = new ObjectMapper(){{
       this.findAndRegisterModules();
    }};


    default ObjectMapper getObjectMapper(){
        return OBJECT_MAPPER;
    }
}
