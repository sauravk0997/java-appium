package com.disney.qa.api.disney;

public enum DisneyEntityIds {
    END_GAME("Avengers: Endgame", "entity-b39aa962-be56-4b09-a536-98617031717f"),
    INCREDIBLES_2("Incredibles 2", "entity-9da2c0fb-a380-4180-b67f-006fbaaa89ab"),
    IRONMAN("Iron Man", "entity-3ac839c4-864b-41ed-ad98-043d6b4ac564"),
    LUCA("Luca", "entity-f28b825f-c207-406b-923a-67f85e6d90e0"),
    SOUL("Soul", "entity-02a00d44-4607-4866-862f-9d260a8dbdca"),
    WANDA_VISION("WandaVision", "entity-90affd1f-0851-48bc-9cab-c142d5c9c20c"),
    MARVELS("Marvels", "entity-75c90eca-8969-4edb-ac1a-7165cff2671c"),
    SERIES("Series", "entity-cac75c8f-a9e2-4d95-ac73-1cf1cc7b9568"),
    SERIES_EXTRA("Series Extra","entity-aa7bff48-41cd-4fe3-9eaa-b9951bb316d6"),
    IMAX_ENHANCED_SET("IMAX Enhanced Set","7cd344eb-73db-4b5f-9359-f51cead40e23"),
    ORIGINALS_PAGE("Originals Page", "page-fc0d373c-12dc-498b-966b-197938a4264c"),
    HOME_PAGE("Home Page","page-4a8e20b7-1848-49e1-ae23-d45624f4498a"),
    HULU_PAGE("Hulu Page", "page-ff723d29-20d5-4303-9cce-4a9aac8e269e"),
    END_GAME_AVENGERS("Marvel Studios' Avengers: Endgame", "211b146d-13a0-4c71-b2f1-75de907781e9"),
    DANCING_WITH_THE_STARS("Dancing with the Stars", "38c1eef7-aa8b-4fa4-8bdc-88e596ea565f");

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
