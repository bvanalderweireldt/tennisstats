package com.hybhub.atp;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface Ranking {

    @JsonProperty
    String getPlayerId();

    @JsonProperty
    void setPlayerId(final String playerId);

    @JsonProperty
    int getRanking();

    @JsonProperty
    void setRanking(final int ranking);

    @JsonProperty
    int getPoints();

    @JsonProperty
    void setPoints(final int points);

    @JsonProperty
    int getTournamentPlayed();

    @JsonProperty
    void setTournamentPlayed(final int tournamentPlayed);
}
