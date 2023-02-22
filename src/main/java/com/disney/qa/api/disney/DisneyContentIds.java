package com.disney.qa.api.disney;

public enum DisneyContentIds {
    END_GAME("Marvel Studios' Avengers: Endgame", "211b146d-13a0-4c71-b2f1-75de907781e9", DisneyContentIds.PROGRAM_ID),
    END_GAME_QA("Marvel Studios' Avengers: Endgame", "4193d521-c460-4a98-a3a1-3786e4b21631", DisneyContentIds.PROGRAM_ID),
    INCREDIBLES2("Incredibles 2", "fc8388d4-c0ae-4b4e-90fe-60d1ab8f1cc1", DisneyContentIds.PROGRAM_ID),
    LUCA("Luca", "dbe51a04-9611-4ea9-b64f-542f865974a2", DisneyContentIds.PROGRAM_ID),
    MANDALORIAN("The Mandalorian", "cda8e42d-0cc4-484f-bb5b-b8dd3b8dd496", DisneyContentIds.SERIES_ID),
    SOUL("Soul", "064850f1-5a8b-47e5-b12a-f0bdefd26e44", DisneyContentIds.PROGRAM_ID),
    WANDA_VISION("WandaVision", "da05ebb0-bcdd-45ea-9100-ca6a63484d86", DisneyContentIds.SERIES_ID),
    DANCING_WITH_THE_STARS("Dancing with the Stars", "38c1eef7-aa8b-4fa4-8bdc-88e596ea565f", DisneyContentIds.SERIES_ID);

    private static final String PROGRAM_ID = "programId";
    private static final String SERIES_ID = "seriesId";
    private String title;
    private String contentId;
    private String contentType;

    DisneyContentIds(String title, String contentId, String contentType) {
        this.title = title;
        this.contentId = contentId;
        this.contentType = contentType;
    }

    public String getTitle() {
        return title;
    }

    public String getContentId() {
        return contentId;
    }

    public String getContentType() {
        return contentType;
    }
}
