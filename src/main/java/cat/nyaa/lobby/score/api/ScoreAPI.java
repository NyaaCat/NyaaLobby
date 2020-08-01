package cat.nyaa.lobby.score.api;

import cat.nyaa.lobby.score.ScoreApiImpl;

public interface ScoreAPI extends SessionHolder{
    static ScoreAPI getImpl(){
        return ScoreApiImpl.getInstance();
    }

}
