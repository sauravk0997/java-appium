package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.util.*;
import com.zebrunner.agent.core.annotation.*;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.*;

import static com.disney.qa.common.constant.RatingConstant.BRAZIL;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

public class DisneyPlusBrazilDJCTQRatingsTest extends DisneyPlusRatingsBase {
    private boolean isMovie;

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68359"})
    @Test(groups = {TestGroup.NON_US_RATINGS})
    public void verifyBrazilRatingL() {
        ratingsSetup(L.getContentRating(), BRAZIL_LANG, BRAZIL);
        confirmRegionalRatingsDisplays(L.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73142"})
    @Test(groups = {TestGroup.NON_US_RATINGS})
    public void verifyBrazilRating10() {
        ratingsSetup(TEN.getContentRating(), BRAZIL_LANG, BRAZIL);
        confirmRegionalRatingsDisplays(TEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73143"})
    @Test(groups = {TestGroup.NON_US_RATINGS})
    public void verifyBrazilRating12() {
        ratingsSetup(TWELVE.getContentRating(), BRAZIL_LANG, BRAZIL);
        confirmRegionalRatingsDisplays(TWELVE.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74643"})
    @Test(groups = {TestGroup.NON_US_RATINGS})
    public void verifyBrazilSeasonLevelRating() {
//        disney_prod_brazil_12_series_deeplink
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        ratingsSetup(BRAZIL_LANG, BRAZIL);
        launchDeeplink(true, R.TESTDATA.get("disney_prod_brazil_12_series_deeplink"), 10);
        detailsPage.clickOpenButton();
        pause(5);
        System.out.println(getDriver().getPageSource());
        LOGGER.info("Is season rating text present? " + detailsPage.getStaticTextByLabelContains("Classificação da Temporada 1:").isPresent());
        LOGGER.info("what is SEASON RATING DICT KEY? " + detailsPage.getSeasonRating().getText());
        Assert.assertTrue(detailsPage.isSeasonRatingPresent(), "Season rating was not found.");
    }


    private void validateSeasonLevelRatigin(String rating) {
        if (isMovie) {
            LOGGER.info("Testing against Movie content.");
            validateMovieContent(rating);
        } else {
            LOGGER.info("Testing against Series content.");
            validateSeriesContent(rating);
        }
    }
}
