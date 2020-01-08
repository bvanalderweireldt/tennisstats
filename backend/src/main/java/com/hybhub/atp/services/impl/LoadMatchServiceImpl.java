package com.hybhub.atp.services.impl;

import com.hybhub.atp.Exception.ParseStringException;
import com.hybhub.atp.Tournament;
import com.hybhub.atp.impl.DefaultMatch;
import com.hybhub.atp.services.AtpService;
import com.hybhub.atp.services.LoadMatchService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadMatchServiceImpl extends AbstractLoad implements LoadMatchService {

    private static Logger LOGGER = Logger.getLogger(LoadMatchServiceImpl.class.getName());

    @Override
    public void loadMatchesFromFile() {
        atpService.getAtpMatches().addAll(loadObjectsFromFile(MATCHES_FILE, DefaultMatch.class));
    }

    @Override
    public void loadMatchesFromAtpTournament(Tournament tournament) {
        Instant start = Instant.now();
        final Document doc = loadDocument(ATP_TOURNAMENT_RESULTS_URL, tournament.getId());

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Elements elements = doc.select("table.day-table > tbody > tr");
        for(Element element : elements){
            executorService.submit(new LoadMatch(element, tournament));
        }
        try {
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Waited 5 minutes for completion", e);
        }
        LOGGER.info(String.format("Loaded %d matches for tournament %s in %ds.",elements.size(), tournament.getId(), Duration.between(start, Instant.now()).getSeconds()));

    }

    private class LoadMatch implements Runnable {

        private final Element element;
        private final Tournament tournament;

        private LoadMatch(Element element, Tournament tournament) {
            this.element = element;
            this.tournament = tournament;
        }

        @Override
        public void run() {
            try {
                final Elements scoreMatchIdElement = element.select("td.day-table-score a");
                final String score = scoreMatchIdElement.text();

                String winnerId = null;
                String loserId = null;
                Duration duration = Duration.ZERO;

                String matchId = parseString(MATCH_ID_PARSER, scoreMatchIdElement.attr("href"), "matchid");

                if(matchId == null || matchId.isEmpty()){
                    if(score != null && !score.isEmpty()){
                        winnerId = parseString(PLAYER_ID_PARSER, element.select("td.day-table-name:nth-child(3) a").attr("href"), "id");
                        loserId = parseString(PLAYER_ID_PARSER, element.select("td.day-table-name:nth-child(7) a").attr("href"), "id");
                        matchId = String.format("%s%s%s%d", tournament.getId(), winnerId, loserId, tournament.getStartingDate().getYear());
                    }
                }
                else {
                    Elements matchDoc = loadDocument(ATP_MATCH_URL, matchId).select("div.modal-scores-match-stats-players table.scores-table > tbody");

                    winnerId = parseString(PLAYER_ID_PARSER, matchDoc.select("tr:nth-child(1) a.scoring-player-flag").attr("href"), "id");
                    loserId = parseString(PLAYER_ID_PARSER, matchDoc.select("tr:nth-child(2) a.scoring-player-flag").attr("href"), "id");
                    duration = parseDuration(matchDoc.select("tr.match-info-row > td.time").text());
                }


                if(StringUtils.isBlank(matchId) ||
                        StringUtils.isBlank(winnerId) ||
                        StringUtils.isBlank(loserId) ||
                        StringUtils.isBlank(tournament.getId())) {
                    LOGGER.warning(String.format("Warning can't save the match : '%s' (matchId)," +
                                    "'%s' (winnerId)," +
                                    "'%s' (loserId)," +
                                    "'%s' (tournamentId)" +
                                    "'%s' (tournamentName)",
                            matchId,winnerId,loserId,tournament.getId(),tournament.getName()));
                } else {
                    atpService.getAtpMatches()
                            .add(new DefaultMatch(matchId,winnerId,loserId, score, duration, tournament.getId()));
                }
            } catch (ParseStringException e) {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            }
        }
    }
}
