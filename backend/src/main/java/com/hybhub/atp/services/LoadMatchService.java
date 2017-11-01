package com.hybhub.atp.services;

import com.hybhub.atp.Player;
import com.hybhub.atp.Tournament;

public interface LoadMatchService {
    void loadMatchesFromFile();
    void loadMatchesFromAtpTournament(final Tournament tournament);
}
