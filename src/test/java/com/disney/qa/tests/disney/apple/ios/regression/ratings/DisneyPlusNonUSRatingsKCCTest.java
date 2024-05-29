package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.RatingConstant.Country.KR;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

public class DisneyPlusNonUSRatingsKCCTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75187"})
    @Test(description = "Ratings-South Korea 12+ KCC", groups = {"NonUS-Ratings"})
    public void verifyRatingSystemSouthKoreaKCC7() {
        ratingsSetup(SEVEN_PLUS.getContentRating(), KOREAN_LANG, KR.toString());
        confirmRegionalRatingsDisplays(KCC_7, DictionaryKeys.RATING_KCC_7.getText());
    }
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75186"})
    @Test(description = "Ratings-South Korea 12+ KCC", groups = {"NonUS-Ratings"})
    public void verifyRatingSystemSouthKoreaKCC12() {
        ratingsSetup(TWELVE_PLUS.getContentRating(), KOREAN_LANG, KR.toString());
        confirmRegionalRatingsDisplays(TWELVE_PLUS.getContentRating(), DictionaryKeys.RATING_KCC_12.getText());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75185"})
    @Test(description = "Ratings-South Korea 15+ KCC", groups = {"NonUS-Ratings"})
    public void verifyRatingSystemSouthKoreaKCC15() {
        ratingsSetup(FIFTEEN_PLUS.getContentRating(), KOREAN_LANG, KR.toString());
        confirmRegionalRatingsDisplays(FIFTEEN_PLUS.getContentRating(), DictionaryKeys.RATING_KCC_15.getText());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75184"})
    @Test(description = "Ratings-South Korea 15+ KCC", groups = {"NonUS-Ratings"})
    public void verifyRatingSystemSouthKoreaKCC19() {
        ratingsSetup(NINETEEN_PLUS.getContentRating(), KOREAN_LANG, KR.toString(), true);
        confirmRegionalRatingsDisplays(NINETEEN_PLUS.getContentRating(), DictionaryKeys.RATING_KCC_19.getText());
    }
}
