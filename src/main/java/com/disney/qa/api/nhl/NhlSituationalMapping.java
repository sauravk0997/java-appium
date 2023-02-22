package com.disney.qa.api.nhl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by boyle on 4/17/17.
 */
public class NhlSituationalMapping {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static Map<String, List> situationalMapping = new HashMap<String, List>();

    public NhlSituationalMapping() {

        addToSituationalMap("1PT", getPeriodAgnosticScenariosTrue());
        addToSituationalMap("1RT", getPeriodAgnosticScenariosTrue());
        addToSituationalMap("1PRT", getPeriodAgnosticScenariosTrue());
        addToSituationalMap("2PT", getPeriodAgnosticScenariosTrue());
        addToSituationalMap("2RT", getPeriodAgnosticScenariosTrue());
        addToSituationalMap("2PRT", getPeriodAgnosticScenariosTrue());
        addToSituationalMap("3PT", getPeriodAgnosticScenariosTrue());
        addToSituationalMap("3RT", getPeriodAgnosticScenariosTrue());
        addToSituationalMap("3PRT", getPeriodAgnosticScenariosTrue());
        addToSituationalMap("4PT", getPeriodAgnosticScenariosTrue());
        addToSituationalMap("4RT", getRegularOverTimeScenariosTrue());
        addToSituationalMap("4PRT", getRegularOverTimeScenariosTrue());
        addToSituationalMap("1PF", getPeriodAgnosticScenariosFalse());
        addToSituationalMap("1RF", getPeriodAgnosticScenariosFalse());
        addToSituationalMap("1PRF", getPeriodAgnosticScenariosFalse());
        addToSituationalMap("2PF", getPeriodAgnosticScenariosFalse());
        addToSituationalMap("2RF", getPeriodAgnosticScenariosFalse());
        addToSituationalMap("2PRF", getPeriodAgnosticScenariosFalse());
        addToSituationalMap("3PF", getPeriodAgnosticScenariosFalse());
        addToSituationalMap("3RF", getPeriodAgnosticScenariosFalse());
        addToSituationalMap("3PRF", getPeriodAgnosticScenariosFalse());
        addToSituationalMap("4PF", getPeriodAgnosticScenariosFalse());
        addToSituationalMap("4RF", getRegularOverTimeScenariosFalse());
        addToSituationalMap("4PRF", getRegularOverTimeScenariosFalse());
        addToSituationalMap("5PF", getPeriodAgnosticScenariosFalse());
        addToSituationalMap("5RF", getRegularOverTimeScenariosFalse());
        addToSituationalMap("6PF", getPeriodAgnosticScenariosFalse());
        
    }

    private void addToSituationalMap(final String periodGameType, final List acceptableSkaters) {
        situationalMapping.put(periodGameType, acceptableSkaters);
    }

    private static boolean containsMapping(final String acceptableMapping) {
        return situationalMapping.containsKey(acceptableMapping);
    }

    public final List getMapping(final String mapping) {
        List<String> emptyList = new ArrayList<>();
        emptyList.add("Not Found");

        if (!containsMapping(mapping)) {
            LOGGER.info(String.format("Mapping was not found... (%s)", mapping));
            return emptyList;
        } else {
            LOGGER.info(String.format("Found Mapping For... (%s)", mapping));
        }

        return situationalMapping.get(mapping);
    }

    private List<String> getPeriodAgnosticScenariosTrue() {
        List<String> genericPeriod = new ArrayList<>();

        genericPeriod.add("33");
        genericPeriod.add("34");
        genericPeriod.add("35");
        genericPeriod.add("43");
        genericPeriod.add("44");
        genericPeriod.add("45");
        genericPeriod.add("53");
        genericPeriod.add("54");

        return genericPeriod;
    }

    private List<String> getPeriodAgnosticScenariosFalse() {
        List<String> genericPeriod = new ArrayList<>();

        genericPeriod.add("36");
        genericPeriod.add("46");
        genericPeriod.add("55");
        genericPeriod.add("56");
        genericPeriod.add("66");
        genericPeriod.add("65");
        genericPeriod.add("64");
        genericPeriod.add("63");

        return genericPeriod;
    }

    private List<String> getRegularOverTimeScenariosTrue() {
        List<String> regularOverTimeScenarios = new ArrayList<>();

        regularOverTimeScenarios.add("34");
        regularOverTimeScenarios.add("35");
        regularOverTimeScenarios.add("36");
        regularOverTimeScenarios.add("43");
        regularOverTimeScenarios.add("44");
        regularOverTimeScenarios.add("45");
        regularOverTimeScenarios.add("46");
        regularOverTimeScenarios.add("53");
        regularOverTimeScenarios.add("54");
        regularOverTimeScenarios.add("55");
        regularOverTimeScenarios.add("56");
        regularOverTimeScenarios.add("63");
        regularOverTimeScenarios.add("64");
        regularOverTimeScenarios.add("65");
        regularOverTimeScenarios.add("66");

        return regularOverTimeScenarios;
    }

    private List<String> getRegularOverTimeScenariosFalse() {
        List<String> regularOverTimeScenarios = new ArrayList<>();

        regularOverTimeScenarios.add("33");
        regularOverTimeScenarios.add("10");
        regularOverTimeScenarios.add("01");

        return regularOverTimeScenarios;
    }
}