package com.hybhub.atp.impl;

import com.hybhub.atp.Tournament;
import com.hybhub.atp.enumeration.CourtType;
import com.hybhub.atp.enumeration.TournamentType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DefaultTournament implements Tournament, Comparable<DefaultTournament> {
    private String id;
    private String name;
    private LocalDate startingDate;
    private LocalDate finishingDate;
    private TournamentType tournamentType;
    private CourtType courtType;

    public DefaultTournament() {
    }

    public DefaultTournament(String id, String name, LocalDate startingDate, LocalDate finishingDate, TournamentType tournamentType, CourtType courtType) {
        this.id = id;
        this.name = name;
        this.startingDate = startingDate;
        this.finishingDate = finishingDate;
        this.tournamentType = tournamentType;
        this.courtType = courtType;
    }

    @Override
    public int hashCode() {
        return String.format("%s%d", this.getId(), this.getStartingDate().getYear()).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof DefaultTournament){
            Tournament tournament = (Tournament) o;
            if(tournament.getStartingDate().equals(this.getStartingDate()) &&
                    tournament.getId().equals(this.getId())){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s (%s on %s), id %s, starting on %s ending on %S", getName(), getTournamentType(), getCourtType(), getId(), DateTimeFormatter.ISO_DATE.format(getStartingDate()), DateTimeFormatter.ISO_DATE.format(getFinishingDate()));
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public LocalDate getStartingDate() {
        return startingDate;
    }

    @Override
    public void setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
    }

    @Override
    public LocalDate getFinishingDate() {
        return finishingDate;
    }

    @Override
    public void setFinishingDate(LocalDate finishingDate) {
        this.finishingDate = finishingDate;
    }

    @Override
    public TournamentType getTournamentType() {
        return tournamentType;
    }

    @Override
    public void setTournamentType(TournamentType tournamentType) {
        this.tournamentType = tournamentType;
    }

    @Override
    public CourtType getCourtType() {
        return courtType;
    }

    @Override
    public void setCourtType(CourtType courtType) {
        this.courtType = courtType;
    }

    @Override
    public int compareTo(DefaultTournament tournament) {
        return this.getStartingDate().compareTo(tournament.getStartingDate());
    }
}
