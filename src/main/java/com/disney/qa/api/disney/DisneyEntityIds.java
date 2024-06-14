package com.disney.qa.api.disney;

public enum DisneyEntityIds {
    END_GAME("Avengers: Endgame", "entity-b39aa962-be56-4b09-a536-98617031717f"),
    INCREDIBLES2("Incredibles 2", "entity-9da2c0fb-a380-4180-b67f-006fbaaa89ab"),
    IRONMAN("Iron Man", "entity-3ac839c4-864b-41ed-ad98-043d6b4ac564"),
    LUCA("Luca", "entity-f28b825f-c207-406b-923a-67f85e6d90e0"),
    SOUL("Soul", "entity-02a00d44-4607-4866-862f-9d260a8dbdca"),
    WANDA_VISION("WandaVision", "entity-90affd1f-0851-48bc-9cab-c142d5c9c20c");

    private final String title;
    private final String entityId;

    DisneyEntityIds(String title, String entityId) {
        this.title = title;
        this.entityId = entityId;
    }

    public String getTitle() {
        return title;
    }

    public String getEntityId() {
        return entityId;
    }
}
