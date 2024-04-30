package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

public class DisneyPlusNonUSRatingsKMRBTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75173"})
    @Test(description = "Ratings-South Korea 12+ KCC", groups = {"NonUS-Ratings"})
    public void verifyRatingSystemSouthKoreaKCC7() {
        ratingsSetup(KCC_7, KOREAN_LANG, KOREA_LOCALE);
        confirmRegionalRatingsDisplays(KCC_7, DictionaryKeys.RATING_KCC_7.getText());
    }
}
