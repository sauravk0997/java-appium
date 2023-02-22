package com.disney.qa.tests.disney.android.tv;

import com.disney.qa.api.client.requests.content.CollectionRequest;
import com.disney.qa.api.client.requests.content.SetRequest;
import com.disney.qa.api.client.responses.content.ContentCollection;
import com.disney.qa.api.client.responses.content.ContentMovie;
import com.disney.qa.api.search.assets.DisneyCollectionType;
import com.disney.qa.api.search.assets.DisneyStandardCollection;
import com.disney.qa.api.search.sets.DisneyCollectionSet;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage;
import com.disney.util.disney.ZebrunnerXrayLabels;
import exceptions.DisneySearchResultNotFoundException;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.disney.qa.tests.disney.DisneyPlusBaseTest.*;

public class DisneyPlusAndroidTVOriginalsScreenTests extends DisneyPlusAndroidTVBaseTest {

    private static final String LANDING_ERROR = "Originals page did not launch";

    @Test
    public void detailsFromOriginalsPage() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67316", "XCDQA-67324", "XCDQA-67326"));
        CollectionRequest collectionRequest = CollectionRequest.builder()
                .region(country)
                .collectionType(DisneyCollectionType.PERSONALIZED_COLLECTION.getCollectionType())
                .account(entitledUser.get())
                .language(language).slug(DisneyStandardCollection.ORIGINALS.getSlug())
                .contentClass(DisneyStandardCollection.ORIGINALS.getSlug())
                .build();

        ContentCollection collection = disneySearchApi.get().getCollection(collectionRequest);

        DisneyCollectionSet featuredCollectionSet = collection.getCollectionSetsInfo().get(0);
        DisneyCollectionSet secondCollectionSet = collection.getCollectionSetsInfo().get(1);

        SetRequest featuredSetRequest = SetRequest.builder()
                .region(country)
                .language(language)
                .setId(featuredCollectionSet.getRefId())
                .refType(featuredCollectionSet.getRefType())
                .build();
        SetRequest secondSetRequest = SetRequest.builder()
                .region(country)
                .language(language)
                .setId(secondCollectionSet.getRefId())
                .refType(secondCollectionSet.getRefType())
                .build();

