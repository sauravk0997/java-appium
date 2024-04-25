package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

public class DisneyPlusNonUSRatingsKCCTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75187"})
    @Test(description = "Ratings-South Korea 12+ KCC", groups = {"NonUS-Ratings"})
    public void verifyRatingSystemSouthKoreaKCC7() {
        ratingsSetup(KCC_7, KOREAN_LANG, KOREA_LOCALE);
        confirmRegionalRatingsDisplays(KCC_7, DictionaryKeys.RATING_KCC_7.getText());
    }
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75186"})
    @Test(description = "Ratings-South Korea 12+ KCC", groups = {"NonUS-Ratings"})
    public void verifyRatingSystemSouthKoreaKCC12() {
        ratingsSetup(KCC_12, KOREAN_LANG, KOREA_LOCALE);
        confirmRegionalRatingsDisplays(KCC_12, DictionaryKeys.RATING_KCC_12.getText());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75185"})
    @Test(description = "Ratings-South Korea 15+ KCC", groups = {"NonUS-Ratings"})
    public void verifyRatingSystemSouthKoreaKCC15() {
        ratingsSetup(KCC_15, KOREAN_LANG, KOREA_LOCALE);
        confirmRegionalRatingsDisplays(KCC_15, DictionaryKeys.RATING_KCC_15.getText());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75184"})
    @Test(description = "Ratings-South Korea 15+ KCC", groups = {"NonUS-Ratings"})
    public void verifyRatingSystemSouthKoreaKCC19() {
        ratingsSetup(KCC_19, KOREAN_LANG, KOREA_LOCALE);
        confirmRegionalRatingsDisplays(KCC_19, DictionaryKeys.RATING_KCC_19.getText());
    }
}
