package com.hybhub.atp.impl;

import com.hybhub.atp.Ranking;

public class DefaultRanking implements Ranking {
    private String playerId;
    private int ranking;
    private int points;
    private int tournamentPlayed;

    public DefaultRanking() {
    }

    public DefaultRanking(String playerId, int ranking, int points, int tournamentPlayed) {
        this.playerId = playerId;
        this.ranking = ranking;
        this.points = points;
        this.tournamentPlayed = tournamentPlayed;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null &&
                obj instanceof DefaultRanking &&
                ((DefaultRanking) obj).getPlayerId().equals(this.getPlayerId())){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getPlayerId().hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s is ranked %d with %d points after playing %d tournaments", getPlayerId(), getRanking(), getPoints(), getTournamentPlayed());
    }

    @Override
    public String getPlayerId() {
        return playerId;
    }

    @Override
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    @Override
    public int getRanking() {
        return ranking;
    }

    @Override
    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public int getTournamentPlayed() {
        return tournamentPlayed;
    }

    @Override
    public void setTournamentPlayed(int tournamentPlayed) {
        this.tournamentPlayed = tournamentPlayed;
    }
}
