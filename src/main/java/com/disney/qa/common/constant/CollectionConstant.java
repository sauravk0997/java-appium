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
        HULU_FEATURED_PROD,
        HULU_NEWLY_ADDED_PREVIEW,
        HULU_ORIGINALS_PROD,
        KIDS_PRINCESSES_AND_FAIRY_TALES_PROD,
        KIDS_MICKEY_AND_FRIENDS_PROD,
        KIDS_DOGS_AND_CATS_PREVIEW,
        NEW_TO_DISNEY_PROD,
        RECOMMENDED_FOR_YOU_PREVIEW;
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
                return "52ae7413-2c06-435f-bc72-74bc6edda108";
            case DISNEY_JUNIOR:
                return "ce35f2ea-ba2c-4892-9c12-d4a4ab3bfb32";
            case MUSIC_AND_DANCE:
                return "73d4a334-87c8-4059-91de-9d49171c41fb";
            case NEW_TO_DISNEY:
                return "96f8f6f2-01c9-41a1-b40a-5f9fa585dcb2";
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
            case HULU_FEATURED_PROD:
                return "816ba94e-4153-48ba-941e-40e5c81ba631";
            case HULU_NEWLY_ADDED_PREVIEW:
                return "48d99856-a5e2-44ef-bd1f-a142d7d5b247";
            case HULU_ORIGINALS_PROD:
                return "e0c26361-9a7f-460f-b6d1-29f95467bbe0";
            case KIDS_PRINCESSES_AND_FAIRY_TALES_PROD:
                return "a7b1bd9d-59ef-4345-b1a6-19c208b5357e";
            case KIDS_MICKEY_AND_FRIENDS_PROD:
                return "65236ebc-b453-4a7b-9a26-7e1388f909b4";
            case KIDS_DOGS_AND_CATS_PREVIEW:
                return "1b2d6a9e-26b2-4259-8767-67a91ada9c87";
            case NEW_TO_DISNEY_PROD:
                return "2e347570-8e01-4258-8f97-882afe202ee2";
            case RECOMMENDED_FOR_YOU_PREVIEW:
                return "c93d66b6-cbf4-4125-ac3e-a0e605157cce";
            default:
                throw new IllegalArgumentException(String.format("'%s collection is not found", collection));
        }
    }
}
