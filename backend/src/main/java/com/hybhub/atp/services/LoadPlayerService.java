package com.hybhub.atp.services;

import com.hybhub.atp.Player;

import java.util.Set;

public interface LoadPlayerService extends IOService {

    void loadPlayersFromAtp(final int startRange, final int endRange);
    void loadPlayersFromFile();

}
