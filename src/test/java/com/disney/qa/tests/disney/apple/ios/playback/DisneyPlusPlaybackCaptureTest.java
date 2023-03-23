package com.disney.qa.tests.disney.apple.ios.playback;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.HARUtils;
import com.qaprosoft.carina.core.foundation.utils.R;
import org.testng.annotations.Test;

public class DisneyPlusPlaybackCaptureTest extends DisneyBaseTest {

    @Test(description = "Capture playback requests in har file.")
    public void capturePlaybackHar() {
        setGlobalVariables();
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWatchlistIOSPageBase watchlistPage = initPage(DisneyPlusWatchlistIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);
        HARUtils harUtils = new HARUtils(proxy.get());

        String mediaId = R.CONFIG.get("capabilities.contentId");

        if(R.CONFIG.get("capabilities.contentType").equals("programId")) {
            searchApi.get().addMovieToWatchlist(disneyAccount.get(), mediaId);
        } else {
            searchApi.get().addSeriesToWatchlist(disneyAccount.get(), mediaId);
        }



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
