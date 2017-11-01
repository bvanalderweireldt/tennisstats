package com.hybhub.atp.services.impl;

import com.hybhub.atp.Exception.ParseStringException;
import com.hybhub.atp.services.AtpService;
import com.hybhub.atp.services.IOService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public abstract class AbstractLoad implements IOService {

    private final static Logger LOGGER = Logger.getLogger(AbstractLoad.class.getName());

    private final static int MAX_HTTP_RETRY = 5;

    final static String ATP_URL = "http://www.atpworldtour.com/";

    final static String ATP_URL_EN = ATP_URL + "en/";

    final static String ATP_WORLD_TOUR_PLAYERS_URL = "http://www.atpworldtour.com/en/rankings/singles?rankRange=%d-%d";

    final static String ATP_WORLD_TOUR_PLAYERS_DATE_URL = ATP_WORLD_TOUR_PLAYERS_URL + "&rankDate=%d-%d-%d";

    final static String ATP_WORLD_TOUR_PLAYER_URL = "http://www.atpworldtour.com/en/players/abc/%s/overview";

    final static String ATP_TOURNAMENTS_URL = ATP_URL_EN + "scores/results-archive?year=%d";

    final static String ATP_TOURNAMENT_RESULTS_URL = ATP_URL_EN + "scores/archive/%s/results";

    final static String ATP_MATCH_URL = ATP_URL_EN + "scores/%s/match-stats";

    final static Pattern PLAYER_ID_PARSER = Pattern.compile("/\\w+/\\w+/[^/]+/(?<id>\\w+)/\\w+");

    final static Pattern MATCH_ID_PARSER = Pattern.compile("/\\w+/\\w+/(?<matchid>\\d+/\\d+/\\w+)/match-stats");

    final static Pattern TOURNAMENT_ID_PARSER = Pattern.compile("/\\w+/(?<id>[^/]+/[^/]+/[^/]+)/results$");

    final static Pattern NUMBER_PARSER = Pattern.compile("(?<number>\\d+)");

    final static Pattern DOB_PARSER = Pattern.compile("(?:(?<year>\\d{4})\\.(?<month>\\d{2})\\.(?<day>\\d{2}))");

    final static Pattern DATE_START_PARSER = Pattern.compile("(?<year>\\d{4})\\.(?<month>\\d{2})\\.(?<day>\\d{2}) - \\d{4}\\.\\d{2}\\.\\d{2}");

    final static Pattern DATE_END_PARSER = Pattern.compile("\\d{4}\\.\\d{2}\\.\\d{2} - (?<year>\\d{4})\\.(?<month>\\d{2})\\.(?<day>\\d{2})");

    final static Pattern HANDEDNESS_PARSER = Pattern.compile("(?<handedness>Right|Left|AMBIDEXTROUS)(?:-Handed)?", Pattern.CASE_INSENSITIVE);

    final static Pattern BACKHAND_PARSER = Pattern.compile("(?<backhand>Two|One)-Handed Backhand", Pattern.CASE_INSENSITIVE);

    final static Pattern MATCH_DURATION_PARSER = Pattern.compile("Time: (?<hours>\\d+):(?<minutes>\\d+):(?<seconds>\\d+)");

    final static Pattern TOURNAMENT_TYPE = Pattern.compile("/-/media/images/tourtypes/(categorystamps_)?(?<tournamentType>itf|250|500|1000s|grandslam|nitto_atp_finals|atpwt|challenger)_");

    final static Function<Element,String> ABS_HREF =
            (element) -> element.attr("abs:href");

    final static Function<Elements, List<String>> ABS_HREFS =
            (elements) -> elements.stream()
                        .map(ABS_HREF::apply)
                        .filter(StringUtils::isNotBlank)
                        .collect(Collectors.toList());

    final static Function<Element, String> TEXT = Element::text;

    final static BiFunction<Element, String, String> TEXT_Q =
            (e, q) -> TEXT.apply(e.select(q).first());

    final static List<Function<Element, String>> GUESS_FUNCTIONS = Arrays.asList(TEXT, ABS_HREF);

    final static BiFunction<Element, List<String>, String> GUESS =
            (element, selectors) -> {
                String toReturn;
                for(String selector : selectors){
                    for(Function<Element, String> function : GUESS_FUNCTIONS){
                        Elements selected = element.select(selector);
                        if(selected.isEmpty()){
                            continue;
                        }
                        toReturn = function.apply(selected.first());
                        if(StringUtils.isNotBlank(toReturn)){
                            return toReturn;
                        }
                    }
                }
                return null;
            };

    @Autowired
    AtpService atpService;


    Document loadDocument(final String url, Object... objects) {
        int retries = 0;
        while(retries < MAX_HTTP_RETRY) {
            try {
                return Jsoup.connect(
                        String.format(url,
                                objects))
                        .userAgent("curl/7.47.0")
                        .maxBodySize(0)
                        .get();
            } catch (HttpStatusException h) {
                LOGGER.warning(String.format("Http status is not ok : %s (%s), will retry (%d attempts sor far)", h.getStatusCode(), String.format(url,objects), retries));
                retries =+ 1;
            } catch (IOException e) {
                LOGGER.severe(e.getMessage());
                throw new IOError(e);
            }
        }
        throw new IllegalStateException(String.format("Couldn't load the document for url %s", url));
    }

    LocalDate parseLocalDate(Pattern pattern, String toParse) throws ParseStringException {
        return parseLocalDate(pattern,toParse,"year","month","day");
    }

    LocalDate parseLocalDate(final Pattern pattern, final String toParse, final String groupNamesYear, final String groupNamesMonth, final String groupNamesDay) throws ParseStringException {
        try {
            return LocalDate.of(
                    parseInteger(pattern,toParse,groupNamesYear),
                    parseInteger(pattern,toParse,groupNamesMonth),
                    parseInteger(pattern,toParse,groupNamesDay));
        } catch (IllegalArgumentException illegalArgumentException) {
            LOGGER.fine("Will return null for date : " + toParse);
            return null;
        }
    }

    Integer parseInteger(final Pattern pattern, final String toParse, final String groupName) throws ParseStringException {
        try {
            return Integer.valueOf(parseString(pattern,toParse,groupName));
        } catch (IllegalArgumentException illegalArgumentException) {
            LOGGER.fine("Will return 0 for Integer : " + toParse);
            return 0;
        }
    }

    <T extends Enum<T>> T parseEnum(final Pattern pattern, final String toParse, final String groupName, Class<T> type) throws ParseStringException {
        try {
            String enumValueToParse = parseString(pattern, toParse, groupName);
            if(enumValueToParse != null && !enumValueToParse.isEmpty()){
                return Enum.valueOf(type, enumValueToParse.toUpperCase());
            }
        } catch (ParseStringException | IllegalArgumentException illegalArgumentException) {
            LOGGER.fine("Will return null for : " + toParse + ", enum " + type.getTypeName());
        }
        return null;
    }

    String parseString(final Pattern pattern, final String toParse, final String groupName) throws ParseStringException {
        if(toParse == null || toParse.isEmpty()){
            return null;
        }
        final Matcher matcher = pattern.matcher(toParse);
        if (matcher.find()){
            try {
                final String toReturn = matcher.group(groupName);
                if(toReturn.isEmpty()){
                    LOGGER.warning("Parsed an empty String for : " + toParse);
                }
                return toReturn;
            } catch (IllegalArgumentException illegalArgumentException) {
                // Throw exception after
            }
        }
        throw new ParseStringException("Can't parse String " + toParse + ", looking for group id " + groupName);
    }

    /**
     * Expected format is 'Time: 01:20:00'
     * @param toParse
     * @return
     */
    Duration parseDuration(final String toParse){
        try {
            final Integer hours = Integer.valueOf(parseString(MATCH_DURATION_PARSER, toParse, "hours"));
            final Integer minutes = Integer.valueOf(parseString(MATCH_DURATION_PARSER, toParse, "minutes"));
            final Integer seconds = Integer.valueOf(parseString(MATCH_DURATION_PARSER, toParse, "seconds"));
            return Duration.of(hours, ChronoUnit.HOURS).plusMinutes(minutes).plusSeconds(seconds);

        } catch (IllegalArgumentException | ParseStringException e) {
            LOGGER.warning("Can't extract duration will return 0 !");
        }
        return Duration.ZERO;
    }

    public <T> Set<T> loadObjectsFromFile(final String filename, Class className) {
        try {
            if(!Files.exists(Paths.get(filename))){
                LOGGER.warning("Tried to load a file that doesn't exist : " + filename);
                return Collections.EMPTY_SET;
            }
            final String content = new String(Files.readAllBytes(Paths.get(filename)));
            return getObjectMapper().readValue(content, getObjectMapper().getTypeFactory().constructCollectionType(Set.class, className));
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
            throw new IOError(e);
        }
    }

    public AtpService getAtpService() {
        return atpService;
    }

    public void setAtpService(AtpService atpService) {
        this.atpService = atpService;
    }

}