        List<String> featuredTitles = disneySearchApi.get().getSet(featuredSetRequest).getTitles();
        List<String> secondSetTitles = disneySearchApi.get().getSet(secondSetRequest).getTitles();
        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.ORIGINALS,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());

        sa.assertTrue(disneyPlusAndroidTVOriginalsPageBase.get().isOpened(), LANDING_ERROR);
        // Check the first tile is focused
        sa.assertEquals(disneyPlusAndroidTVCommonPage.get().getAndroidTVUtilsInstance().getContentDescription(
                        disneyPlusAndroidTVOriginalsPageBase.get().getMediaPosters().get(0)), featuredTitles.get(0),
                "Focus should be on first item");

        // Verify Featured set is 4 tiles wide or less
        if (featuredTitles.size() > 4 && featuredCollectionSet.getLayoutType().equals("GridContainer")) {
            sa.assertTrue(disneyPlusAndroidTVOriginalsPageBase.get().fourTilesPerRow(featuredTitles.size() / 4),
                    "Should contain four tiles per row");

            disneyPlusAndroidTVCommonPage.get().pressRight(4);
            sa.assertEquals(disneyPlusAndroidTVCommonPage.get().getAndroidTVUtilsInstance().getContentDescription(
                    disneyPlusAndroidTVOriginalsPageBase.get().getMediaPosters().get(3)), featuredTitles.get(3),
                    "Focus should be on 4th tile");

            // Scroll back to first tile
            disneyPlusAndroidTVCommonPage.get().pressLeft(3);
        }

        // Scroll down to a new set and check originals' logo is still present
        disneyPlusAndroidTVCommonPage.get().selectNextSet(featuredCollectionSet, featuredTitles.size());

        // Navigate enough to scroll the set if a shelf layout
        int rightPressNumber = disneyPlusAndroidTVCommonPage.get().scrollShelfSet(secondCollectionSet);

        // Verify the details' page is displayed and correct.
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), DETAILS_PAGE_LOAD_ERROR);
        sa.assertEquals(disneyPlusAndroidTVDetailsPageBase.get().getTitleImageContentDesc(), secondSetTitles.get(rightPressNumber),
                "Details content should match selected item");

        sa.assertAll();
    }

    @Test
    public void originalsPageAppearance() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67320"));
        CollectionRequest collectionRequest = CollectionRequest.builder()
                .region(country)
                .collectionType(DisneyCollectionType.PERSONALIZED_COLLECTION.getCollectionType())
                .account(entitledUser.get())
                .language(language).slug(DisneyStandardCollection.ORIGINALS.getSlug())
                .contentClass(DisneyStandardCollection.ORIGINALS.getSlug())
                .build();

        ContentCollection collection = disneySearchApi.get().getCollection(collectionRequest);

        DisneyCollectionSet featuredCollectionSet = collection.getCollectionSetsInfo().get(0);

        SetRequest featuredSetRequest = SetRequest.builder()
                .region(country)
                .language(language)
                .setId(featuredCollectionSet.getRefId())
                .refType(featuredCollectionSet.getRefType())
                .build();

        String firstFeaturedTitleApi = disneySearchApi.get().getSet(featuredSetRequest).getTitles().get(0);

        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.ORIGINALS,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());

        sa.assertTrue(disneyPlusAndroidTVOriginalsPageBase.get().isOpened(), LANDING_ERROR);
        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().isGlobalNavCollapsed(), GLOBAL_NAV_COLLAPSE_ERROR);

        // Check the first tile is focused
        sa.assertEquals(disneyPlusAndroidTVCommonPage.get().getAndroidTVUtilsInstance().getContentDescription(
                disneyPlusAndroidTVOriginalsPageBase.get().getMediaPosters().get(0)), firstFeaturedTitleApi,
                "Focus is not on first item");

        sa.assertAll();
    }

    @Test
    public void originalsComingSoonAppearance() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67328"));
        String comingSoonText = "Coming to Disney+ ";
        String comingSoonMovieId;
        String comingSoonMovieName;

        CollectionRequest collectionRequest = CollectionRequest.builder()
                .region(country)
                .collectionType(DisneyCollectionType.PERSONALIZED_COLLECTION.getCollectionType())
                .account(entitledUser.get())
                .language(language).slug(DisneyStandardCollection.ORIGINALS.getSlug())
                .contentClass(DisneyStandardCollection.ORIGINALS.getSlug())
                .build();

        try {
            comingSoonMovieId = disneySearchApi.get().retrieveComingSoonMovieWithTrailer(collectionRequest);
            ContentMovie moviesDetailsApi = disneySearchApi.get().getMovie(comingSoonMovieId, country, language);
            comingSoonMovieName = moviesDetailsApi.getVideoTitle();
        } catch (DisneySearchResultNotFoundException e) {
            throw new SkipException("Skipping Test - " + e);
        }

        ContentCollection collection = disneySearchApi.get().getCollection(collectionRequest);

        DisneyCollectionSet featuredCollectionSet = collection.getCollectionSetsInfo().get(0);
        SetRequest featuredSetRequest = SetRequest.builder()
                .region(country)
                .language(language)
                .setId(featuredCollectionSet.getRefId())
                .refType(featuredCollectionSet.getRefType())
                .build();

        int featuredTitlesSize = disneySearchApi.get().getSet(featuredSetRequest).getTitles().size();
        int featuredSetRowCount = featuredTitlesSize % 4 == 0 ? featuredTitlesSize / 4 : featuredTitlesSize / 4 + 1;

        // Determine the Coming Soon set index
        int comingSoonSetIndex = 0;
        int extraSet = 0;
        for (String setName : collection.getCollectionSetTitles()) {
            // Catch any "Because You Watched" sets since this is a new account.
            if (setName.contains("Because You Watched")) {
                extraSet ++;
            }
            if (setName.equals("Coming Soon")) {
                comingSoonSetIndex = collection.getCollectionSetTitles().indexOf(setName) - extraSet;
            }
        }

        int pressDownCount = comingSoonSetIndex + featuredSetRowCount - 2;
        Assert.assertTrue(pressDownCount > -1, "pressDownCount should be 0 or greater");

        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.ORIGINALS,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVOriginalsPageBase.get().isOpened(), LANDING_ERROR);

        // Scroll to Coming Soon set and select tile.
        disneyPlusAndroidTVCommonPage.get().pressDown(pressDownCount);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), DETAILS_PAGE_LOAD_ERROR);
        sa.assertEquals(disneyPlusAndroidTVDetailsPageBase.get().getTitleImageContentDesc(), comingSoonMovieName,
                "Incorrect details page shown");

        // Check Coming Soon Details Page elements.
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().getTrailerButton().isPresent(),
                "Trailer button should be present:");
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().getWatchlistButton().isPresent(),
                "Watchlist button should be present:");
        sa.assertFalse(disneyPlusAndroidTVDetailsPageBase.get().getPlayButton().isPresent(),
                "Play Button should not present:");
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().getPromoText().contains(comingSoonText),
                "Promo test should contain " + comingSoonText);
        sa.assertAll();
    }
}
