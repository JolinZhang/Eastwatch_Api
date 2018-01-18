package com.eastwatch.api.Entity;

import java.util.List;

/**
 * Created by Jonelezhang on 1/17/18.
 */
public class EpisodeImage{

    private int id;
    private List<Still> stills;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Still> getStills() {
        return stills;
    }

    public void setStills(List<Still> stills) {
        this.stills = stills;
    }
}