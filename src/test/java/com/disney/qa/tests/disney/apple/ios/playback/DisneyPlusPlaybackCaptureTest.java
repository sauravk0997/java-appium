package com.disney.qa.tests.disney.apple.ios.playback;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.HARUtils;
import com.disney.util.TestGroup;
import com.zebrunner.carina.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.lang.invoke.MethodHandles;

public class DisneyPlusPlaybackCaptureTest extends DisneyBaseTest {

    @Test(description = "Capture playback requests in har file.", groups = {TestGroup.PRE_CONFIGURATION, TestGroup.PROXY})
    public void capturePlaybackHar() {
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWatchlistIOSPageBase watchlistPage = initPage(DisneyPlusWatchlistIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);

        String mediaId = R.CONFIG.get("capabilities.contentId");

        if(R.CONFIG.get("capabilities.contentType").equals("programId")) {
            searchApi.get().addMovieToWatchlist(disneyAccount.get(), mediaId);
        } else {
            searchApi.get().addSeriesToWatchlist(disneyAccount.get(), mediaId);
        }

        setAppToHomeScreen(disneyAccount.get());

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuPage.getDynamicCellByLabel(
                DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();
        watchlistPage.getBadgeContentView().click();

        detailsIOSPageBase.clickPlayButton();
        pause(10);

        HARUtils.attachHarAsArtifact(proxy.get(), "playback");
    }
}
