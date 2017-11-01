package com.hybhub.atp.services.impl;

import com.hybhub.atp.Exception.ParseStringException;
import com.hybhub.atp.impl.DefaultRanking;
import com.hybhub.atp.services.AtpService;
import com.hybhub.atp.services.LoadRankingService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.logging.Logger;

public class LoadRankingServiceImpl extends AbstractLoad implements LoadRankingService {

    private final static Logger LOGGER = Logger.getLogger(LoadRankingServiceImpl.class.getName());

    private final static int HIGH_RANKING_BOUNDARY = 1000;

    @Override
    public void loadRankingsFromAtp() {
        final LocalDate date = atpService.getAtpRankingsDate();
        final Elements allPlayersElements = loadDocument(ATP_WORLD_TOUR_PLAYERS_DATE_URL,
                0, HIGH_RANKING_BOUNDARY,
                date.getYear(), date.getMonth().getValue(), date.getDayOfMonth())
                .select("div#singlesRanking > div.table-rankings-wrapper > table.mega-table tbody tr");

        for(Element playerEl : allPlayersElements){
            try {
                final int rank = Integer.valueOf(TEXT_Q.apply(playerEl, "td.rank-cell").replaceAll("T", ""));
                final String id = parseString(PLAYER_ID_PARSER, playerEl.select("td.player-cell a").attr("href"), "id");
                final int points = NumberFormat.getInstance(Locale.US).parse(TEXT_Q.apply(playerEl, "td.points-cell")).intValue();
                final int tournamentsPlayed = Integer.valueOf(TEXT_Q.apply(playerEl, "td.tourn-cell"));

                getAtpService().getAtpRankings().add(new DefaultRanking(id,rank,points,tournamentsPlayed));
            } catch (ParseException | ParseStringException | NumberFormatException e) {
                LOGGER.warning(String.format("Couldn't get a player : %s", e.getMessage()));
                continue;
            }
        }
    }

    @Override
    public void loadRankingsFromFile() {
        atpService.getAtpRankings().clear();
        atpService.getAtpRankings().addAll(
                loadObjectsFromFile(FORMAT_RANKINGS_DATE.apply(atpService.getAtpRankingsDate()), DefaultRanking.class));
    }

}
