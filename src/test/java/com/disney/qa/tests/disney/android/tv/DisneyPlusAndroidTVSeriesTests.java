package com.disney.qa.tests.disney.android.tv;

import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage;
import com.disney.util.disney.ZebrunnerXrayLabels;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.tests.disney.DisneyPlusBaseTest.DIS;

public class DisneyPlusAndroidTVSeriesTests extends DisneyPlusAndroidTVBaseTest {

    @Test(description = "Verify only 4 tiles are present per row on Series page")
    public void fourTilesPerRowSeriesPage() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67382"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1886"));
        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.SERIES,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());

        sa.assertTrue(disneyPlusAndroidTVSeriesPageBase.get().isOpened(), SERIES_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVSeriesPageBase.get().focusFirstTile();

        sa.assertTrue(disneyPlusAndroidTVSeriesPageBase.get().fourTilesPerRow(3), "Did not find four tiles per row");

        checkAssertions(sa);
    }

    @Test(description = "Check whether the tabs on Series detail page are selected and focused while navigating", groups = {"smoke"})
    public void tabNavigationFocusAndSelectionSeriesDetailPage() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67384"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1887"));
        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.SERIES,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());

        sa.assertTrue(disneyPlusAndroidTVSeriesPageBase.get().isOpened(), SERIES_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVCommonPage.get().pressDown(1);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();

        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), DETAILS_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVSeriesPageBase.get().pressDownAndSelect();
        disneyPlusAndroidTVSeriesPageBase.get()
                .verifyTabsAreSelectedAndFocused(disneyPlusAndroidTVSeriesPageBase.get().getTabs(), sa);

        checkAssertions(sa);
    }

    @Test(description = "User can navigate around Series page")
    public void seriesPageNavigation() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67386"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1888"));
        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.SERIES,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());

        sa.assertTrue(disneyPlusAndroidTVSeriesPageBase.get().isOpened(), SERIES_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVSeriesPageBase.get()
                .verifyTabsAreSelectedAndFocused(disneyPlusAndroidTVSeriesPageBase.get().getTitleView(), sa);

        disneyPlusAndroidTVSeriesPageBase.get().pressDown(1);

        disneyPlusAndroidTVSeriesPageBase.get().navigateRowsAndTiles(sa);

        checkAssertions(sa);
    }
}
