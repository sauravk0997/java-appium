package com.disney.qa.common.constant;

public class CollectionConstant {

    public enum Collection {
        ACTION_AND_ADVENTURE,
        ANIMATED_MOVIES,
        ANIMATED_SERIES,
        CLASSIC_SHORTS,
        CAROUSEL, //carousel rotating slides
        COLLECTIONS,
        COMEDIES,
        CONTINUE_WATCHING,
        DISNEY_JUNIOR,
        DOCUMENTARIES_AND_REALITY,
        MUSIC_AND_DANCE,
        NEW_TO_DISNEY,
        ORIGINALS,
        REALITY_SERIES,
        RECOMMENDED_FOR_YOU,
        SHORTS,
        TRENDING,
        ANIMATED_MOVIES_QA,
        NEW_TO_DISNEY_QA,
        HULK_MOVIES_QA;
    }

    public static String getCollectionName(Collection collection) {
        switch (collection) {
            case ACTION_AND_ADVENTURE:
                return "f977cd3e-9983-417d-90e7-dd40101cbda6";
            case ANIMATED_MOVIES:
                return "a221f47c-0e16-476b-bdc0-35ed038b72b8";
            case ANIMATED_SERIES:
                return "0af2fea3-8cc5-45e0-90d4-7e964d4ac4f8";
            case CAROUSEL:
                return "5c3a73f7-f06c-42c6-ab6c-525ba9af1327";
            case CLASSIC_SHORTS:
                return "dd04d249-96f2-4149-a2da-a9f2302d157b";
            case COLLECTIONS:
                return "1da80bf5-b832-44fd-b6ab-4b257e319094";
            case COMEDIES:
                return "cad0303b-5f54-4276-8303-7f9ba3d4303e";
            case CONTINUE_WATCHING:
                return "76aed686-1837-49bd-b4f5-5d2a27c0c8d4";
            case DOCUMENTARIES_AND_REALITY:
                return "5537a5bf-697a-403e-875d-40794922fd13";
            case DISNEY_JUNIOR:
                return "ce35f2ea-ba2c-4892-9c12-d4a4ab3bfb32";
            case MUSIC_AND_DANCE:
                return "73d4a334-87c8-4059-91de-9d49171c41fb";
            case NEW_TO_DISNEY:
                return "3c61bd68-b639-44a9-97b4-5bed89a7c479";
            case ORIGINALS:
                return "6711b1f3-da45-4236-8ae6-faf241d66102";
            case REALITY_SERIES:
                return "bd532890-e3d4-415e-831d-348eb949b9ec";
            case RECOMMENDED_FOR_YOU:
                return "7894d9c6-43ab-4691-b349-cf72362095dd";
            case SHORTS:
                return "c082e3f6-4e7e-44f3-b6d8-aab7a8bf7367";
            case TRENDING:
                return "25b87551-fd19-421a-be0f-b7f2eea978b3";
            case ANIMATED_MOVIES_QA:
                return "43b6e7d1-ee42-4699-ac18-17c4658f5219";
            case NEW_TO_DISNEY_QA:
                return "76317bf0-3465-48d1-a7f5-80e37e3338b2";
            case HULK_MOVIES_QA:
                return "e57ab7d2-e60b-49fa-89bb-3d8f7ec066f7";
            default:
                throw new IllegalArgumentException(String.format("'%s collection is not found", collection));
        }
    }
}
