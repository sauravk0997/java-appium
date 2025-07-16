package com.disney.qa.common.constant;

public class CollectionConstant {

    public enum Collection {
        ABC_NEWS,
        ACTION_AND_ADVENTURE,
        ANIMATED_MOVIES,
        ANIMATED_SERIES,
        BRANDS_COLLECTION,
        BRANDS_DISNEY_ORIGINALS,
        BRANDS_MARVEL_FEATURED,
        BRANDS_NATIONAL_GEOGRAPHIC_FEATURED,
        BRANDS_PIXAR_FEATURED,
        BRANDS_STAR_WARS_ORIGINALS,
        COLLECTIONS,
        COMEDIES,
        CONTINUE_WATCHING,
        DISNEY_JUNIOR,
        DISNEY_PLAYTIME,
        DOCUMENTARIES_AND_REALITY,
        ENJOY_THESE_MOVIES_FROM_HULU,
        ENJOY_THESE_SERIES_FROM_HULU,
        ESPN_EXPLORE_MORE, // collection with locked live and upcoming events for non ESPN entitled user
        ESPN_LEAGUES,
        ESPN_SERIES,
        ESPN_PLUS_LIVE_AND_UPCOMING,// live collection for bundle user
        ESPN_SPORTS,
        HULU_FEATURED,
        HULU_ORIGINALS,
        KIDS_CAROUSEL, //Kids carousel rotating slides
        KIDS_CAROUSEL_TV,
        KIDS_MICKEY_AND_FRIENDS,
        KIDS_PRINCESSES_AND_FAIRY_TALES,
        LIVE_AND_UPCOMING_FROM_ESPN, //live collection for D+ premium user
        NEWLY_ADDED,
        NEW_TO_DISNEY,
        ORIGINALS,
        ORIGINALS_DISNEY_CAROUSEL,
        RECOMMENDED_FOR_YOU,
        REPLAYS_COLLECTION,
        SPORT_REPLAYS,
        STREAMS_NON_STOP_PLAYLISTS,
        STREAMS,
        STUDIOS_AND_NETWORKS,
        TREEHOUSE_OF_HORROR,
        TREEHOUSE_OF_HORROR_I_TO_V,
        TRENDING,
        UNLOCK_TO_STREAM_MORE_HULU,
        WATCHLIST
    }

