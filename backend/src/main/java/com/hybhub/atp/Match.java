package com.hybhub.atp;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;

public interface Match {
    @JsonProperty
    String getId();
    @JsonProperty
    void setId(final String id);
    @JsonProperty
    String getTournament();
    @JsonProperty
    void setTournament(final String tournament);
    @JsonProperty
    LocalDate getTime();
    @JsonProperty
    void setTime(final LocalDate localDate);
    @JsonProperty
    Duration getDuration();
    @JsonProperty
    void setDuration(final Duration duration);
    @JsonProperty
    String getWinner();
    @JsonProperty
    void setWinner(final String winner);
    @JsonProperty
    int getWinnerRank();
    @JsonProperty
    int setWinnerRank(final int winnerRank);
    @JsonProperty
    String getLoser();
    @JsonProperty
    void setLoser(final String loser);
    @JsonProperty
    int getLoserRank();
    @JsonProperty
    void setLoserRank(final int loserRank);
    @JsonProperty
    String getScore();
    @JsonProperty
    void setScore(final String score);
}
