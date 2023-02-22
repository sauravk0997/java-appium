package com.disney.qa.tests.disney.android.mobile.ratings;

import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import java.io.IOException;

public class DisneyPlusRatingsTestMDA extends DisneyPlusAndroidRatingsTestBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69568"})
    @Test(description = "MDA - G", groups = {"Ratings", "MDA"})
    public void testRating_G() throws IOException {
        confirmRegionalRatingsDisplays("G");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69568"})
    @Test(description = "MDA - PG", groups = {"Ratings", "MDA"})
    public void testRating_PG() throws IOException {
        confirmRegionalRatingsDisplays("PG");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69568"})
    @Test(description = "MDA - PG13", groups = {"Ratings", "MDA"})
    public void testRating_PG13() throws IOException {
        confirmRegionalRatingsDisplays("PG13");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69568"})
    @Test(description = "MDA - NC16", groups = {"Ratings", "MDA"})
    public void testRating_NC16() throws IOException {
        confirmRegionalRatingsDisplays("NC16");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69568"})
    @Test(description = "MDA - M18", groups = {"Ratings", "MDA"})
    public void testRating_M18() throws IOException {
        confirmRegionalRatingsDisplays("M18");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69568"})
    @Test(description = "MDA - R21", groups = {"Ratings", "MDA"})
    public void testRating_R21() throws IOException {
        confirmRegionalRatingsDisplays("R21");
    }
}
