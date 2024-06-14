package com.disney.qa.api.disney;

@Deprecated
public enum DisneyContentIds {
    END_GAME("Marvel Studios' Avengers: Endgame", "211b146d-13a0-4c71-b2f1-75de907781e9", DisneyContentIds.PROGRAM_ID),
    END_GAME_QA("Marvel Studios' Avengers: Endgame", "4193d521-c460-4a98-a3a1-3786e4b21631", DisneyContentIds.PROGRAM_ID),
    DANCING_WITH_THE_STARS("Dancing with the Stars", "38c1eef7-aa8b-4fa4-8bdc-88e596ea565f", DisneyContentIds.SERIES_ID);

    private static final String PROGRAM_ID = "programId";
    private static final String SERIES_ID = "seriesId";
    private final String title;
    private final String contentId;
    private final String contentType;

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
