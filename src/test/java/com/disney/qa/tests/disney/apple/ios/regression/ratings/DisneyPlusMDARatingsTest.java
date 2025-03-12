package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.stream.IntStream;

import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_PREMIUM_MONTHLY_SINGAPORE;
import static com.disney.qa.common.constant.IConstantHelper.SG;
import static com.disney.qa.common.constant.RatingConstant.Rating.G;
import static com.disney.qa.common.constant.RatingConstant.Rating.M18;
import static com.disney.qa.common.constant.RatingConstant.Rating.NC16;
import static com.disney.qa.common.constant.RatingConstant.Rating.PG;
import static com.disney.qa.common.constant.RatingConstant.Rating.PG13;
import static com.disney.qa.common.constant.RatingConstant.Rating.R21;

public class DisneyPlusMDARatingsTest extends DisneyPlusRatingsBase {
    private final int DOWNLOAD_TIMEOUT = 150;
    private final int DOWNLOAD_POLLING = 15;

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69568"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MDA, SG})
    public void verifyRatingSystemSingaporeMDA_G() {
        ratingsSetup(DISNEY_PREMIUM_MONTHLY_SINGAPORE, G.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(G.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73178"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MDA, SG})
    public void verifyRatingSystemSingaporeMDA_PG() {
        ratingsSetup(DISNEY_PREMIUM_MONTHLY_SINGAPORE, PG.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(PG.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73179"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MDA, SG})
    public void verifyRatingSystemSingaporeMDA_PG13() {
        ratingsSetup(DISNEY_PREMIUM_MONTHLY_SINGAPORE, PG13.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(PG13.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73180"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MDA, SG})
    public void verifyRatingSystemSingaporeMDA_NC16() {
        ratingsSetup(DISNEY_PREMIUM_MONTHLY_SINGAPORE, NC16.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(NC16.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73181"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MDA, SG})
    public void verifyRatingSystemSingaporeMDA_M18() {
        ratingsSetup(DISNEY_PREMIUM_MONTHLY_SINGAPORE, M18.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(M18.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73182"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MDA, TestGroup.R21, SG})
    public void verifyRatingSystemSingaporeMDA_R21() {
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusVerifyAgeDOBCollectionIOSPageBase verifyAgeDOBPage = initPage(DisneyPlusVerifyAgeDOBCollectionIOSPageBase.class);
        DisneyPlusPinIOSPageBase pinPage = initPage(DisneyPlusPinIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        ratingsSetup(DISNEY_PREMIUM_MONTHLY_SINGAPORE, R21.getContentRating(), getCountry());
        // search results validation
        homePage.clickSearchIcon();
        searchPage.searchForMedia(CONTENT_TITLE.get());
        sa.assertTrue(searchPage.isRatingPresentInSearchResults(R21.getContentRating()), "Rating was not found in search results");
        searchPage.getDynamicAccessibilityId(CONTENT_TITLE.get()).click();
        // adding title to watchlist and details page validation
        detailsPage.verifyRatingsInDetailsFeaturedArea(R21.getContentRating(), sa);
        detailsPage.addToWatchlist();
        detailsPage.clickPlayButton(SHORT_TIMEOUT);
        sa.assertTrue(verifyAgePage.isOpened(), "'Verify your age' page should open");
        verifyAgePage.clickIAm21PlusButton();
        Assert.assertTrue(passwordPage.isOpened(), "Password page did not open");
        passwordPage.enterPassword(getUnifiedAccount());
        sa.assertTrue(verifyAgeDOBPage.isOpened(), "Enter your birthdate page should open");
        verifyAgeDOBPage.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(), Person.ADULT.getYear());
        verifyAgeDOBPage.clickVerifyAgeButton();
        sa.assertTrue(pinPage.isR21PinPageOpen(), "R21 pin page did not open");
        IntStream.range(0, 4).forEach(i -> {
            pinPage.getTypeKey(String.valueOf(i)).click();
        });
        pinPage.getR21SetPinButton().click();
        // video player validation
        Assert.assertTrue(videoPlayer.isOpened(), "Video did not begin to play for first R21 content");
        videoPlayer.clickBackButton();
        // downloads validation
        detailsPage.startDownload();
        if (detailsPage.isSeriesDownloadButtonPresent()) {
            detailsPage.waitForSeriesDownloadToComplete(DOWNLOAD_TIMEOUT, DOWNLOAD_POLLING);
        } else {
            detailsPage.waitForMovieDownloadComplete(DOWNLOAD_TIMEOUT, DOWNLOAD_POLLING);
        }
        navigateToTab((DisneyPlusApplePageBase.FooterTabs.DOWNLOADS));
        Assert.assertTrue(downloads.isOpened(), "Downloads page did not open");

        // watchlist validation
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.getDynamicCellByLabel(moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST)).click();
        sa.assertTrue(moreMenu.getTypeCellLabelContains(CONTENT_TITLE.get()).isPresent(), "Media content title was not added to the watchlist");

        sa.assertAll();
    }
}

