package com.hybhub.atp.enumeration;


import java.util.logging.Logger;

public enum TournamentType {
    ATP_250(250), ATP_500(500), MASTERS_1000(1000), GRAND_SLAM(2000), ATP_FINAL(1500), ITF(0), ATPWT(0), CHALLENGER(0), UNKNOWN(0);

    private final static Logger LOGGER = Logger.getLogger(TournamentType.class.getName());

    int atpPoints;

    TournamentType(int atpPoints) {
        this.atpPoints = atpPoints;
    }

    public static TournamentType parseTournamentType(final String toParse){
        switch (toParse){
            case "250":
                return ATP_250;
            case "500":
                return ATP_500;
            case "nitto_atp_finals":
                return ATP_FINAL;
            case "grandslam":
                return GRAND_SLAM;
            case "1000s":
                return MASTERS_1000;
            case "itf":
                return ITF;
            case "atpwt":
                return ATPWT;
            case "challenger":
                return CHALLENGER;
            default:
                LOGGER.warning("Unknown tournament type : " + toParse);
                return UNKNOWN;
        }
    }
    public int getAtpPoints() {
        return atpPoints;
    }
}
