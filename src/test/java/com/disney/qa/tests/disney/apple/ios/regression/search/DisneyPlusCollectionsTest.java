package com.disney.qa.tests.disney.apple.ios.regression.search;

import com.disney.qa.disney.apple.pages.common.DisneyPlusBrandIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusCollectionsTest extends DisneyBaseTest {

    @Maintainer("mparra5")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62191"})
    @Test(description = "Search - Explore - Editorials & Collections", groups = {"Search", TestGroup.PRE_CONFIGURATION })
    public void verifySearchExploreEditorialsAndCollections() {
        SoftAssert sa = new SoftAssert();

        DisneyPlusBrandIOSPageBase disneyPlusBrandIOSPageBase = initPage(DisneyPlusBrandIOSPageBase.class);
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = initPage(DisneyPlusHomeIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        disneyPlusHomeIOSPageBase.clickMarvelTile();
        disneyPlusBrandIOSPageBase.isOpened();

        sa.assertTrue(disneyPlusBrandIOSPageBase.isCollectionBrandImageExpanded(), "Collection brand logo is not expanded");
        sa.assertTrue(disneyPlusBrandIOSPageBase.getBackArrow().isPresent(), "Back button is not present");
        sa.assertTrue(disneyPlusBrandIOSPageBase.isArtworkBackgroundPresent(), "Artwork images is not present");

        disneyPlusBrandIOSPageBase.swipeInCollectionBrandPage(Direction.UP);
        sa.assertTrue(disneyPlusBrandIOSPageBase.isCollectionBrandImageCollapsed(), "Collection brand logo is not collapsed");
        sa.assertTrue(disneyPlusBrandIOSPageBase.getBackArrow().isPresent(), "Back button is not present");

        disneyPlusBrandIOSPageBase.swipeInCollectionBrandPage(Direction.DOWN);
        sa.assertTrue(disneyPlusBrandIOSPageBase.isCollectionBrandImageExpanded(), "Collection brand logo is not expanded");

        disneyPlusBrandIOSPageBase.getBackArrow().click();

        disneyPlusHomeIOSPageBase.clickMarvelTile();
        disneyPlusBrandIOSPageBase.isOpened();

        sa.assertAll();
    }
}
