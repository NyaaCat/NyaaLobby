package cat.nyaa.lobby.score.api;

import cat.nyaa.lobby.score.ScoreSession;

public interface SessionHolder {
    ScoreSession getOrCreateSession(String name);
    void createSession(String name);
    void dumpSession(String name);
    void removeSession(String name);
}
