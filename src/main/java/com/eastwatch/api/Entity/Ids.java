package com.eastwatch.api.Entity;

/**
 * Created by Jonelezhang on 1/17/18.
 */
public class Ids{

    private int trakt;
    private String slug;
    private int tvdb;
    private String imdb;
    private int tmdb;
    private int tvrage;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getTvdb() {
        return tvdb;
    }

    public void setTvdb(int tvdb) {
        this.tvdb = tvdb;
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    public int getTmdb() {
        return tmdb;
    }

    public void setTmdb(int tmdb) {
        this.tmdb = tmdb;
    }

    public int getTvrage() {
        return tvrage;
    }

    public void setTvrage(int tvrage) {
        this.tvrage = tvrage;
    }

    public int getTrakt() {

        return trakt;
    }

    public void setTrakt(int trakt) {
        this.trakt = trakt;
    }
}
