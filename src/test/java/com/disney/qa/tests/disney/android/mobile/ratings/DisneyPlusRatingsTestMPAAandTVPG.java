package com.disney.qa.tests.disney.android.mobile.ratings;

import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import java.io.IOException;

public class DisneyPlusRatingsTestMPAAandTVPG extends DisneyPlusAndroidRatingsTestBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71631"})
    @Test(description = "MPAA/TVPG - TV-Y", groups = {"Ratings", "MPAATVPG"})
    public void testRating_TV_Y() throws IOException {
        confirmRegionalRatingsDisplays("TV-Y");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71631"})
    @Test(description = "MPAA/TVPG - TV-Y7", groups = {"Ratings", "MPAATVPG"})
    public void testRating_TV_Y7() throws IOException {
        confirmRegionalRatingsDisplays("TV-Y7");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71631"})
    @Test(description = "MPAA/TVPG - TV-Y7-FV", groups = {"Ratings", "MPAATVPG"})
    public void testRating_TV_Y7_FV() throws IOException {
        confirmRegionalRatingsDisplays("TV-Y7-FV");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71631"})
    @Test(description = "MPAA/TVPG - TV-G", groups = {"Ratings", "MPAATVPG"})
    public void testRating_TV_G() throws IOException {
        confirmRegionalRatingsDisplays("TV-G");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71631"})
    @Test(description = "MPAA/TVPG - G", groups = {"Ratings", "MPAATVPG"})
    public void testRating_G() throws IOException {
        confirmRegionalRatingsDisplays("G");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71631"})
    @Test(description = "MPAA/TVPG - TV-PG", groups = {"Ratings", "MPAATVPG"})
    public void testRating_TV_PG() throws IOException {
        confirmRegionalRatingsDisplays("TV-PG");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71631"})
    @Test(description = "MPAA/TVPG - PG+TV-PG", groups = {"Ratings", "MPAATVPG"})
    public void testRating_PG() throws IOException {
        confirmRegionalRatingsDisplays("PG");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71631"})
    @Test(description = "MPAA/TVPG - PG-13", groups = {"Ratings", "MPAATVPG"})
    public void testRating_PG_13() throws IOException {
        confirmRegionalRatingsDisplays("PG-13");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71631"})
    @Test(description = "MPAA/TVPG - TV-14", groups = {"Ratings", "MPAATVPG"})
    public void testRating_TV_14() throws IOException {
        confirmRegionalRatingsDisplays("TV-14");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71631"})
    @Test(description = "MPAA/TVPG - R", groups = {"Ratings", "MPAATVPG"})
    public void testRating_R() throws IOException {
        confirmRegionalRatingsDisplays("R");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71631"})
    @Test(description = "MPAA/TVPG - TV-MA", groups = {"Ratings", "MPAATVPG"})
    public void testRating_TV_MA() throws IOException {
        confirmRegionalRatingsDisplays("TV-MA");
    }


}
