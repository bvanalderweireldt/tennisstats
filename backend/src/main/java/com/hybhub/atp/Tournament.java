package com.hybhub.atp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hybhub.atp.enumeration.CourtType;
import com.hybhub.atp.enumeration.TournamentType;

import java.time.LocalDate;

public interface Tournament {
    @JsonProperty
    String getName();
    @JsonProperty
    void setName(final String name);
    @JsonProperty
    String getId();
    @JsonProperty
    void setId(final String id);
    @JsonProperty
    LocalDate getStartingDate();
    @JsonProperty
    void setStartingDate(final LocalDate startingDate);
    @JsonProperty
    LocalDate getFinishingDate();
    @JsonProperty
    void setFinishingDate(final LocalDate finishingDate);
    @JsonProperty
    TournamentType getTournamentType();
    @JsonProperty
    void setTournamentType(final TournamentType tournamentType);
    @JsonProperty
    CourtType getCourtType();
    @JsonProperty
    void setCourtType(final CourtType courtType);
}
