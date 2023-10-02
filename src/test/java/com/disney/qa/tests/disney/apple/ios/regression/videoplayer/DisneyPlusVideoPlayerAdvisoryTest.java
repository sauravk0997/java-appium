package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.qa.api.client.responses.content.ContentMovie;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Map;

public class DisneyPlusVideoPlayerAdvisoryTest extends DisneyBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61187"})
    @Test(description = "Video Player > Content Rating Overlay > Verify Additional Advisory > smoking_disclaimer", groups = {"Video Player"})
    @Maintainer("gkrishna1")
    public void verifyAdditionalAdvisorySmokingDisclaimer() {
        initialSetup();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(disneyAccount.get());

        //TODO: Add 'Who Framed Roger Rabbit' in DisneySeries.java
        ContentMovie series = searchApi.get().getMovie("20GDm8DYpIsC", languageUtils.get().getLocale(), languageUtils.get().getUserLanguage());
        String dictRatingVerbiage = detailsPage.getDictionary().formatPlaceholderString(detailsPage.getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.VIDEO_PLAYER_RATING.getText()),
                Map.of("rating", series.getContentRatingsValue(), "rating_reasons", ""));
        String expectedSmokingDisclaimer = detailsPage.getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.RATINGS, DictionaryKeys.SMOKING_DISCLAIMER.getText());
        homePage.clickSearchIcon();
        homePage.getSearchNav().click();
        searchPage.searchForMedia("Who Framed Roger Rabbit");
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.clickPlayButton();

        sa.assertTrue(videoPlayer.getAdvisoryLabelText().contains(expectedSmokingDisclaimer), "Smoking disclaimer warning not found on video player");
        videoPlayer.clickBackButton();
        detailsPage.clickContinueButton();
        sa.assertTrue(videoPlayer.getRatingLabelText().contains(dictRatingVerbiage.split("\\.")[0]), "Rating label displayed on player is not as expected");
        sa.assertAll();
    }
}
