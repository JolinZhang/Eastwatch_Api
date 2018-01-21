package com.eastwatch.api.Entity;

import java.io.Serializable;

/**
 * Created by Jonelezhang on 1/20/18.
 */
public class TmdbInfo implements Serializable {
    private int tv;
    private int tmdb;
    private int season;
    private int episodeCount;

    public TmdbInfo(int _tv, int _tmdb, int _season, int _numberOfEpisodes) {
        tv = _tv;
        tmdb = _tmdb;
        season = _season;
        episodeCount = _numberOfEpisodes;
    }


    public int getTv() {
        return tv;
    }

    public void setTv(int tv) {
        this.tv = tv;
    }

    public int getTmdb() {
        return tmdb;
    }

    public void setTmdb(int tmdb) {
        this.tmdb = tmdb;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(int episodeCount) {
        this.episodeCount = episodeCount;
    }
}
