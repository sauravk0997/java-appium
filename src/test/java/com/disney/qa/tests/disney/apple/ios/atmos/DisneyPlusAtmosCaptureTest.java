package com.disney.qa.tests.disney.apple.ios.atmos;

import com.disney.qa.api.search.assets.DisneyMovies;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.HARUtils;
import com.qaprosoft.carina.core.foundation.utils.R;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusAtmosCaptureTest extends DisneyBaseTest {

    @Test(description = "Capture atmos requests in har file.")
    public void captureAtmosHar() {
        setGlobalVariables();
        DisneyPlusVideoPlayerIOSPageBase videoPlayerPage = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWatchlistIOSPageBase watchlistPage = initPage(DisneyPlusWatchlistIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);
        HARUtils harUtils = new HARUtils(proxy.get());

        String movieProgramId = R.CONFIG.get("env").equals("PROD")
                ? DisneyMovies.AVENGERS_ENDGAME_PROD.getProgramId()
                : DisneyMovies.AVENGERS_ENDGAME_QA.getProgramId();

        searchApi.get().addMovieToWatchlist(disneyAccount.get(), movieProgramId);

        //Login
        setAppToHomeScreen(disneyAccount.get());
        //use API to add content to watchlist

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuPage.getDynamicCellByLabel(
                DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();

        //click badge content view
        watchlistPage.getBadgeContentView().click();

        detailsIOSPageBase.clickPlayButton();
        pause(10);

        harUtils.publishHAR("Atmos.har");
    }
}
