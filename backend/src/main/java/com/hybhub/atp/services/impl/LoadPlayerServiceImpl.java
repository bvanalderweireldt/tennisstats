package com.hybhub.atp.services.impl;

import com.hybhub.atp.Exception.ParseStringException;
import com.hybhub.atp.Match;
import com.hybhub.atp.Player;
import com.hybhub.atp.Tournament;
import com.hybhub.atp.enumeration.Backhand;
import com.hybhub.atp.enumeration.Handedness;
import com.hybhub.atp.impl.DefaultPlayer;
import com.hybhub.atp.impl.DefaultTournament;
import com.hybhub.atp.services.AtpService;
import com.hybhub.atp.services.LoadPlayerService;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class LoadPlayerServiceImpl extends AbstractLoad implements LoadPlayerService {

    private final static Logger LOGGER = Logger.getLogger(LoadPlayerServiceImpl.class.getName());

    @Autowired
    AtpService atpService;

    @Override
    public void loadPlayersFromAtp(final int startRange, final int endRange) {

        Document allPlayersDoc = loadDocument(ATP_WORLD_TOUR_PLAYERS_URL, startRange, endRange);

        Elements results = allPlayersDoc.select("#rankingDetailAjaxContainer > table > tbody > tr > td.player-cell a");

        Set<Player> players = new HashSet<>();

        for(Element element : results){

            try{
                final String id = parseString(PLAYER_ID_PARSER, element.attr("href"), "id");
                players.add(loadPlayer(id));
            } catch (Exception e){
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                continue;
            }
        }
        atpService.getAtpPlayers().addAll(players);
    }

    @Override
    public void loadPlayersFromFile() {
        atpService.getAtpPlayers().addAll(loadObjectsFromFile(PLAYERS_FILE, DefaultPlayer.class));
    }

    private Player loadPlayer(final String id) throws ParseStringException {
        final Element playerDoc = loadDocument(ATP_WORLD_TOUR_PLAYER_URL, id).select("#playerProfileHero > div.player-profile-hero-overflow").first();

        final String country = playerDoc.select("div.player-profile-hero-dash > div > div.player-profile-hero-ranking > div.player-flag > div.player-flag-code").text();
        final String name = playerDoc.select("div.player-profile-hero-dash > div > div.player-profile-hero-name").text();


        final LocalDate dob = parseLocalDate(DOB_PARSER, playerDoc.select("div.player-profile-hero-table > div > table > tbody > tr:nth-child(1) > td:nth-child(1) > div > div.table-big-value > span > span").text(), "year", "month", "day");


        final String handBackhandString = playerDoc.select("div.player-profile-hero-table > div > table > tbody > tr:nth-child(2) > td:nth-child(3) > div > div.table-value").text();
        final Handedness handedness = parseEnum(HANDEDNESS_PARSER, handBackhandString, "handedness", Handedness.class);
        final Backhand backhand = parseEnum(BACKHAND_PARSER, handBackhandString, "backhand", Backhand.class);

        final int weight = parseInteger(NUMBER_PARSER, playerDoc.select("div.player-profile-hero-table > div > table > tbody > tr:nth-child(1) > td:nth-child(3) > div > div.table-big-value > span.table-weight-kg-wrapper").text(), "number");

        final int height = parseInteger(NUMBER_PARSER, playerDoc.select("div.player-profile-hero-table > div > table > tbody > tr:nth-child(1) > td:nth-child(4) > div > div.table-big-value > span.table-height-cm-wrapper").text(), "number");

        return new DefaultPlayer(id,name,country,dob,handedness,backhand,weight,height);
    }
}
