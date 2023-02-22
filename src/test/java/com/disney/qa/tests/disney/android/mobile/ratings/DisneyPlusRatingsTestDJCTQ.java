package com.disney.qa.tests.disney.android.mobile.ratings;

import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import java.io.IOException;

public class DisneyPlusRatingsTestDJCTQ extends DisneyPlusAndroidRatingsTestBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68359"})
    @Test(description = "DJTQ - L", groups = {"Ratings", "DJCTQ"})
    public void testRating_L() throws IOException {
        confirmRegionalRatingsDisplays("L");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68359"})
    @Test(description = "DJTQ - 10", groups = {"Ratings", "DJCTQ"})
    public void testRating_10() throws IOException {
        confirmRegionalRatingsDisplays("10");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68359"})
    @Test(description = "DJTQ - 12", groups = {"Ratings", "DJCTQ"})
    public void testRating_12() throws IOException {
        confirmRegionalRatingsDisplays("12");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68359"})
    @Test(description = "DJTQ - 14", groups = {"Ratings", "DJCTQ"})
    public void testRating_14() throws IOException {
        confirmRegionalRatingsDisplays("14");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68359"})
    @Test(description = "DJTQ - 16", groups = {"Ratings", "DJCTQ"})
    public void testRating_16() throws IOException {
        confirmRegionalRatingsDisplays("16");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68359"})
    @Test(description = "DJTQ - 18", groups = {"Ratings", "DJCTQ"})
    public void testRating_18() throws IOException {
        confirmRegionalRatingsDisplays("18");
    }
}
