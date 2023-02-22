package com.disney.qa.tests.disney.android.tv;

import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage;
import com.disney.util.disney.ZebrunnerXrayLabels;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.tests.disney.DisneyPlusBaseTest.*;

public class DisneyPlusAndroidTVMoviesScreenTests extends DisneyPlusAndroidTVBaseTest {

    @Test(description = "Verify only 4 tiles are present per row on movies page")
    public void fourTilesPerRowMoviesPage() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67276"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1849"));
        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.MOVIES,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());

        sa.assertTrue(disneyPlusAndroidTVMoviesPageBase.get().isOpened(), MOVIES_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVMoviesPageBase.get().focusFirstTile();

        sa.assertTrue(disneyPlusAndroidTVMoviesPageBase.get().fourTilesPerRow(3), "Did not find four tiles per row");

        checkAssertions(sa);
    }

    @Test(description = "Check whether the tabs on movie detail page are selected and focused while navigating", groups = {"smoke"})
    public void tabNavigationFocusAndSelectionMoviesDetailPage() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67278"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1850"));
        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.MOVIES,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());

        sa.assertTrue(disneyPlusAndroidTVMoviesPageBase.get().isOpened(), MOVIES_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVMoviesPageBase.get().selectMediaItem(false);
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), "Movies details page did not launch");
        disneyPlusAndroidTVSeriesPageBase.get().pressDownAndSelect();
        disneyPlusAndroidTVMoviesPageBase.get()
                .verifyTabsAreSelectedAndFocused(disneyPlusAndroidTVMoviesPageBase.get().getTabs(), sa);

        checkAssertions(sa);
    }

    @Test(description = "User can navigate around movies page")
    public void moviesPageNavigation() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67280"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1851"));
        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.MOVIES,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());

        sa.assertTrue(disneyPlusAndroidTVMoviesPageBase.get().isOpened(), MOVIES_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVMoviesPageBase.get()
                .verifyTabsAreSelectedAndFocused(disneyPlusAndroidTVMoviesPageBase.get().getTitleView(), sa);
        disneyPlusAndroidTVMoviesPageBase.get().pressDown(1);

        disneyPlusAndroidTVMoviesPageBase.get().navigateRowsAndTiles(sa);

        checkAssertions(sa);
    }

}
