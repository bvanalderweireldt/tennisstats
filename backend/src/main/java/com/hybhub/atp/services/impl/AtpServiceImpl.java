package com.hybhub.atp.services.impl;

import com.hybhub.atp.Ranking;
import com.hybhub.atp.services.AtpService;
import com.hybhub.atp.Match;
import com.hybhub.atp.Player;
import com.hybhub.atp.Tournament;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AtpServiceImpl implements AtpService {

    private final Set<Player> players = ConcurrentHashMap.newKeySet();
    private final Set<Match> matches = ConcurrentHashMap.newKeySet();
    private final Set<Tournament> tournaments = ConcurrentHashMap.newKeySet();
    private final Set<Ranking> rankings = ConcurrentHashMap.newKeySet();
    private LocalDate atpRankingsDate = LocalDate.now().with(DayOfWeek.MONDAY);

    @Override
    public Set<Player> getAtpPlayers() {
        return this.players;
    }

    @Override
    public Set<Match> getAtpMatches() {
        return this.matches;
    }

    @Override
    public Set<Tournament> getAtpTournaments() {
        return this.tournaments ;
    }

    @Override
    public Set<Ranking> getAtpRankings() {
        return this.rankings;
    }

    @Override
    public LocalDate getAtpRankingsDate() {
        return atpRankingsDate;
    }

    @Override
    public void setAtpRankingsDate(final LocalDate date) {
        this.atpRankingsDate = date;
    }
}
