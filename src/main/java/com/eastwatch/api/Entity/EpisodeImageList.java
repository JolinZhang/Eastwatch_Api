package com.eastwatch.api.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jonelezhang on 1/20/18.
 */
public class EpisodeImageList implements Serializable {
    private int tv;
    private int tmdb;
    private int season;
    private List<EpisodeImage> episodeImageList;


    public EpisodeImageList(int _tv, int _tmdb, int _season, List<EpisodeImage> _episodeImageList){
        tv = _tv;
        tmdb = _tmdb;
        season = _season;
        episodeImageList = _episodeImageList;

    }


    public List<EpisodeImage> getEpisodeImageList() {
        return episodeImageList;
    }

    public void setEpisodeImageList(List<EpisodeImage> episodeImageList) {
        this.episodeImageList = episodeImageList;
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
}
