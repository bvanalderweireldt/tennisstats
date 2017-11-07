package com.hybhub.atp;

import com.hybhub.atp.services.AtpService;
import com.hybhub.atp.services.LoadRankingService;
import com.hybhub.atp.services.SaveService;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Run {

    private final ApplicationContext applicationContext;

    public Run() {
        this.applicationContext = new AnnotationConfigApplicationContext(Application.class);
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void loadRankings(){
        final SaveService saveService = getApplicationContext().getBean(SaveService.class);
        final LoadRankingService loadRankingService = getApplicationContext().getBean(LoadRankingService.class);

        loadRankingService.loadRankingsFromAtp();
        saveService.saveRankingsToFile();
    }

    public static void main(String[] args) throws IOException {
        final Run run = new Run();

        final Options options = new Options();
        final Option debug = new Option( "debug", "print debugging information" );
        options.addOption(debug);

        final Option dirProperty = Option.builder( "dir" )
                .numberOfArgs(1)
                .desc("Directory where to save json files")
                .build();
        options.addOption(dirProperty);

        final Option rankingDateProperty = Option.builder( "rankingDate" )
                .numberOfArgs(1)
                .desc("Ranking date to parse if not last Monday (format : yyyy-mm-dd)")
                .build();
        options.addOption(rankingDateProperty);

        final Option commandToRun = Option.builder("cmd")
                .required()
                .hasArg()
                .desc("Command to run (rankings)")
                .build();
        options.addOption(commandToRun);

        final CommandLineParser commandLineParser = new DefaultParser();
        try {
            final CommandLine cli = commandLineParser.parse(options, args);

            if(cli.hasOption("debug")){
                Logger.getGlobal().setLevel(Level.FINEST);
            }

            if(cli.hasOption("dir")){
                run.getApplicationContext().getBean(SaveService.class).setFilesPath(cli.getOptionValue("dir"));
            }

            if(cli.hasOption("rankingDate")){
                final LocalDate rankingDate = LocalDate.parse(cli.getOptionValue("rankingDate"));
                run.getApplicationContext().getBean(AtpService.class).setAtpRankingsDate(rankingDate);
            }

            if(cli.getOptionValue("cmd").equalsIgnoreCase("rankings")){
                run.loadRankings();
            }

        } catch (ParseException e) {
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "Atp parser", options );
        }
    }
}
