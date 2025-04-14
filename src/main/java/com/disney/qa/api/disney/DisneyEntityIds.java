package com.disney.qa.api.disney;

public enum DisneyEntityIds {
    END_GAME("Avengers: Endgame", "entity-b39aa962-be56-4b09-a536-98617031717f"),
    INCREDIBLES_2("Incredibles 2", "entity-9da2c0fb-a380-4180-b67f-006fbaaa89ab"),
    IRONMAN("Iron Man", "entity-3ac839c4-864b-41ed-ad98-043d6b4ac564"),
    LOKI("Loki", "entity-8f8c5cbb-e5ba-4285-9e2c-86abcac9fd50"),
    LUCA("Luca", "entity-f28b825f-c207-406b-923a-67f85e6d90e0"),
    PREY("Prey", "entity-55349764-323e-4d0e-898f-a4c12c9bf615"),
    SOUL("Soul", "entity-02a00d44-4607-4866-862f-9d260a8dbdca"),
    WANDA_VISION("WandaVision", "entity-90affd1f-0851-48bc-9cab-c142d5c9c20c"),
    MARVELS("Marvels", "entity-75c90eca-8969-4edb-ac1a-7165cff2671c"),
    MOVIES("Movies", "page-c44952c4-c788-44c3-bdf7-e99fca172f36"),
    SERIES("Series", "entity-cac75c8f-a9e2-4d95-ac73-1cf1cc7b9568"),
    SERIES_EXTRA("Series Extra","entity-aa7bff48-41cd-4fe3-9eaa-b9951bb316d6"),
    IMAX_ENHANCED_SET("IMAX Enhanced Set","7cd344eb-73db-4b5f-9359-f51cead40e23"),
    ORIGINALS_PAGE("Originals Page", "page-fc0d373c-12dc-498b-966b-197938a4264c"),
    HOME_PAGE("Home Page","page-4a8e20b7-1848-49e1-ae23-d45624f4498a"),
    HULU_PAGE("Hulu Page", "page-ff723d29-20d5-4303-9cce-4a9aac8e269e"),
    THE_AVENGERS("Marvel Studios' The Avengers", "entity-3a5596d6-5133-4a8e-8d21-00e1531a4e0f"),
    DANCING_WITH_THE_STARS("Dancing with the Stars", "entity-7d918be0-4130-4551-aab8-c7dcae85d35f"),
    DISNEY_PAGE("Disney Brand", "page-4c4b78ed-4a17-43eb-8221-14a3959e4517"),
    SKELETON_CREW("Star Wars: Skeleton Crew", "entity-4a73a750-f18c-450a-b9f7-d9f40974ff9d"),
    DAREDEVIL_BORN_AGAIN("Daredevil: Born Again", "entity-85e7a914-c8e6-41db-95df-c740dc2cf1b7");

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
