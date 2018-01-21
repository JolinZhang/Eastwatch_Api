package com.eastwatch.api.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jonelezhang on 1/17/18.
 */
public class EpisodeImage implements Serializable {

    private int id;

    public int getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(int episodeId) {
        this.episodeId = episodeId;
    }

    private int episodeId;
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