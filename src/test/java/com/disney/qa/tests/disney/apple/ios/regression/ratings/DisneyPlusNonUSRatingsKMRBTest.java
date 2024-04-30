package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

public class DisneyPlusNonUSRatingsKMRBTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75173"})
    @Test(description = "Ratings-South Korea 12+ KCC", groups = {"NonUS-Ratings"})
    public void verifyRatingSystemSouthKoreaKMRB12() {
        ratingsSetup(KMRB_12, KOREAN_LANG, KOREA_LOCALE);
        confirmRegionalRatingsDisplays(KMRB_12, DictionaryKeys.RATING_KMRB_12.getText());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75174"})
    @Test(description = "Ratings-South Korea 15+ KCC", groups = {"NonUS-Ratings"})
    public void verifyRatingSystemSouthKoreaKMRB15() {
        ratingsSetup(KMRB_15, KOREAN_LANG, KOREA_LOCALE);
        confirmRegionalRatingsDisplays(KMRB_15, DictionaryKeys.RATING_KMRB_15.getText());
    }
}
