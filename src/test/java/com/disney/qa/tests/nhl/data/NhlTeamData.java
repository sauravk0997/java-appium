package com.disney.qa.tests.nhl.data;

import com.qaprosoft.carina.core.foundation.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class NhlTeamData {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Yaml yaml = new Yaml();
    private InputStream teamStream = this.getClass()
            .getClassLoader()
            .getResourceAsStream("YML_data/nhl/teams_data.yaml");
    private ArrayList<Object> teamsYml = yaml.load(teamStream);
    private List<Object[]> teamsList = new CopyOnWriteArrayList<>();
    private List<Object[]> chosenTeam = new ArrayList<>();

    private static final String TEAM_NAME = "teamName";
    private static final String CUSTOM_STRING_2 = "custom_string2";

    @DataProvider(name = "generateTeamsToScan")
    public Iterator<Object[]> generateTeamsToScan() {
        String teamValue = setTeamValue();

        for (Object item : teamsYml) {
            Map<String, String> team = (HashMap<String, String>) item;
            String name = team.get(TEAM_NAME);
            String id = String.valueOf(team.get("teamId"));
            if (name == null) {
                name = "";
            }

            addToDataProvider(name, id);
        }

        LOGGER.info("Checking TeamsList Size: " + teamsList.size());

        if (teamValue.equals("All")) {
            return teamsList.iterator();
        } else {
            for(int i = 0; i < teamsList.size(); i++){
                if(teamsList.get(i)[1].equals(teamValue)){
                    chosenTeam.add(teamsList.get(i));
                    break;
                }
            }
            return chosenTeam.iterator();
        }
    }

    private void addToDataProvider(String team, String teamId) {

        LOGGER.info("Team Added to Data Provider: " + team);
        LOGGER.info("Team ID: " + teamId);
        teamsList.add(new Object[]{
                String.format("TUID: Team Name: (%s) - Team ID (%s)", team, teamId), team, teamId});
    }

    public String getTeamId(String teamName){
        String name = "";
        String teamID = "";
        for (Object item : teamsYml) {
            Map<String, String> team = (HashMap<String, String>) item;
            name = team.get(TEAM_NAME);
            teamID = String.valueOf(team.get("teamId"));
            if (name.equalsIgnoreCase(teamName)) {
                break;
            }
        }

        return teamID;
    }

    public String getTeamShort(String teamName){
        String name = "";
        String teamShort = "";
        for(Object item : teamsYml){
            Map<String, String> team = (HashMap<String, String>) item;
            name = team.get(TEAM_NAME);
            teamShort = String.valueOf(team.get("teamShort"));
            if(name.equalsIgnoreCase(teamName)){
                break;
            }
        }

        return teamShort;
    }

    private String setTeamValue(){
        String teamValue;
        List<String> allTeamNames = new NhlTeamData().getAllTeamNames();
        try {
            teamValue = R.CONFIG.get(CUSTOM_STRING_2);
        } catch (Exception e){
            teamValue = "Random";
        }

        if(teamValue.equals("Random") || teamValue.equals("NULL")){
            R.CONFIG.put(CUSTOM_STRING_2, allTeamNames.get(new Random().nextInt(allTeamNames.size())));
        }
        return R.CONFIG.get(CUSTOM_STRING_2);
    }

    public List<String> getAllTeamNames(){
        List<String> teams = new ArrayList<>();
        for(Object item : teamsYml){
            Map<String, String> team = (HashMap<String, String>) item;
            teams.add(team.get("teamName"));
        }
        return teams;
    }
}
