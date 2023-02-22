package com.disney.qa.tests.disney.android.tv;

import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.api.account.PatchType;
import com.disney.qa.api.client.requests.content.CollectionRequest;
import com.disney.qa.api.client.requests.content.ContinueWatchingSetRequest;
import com.disney.qa.api.client.responses.content.ContentCollection;
import com.disney.qa.api.client.responses.content.ContentSet;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.search.DisneySearchApi;
import com.disney.qa.api.search.assets.DisneyCollectionType;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage;
import com.disney.qa.disney.android.pages.tv.utility.navhelper.NavHelper;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.fasterxml.jackson.databind.JsonNode;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import org.apache.commons.lang.WordUtils;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage.HomePageItems.GET_CONTAINER_ORIGINAL_TYPE;
import static com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage.HomePageItems.GET_DISNEY_BRAND_KEYS;
import static com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage.HomePageItems.GET_HERO_TILE;
import static com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage.HomePageItems.GET_ITEMS_IN_HERO_CONTAINER;
import static com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage.HomePageItems.REF_ID_BY_TYPE;
import static com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage.HomePageItems.RIPCUT_MASTER_ID;
import static com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage.HomePageItems.SET_TITLE;
import static com.disney.qa.tests.disney.DisneyPlusBaseTest.*;

public class DisneyPlusAndroidTVHomeScreenTests extends DisneyPlusAndroidTVBaseTest {
    private static final String HOME_SLUG = R.TESTDATA.get("disney_home_content_class");
    private static final String HERO_CONTAINER = "HeroContainer";
    private static final String HERO_ERROR = "Index Before: %d Index After: %d";
    private static final String HERO_CAROUSEL_PASSIVE_CYCLE_ERROR = "Carousel should automatically cycle to the right";
    private static final int DISNEY_INDEX = 0;
    private static final int PIXAR_INDEX = 1;
    private static final int MARVEL_INDEX = 2;
    private static final int STAR_WARS_INDEX = 3;
    private static final int NAT_GEO_INDEX = 4;
    final int CAROUSEL_ROTATION_WAIT_PERIOD = 5;

