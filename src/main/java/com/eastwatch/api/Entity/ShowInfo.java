package com.eastwatch.api.Entity;

import java.io.Serializable;

/**
 * Created by Jonelezhang on 1/17/18.
 */

    public class ShowInfo implements Serializable {

        public ShowInfo(){

        }

        private String title;
        private int year;
        private Ids ids;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public Ids getIds() {
            return ids;
        }

        public void setIds(Ids ids) {
            this.ids = ids;
        }
    }

