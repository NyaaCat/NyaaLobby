package cat.nyaa.lobby.score.api;

import cat.nyaa.lobby.score.ScoreApiImpl;

public interface ScoreAPI extends ScoreHolder{
    static ScoreAPI getImpl(){
        return ScoreApiImpl.getInstance();
    }

}
