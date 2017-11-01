package com.hybhub.atp.services;

import com.hybhub.atp.Match;
import com.hybhub.atp.Tournament;

import java.util.Set;

public interface LoadTournamentService {
    void loadTournamentsFromAtp(int year);
    void loadTournamentsFromFile();
}
