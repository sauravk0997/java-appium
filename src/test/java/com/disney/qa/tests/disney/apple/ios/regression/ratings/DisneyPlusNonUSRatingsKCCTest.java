package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

public class DisneyPlusNonUSRatingsKCCTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75186"})
    @Test(description = "Ratings-South Korea 12+ KCC", groups = {"NonUS-Ratings"})
    public void verifyRatingSystemSouthKoreaKCC12() {
        ratingsSetup(KCC_12, KOREAN_LANG, KOREA_LOCALE);
        confirmRegionalRatingsDisplays(KCC_12, DictionaryKeys.RATING_KCC_12.getText());
    }
}
