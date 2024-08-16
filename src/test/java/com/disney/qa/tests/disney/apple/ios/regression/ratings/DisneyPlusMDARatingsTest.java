package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.stream.IntStream;

import static com.disney.qa.common.constant.RatingConstant.Rating.G;
import static com.disney.qa.common.constant.RatingConstant.Rating.M18;
import static com.disney.qa.common.constant.RatingConstant.Rating.NC16;
import static com.disney.qa.common.constant.RatingConstant.Rating.PG;
import static com.disney.qa.common.constant.RatingConstant.Rating.PG13;
import static com.disney.qa.common.constant.RatingConstant.SINGAPORE;
import static com.disney.qa.common.constant.RatingConstant.Rating.R21;

public class DisneyPlusMDARatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69568"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MDA})
    public void verifyRatingSystemSingaporeMDA_G() {
        ratingsSetup(G.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        confirmRegionalRatingsDisplays(G.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73178"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MDA})
    public void verifyRatingSystemSingaporeMDA_PG() {
        ratingsSetup(PG.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        confirmRegionalRatingsDisplays(PG.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73179"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MDA})
    public void verifyRatingSystemSingaporeMDA_PG13() {
        ratingsSetup(PG13.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        confirmRegionalRatingsDisplays(PG13.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73180"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MDA})
    public void verifyRatingSystemSingaporeMDA_NC16() {
        ratingsSetup(NC16.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        confirmRegionalRatingsDisplays(NC16.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73181"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MDA})
    public void verifyRatingSystemSingaporeMDA_M18() {
        ratingsSetup(M18.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        confirmRegionalRatingsDisplays(M18.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73182"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MDA, TestGroup.R21})
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

        SoftAssert sa = new SoftAssert();

        ratingsSetup(R21.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        homePage.clickSearchIcon();
        searchPage.searchForMedia(contentTitle);
        // rating present in search results
        sa.assertTrue(searchPage.isRatingPresentInSearchResults(R21.getContentRating()), "Rating was not found in search results");

        // downloads validation
        detailsPage.getMovieDownloadButton().click();
        detailsPage.getDownloadNav().click();
        sa.assertTrue(downloads.isRatingPresent(R21.getContentRating()), R21.getContentRating() + " Rating was not found on movie downloads.");
        homePage.clickSearchIcon();
        detailsPage.verifyRatingsInDetailsFeaturedArea(R21.getContentRating(), sa);
        videoPlayer.validateRatingsOnPlayer(R21.getContentRating(), sa, detailsPage); // failing here
        detailsPage.waitForRestartButtonToAppear();
        detailsPage.validateRatingsInDetailsTab(R21.getContentRating(), sa);
        searchPage.getDisplayedTitles().get(0).click();
     //   searchPage.getDynamicAccessibilityId(contentTitle).click();


        detailsPage.clickPlayButton(SHORT_TIMEOUT);
        sa.assertTrue(verifyAgePage.isOpened(), "'Verify your age' page should open");
        verifyAgePage.clickIAm21PlusButton();
        passwordPage.enterPassword(getAccount());
        sa.assertTrue(verifyAgeDOBPage.isOpened(), "Enter your birthdate page should open");
        verifyAgeDOBPage.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(), Person.ADULT.getYear());
        verifyAgeDOBPage.clickVerifyAgeButton();
        sa.assertTrue(pinPage.isR21PinPageOpen(), "R21 pin page did not open");
        sa.assertTrue(pinPage.isR21PinPageOpen(), "R21 pin page did not open");

        IntStream.range(0, 4).forEach(i -> {
            pinPage.getTypeKey(String.valueOf(i)).click();
        });
        pressByElement(pinPage.getR21SetPinButton(), 1);

        confirmRegionalRatingsDisplays(R21.getContentRating());
        sa.assertTrue(searchPage.isRatingPresentInSearchResults(R21.getContentRating()), "Rating was not found in search results");

        sa.assertAll();
    }
}
