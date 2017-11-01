package com.hybhub.atp;

import com.hybhub.atp.Exception.ParseStringException;
import com.hybhub.atp.services.*;
import com.hybhub.atp.services.impl.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
@ComponentScan
public class Application {

    @Bean
    AtpService atpService(){
        return new AtpServiceImpl();
    }

    @Bean
    LoadTournamentService loadTournamentService() {
        return new LoadTournamentServiceImpl();
    }

    @Bean
    LoadPlayerService loadPlayerService() {
        return new LoadPlayerServiceImpl();
    }

    @Bean
    LoadRankingService loadRankingService() {
        return new LoadRankingServiceImpl();
    }

    @Bean
    SaveService saveService() {
        return new SaveServiceImpl();
    }

    @Bean
    LoadMatchService loadMatchService(){
        return new LoadMatchServiceImpl();
    }

    public static void loadRankings(){
        final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Application.class);
        final SaveService saveService = applicationContext.getBean(SaveService.class);
        final LoadRankingService loadRankingService = applicationContext.getBean(LoadRankingService.class);

        loadRankingService.loadRankingsFromAtp();
        saveService.saveRankingsToFile();
    }

    public static void main2(String[] args) throws ParseStringException {
        Logger.getGlobal().setLevel(Level.FINEST);

        final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Application.class);
        LoadTournamentService loadTournamentService = applicationContext.getBean(LoadTournamentService.class);
        AtpService atpService = applicationContext.getBean(AtpService.class);
        SaveService saveService = applicationContext.getBean(SaveService.class);
        LoadPlayerService loadPlayerService = applicationContext.getBean(LoadPlayerService.class);
        LoadMatchService loadMatchService = applicationContext.getBean(LoadMatchService.class);
        LoadRankingService loadRankingService = applicationContext.getBean(LoadRankingService.class);


        loadTournamentService.loadTournamentsFromFile();
        loadPlayerService.loadPlayersFromFile();
        loadMatchService.loadMatchesFromFile();

        atpService.setAtpRankingsDate(LocalDate.of(2017, 10, 16));
        loadRankingService.loadRankingsFromAtp();
        saveService.saveRankingsToFile();

        System.out.println(atpService.getAtpRankings().size());
        //atpService.getAtpRankings().stream().sorted(Comparator.comparing(Ranking::getRanking)).forEach(System.out::println);
//        atpService.getAtpTournaments().stream().forEach(loadMatchService::loadMatchesFromAtpTournament);
//        IntStream.range(2000,2018).forEach(loadTournamentService::loadTournamentsFromAtp);
//        saveService.saveTournamentsToFile(false);
//        saveService.saveMatchesToFile(false);
        //        atpService.getAtpTournaments().stream()
//                .filter((t) -> t.getStartingDate().getYear() < 2016)
//                .forEach(loadMatchService::loadMatchesFromAtpTournament);
//
//        saveService.saveTournamentsToFile(true);
//        saveService.saveMatchesToFile(true);

//        Player roger = atpService.getAtpPlayers().stream().filter((p)->p.getId().equals("tb69")).findFirst().get();
//
//        System.out.println(atpService.getAtpMatches().stream()
//                .filter((m)->m.getLoser().equals(roger.getId())).count());
//        atpService.getAtpMatches().stream()
//                .filter((m)->m.getWinner().equals(roger.getId()))
//                .sorted(Comparator.comparing(Match::getDuration))
//                .forEach((m) -> {
//                    Player loser = atpService.getAtpPlayers().stream().filter((p)->p.getId().equals(m.getLoser())).findFirst().orElse(new DefaultPlayer("noid", "unknown"));
//                    System.out.println(roger.getName() + " lost ("+m.getScore()+") against " + loser.getName() + " in : " + m.getDuration().toMinutes() + " minutes ("+m.getId()+")");
//                });
    }

    public static void main(String[] args) throws IOException {
        loadRankings();
    }
}
