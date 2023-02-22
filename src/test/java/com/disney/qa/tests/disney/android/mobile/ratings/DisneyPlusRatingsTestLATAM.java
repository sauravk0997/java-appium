package com.disney.qa.tests.disney.android.mobile.ratings;

import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import java.io.IOException;

public class DisneyPlusRatingsTestLATAM extends DisneyPlusAndroidRatingsTestBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68362"})
    @Test(description = "Custom DisneyPlus: LatAm - 0+", groups = {"Ratings", "LATAM"})
    public void testRating__0() throws IOException {
        confirmRegionalRatingsDisplays("0+");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68362"})
    @Test(description = "Custom DisneyPlus: LatAm - 7+", groups = {"Ratings", "LATAM"})
    public void testRating__7() throws IOException {
        confirmRegionalRatingsDisplays("7+");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68362"})
    @Test(description = "Custom DisneyPlus: LatAm - 0+", groups = {"Ratings", "LATAM"})
    public void testRating_10() throws IOException {
        confirmRegionalRatingsDisplays("10+");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68362"})
    @Test(description = "Custom DisneyPlus: LatAm - 12+", groups = {"Ratings", "LATAM"})
    public void testRating_12() throws IOException {
        confirmRegionalRatingsDisplays("12+");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68362"})
    @Test(description = "Custom DisneyPlus: LatAm - 13+", groups = {"Ratings", "LATAM"})
    public void testRating_13() throws IOException {
        confirmRegionalRatingsDisplays("13+");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68362"})
    @Test(description = "Custom DisneyPlus: LatAm - 14+", groups = {"Ratings", "LATAM"})
    public void testRating_14() throws IOException {
        confirmRegionalRatingsDisplays("14+");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68362"})
    @Test(description = "Custom DisneyPlus: LatAm - 16+", groups = {"Ratings", "LATAM"})
    public void testRating_16() throws IOException {
        confirmRegionalRatingsDisplays("16+");
    }
}
