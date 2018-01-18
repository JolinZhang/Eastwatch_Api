package com.eastwatch.api.Entity;

/**
 * Created by Jonelezhang on 1/17/18.
 */
public class EpisodeInfo{
    private int season;
    private int number;
    private String title;
    private  Ids ids;

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Ids getIds() {
        return ids;
    }

    public void setIds(Ids ids) {
        this.ids = ids;
    }
}
