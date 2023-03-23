package com.disney.qa.tests.disney.apple.ios.playback;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.HARUtils;
import com.qaprosoft.carina.core.foundation.utils.R;
import org.testng.annotations.Test;

public class DisneyPlusPlaybackCaptureTest extends DisneyBaseTest {

    private static final String DOCTOR_STRANGE_PROD = "058f2e46-e6ad-44ef-9188-81219c510094";
    private static final String DOCTOR_STRANGE_QA = "c0e322cb-85e5-47bc-a568-0a82977799a5";

    @Test(description = "Capture playback requests in har file.")
    public void capturePlaybackHar() {
        setGlobalVariables();
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWatchlistIOSPageBase watchlistPage = initPage(DisneyPlusWatchlistIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);
        HARUtils harUtils = new HARUtils(proxy.get());

        String movieProgramId = R.CONFIG.get("env").equals("PROD")
                ? DOCTOR_STRANGE_PROD
                : DOCTOR_STRANGE_QA;

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

        harUtils.publishHAR("Playback.har");
    }
}
