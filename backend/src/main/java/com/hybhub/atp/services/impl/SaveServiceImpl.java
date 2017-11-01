package com.hybhub.atp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hybhub.atp.Player;
import com.hybhub.atp.Tournament;
import com.hybhub.atp.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveServiceImpl implements SaveService {

    private final static Logger LOGGER = Logger.getLogger(SaveServiceImpl.class.getName());

    @Autowired
    AtpService atpService;

    @Autowired
    LoadPlayerService loadPlayerService;

    @Autowired
    LoadTournamentService loadTournamentService;

    @Autowired
    LoadMatchService loadMatchService;

    @Override
    public boolean savePlayersToFile(final boolean append) {
        if(append){
            loadPlayerService.loadPlayersFromFile();
        }
        return saveToFile(atpService.getAtpPlayers(), PLAYERS_FILE);
    }

    @Override
    public boolean saveTournamentsToFile(final boolean append) {
        if(append){
            loadTournamentService.loadTournamentsFromFile();
        }
        return saveToFile(atpService.getAtpTournaments(), TOURNAMENTS_FILE);
    }

    @Override
    public boolean saveMatchesToFile(boolean append) {
        if(append){
            loadMatchService.loadMatchesFromFile();
        }
        return saveToFile(atpService.getAtpMatches(), MATCHES_FILE);
    }

    @Override
    public boolean saveRankingsToFile() {
        return saveToFile(atpService.getAtpRankings(), FORMAT_RANKINGS_DATE.apply(atpService.getAtpRankingsDate()));
    }

    @Override
    public boolean saveToFile(final Set objects, final String fileName) {
        try(PrintWriter pw = new PrintWriter(fileName)){
            pw.write(getObjectMapper().writeValueAsString(objects));
            return true;
        } catch (FileNotFoundException | JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }
}
