package com.hybhub.atp.services;

import java.util.Set;

public interface SaveService extends IOService {
    boolean savePlayersToFile(final boolean append);
    boolean saveTournamentsToFile(final boolean append);
    boolean saveMatchesToFile(final boolean append);
    boolean saveRankingsToFile();
    boolean saveToFile(final Set objs, final String fileName);
}
