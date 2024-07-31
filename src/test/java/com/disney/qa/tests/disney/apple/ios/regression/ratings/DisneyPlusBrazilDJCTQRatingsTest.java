package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.util.*;
import com.zebrunner.agent.core.annotation.*;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Map;

import static com.disney.qa.common.constant.RatingConstant.BRAZIL;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;
import static com.disney.qa.common.constant.RatingConstant.SINGAPORE;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.getDictionary;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.DETAILS_SEASON_RATING;

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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74582"})
    @Test(groups = {TestGroup.NON_US_RATINGS})
    public void verifyBrazilSeasonLevelRating() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        ratingsSetup(BRAZIL_LANG, BRAZIL);

//        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_PARTNER_VIVO_BR_STANDALONE, BRAZIL, RAZIL_LANG));
//        initialSetup();
//        handleAlert();
//        setAppToHomeScreen(getAccount());
//
//        initPage(DisneyPlusLoginIOSPageBase.class).submitEmail("qaittestguid+1722457787867436a@gsuite.disneyplustesting.com");
//        initPage(DisneyPlusPasswordIOSPageBase.class).submitPasswordForLogin("M1ck3yM0us3#");
        launchDeeplink(true, R.TESTDATA.get("disney_prod_brazil_12_series_deeplink"), 10);
        detailsPage.clickOpenButton();
//        Assert.assertTrue(detailsPage.isOpened(), "Detail page did not open.");
        String seasonRatingIntNum = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DETAILS_SEASON_RATING.getText()), Map.of("season_number", Integer.parseInt(detailsPage.getSeasonSelector())));
        String seasonRatingStringNum = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DETAILS_SEASON_RATING.getText()), Map.of("season_number", detailsPage.getSeasonSelector()));
        pause(5);
        System.out.println(getDriver().getPageSource());
//        Classificação da temporada 1:
//        LOGGER.info("Is season rating text present? " + detailsPage.getStaticTextByLabelContains("Classificação da Temporada 1:").isPresent());
//        LOGGER.info("what is SEASON RATING DICT KEY - int num? " + seasonRatingIntNum + "something");
//        LOGGER.info("is season rating text found - int num? " + detailsPage.getStaticTextByLabelContains(seasonRatingIntNum).isPresent());
//        LOGGER.info("what is SEASON RATING DICT KEY string num? " + seasonRatingStringNum + "something");
//
//        LOGGER.info("is season rating text found - string num? " + detailsPage.getStaticTextByLabelContains(seasonRatingStringNum).isPresent());
        String[] seasonRatingSplits = seasonRatingIntNum.split(" ");
        String tFromSeasonRating = seasonRatingSplits[2].split("e")[0].toUpperCase();
        LOGGER.info("t from season rating: " + tFromSeasonRating);

//        Assert.assertTrue(detailsPage.getStaticTextByLabelContains(seasonRatingSplits[0] + " " + seasonRatingSplits[1]));
        Assert.assertTrue(detailsPage.getStaticTextByLabelContains(seasonRatingIntNum).isPresent(), "Season rating was not found.");
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
