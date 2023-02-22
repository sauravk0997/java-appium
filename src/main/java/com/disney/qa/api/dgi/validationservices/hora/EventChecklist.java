package com.disney.qa.api.dgi.validationservices.hora;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class EventChecklist {
    private String eventType;
    private JSONArray requirements;
    private long reqiurementTime;

    private boolean timingRequired;

    public EventChecklist() {
        this.requirements = new JSONArray();
        this.reqiurementTime = System.currentTimeMillis();
    }
    public EventChecklist(String eventType) {
        this.eventType = eventType;
        this.requirements = new JSONArray();
        this.reqiurementTime = System.currentTimeMillis();
    }
    public EventChecklist(String eventType, int timeFrame) {
        this.eventType = eventType;
        this.requirements = new JSONArray();
        //this.reqiurementTime = System.currentTimeMillis(); To be added back when a solution for timing issues is found
        this.timingRequired = true;
        // this.reqiurementTime = timeFrame * 1000; To be added back when a solution for timing issues is found
    }
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public JSONArray getRequirements() {
        return requirements;
    }

    public void setRequirements(JSONArray requirements) {
        this.requirements = requirements;
    }
    public void addRequirement(String type, String element){
       JSONObject requirement = new JSONObject();
       requirement.put("type", type);
       requirement.put("element", element);
       requirements.add(requirement);
    }
    public void addRequirement(String type, String element, Object value){
        JSONObject requirement = new JSONObject();
        requirement.put("type", type);
        requirement.put("element", element);
        requirement.put("value",value);
        requirements.add(requirement);
    }
    public long getReqiurementTime() {
        return reqiurementTime;
    }
    public boolean isTimingRequired() {
        return timingRequired;
    }

    public void setReqiurementTime(long reqiurementTime) {
        this.reqiurementTime = reqiurementTime;
    }
}
