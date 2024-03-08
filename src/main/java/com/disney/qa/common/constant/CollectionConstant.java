package com.disney.qa.common.constant;

public class CollectionConstant {

    public enum Collection {
        ACTION_AND_ADVENTURE,
        ANIMATED_MOVIES,
        ANIMATED_SERIES,
        BRANDS_DISNEY_ORIGINALS,
        BRANDS_PIXAR_FEATURED,
        BRANDS_NATIONAL_GEOGRAPHIC_FEATURED,
        BRANDS_MARVEL_FEATURED,
        BRANDS_STAR_WARS_ORIGINALS,
        CAROUSEL, //carousel rotating slides
        COLLECTIONS,
        COMEDIES,
        CONTINUE_WATCHING,
        DISNEY_JUNIOR,
        DOCUMENTARIES_AND_REALITY,
        NEW_TO_DISNEY,
        ORIGINALS,
        ORIGINALS_DISNEY_CAROUSEL,
        RECOMMENDED_FOR_YOU,
        TRENDING,
        HULU_FEATURED,
        HULU_ORIGINALS,
        KIDS_CAROUSEL, //Kids carousel rotating slides
        KIDS_PRINCESSES_AND_FAIRY_TALES,
        KIDS_MICKEY_AND_FRIENDS,
        STUDIOS_AND_NETWORKS;
    }

    public static String getCollectionName(Collection collection) {
        switch (collection) {
            case ACTION_AND_ADVENTURE:
                return "f977cd3e-9983-417d-90e7-dd40101cbda6";
            case ANIMATED_MOVIES:
                return "a221f47c-0e16-476b-bdc0-35ed038b72b8";
            case ANIMATED_SERIES:
                return "d2ccd7df-a0ee-4251-a290-e25c05b852b5";
            case BRANDS_DISNEY_ORIGINALS:
                return "c7e20abb-ff52-4d21-a2f6-46618c4118e8";
            case BRANDS_PIXAR_FEATURED:
                return "e1aa64e4-a3aa-4fe8-b9d7-82e5c390d7ba";
            case BRANDS_NATIONAL_GEOGRAPHIC_FEATURED:
                return "4deffc6a-e93d-4720-8aee-cbed79784b46";
            case BRANDS_MARVEL_FEATURED:
                return "15194ee1-cc3d-47ae-a54e-869c64da2e10";
            case BRANDS_STAR_WARS_ORIGINALS:
                return "9fb4cc69-6d90-4d1f-ae5d-0c5dca626e3c";
            case CAROUSEL:
                return "55c72c2f-c8ce-4317-ae6c-6a8fa7213c85";
            case COLLECTIONS:
                return "ed2d5ad2-cfc6-42c7-942e-aa539772f10c";
            case COMEDIES:
                return "4981954b-4043-4a74-924a-5096e0ef9c56";
            case CONTINUE_WATCHING:
                return "76aed686-1837-49bd-b4f5-5d2a27c0c8d4";
            case DOCUMENTARIES_AND_REALITY:
                return "aaab13b2-4e15-4e71-836e-debe6d004193";
            case DISNEY_JUNIOR:
                return "ce35f2ea-ba2c-4892-9c12-d4a4ab3bfb32";
            case NEW_TO_DISNEY:
                return "6b2c1a9b-261c-49f6-ab37-e2b95f2d9612";
            case ORIGINALS:
                return "a93c9e0b-96ef-4e57-a03d-1b780880e0b8";
            case ORIGINALS_DISNEY_CAROUSEL:
                return "c7e20abb-ff52-4d21-a2f6-46618c4118e8";
            case RECOMMENDED_FOR_YOU:
                return "7894d9c6-43ab-4691-b349-cf72362095dd";
            case TRENDING:
                return "25b87551-fd19-421a-be0f-b7f2eea978b3";
            case HULU_FEATURED:
                return "a4af2864-302e-499e-adf5-88ed1735976f";
            case HULU_ORIGINALS:
                return "84b7169a-efd4-4b7a-a1e8-84d0f2d07315";
            case KIDS_CAROUSEL:
                return "2821d7a3-1146-4ca5-8333-e2c565edf79d";
            case KIDS_PRINCESSES_AND_FAIRY_TALES:
                return "a7b1bd9d-59ef-4345-b1a6-19c208b5357e";
            case KIDS_MICKEY_AND_FRIENDS:
                return "65236ebc-b453-4a7b-9a26-7e1388f909b4";
            case STUDIOS_AND_NETWORKS:
                return "917351f3-45cf-4251-b425-c8fd1b18434d";
            default:
                throw new IllegalArgumentException(String.format("'%s collection is not found", collection));
        }
    }
}
