package com.disney.qa.tests.disney.android.mobile.ratings;

import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import java.io.IOException;

public class DisneyPlusRatingsTestNCS extends DisneyPlusAndroidRatingsTestBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67724"})
    @Test(description = "NCS - G", groups = {"Ratings", "NCS"})
    public void testRating_G() throws IOException {
        confirmRegionalRatingsDisplays("G");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67724"})
    @Test(description = "NCS - PG", groups = {"Ratings", "NCS"})
    public void testRating_PG() throws IOException {
        confirmRegionalRatingsDisplays("PG");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67724"})
    @Test(description = "NCS - M", groups = {"Ratings", "NCS"})
    public void testRating_M() throws IOException {
        confirmRegionalRatingsDisplays("M");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67724"})
    @Test(description = "NCS - MA15+", groups = {"Ratings", "NCS"})
    public void testRating_MA15() throws IOException {
        confirmRegionalRatingsDisplays("MA15+");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67724"})
    @Test(description = "NCS - R18+", groups = {"Ratings", "NCS"})
    public void testRating_R18() throws IOException {
        confirmRegionalRatingsDisplays("R18+");
    }
}
