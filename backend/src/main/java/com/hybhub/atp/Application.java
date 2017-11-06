package com.hybhub.atp;

import com.hybhub.atp.services.*;
import com.hybhub.atp.services.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan
@PropertySource("app.properties")
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

}
