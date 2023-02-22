package com.disney.qa.api.nhl.pojo;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO refactor to deal just with list of 'LeagueLeader' object, remove wrapper 'Leaders', because looks like it's unnecessary
 */
public class Leaders {

    private List<LeagueLeader> leagueLeaders = new ArrayList<LeagueLeader>();

    public List<LeagueLeader> getLeagueLeaders() {
        return leagueLeaders;
    }

    public void setLeagueLeaders(List<LeagueLeader> leagueLeaders) {
        this.leagueLeaders = leagueLeaders;
    }

    public LeagueLeader getLeagueLeaders(final String leaderCategory) {
        return Iterables.tryFind(this.leagueLeaders, new Predicate<LeagueLeader>() {
            @Override
            public boolean apply(LeagueLeader input) {
                return input.getLeaderCategory().equals(leaderCategory);
            }
        }).orNull();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Leaders)) return false;

        Leaders leaders = (Leaders) o;

        // TODO [minor] investigate, whether it's necessary to add sorting here
        // Collections.sort(leagueLeaders);
        // Collections.sort(leaders.leagueLeaders);

        return !(leagueLeaders != null ? !leagueLeaders.equals(leaders.leagueLeaders) : leaders.leagueLeaders != null);

    }

    @Override
    public int hashCode() {
        return leagueLeaders != null ? leagueLeaders.hashCode() : 0;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
