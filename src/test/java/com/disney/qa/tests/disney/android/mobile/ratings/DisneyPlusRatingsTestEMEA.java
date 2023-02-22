package com.disney.qa.tests.disney.android.mobile.ratings;

import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import java.io.IOException;

public class DisneyPlusRatingsTestEMEA extends DisneyPlusAndroidRatingsTestBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68351"})
    @Test(description = "Custom DisneyPlus: EMEA - 0", groups = {"Ratings", "EMEA"})
    public void testRating_0() throws IOException {
        confirmRegionalRatingsDisplays("0");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68351"})
    @Test(description = "Custom DisneyPlus: EMEA - 6", groups = {"Ratings", "EMEA"})
    public void testRating_6() throws IOException {
        confirmRegionalRatingsDisplays("6");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68351"})
    @Test(description = "Custom DisneyPlus: EMEA - 9", groups = {"Ratings", "EMEA"})
    public void testRating_9() throws IOException {
        confirmRegionalRatingsDisplays("9");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68351"})
    @Test(description = "Custom DisneyPlus: EMEA - 12", groups = {"Ratings", "EMEA"})
    public void testRating_12() throws IOException {
        confirmRegionalRatingsDisplays("12");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68351"})
    @Test(description = "Custom DisneyPlus: EMEA - 14", groups = {"Ratings", "EMEA"})
    public void testRating_14() throws IOException {
        confirmRegionalRatingsDisplays("14");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68351"})
    @Test(description = "Custom DisneyPlus: EMEA - 16", groups = {"Ratings", "EMEA"})
    public void testRating_16() throws IOException {
        confirmRegionalRatingsDisplays("16");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68351"})
    @Test(description = "Custom DisneyPlus: EMEA - 18", groups = {"Ratings", "EMEA"})
    public void testRating_18() throws IOException {
        confirmRegionalRatingsDisplays("18");
    }
}
