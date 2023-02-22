package com.disney.qa.tests.disney.android.mobile.ratings;

import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import java.io.IOException;

public class DisneyPlusRatingsTestKIJKWIJZER extends DisneyPlusAndroidRatingsTestBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68349"})
    @Test(description = "Kijwijzer - AL", groups = {"Ratings", "Kijkwijzer"})
    public void testRating_AL() throws IOException {
        confirmRegionalRatingsDisplays("AL");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68349"})
    @Test(description = "Kijwijzer - 6", groups = {"Ratings", "Kijkwijzer"})
    public void testRating_6() throws IOException {
        confirmRegionalRatingsDisplays("6");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68349"})
    @Test(description = "Kijwijzer - 9", groups = {"Ratings", "Kijkwijzer"})
    public void testRating_9() throws IOException {
        confirmRegionalRatingsDisplays("9");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68349"})
    @Test(description = "Kijwijzer - 12", groups = {"Ratings", "Kijkwijzer"})
    public void testRating_12() throws IOException {
        confirmRegionalRatingsDisplays("12");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68349"})
    @Test(description = "Kijwijzer - 16", groups = {"Ratings", "Kijkwijzer"})
    public void testRating_16() throws IOException {
        confirmRegionalRatingsDisplays("16");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68349"})
    @Test(description = "Kijwijzer - 18", groups = {"Ratings", "Kijkwijzer"})
    public void testRating_18() throws IOException {
        confirmRegionalRatingsDisplays("18");
    }
}
