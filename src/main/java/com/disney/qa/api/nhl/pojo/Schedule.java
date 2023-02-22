package com.disney.qa.api.nhl.pojo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mk on 11/8/15.
 */
public class Schedule {

    private List<Game> gameList = new ArrayList<>();

    public List<Game> getGameList() {
        return gameList;
    }

    public void setGameList(List<Game> gameList) {
        this.gameList = gameList;
    }

    public Schedule() {
    }

    public Schedule(List<Game> gameList) {
        this.gameList = gameList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Schedule)) return false;

        Schedule schedule = (Schedule) o;

        // TODO [normal] move sorting to object converter
        // sort order is not considered in this equal implementation. By default it's considered
        Collections.sort(gameList);
        Collections.sort(schedule.gameList);

        return !(gameList != null ? !gameList.equals(schedule.gameList) : schedule.gameList != null);

    }

    @Override
    public int hashCode() {
        return gameList != null ? gameList.hashCode() : 0;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
