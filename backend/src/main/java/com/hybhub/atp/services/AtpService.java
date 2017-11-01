package com.hybhub.atp.services;

import com.hybhub.atp.Match;
import com.hybhub.atp.Player;
import com.hybhub.atp.Ranking;
import com.hybhub.atp.Tournament;

import java.time.LocalDate;
import java.util.Set;

public interface AtpService {

    Set<Player> getAtpPlayers();
    Set<Match> getAtpMatches();
    Set<Tournament> getAtpTournaments();
    Set<Ranking> getAtpRankings();
    LocalDate getAtpRankingsDate();
    void setAtpRankingsDate(final LocalDate date);
}
