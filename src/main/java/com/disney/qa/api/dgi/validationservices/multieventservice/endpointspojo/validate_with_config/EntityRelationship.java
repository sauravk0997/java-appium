package com.disney.qa.api.dgi.validationservices.multieventservice.endpointspojo.validate_with_config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/*
 * Create and entity_relationship_list to be sent to 'MultiEvent Validation Service w/ Config'
 */
public class EntityRelationship {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @JsonIgnore
    private JsonNode entityRelationshipList = null;

    private String child = null;

    private String childKey = null;

    private String parentKey = null;

    private String parent = null;

    private boolean checkChildlessParents = false;

    @JsonIgnore
    public JsonNode getEntityRelationshipList() {
        return entityRelationshipList;
    }

    @JsonIgnore
    public void setEntityRelationshipList(JsonNode entityRelationshipList) {
        this.entityRelationshipList = entityRelationshipList;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    @JsonProperty("child_key")
    public String getChildKey() {
        return childKey;
    }

    public void setChildKey(String childKey) {
        this.childKey = childKey;
    }

    @JsonProperty("parent_key")
    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @JsonProperty("check_childless_parents")
    public boolean isCheckChildlessParents() {
        return checkChildlessParents;
    }

    public void setCheckChildlessParents(boolean checkChildlessParents) {
        this.checkChildlessParents = checkChildlessParents;
    }

    //TODO add docs
    @JsonIgnore
    public EntityRelationship(Object... entityRelationshipArr) {
        List relationshipCombos = new ArrayList();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode entityRelationshipNode = objectMapper.createObjectNode();
        ArrayNode entityRelationshipsArrNode = objectMapper.createArrayNode();

            for (int currentEntity = 0; currentEntity < entityRelationshipArr.length; currentEntity++) {
                this.child = String.valueOf(Array.get(entityRelationshipArr[currentEntity], 0));
                this.childKey = String.valueOf(Array.get(entityRelationshipArr[currentEntity], 1));
                this.parentKey = String.valueOf(Array.get(entityRelationshipArr[currentEntity], 2));
                this.parent = String.valueOf(Array.get(entityRelationshipArr[currentEntity], 3));
                this.checkChildlessParents = (Boolean) Array.get(entityRelationshipArr[currentEntity], 4);
                relationshipCombos.add(objectMapper.valueToTree(this));
            }
            setEntityRelationshipList(entityRelationshipNode.set("entity_relationship_list", entityRelationshipsArrNode.addAll(relationshipCombos)));
    }

    public JsonNode generateList() {
        writeConfig(getEntityRelationshipList());
        return getEntityRelationshipList();
    }

    //TODO move to a base class
    private void writeConfig(Object value) {
        ObjectWriter writer = new ObjectMapper().writer(new DefaultPrettyPrinter());
        try {
        writer.writeValue(new File("./src/main/resources/api/dgi/ConfigPayload.json"), value);
        } catch (IOException e) {
            LOGGER.error("Could not write to file!" , e);
        }
    }
}
