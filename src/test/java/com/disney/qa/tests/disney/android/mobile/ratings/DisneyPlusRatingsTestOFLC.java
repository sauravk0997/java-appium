package com.disney.qa.tests.disney.android.mobile.ratings;

import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import java.io.IOException;

public class DisneyPlusRatingsTestOFLC extends DisneyPlusAndroidRatingsTestBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71630"})
    @Test(description = "OFLC - G", groups = {"Ratings", "OFLC"})
    public void testRating_G() throws IOException {
        confirmRegionalRatingsDisplays("G");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71630"})
    @Test(description = "OFLC - PG", groups = {"Ratings", "OFLC"})
    public void testRating_PG() throws IOException {
        confirmRegionalRatingsDisplays("PG");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71630"})
    @Test(description = "OFLC - M", groups = {"Ratings", "OFLC"})
    public void testRating_M() throws IOException {
        confirmRegionalRatingsDisplays("M");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71630"})
    @Test(description = "OFLC - G", groups = {"Ratings", "OFLC"})
    public void testRating_R13() throws IOException {
        confirmRegionalRatingsDisplays("RP13+13+R13");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71630"})
    @Test(description = "OFLC - G", groups = {"Ratings", "OFLC"})
    public void testRating_13() throws IOException {
        confirmRegionalRatingsDisplays("13");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71630"})
    @Test(description = "OFLC - G", groups = {"Ratings", "OFLC"})
    public void testRating_RP13() throws IOException {
        confirmRegionalRatingsDisplays("RP13");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71630"})
    @Test(description = "OFLC - R15", groups = {"Ratings", "OFLC"})
    public void testRating_R15() throws IOException {
        confirmRegionalRatingsDisplays("R15");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71630"})
    @Test(description = "OFLC - RP16+16+R16", groups = {"Ratings", "OFLC"})
    public void testRating_R16() throws IOException {
        confirmRegionalRatingsDisplays("R16");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71630"})
    @Test(description = "OFLC - RP16+16+R16", groups = {"Ratings", "OFLC"})
    public void testRating_16() throws IOException {
        confirmRegionalRatingsDisplays("16");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71630"})
    @Test(description = "OFLC - RP16+16+R16", groups = {"Ratings", "OFLC"})
    public void testRating_RP16() throws IOException {
        confirmRegionalRatingsDisplays("RP16");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71630"})
    @Test(description = "OFLC - RP18+18+R18", groups = {"Ratings", "OFLC"})
    public void testRating_R() throws IOException {
        confirmRegionalRatingsDisplays("R");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71630"})
    @Test(description = "OFLC - RP18+18+R18", groups = {"Ratings", "OFLC"})
    public void testRating_R18() throws IOException {
        confirmRegionalRatingsDisplays("R18");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71630"})
    @Test(description = "OFLC - RP18+18+R18", groups = {"Ratings", "OFLC"})
    public void testRating_18() throws IOException {
        confirmRegionalRatingsDisplays("18");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71630"})
    @Test(description = "OFLC - RP18+18+R18", groups = {"Ratings", "OFLC"})
    public void testRating_RP18() throws IOException {
        confirmRegionalRatingsDisplays("RP18");
    }
}
