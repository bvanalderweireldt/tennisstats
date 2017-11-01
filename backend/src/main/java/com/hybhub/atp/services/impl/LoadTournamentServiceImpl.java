package com.hybhub.atp.services.impl;

import com.hybhub.atp.Exception.ParseStringException;
import com.hybhub.atp.enumeration.CourtType;
import com.hybhub.atp.enumeration.TournamentType;
import com.hybhub.atp.impl.DefaultTournament;
import com.hybhub.atp.services.LoadTournamentService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadTournamentServiceImpl extends AbstractLoad implements LoadTournamentService {

    private final static Logger LOGGER = Logger.getLogger(LoadTournamentServiceImpl.class.getName());

    @Override
    public void loadTournamentsFromAtp(int year) {

        for(String tournamentLink : ABS_HREFS.apply(
                loadDocument(ATP_TOURNAMENTS_URL, year)
                        .select("table.results-archive-table tr.tourney-result a.button-border"))){

            try {

                final Element tournamentDoc = loadDocument(tournamentLink).select("div#mainContent").first();

                final String tournamentId = parseString(TOURNAMENT_ID_PARSER, tournamentLink, "id");

                if(StringUtils.isBlank(tournamentId)){
                    LOGGER.warning(String.format("id for tournament link '%s' is null skipping the current tournament.", tournamentLink));
                    continue;
                }
                final String tournamentTitle = GUESS.apply(tournamentDoc, Arrays.asList("a.tourney-title", "span.tourney-title"));
                final String startEndDates = TEXT_Q.apply(tournamentDoc, "span.tourney-dates");
                final LocalDate tournamentStartDate = parseLocalDate(DATE_START_PARSER,startEndDates);
                final LocalDate tournamentFinishingDate = parseLocalDate(DATE_END_PARSER,startEndDates);

                final TournamentType tournamentType = TournamentType.parseTournamentType(
                        parseString(TOURNAMENT_TYPE,
                                tournamentDoc.select("td.tourney-badge-wrapper img").attr("src"),
                                "tournamentType"));
                final CourtType courtType = CourtType.valueOf(TEXT_Q.apply(tournamentDoc, "td.tourney-details-table-wrapper > table > tbody > tr > td:nth-child(2) > div.info-area > div > span").toUpperCase());

                this.getAtpService().getAtpTournaments().add(new DefaultTournament(tournamentId,tournamentTitle,tournamentStartDate, tournamentFinishingDate, tournamentType, courtType));

            } catch (ParseStringException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    @Override
    public void loadTournamentsFromFile() {
        atpService.getAtpTournaments().addAll(loadObjectsFromFile(TOURNAMENTS_FILE, DefaultTournament.class));
    }

}