    public static String getCollectionName(Collection collection) {
        switch (collection) {
            case ABC_NEWS:
                return "350159d6-0e9e-4d6f-bb2a-9f27e97d7083";
            case ACTION_AND_ADVENTURE:
                return "f977cd3e-9983-417d-90e7-dd40101cbda6";
            case ANIMATED_MOVIES:
                return "a221f47c-0e16-476b-bdc0-35ed038b72b8";
            case ANIMATED_SERIES:
                return "d2ccd7df-a0ee-4251-a290-e25c05b852b5";
            case BRANDS_COLLECTION:
                return "5fc710db-490e-405e-b753-7c0a697696db";
            case BRANDS_DISNEY_ORIGINALS:
                return "e7af894d-04f6-47df-a43c-f982ea67d214";
            case BRANDS_PIXAR_FEATURED:
                return "dd6dda68-2634-4f3d-90ba-af5f99a070f4";
            case BRANDS_NATIONAL_GEOGRAPHIC_FEATURED:
                return "de643056-8009-418c-8833-7dc0c331a5cc";
            case BRANDS_MARVEL_FEATURED:
                return "df6c1d07-c9fa-4532-b487-faa57287570e";
            case BRANDS_STAR_WARS_ORIGINALS:
                return "887acd70-a8bd-45df-94b5-477e31f905b4";
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
            case DISNEY_PLAYTIME:
                return "591a460b-0612-44cd-ab88-c976d7d4a82f";
            case ENJOY_THESE_MOVIES_FROM_HULU:
                return "5c721a08-c7ed-4e57-86c4-d781d3727098";
            case ENJOY_THESE_SERIES_FROM_HULU:
                return "11c96651-1be3-40d1-a249-4407948cf6d4";
            case ESPN_EXPLORE_MORE:
                return "1877aec1-5ff9-447f-a779-534372c0af3c";
            case ESPN_LEAGUES:
                return "ec3984c5-6421-405f-91c0-bf892b538fe8";
            case ESPN_SERIES:
                return "4f6ba6c5-57b3-456a-9faa-11a02c48727d";
            case ESPN_PLUS_LIVE_AND_UPCOMING:
                return "21a6e9fa-2a5c-4f77-b1b1-ec956f3038c7";
            case ESPN_SPORTS:
                return "4e177574-aed4-4ad5-8d01-6fc7513bd3e5";
            case LIVE_AND_UPCOMING_FROM_ESPN:
                return "3fd573af-c11c-4f29-a201-b543829aad21";
            case NEWLY_ADDED:
                return "b66f6fa4-4674-4340-8338-9d65f95767a1";
            case NEW_TO_DISNEY:
                return "6b2c1a9b-261c-49f6-ab37-e2b95f2d9612";
            case ORIGINALS:
                return "a93c9e0b-96ef-4e57-a03d-1b780880e0b8";
            case ORIGINALS_DISNEY_CAROUSEL:
                return "c7e20abb-ff52-4d21-a2f6-46618c4118e8";
            case RECOMMENDED_FOR_YOU:
                return "7894d9c6-43ab-4691-b349-cf72362095dd";
            case STREAMS_NON_STOP_PLAYLISTS:
                return "041c9af9-841e-428b-8e9d-0821defc90df";
            case STREAMS:
                return "005ff3cd-66b0-4545-970f-dc86386b27db";
            case TRENDING:
                return "25b87551-fd19-421a-be0f-b7f2eea978b3";
            case HULU_FEATURED:
                return "a4af2864-302e-499e-adf5-88ed1735976f";
            case HULU_ORIGINALS:
                return "d0c4d6a0-3e18-43e4-9db0-9e94ce587273";
            case KIDS_CAROUSEL:
                return "aae103b8-26d4-4150-b83c-2069dc1cc441";
            case KIDS_CAROUSEL_TV:
                return "9ae4a140-a05e-4099-bcd9-d5c7389bc973";
            case KIDS_PRINCESSES_AND_FAIRY_TALES:
                return "a7b1bd9d-59ef-4345-b1a6-19c208b5357e";
            case KIDS_MICKEY_AND_FRIENDS:
                return "65236ebc-b453-4a7b-9a26-7e1388f909b4";
            case REPLAYS_COLLECTION:
                return "971963b2-2bf5-4416-aa7d-41950d5760d7";
            case SPORT_REPLAYS:
                return "cb338e1f-9162-47e1-b6ba-f3a9cf2f9815";
            case STUDIOS_AND_NETWORKS:
                return "775c549b-3308-4694-a0d6-986934b10f3a";
            case TREEHOUSE_OF_HORROR_I_TO_V:
                return "43a35f2b-3788-4449-a54d-cd37263f0940";
            case UNLOCK_TO_STREAM_MORE_HULU:
                return "ab0bba56-0738-4348-b29b-ed2ae2d51130";
            case WATCHLIST:
                return "6f3e3200-ce38-4865-8500-a9f463c1971e";
            default:
                throw new IllegalArgumentException(String.format("'%s collection is not found", collection));
        }
    }

    public static String getCollectionTitle(Collection collection) {
        switch (collection) {
            case COMEDIES:
                return "Comedies";
            case CONTINUE_WATCHING:
                return "Continue Watching";
            case RECOMMENDED_FOR_YOU:
                return "Recommended For You";
            case TREEHOUSE_OF_HORROR:
                return "The Simpsons Treehouse of Horror";
            case UNLOCK_TO_STREAM_MORE_HULU:
                return "Unlock to Stream More Hulu";
            case ESPN_EXPLORE_MORE:
                return "Explore More with ESPN+";
            case ESPN_PLUS_LIVE_AND_UPCOMING:
                return "ESPN+ Live and Upcoming";
            case LIVE_AND_UPCOMING_FROM_ESPN:
                return "Live and Upcoming From ESPN+";
            case ESPN_LEAGUES:
                return "Leagues";
            case ESPN_SPORTS:
                return "Sports";
            case HULU_ORIGINALS:
                return "Hulu Originals";
            case REPLAYS_COLLECTION:
                return "Replays";
            case STREAMS:
                return "Streams";
            case STREAMS_NON_STOP_PLAYLISTS:
                return "Streams: Non-Stop Playlists";
            case STUDIOS_AND_NETWORKS:
                return "Studios and Networks";
            case NEWLY_ADDED:
                return "Newly Added";
            default:
                throw new IllegalArgumentException(String.format("'%s collection title was not found", collection));
        }
    }
}