    @Test(description = "Verify that content tiles have an aspect ratio of 16:9")
    public void contentTilesAspectRatio() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67230"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1818"));
        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());
        List<ExtendedWebElement> postersList = disneyPlusAndroidTVDiscoverPage.get().getShelfContentPosters();
        // Only checking first 5 as all the others are cropped off on the screen
        IntStream.range(0, 5).forEach(i -> {
            double aspectRatio = (double) postersList.get(i).getSize().getWidth() / postersList.get(i).getSize().getHeight();
            sa.assertFalse(aspectRatio < 1.77 || aspectRatio > 1.85,
                    String.format("Aspect ratio for content tile %s is %f ",
                            disneyPlusAndroidTVCommonPage.get().getAndroidTVUtilsInstance().getContentDescription(postersList.get(i)), aspectRatio));
        });
        checkAssertions(sa);
    }

    @Test(description = "Selecting a tile opens the content's Details Page")
    public void detailsFromContentTile() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67236"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1822"));
        SoftAssert sa = new SoftAssert();
        List<ContentSet> homeSets = disneySearchApi.get().getAllSetsInHomeCollection(entitledUser.get(), country, language, DisneyCollectionType.PERSONALIZED_COLLECTION.getCollectionType());
        login(entitledUser.get());

        // Navigate to the first carousel from hero carousel
        disneyPlusAndroidTVCommonPage.get().pressDown(2);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();

        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), DETAILS_PAGE_LOAD_ERROR);
        sa.assertEquals(disneyPlusAndroidTVDetailsPageBase.get().getTitleImageContentDesc(), homeSets.get(1).getTitle(0));

        checkAssertions(sa);
    }

    @Test(description = "Check Continue Watching set data")
    public void homeScreenWithContentPlayed() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67020", "XCDQA-67022", "XCDQA-67024",
                "XCDQA-67027", "XCDQA-67028", "XCDQA-67026", "XCDQA-67228"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1808"));
        SoftAssert sa = new SoftAssert();

        // Login and start playback of the first piece of content on the Series page to get the Continue Watching set
        login(entitledUser.get());

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.SERIES,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        Assert.assertTrue(disneyPlusAndroidTVSeriesPageBase.get().isOpened(), SERIES_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVCommonPage.get().pressDown(1);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        Assert.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), DETAILS_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVDetailsPageBase.get().navigateToEpisodeFromPlayBtnForSeries(1, 1);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        pause(30);

        // Return to Series and select a 2nd piece of content
        disneyPlusAndroidTVCommonPage.get().pressBackTimes(1);
        Assert.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), DETAILS_PAGE_LOAD_ERROR);
        while (disneyPlusAndroidTVDetailsPageBase.get().isOpened()) {
            disneyPlusAndroidTVCommonPage.get().pressBackTimes(1);
        }
        Assert.assertTrue(disneyPlusAndroidTVSeriesPageBase.get().isOpened(), SERIES_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVCommonPage.get().pressRight(1);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        Assert.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), DETAILS_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVDetailsPageBase.get().navigateToEpisodeFromPlayBtnForSeries(1, 1);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        pause(30);

        // Return to the Home page and confirm Continue Watching set comes back as expected with play icon on tiles
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.HOME,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);

        // Grab CW shelf data
        CollectionRequest collectionRequest = CollectionRequest.builder().collectionType(DisneyCollectionType.PERSONALIZED_COLLECTION
                .getCollectionType()).account(entitledUser.get()).region(country).language(language).contentClass(HOME_SLUG).slug(HOME_SLUG).build();
        ContentCollection contentCollection = disneySearchApi.get().getCollection(collectionRequest);
        String setType = R.TESTDATA.get("disney_continue_watching_set");
        String setId = contentCollection.getSetsByRefType(setType).get(0).getRefId();

        ContinueWatchingSetRequest continueWatchingSetRequest = ContinueWatchingSetRequest
                .builder().language(language).region(country).setId(setId).account(entitledUser.get()).build();
        JsonNode continueWatchingSet = disneySearchApi.get().getContinueWatchingSet(continueWatchingSetRequest).getJsonNode().get(0);
        String shelf = DisneySearchApi.parseValueFromJson(continueWatchingSet.toString(), String.format(SET_TITLE.getValue(), setType)).get(0);
        String firstContentTitle = DisneySearchApi.parseValueFromJson(continueWatchingSet.toString(), "$..text.title.full.series..content").get(0);
        String secondContentTitle = DisneySearchApi.parseValueFromJson(continueWatchingSet.toString(), "$..text.title.full.series..content").get(1);

        // Navigate to CW shelf, confirm content, and check for Play Icon on first tile
        disneyPlusAndroidTVDiscoverPage.get().navigateToShelf(shelf);
        sa.assertEquals(shelf,disneyPlusAndroidTVDiscoverPage.get().getFocusedShelfTitle(),"Shelf title should be CW after navigating to CW.");
        sa.assertEquals(disneyPlusAndroidTVDiscoverPage.get().getAssetTitle(), firstContentTitle, "First item in CW set was not the last item watched.");
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isPlayIconVisible(), "Play icon should be displayed on CW episodic tiles.");

        // Focus boundary check for Continue Watching set and check no Play Icon on non-CW set tiles
        // Validate lateral navigation and focus...
        disneyPlusAndroidTVCommonPage.get().pressLeft(1);
        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().isNavSelectionFocused(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.HOME), "Global Nav should be focused after pressing left from CW set.");
        disneyPlusAndroidTVCommonPage.get().pressRight(2);
        sa.assertEquals(disneyPlusAndroidTVDiscoverPage.get().getAssetTitle(), secondContentTitle, "Focus should move to second/last item in CW set.");
        disneyPlusAndroidTVCommonPage.get().pressRight(1); // Focus should not move as it is already on the last item in the set
        sa.assertEquals(disneyPlusAndroidTVDiscoverPage.get().getAssetTitle(), secondContentTitle, "Last item should be focused when pressing right from last item.");

        // Validate vertical navigation and focus.
        disneyPlusAndroidTVCommonPage.get().pressUp(1);
        sa.assertTrue(!disneyPlusAndroidTVDiscoverPage.get().isPlayIconVisible(), "Play icon should not be displayed on non-episodic tiles.");
        sa.assertNotEquals(shelf,disneyPlusAndroidTVDiscoverPage.get().getFocusedShelfTitle(),"Shelf title should not be CW after pressing UP from CW set." );

        disneyPlusAndroidTVCommonPage.get().pressDown(1);
        sa.assertEquals(shelf,disneyPlusAndroidTVDiscoverPage.get().getFocusedShelfTitle(),"Shelf title should be CW after returning to CW set.");

        disneyPlusAndroidTVCommonPage.get().pressDown(1);
        sa.assertNotEquals(shelf,disneyPlusAndroidTVDiscoverPage.get().getFocusedShelfTitle(),"Shelf title should not be CW after pressing DOWN from CW set.");

        disneyPlusAndroidTVCommonPage.get().pressUp(1);
        sa.assertEquals(shelf,disneyPlusAndroidTVDiscoverPage.get().getFocusedShelfTitle(),"Shelf title should be CW after returning to CW set.");

        // Check aspect ratios on CW tiles - Specifically look at index 12-14 because we are only looking for the tiles in CW set
        List<ExtendedWebElement> allVisibleTiles = disneyPlusAndroidTVDiscoverPage.get().getAllTiles();
        IntStream.range(12,14).forEach(i -> {
            double aspectRatio = (double) allVisibleTiles.get(i).getSize().getWidth() / allVisibleTiles.get(i).getSize().getHeight();

            double minAspectRatio = 1.77;
            double maxAspectRatio = 1.85;
            sa.assertTrue(aspectRatio >= minAspectRatio && aspectRatio <= maxAspectRatio, String.format("Aspect ratio %f for tile %s should be between %f and %f.",
                    aspectRatio, disneyPlusAndroidTVCommonPage.get().getAndroidTVUtilsInstance().getContentDescription(allVisibleTiles.get(i)),
                    minAspectRatio, maxAspectRatio));
        });
        checkAssertions(sa);
    }

    @Test(description = "Horizontally scrolling through content on the Home Page works correctly")
    public void horizontallyScrollThroughContent() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67234"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1821"));
        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());

        // Navigate to the first non hero and non brand carousel
        disneyPlusAndroidTVCommonPage.get().pressDown(2);

        // Scroll right and left in row
        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().getAndroidTVUtilsInstance()
                .isFocused(disneyPlusAndroidTVDiscoverPage.get().getAllTiles().get(7)), "Focus should be on first tile");
        disneyPlusAndroidTVCommonPage.get().pressRight(3);
        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().getAndroidTVUtilsInstance()
                .isFocused(disneyPlusAndroidTVDiscoverPage.get().getAllTiles().get(10)), "Focus should be on fourth tile");
        disneyPlusAndroidTVCommonPage.get().pressLeft(1);
        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().getAndroidTVUtilsInstance()
                .isFocused(disneyPlusAndroidTVDiscoverPage.get().getAllTiles().get(9)), "Focus should be on third tile");

        checkAssertions(sa);
    }

    @Test(description = "Verify that Brand Tiles appear as expected")
    public void verifyBrandsArePresent() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67200"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1816"));

        CollectionRequest collectionRequest = CollectionRequest.builder().collectionType("PersonalizedCollection")
                .account(entitledUser.get()).region(country).language(language).contentClass(HOME_SLUG).slug(HOME_SLUG).build();
        JsonNode collectionBody = disneySearchApi.get().getCollection(collectionRequest).getJsonNode();
        List<String> brandKeys = apiProvider.get().queryResponse(collectionBody, GET_DISNEY_BRAND_KEYS.getValue());
        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());
        List<String> actualBrands = disneyPlusAndroidTVDiscoverPage.get().getBrandTilesContentDesc(brandKeys.size());

        List<String> expectedBrands = new ArrayList<>();
        brandKeys.forEach(item -> expectedBrands.add(WordUtils.capitalizeFully(item.replaceAll("-", " "))));

        for (int i = 0; i < brandKeys.size(); i++) {
            sa.assertEquals(actualBrands.get(i), expectedBrands.get(i), "Brand tiles should be in the expected order");
        }

        checkAssertions(sa);
    }

    @Test(description = "Verify the Disney brand tile is displayed correctly and in the correct location")
    public void verifyBrandTileDisney() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67204"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1817"));
        verifyBrandTile(DISNEY_INDEX);
    }

    @Test(description = "Verify the Marvel brand tile is displayed correctly and in the correct location")
    public void verifyBrandTileMarvel() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67208"));
        verifyBrandTile(MARVEL_INDEX);
    }

    @Test(description = "Verify the National Geographic brand tile is displayed correctly and in the correct location")
    public void verifyBrandTileNatGeo() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67212"));
        verifyBrandTile(NAT_GEO_INDEX);
    }

    @Test(description = "Verify the Pixar brand tile is displayed correctly and in the correct location")
    public void verifyBrandTilePixar() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67206"));
        verifyBrandTile(PIXAR_INDEX);
    }

    @Test(description = "Verify the Star Wars brand tile is displayed correctly and in the correct location")
    public void verifyBrandTileStarWars() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67210"));
        verifyBrandTile(STAR_WARS_INDEX);
    }

    @Test(description = "Verify returning to the Home Page resumes Hero Carousel rotation if previously stopped")
    public void verifyHeroCarouselResumesRotation() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-96085", "XCDQA-67080"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1812"));
        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());
        // Ensure home page loads properly
        pause(CAROUSEL_ROTATION_WAIT_PERIOD);
        disneyPlusAndroidTVCommonPage.get().pressRight(1);
        int indexAfterRight = disneyPlusAndroidTVDiscoverPage.get().getHeroCarouselIndexSelected();
        // Wait twice the duration of a single hero carousel move
        pause(CAROUSEL_ROTATION_WAIT_PERIOD * 2);
        int indexAfterTen = disneyPlusAndroidTVDiscoverPage.get().getHeroCarouselIndexSelected();
        sa.assertEquals(indexAfterRight, indexAfterTen, String.format(HERO_ERROR, indexAfterRight, indexAfterTen));

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.SERIES, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVSeriesPageBase.get().isOpened(), SERIES_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.HOME, disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());

        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);
        int currentIndex = disneyPlusAndroidTVDiscoverPage.get().getHeroCarouselIndexSelected();
        pause(CAROUSEL_ROTATION_WAIT_PERIOD);
        int indexAfterRotation = disneyPlusAndroidTVDiscoverPage.get().getHeroCarouselIndexSelected();
        sa.assertNotEquals(currentIndex, indexAfterRotation, "Index before and After are the same");

        checkAssertions(sa);
    }

    @Test(description = "Verify the Hero Carousel rotates every 5 seconds")
    public void verifyHeroCarouselRotation() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67074", "XCDQA-67076"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1810"));
        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());
        int initialIndex = disneyPlusAndroidTVDiscoverPage.get().getHeroCarouselIndexSelected();
        pause(CAROUSEL_ROTATION_WAIT_PERIOD); // Ensure that at least 5 seconds have passed after user lands on login page
        int oneRotationIndex = disneyPlusAndroidTVDiscoverPage.get().getHeroCarouselIndexSelected();
        sa.assertNotEquals(oneRotationIndex, initialIndex, HERO_CAROUSEL_PASSIVE_CYCLE_ERROR);
        pause(CAROUSEL_ROTATION_WAIT_PERIOD); // Wait for carousel to move to the next hero tile
        sa.assertNotEquals(disneyPlusAndroidTVDiscoverPage.get().getHeroCarouselIndexSelected(), oneRotationIndex, HERO_CAROUSEL_PASSIVE_CYCLE_ERROR);

        checkAssertions(sa);
    }

    @Test(description = "Verify the Hero Carousel stops after the user cycles left or right")
    public void verifyHeroCarouselStopsAfterLeftAndRight() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67078"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1811"));
        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());
        Assert.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR); // Ensure that home page has properly loaded
        pause(CAROUSEL_ROTATION_WAIT_PERIOD);
        disneyPlusAndroidTVCommonPage.get().pressRight(1);
        int indexAfterRight = disneyPlusAndroidTVDiscoverPage.get().getHeroCarouselIndexSelected();
        pause(CAROUSEL_ROTATION_WAIT_PERIOD * 2); // Wait twice the duration of a single hero carousel move
        int indexAfterTen = disneyPlusAndroidTVDiscoverPage.get().getHeroCarouselIndexSelected();
        sa.assertEquals(indexAfterRight, indexAfterTen, String.format(HERO_ERROR, indexAfterRight, indexAfterTen));
        pause(CAROUSEL_ROTATION_WAIT_PERIOD);
        disneyPlusAndroidTVCommonPage.get().pressLeft(1);
        int indexAfterLeft = disneyPlusAndroidTVDiscoverPage.get().getHeroCarouselIndexSelected();
        pause(CAROUSEL_ROTATION_WAIT_PERIOD * 2); // Wait twice the duration of a single hero carousel move
        indexAfterTen = disneyPlusAndroidTVDiscoverPage.get().getHeroCarouselIndexSelected();
        sa.assertEquals(indexAfterLeft, indexAfterTen, String.format(HERO_ERROR, indexAfterLeft, indexAfterTen));

        checkAssertions(sa);
    }
    
    @Test(description = "Verify Home contains row containers with images and metadata")
    public void verifyHomePageLayout() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67224"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1807"));
        SoftAssert sa = new SoftAssert();
        login(entitledUser.get());
        disneyPlusAndroidTVDiscoverPage.get().verifyHomePageLayout(language, disneySearchApi.get(), entitledUser.get(), sa);
        checkAssertions(sa);
    }

    @Test(description = "Verify Hero Carousel indicators match the expected count")
    public void verifyIconIndicatorsForHeroCarousel() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67072"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1809"));
        SoftAssert sa = new SoftAssert();

        CollectionRequest collectionRequest = CollectionRequest.builder().collectionType("PersonalizedCollection")
                .account(entitledUser.get()).region(country).language(language).contentClass(HOME_SLUG).slug(HOME_SLUG).build();
        JsonNode homeSet = disneySearchApi.get().getCollection(collectionRequest).getJsonNode();

        String heroContainerSetId = DisneySearchApi.parseValueFromJson(homeSet.toString(), String.format(REF_ID_BY_TYPE.getValue(), HERO_CONTAINER)).get(0);
        ContentSet heroContainerSet = disneySearchApi.get().getCuratedSet(entitledUser.get(), heroContainerSetId, country, language);
        int itemsInHeroContainer = heroContainerSet.getHits();
        // Even if the hits come back > 15 the page size is set to 15, the hero container will only display 15 items max
        itemsInHeroContainer = Math.min(itemsInHeroContainer, 15);

        login(entitledUser.get());
        Assert.assertEquals(disneyPlusAndroidTVDiscoverPage.get().getHeroIndicatorSize(), itemsInHeroContainer);

        checkAssertions(sa);
    }

    @Test(description = "Verify Original content tile image appears as expected")
    public void verifyOriginalImage() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67082"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1813"));
        SoftAssert sa = new SoftAssert();

        CollectionRequest collectionRequest = CollectionRequest.builder().language(language).account(entitledUser.get()).slug(HOME_SLUG).contentClass(HOME_SLUG).build();
        JsonNode collectionBody = disneySearchApi.get().getCollection(collectionRequest).getJsonNode();
        List<String> originalsPresent =  DisneySearchApi.parseValueFromJson((collectionBody.toString()), String.format(GET_CONTAINER_ORIGINAL_TYPE.getValue(), HERO_CONTAINER));
        int originalsIndex = disneyPlusAndroidTVDiscoverPage.get().getOriginalIndexIfPresent(originalsPresent);
        String masterId = DisneySearchApi.parseValueFromJson(collectionBody.toString(),
                String.format(GET_ITEMS_IN_HERO_CONTAINER.getValue(), originalsIndex) + GET_HERO_TILE.getValue() + RIPCUT_MASTER_ID.getValue()).get(0);

        login(entitledUser.get());
        // Stop carousel rotation
        disneyPlusAndroidTVCommonPage.get().pressRight(1);

        int currentIndex = disneyPlusAndroidTVDiscoverPage.get().getHeroCarouselIndexSelected();
        disneyPlusAndroidTVDiscoverPage.get().moveToHeroCarousel(currentIndex, originalsIndex);

        try {
            BufferedImage actualImage = disneyPlusAndroidTVDiscoverPage.get().getCurrentlySelectedLogo();
            BufferedImage expectedImage = ImageIO.read(new ByteArrayInputStream(apiProvider.get().getRipcutImageAsByteArray(masterId, actualImage.getWidth())));
            ByteArrayOutputStream byteArrayOutputStreamActual = new ByteArrayOutputStream();

            expectedImage = expectedImage.getSubimage(expectedImage.getWidth() - 240, 0, 200, 200);

            actualImage = actualImage.getSubimage(actualImage.getWidth() - 225, 0, 200, 200);
            ImageIO.write(actualImage, "png", byteArrayOutputStreamActual);

            Assert.assertTrue(compareImages(Base64.getEncoder().encode(byteArrayOutputStreamActual.toByteArray()), expectedImage, .77));
        } catch (Exception e) {
            throw new SkipException("Test Skipped due to " + e);
        }
        checkAssertions(sa);
    }

    @Test(description = "Verify series Details Page content in US locale")
    public void verifySeriesDetailsPageFromHeroCarouselUS() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67084"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1814"));
        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());
        // numeric rating limit for US is not a thing...
        List<String> results = disneyPlusAndroidTVDiscoverPage.get().getCarouselToMoveAndDescription(disneySearchApi.get(), entitledUser.get(), -1);
        disneyPlusAndroidTVCommonPage.get().pressRight(1);
        int currentIndex = disneyPlusAndroidTVDiscoverPage.get().getHeroCarouselIndexSelected();
        disneyPlusAndroidTVDiscoverPage.get().moveToHeroCarousel(currentIndex, Integer.parseInt(results.get(0)));
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();

        Assert.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), DETAILS_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVDetailsPageBase.get().navigateToEpisodeFromPlayBtnForSeries(Integer.parseInt(results.get(1)), Integer.parseInt(results.get(2)));

        List<String> episodeTexts = disneyPlusAndroidTVDetailsPageBase.get().getAssetDescriptions();

        sa.assertEquals(episodeTexts.get(episodeTexts.size() - 1), results.get(3));

        checkAssertions(sa);
    }

    @Test(description = "Verify series Details Page content in France locale")
    public void verifySeriesDetailsPageFromHeroCarouselNonUS() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67086"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1815"));
        DisneyAccount user = disneyAccountApi.get().createAccount("Yearly", "FR", language, "V1");
        Integer ratingsLimit = 16;
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());
        NavHelper navHelper = new NavHelper(androidTVUtils.getCastedDriver());

        try {
            disneyAccountApi.get().overrideLocations(user, "FR");
            disneyAccountApi.get().patchAccountAttributeForLocation(user, "FR", PatchType.ACCOUNT);
        } catch (Exception e) {
            throw new SkipException("Test skipped due to location override " + e);
        }

        SoftAssert sa = new SoftAssert();
        login(user);

        // Locale is France, so scope search of carousel to rating of "16" or less.
        List<String> results = disneyPlusAndroidTVDiscoverPage.get().
                getCarouselToMoveAndDescription(disneySearchApi.get(), user, ratingsLimit);
        sa.assertNotEquals(results.size(), 0, "Could not find hero carousel item with rating less than or equal to: " + ratingsLimit);

        disneyPlusAndroidTVCommonPage.get().pressRight(1);
        int currentIndex = disneyPlusAndroidTVDiscoverPage.get().getHeroCarouselIndexSelected();
        disneyPlusAndroidTVDiscoverPage.get().moveToHeroCarousel(currentIndex, Integer.parseInt(results.get(0)));
        navHelper.keyUntilElementVisible(()->disneyPlusAndroidTVDetailsPageBase.get().getDetailsBackground(), AndroidKey.DPAD_CENTER, 30, 3);

        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), SERIES_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVDetailsPageBase.get().
                navigateToEpisodeFromPlayBtnForSeries(Integer.parseInt(results.get(1)), Integer.parseInt(results.get(2)));
        List<String> episodeTexts = disneyPlusAndroidTVDetailsPageBase.get().getAssetDescriptions();
        sa.assertEquals(episodeTexts.get(episodeTexts.size() - 1), results.get(3));

        checkAssertions(sa);
    }

    @Test(description = "Verify shelf titles come back as expected")
    public void verifyShelfTitles() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67232"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1820"));
        SoftAssert sa = new SoftAssert();
        CollectionRequest collectionRequest = CollectionRequest.builder().collectionType("PersonalizedCollection")
                .account(entitledUser.get()).region(country).language(language).contentClass(HOME_SLUG).slug(HOME_SLUG).build();
        JsonNode homePageResponse = disneySearchApi.get().getCollection(collectionRequest).getJsonNode();
        List<String> expectedShelfTitles = DisneySearchApi.parseValueFromJson(homePageResponse.toString(), DisneyPlusAndroidTVDiscoverPage.HomePageItems.HOME_PAGE_ROW_TITLES.getValue());
        expectedShelfTitles.removeAll(Stream.of("Because You Watched ${title}", "Continue Watching", "Watch Again").collect(Collectors.toList()));

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().pressDown(2);

        for (int i = 0; i < expectedShelfTitles.size() - 1; i++) {
            sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().getShelfTitles().contains(expectedShelfTitles.get(i)),
                    "Row tiles should have the expected titles");
            disneyPlusAndroidTVCommonPage.get().pressDown(1);
        }

        checkAssertions(sa);
    }

    public void verifyBrandTile(int brandIndex) {
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        NavHelper navHelper = new NavHelper(this.getCastedDriver());

        login(entitledUser.get());
        Assert.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVCommonPage.get().pressDown(1);
        switch (brandIndex) {
            case DISNEY_INDEX:
                navHelper.keyUntilElementDescContains(() -> disneyPlusAndroidTVDiscoverPage.get().getFocusedHeroTile(),
                        AndroidKey.DPAD_RIGHT, "Disney");
                aliceDriver.screenshotAndRecognize().isLabelPresent(sa, AliceLabels.DISNEY_LOGO.getText());
                break;
            case PIXAR_INDEX:
                navHelper.keyUntilElementDescContains(() -> disneyPlusAndroidTVDiscoverPage.get().getFocusedHeroTile(),
                        AndroidKey.DPAD_RIGHT, "Pixar");
                aliceDriver.screenshotAndRecognize().isLabelPresent(sa, AliceLabels.PIXAR_LOGO.getText());
                break;
            case MARVEL_INDEX:
                navHelper.keyUntilElementDescContains(() -> disneyPlusAndroidTVDiscoverPage.get().getFocusedHeroTile(),
                        AndroidKey.DPAD_RIGHT, "Marvel");
                aliceDriver.screenshotAndRecognize().isLabelPresent(sa, AliceLabels.MARVEL_LOGO.getText());
                break;
            case STAR_WARS_INDEX:
                navHelper.keyUntilElementDescContains(() -> disneyPlusAndroidTVDiscoverPage.get().getFocusedHeroTile(),
                        AndroidKey.DPAD_RIGHT, "Star Wars");
                aliceDriver.screenshotAndRecognize().isLabelPresent(sa, AliceLabels.STAR_WARS_LOGO.getText());
                break;
            case NAT_GEO_INDEX:
                navHelper.keyUntilElementDescContains(() -> disneyPlusAndroidTVDiscoverPage.get().getFocusedHeroTile(),
                        AndroidKey.DPAD_RIGHT, "National Geographic");
                aliceDriver.screenshotAndRecognize().isLabelPresent(sa, AliceLabels.NAT_GEO_LOGO.getText());
                break;
        }
        sa.assertAll();
    }
}
