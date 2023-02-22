package com.disney.qa.api.nhl.pojo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mk on 1/13/16.
 */
public class LeagueLeader implements Comparable {

    private String leaderCategory;

    private List<Leader> leaders = new ArrayList<Leader>();

    public String getLeaderCategory() {
        return leaderCategory;
    }

    public void setLeaderCategory(String leaderCategory) {
        this.leaderCategory = leaderCategory;
    }

    public List<Leader> getLeaders() {
        return leaders;
    }

    public void setLeaders(List<Leader> leaders) {
        this.leaders = leaders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LeagueLeader)) return false;

        LeagueLeader that = (LeagueLeader) o;

        if (leaderCategory != null ? !leaderCategory.equals(that.leaderCategory) : that.leaderCategory != null)
            return false;
        return !(leaders != null ? !leaders.equals(that.leaders) : that.leaders != null);

    }

    @Override
    public int hashCode() {
        int result = leaderCategory != null ? leaderCategory.hashCode() : 0;
        result = 31 * result + (leaders != null ? leaders.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Object o) {
        return leaderCategory.compareTo(((LeagueLeader) o).getLeaderCategory());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
