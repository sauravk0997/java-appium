package com.disney.qa.tests.disney.apple.tvos.playback;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVDetailsPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVVideoPlayerPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVWatchListPage;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.carina.utils.R;
import com.zebrunner.agent.core.registrar.Artifact;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.util.UUID;

import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST;

public class PlaybackCaptureTest extends DisneyPlusAppleTVBaseTest {

    @Test(groups = TestGroup.PROXY)
    public void playbackCapture() {
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage disneyPlusAppleTVWatchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage disneyPlusAppleTVVideoPlayerPage = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyAccount entitledUser = getAccountApi().createAccount("Yearly", getCountry(), getLanguage(), "V2");
        String contentType = R.CONFIG.get("custom_string");
        String contentId = R.CONFIG.get("custom_string2");
        getSearchApi().addToWatchlist(entitledUser, contentType, contentId);

        logIn(entitledUser);

        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        disneyPlusAppleTVHomePage.navigateToOneGlobalNavMenu(WATCHLIST.getText());
        disneyPlusAppleTVHomePage.clickSelect();

        Assert.assertTrue(disneyPlusAppleTVWatchListPage.isOpened(), "Watchlist page did not launch");
        pause(5);
        disneyPlusAppleTVHomePage.clickSelect();

        Assert.assertTrue(detailsPage.isOpened(), "Details page did not launch");
        detailsPage.clickPlayButton();

        Assert.assertTrue(disneyPlusAppleTVVideoPlayerPage.isOpened(), "Video Player page did not launch");
        pause(20);

        String name = UUID.randomUUID() + "playback_tvOS";
//        HARUtils.generateValidHarForCharles(proxy.get(), name);
        name = name + ".har";
        Artifact.attachToTest(name, Path.of(name));
    }
}
