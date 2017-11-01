package com.hybhub.atp.impl;

import com.hybhub.atp.Match;
import com.hybhub.atp.Tournament;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Created by bvanalderweireldt on 10/15/17.
 */
public class DefaultMatch implements Match {
    private String id;
    private String winner;
    private String loser;
    private String tournament;
    private Duration duration;
    private String score;

    public DefaultMatch() {
    }

    public DefaultMatch(String id, String winner, String loser, String score, Duration duration, String tournamentId) {
        this.id = id;
        this.winner = winner;
        this.loser = loser;
        this.score = score;
        this.tournament = tournamentId;
        this.duration = duration;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof DefaultMatch
        && ((DefaultMatch) o).getId().equals(this.id));
    }

    @Override
    public String toString() {
        return String.format("%s won against %s %s in %s", getWinner(), getLoser(), getScore(), DurationFormatUtils.formatDuration(getDuration().toMillis(), "H:mm", true));
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getWinner() {
        return winner;
    }

    @Override
    public void setWinner(String winner) {
        this.winner = winner;
    }

    @Override
    public int getWinnerRank() {
        return 0;
    }

    @Override
    public int setWinnerRank(int winnerRank) {
        return 0;
    }

    @Override
    public String getLoser() {
        return loser;
    }

    @Override
    public void setLoser(String loser) {
        this.loser = loser;
    }

    @Override
    public int getLoserRank() {
        return 0;
    }

    @Override
    public void setLoserRank(int loserRank) {

    }

    @Override
    public String getTournament() {
        return tournament;
    }

    @Override
    public void setTournament(String tournament) {
        this.tournament = tournament;
    }

    @Override
    public LocalDate getTime() {
        return null;
    }

    @Override
    public void setTime(LocalDate localDate) {

    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String getScore() {
        return score;
    }

    @Override
    public void setScore(String score) {
        this.score = score;
    }
}
