package com.disney.qa.api.dgi.validationservices.multieventservice.endpointspojo.validate_with_config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Create and customize configPayload to be sent to 'MultiEvent Validation Service w/ Config'
 */
public class ConfigSetup {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    String configPayload = null;

    private EventSequences[] eventSequences;

    private DuplicateKeys[] duplicateKeys;

    private Object[] entityRelationships;

    public ConfigSetup(Object[] entityRelationships, EventSequences... eventSequences) {
        this.entityRelationships = entityRelationships;
        this.eventSequences = eventSequences;
        createConfig();
    }

    public ConfigSetup(EventSequences[] eventSequences, DuplicateKeys[] duplicateKeys) {
        this.eventSequences = eventSequences;
        this.duplicateKeys = duplicateKeys;
        createConfig();
    }

    public ConfigSetup(Object[] entityRelationships, EventSequences[] eventSequences, DuplicateKeys[] duplicateKeys) {
        this.entityRelationships = entityRelationships;
        this.eventSequences = eventSequences;
        this.duplicateKeys = duplicateKeys;
        createConfig();
    }

    public String getConfigPayload() {
        return configPayload;
    }

    public EventSequences[] getEventSequences() {
        return eventSequences;
    }

    public DuplicateKeys[] getDuplicateKeys() {
        return duplicateKeys;
    }

    public Object[] getEntityRelationships() {
        return entityRelationships;
    }

    public String setConfigPayload(String configPayload) {
        this.configPayload = configPayload;
        return configPayload;
    }

    public void setEventSequences(EventSequences... eventSequences) {
        this.eventSequences = eventSequences;
    }

    public void setDuplicateKeys(DuplicateKeys... duplicateKeys) {
        this.duplicateKeys = duplicateKeys;
    }

    public void setEntityRelationships(EntityRelationship[] entityRelationships) {
        this.entityRelationships = entityRelationships;
    }

    //TODO move to a base class
    public String readConfig() {
        JsonNode configJson = null;
        try {
            configJson = new ObjectMapper()
                    .readTree(new File("./src/main/resources/api/dgi/ConfigPayload.json"));
        } catch (IOException e) {
            LOGGER.error("Couldn't read file!" + e);
        }
        return configJson.toString();
    }

    public String addEventSequences() {
        List eventSequencesList = new ArrayList();
        Arrays.asList(getEventSequences()).forEach(eventSequence ->
                eventSequencesList.add(eventSequence.getEventSequence())
        );
        return String.join(",", eventSequencesList);
    }

    public String addDuplicateKeys() {
        List duplicateKeysList = new ArrayList();
        Arrays.asList(getDuplicateKeys()).forEach(duplicateKey ->
                duplicateKeysList.add(duplicateKey.getDuplicateKey())
        );
        return String.join(",", duplicateKeysList);
    }

    //TODO look into deserialization approach rather than string manipulation
    //TODO add docs
    public String createConfig() {

        JsonNode entityRelations = new EntityRelationship(getEntityRelationships()).generateList();

        if(this.eventSequences[0] == EventSequences.DEFAULT) {
            return setConfigPayload(EventSequences.DEFAULT.getEventSequence());
        }
        if(getDuplicateKeys() != null) {
            setConfigPayload(readConfig().substring(0, readConfig().length() - 1) + ", \"expected_event_sequences\": {" + addEventSequences() + "}, \"duplicates_key_mapping\": {" +  addDuplicateKeys()+ "}}");
        } else {
            setConfigPayload(readConfig().substring(0, readConfig().length() - 1) + ", \"expected_event_sequences\": {" + addEventSequences() + "}}");
        }
        if(entityRelations.findValue("entity_relationship_list").size() == 0) {
            setConfigPayload(getConfigPayload().replace("\"entity_relationship_list\":[],", ""));
        }
        return getConfigPayload();
    }
}