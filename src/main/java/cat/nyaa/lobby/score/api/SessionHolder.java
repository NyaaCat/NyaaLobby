package cat.nyaa.lobby.score.api;

public interface SessionHolder {
    void createSession(String name);
    void dumpSession(String name);
    void removeSession(String name);
}
