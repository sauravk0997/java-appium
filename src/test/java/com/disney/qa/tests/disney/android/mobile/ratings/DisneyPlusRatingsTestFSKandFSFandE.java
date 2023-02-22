package com.disney.qa.tests.disney.android.mobile.ratings;

import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import java.io.IOException;

public class DisneyPlusRatingsTestFSKandFSFandE extends DisneyPlusAndroidRatingsTestBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68353"})
    @Test(description = "FSK - 0", groups = {"Ratings", "FSK"})
    public void testRating_FSK_0() throws IOException {
        confirmRegionalRatingsDisplays("FSK_0");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68353"})
    @Test(description = "FSK - 6", groups = {"Ratings", "FSK"})
    public void testRating_FSK_6() throws IOException {
        confirmRegionalRatingsDisplays("FSK_6");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68353"})
    @Test(description = "FSK - 12", groups = {"Ratings", "FSK"})
    public void testRating_FSK_12() throws IOException {
        confirmRegionalRatingsDisplays("FSK_12");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68353"})
    @Test(description = "FSK - 16", groups = {"Ratings", "FSK"})
    public void testRating_FSK_16() throws IOException {
        confirmRegionalRatingsDisplays("FSK_16");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68353"})
    @Test(description = "FSK - 18", groups = {"Ratings", "FSK"})
    public void testRating_FSK_18() throws IOException {
        confirmRegionalRatingsDisplays("FSK_18");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68353"})
    @Test(description = "FSF - 0", groups = {"Ratings", "FSF"})
    public void testRating_FSF_0() throws IOException {
        confirmRegionalRatingsDisplays("FSF_0");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68353"})
    @Test(description = "FSF - 6", groups = {"Ratings", "FSF"})
    public void testRating_FSF_6() throws IOException {
        confirmRegionalRatingsDisplays("FSF_6");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68353"})
    @Test(description = "FSF - 12", groups = {"Ratings", "FSF"})
    public void testRating_FSF_12() throws IOException {
        confirmRegionalRatingsDisplays("FSF_12");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68353"})
    @Test(description = "FSF - 16", groups = {"Ratings", "FSF"})
    public void testRating_FSF_16() throws IOException {
        confirmRegionalRatingsDisplays("FSF_16");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68353"})
    @Test(description = "FSF - 18", groups = {"Ratings", "FSF"})
    public void testRating_FSF_18() throws IOException {
        confirmRegionalRatingsDisplays("FSF_18");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68353"})
    @Test(description = "E - 0", groups = {"Ratings", "E"})
    public void testRating_E_0() throws IOException {
        confirmRegionalRatingsDisplays("E_0");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68353"})
    @Test(description = "E - 6", groups = {"Ratings", "E"})
    public void testRating_E_6() throws IOException {
        confirmRegionalRatingsDisplays("E_6");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68353"})
    @Test(description = "E - 12", groups = {"Ratings", "E"})
    public void testRating_E_12() throws IOException {
        confirmRegionalRatingsDisplays("E_12");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68353"})
    @Test(description = "E - 16", groups = {"Ratings", "E"})
    public void testRating_E_16() throws IOException {
        confirmRegionalRatingsDisplays("E_16");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68353"})
    @Test(description = "E - 18", groups = {"Ratings", "E"})
    public void testRating_E_18() throws IOException {
        confirmRegionalRatingsDisplays("E_18");
    }
}
