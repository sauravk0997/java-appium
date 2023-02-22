package com.disney.qa.tests.disney.android.tv;

import com.disney.qa.api.client.requests.content.CollectionRequest;
import com.disney.qa.api.search.DisneySearchApi;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVBrandPage;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.fasterxml.jackson.databind.JsonNode;
import com.qaprosoft.carina.core.foundation.utils.R;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.stream.IntStream;

import static com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage.HomePageItems.GET_DISNEY_BRAND_KEYS;
import static com.disney.qa.tests.disney.DisneyPlusBaseTest.*;

public class DisneyPlusAndroidTVBrandTests extends DisneyPlusAndroidTVBaseTest {
    ThreadLocal<List<String>> brandKeys = new ThreadLocal<>();

    private static final String HOME_SLUG = R.TESTDATA.get("disney_home_content_class");
    private static final int DISNEY_INDEX = 0;
    private static final int PIXAR_INDEX = 1;
    private static final int MARVEL_INDEX = 2;
    private static final int STAR_WARS_INDEX = 3;
    private static final int NAT_GEO_INDEX = 4;

    @BeforeMethod
    public void testSetup() {
        CollectionRequest collectionRequest = CollectionRequest.builder().account(entitledUser.get()).language(language).region(country).slug(HOME_SLUG).contentClass(HOME_SLUG).build();
        JsonNode collectionBody = disneySearchApi.get().getCollection(collectionRequest).getJsonNode();
        brandKeys.set(DisneySearchApi.parseValueFromJson(collectionBody.toString(), GET_DISNEY_BRAND_KEYS.getValue()));
    }

    @Test
    public void disneyBrandTraverseAndVerify() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67100"));
        verifyAndTraverseBrand(DISNEY_INDEX);
    }

    @Test
    public void pixarBrandTraverseAndVerify() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67166"));
        verifyAndTraverseBrand(PIXAR_INDEX);
    }

    @Test
    public void marvelBrandTraverseAndVerify() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67130"));
        verifyAndTraverseBrand(MARVEL_INDEX);
    }

    @Test
    public void starWarsBrandTraverseAndVerify() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67184"));
        verifyAndTraverseBrand(STAR_WARS_INDEX);
    }

    @Test
    public void natGeoBrandTraverseAndVerify() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67148"));
        verifyAndTraverseBrand(NAT_GEO_INDEX);
    }

    @Test
    public void verifyDisneyTileIsFocusedBackFromBrandLanding() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67096"));
        verifyTileIsFocusedBackFromBrandLanding(DISNEY_INDEX);
    }

    @Test
    public void verifyPixarTileIsFocusedBackFromBrandLanding() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67162"));
        verifyTileIsFocusedBackFromBrandLanding(PIXAR_INDEX);
    }

    @Test
    public void verifyMarvelTileIsFocusedBackFromBrandLanding() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67126"));
        verifyTileIsFocusedBackFromBrandLanding(MARVEL_INDEX);
    }

    @Test
    public void verifyStarWarsTileIsFocusedBackFromBrandLanding() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67180"));
        verifyTileIsFocusedBackFromBrandLanding(STAR_WARS_INDEX);
    }

    @Test
    public void verifyNatGeoTileIsFocusedBackFromBrandLanding() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67144"));
        verifyTileIsFocusedBackFromBrandLanding(NAT_GEO_INDEX);
    }

    @Test
    public void verifyOnlyTwoRowsVisibleDisneyBrandPage() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67102"));
        verifyOnlyTwoRowsVisibleBrandPage(DISNEY_INDEX);
    }

    @Test
    public void verifyOnlyTwoRowsVisiblePixarBrandPage() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67168"));
        verifyOnlyTwoRowsVisibleBrandPage(PIXAR_INDEX);
    }

    @Test
    public void verifyOnlyTwoRowsVisibleMarvelBrandPage() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67132"));
        verifyOnlyTwoRowsVisibleBrandPage(MARVEL_INDEX);
    }

    @Test
    public void verifyOnlyTwoRowsVisibleStarWarsBrandPage() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67186"));
        verifyOnlyTwoRowsVisibleBrandPage(STAR_WARS_INDEX);
    }

    @Test
    public void verifyOnlyTwoRowsVisibleNatGeoBrandPage() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67150"));
        verifyOnlyTwoRowsVisibleBrandPage(NAT_GEO_INDEX);
    }

    public void verifyOnlyTwoRowsVisibleBrandPage(int index) {
        SoftAssert sa = new SoftAssert();
        CollectionRequest collectionRequest = CollectionRequest.builder().collectionType("PersonalizedCollection").account(entitledUser.get())
                .region(country).language(entitledUser.get().getProfileLang()).contentClass("Brand").slug(brandKeys.get().get(index)).build();
        JsonNode disneyBrand = disneySearchApi.get().getCollection(collectionRequest).getJsonNode();

        int totalRows = Integer.parseInt(apiProvider.get().queryResponse(disneyBrand, DisneyPlusAndroidTVBrandPage.BrandItems.GET_NUMBER_OF_ROWS.getText()).get(0)) - 1;

        login(entitledUser.get());
        disneyPlusAndroidTVBrandPage.get().selectBrandTileFromHeroCarousel(index);
        sa.assertTrue(disneyPlusAndroidTVBrandPage.get().isOpened(), brandKeys.get().get(index) + BRAND_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVBrandPage.get().waitUntilFirstTileIsFocused();

        IntStream.range(0, totalRows - 1).forEach(i -> {
            sa.assertEquals(disneyPlusAndroidTVBrandPage.get().getNumberOfRowsDisplayed(), 2, "Two Rows were not displayed");
            disneyPlusAndroidTVCommonPage.get().pressDown(1);
        });
        sa.assertAll();
    }

    public void verifyAndTraverseBrand(int index) {
        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());
        disneyPlusAndroidTVBrandPage.get().selectBrandTileFromHeroCarousel(index);
        sa.assertTrue(disneyPlusAndroidTVBrandPage.get().isOpened(), brandKeys.get().get(index).toUpperCase() + BRAND_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVBrandPage.get().stopBrandLandingVideo();
        disneyPlusAndroidTVBrandPage.get().waitUntilFirstTileIsFocused();
        disneyPlusAndroidTVBrandPage.get().traverseAndVerifyContent(brandKeys.get().get(index), disneySearchApi.get(), entitledUser.get(), sa);
        sa.assertAll();
    }

    public void verifyTileIsFocusedBackFromBrandLanding(int index) {
        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());
        disneyPlusAndroidTVBrandPage.get().selectBrandTileFromHeroCarousel(index);
        sa.assertTrue(disneyPlusAndroidTVBrandPage.get().isOpened(), brandKeys.get().get(index) + BRAND_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVBrandPage.get().stopBrandLandingVideo();
        disneyPlusAndroidTVBrandPage.get().waitUntilFirstTileIsFocused();
        disneyPlusAndroidTVCommonPage.get().pressBackTimes(1);
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isBrandTileFocused(index), "Focus should be on " + brandKeys.get().get(index));

        sa.assertAll();
    }
}
