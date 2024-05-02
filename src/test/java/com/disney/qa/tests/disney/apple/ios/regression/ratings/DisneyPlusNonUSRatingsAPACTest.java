package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

public class DisneyPlusNonUSRatingsAPACTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75201"})
    @Test(description = "Rating System - APAC Proprietary - G", groups = {"NonUS-Ratings"})
    public void verifyRatingSystemAPACG() {
        ratingsSetup(APAC_G, JAPAN_LANG, JAPAN_LOCALE);
        confirmRegionalRatingsDisplays(APAC_G, DictionaryKeys.RATING_APAC_G.getText());
    }
}
