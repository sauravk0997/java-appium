package com.disney.qa.tests.disney.android.mobile.update;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.disney.android.pages.common.DisneyPlusCommonPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusDiscoverPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusMediaPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusSearchPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusVideoPageBase;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.qaprosoft.appcenter.AppCenterManager;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DisneyPlusAndroidVersionUpgradeTest extends BaseDisneyTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    String latestBuild;

    AndroidUtilsExtended utility = new AndroidUtilsExtended();

    String latestVersionNum;
    
    @DataProvider
    public Object[] elevatorStops(){
        List<String> tuidList = new ArrayList<>();
        Arrays.asList(R.CONFIG.get("custom_string").split(",")).forEach(version -> tuidList.add("TUID: " + version));
        this.latestBuild = AppCenterManager.getInstance().getDownloadUrl("Disney-Mobile", "android", "mobile.google.debug", "latest");
        return tuidList.toArray();
    }

    @BeforeMethod(alwaysRun = true)
    public void uninstallLatest(){
        latestVersionNum = utility.getAppVersionName(APP_PACKAGE);
        utility.removeApp(APP_PACKAGE);
    }

    public void installAndLaunchElevatorBuild(String version){
        String elevatorBuild = AppCenterManager.getInstance().getDownloadUrl("Disney-Mobile", "android", "mobile.google.debug", version);
        utility.installApp(elevatorBuild);
        activityAndPackageLaunch();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69850"})
    @Test(description = "Verify Elevator Upgrade from base to latest is functional", dataProvider = "elevatorStops", groups = {"Upgrade"}, enabled = false)
    public void testVersionUpgradeFunctionality(String TUID){
        String version = StringUtils.substringAfter(TUID, "TUID: ");

        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);

        LOGGER.info("Validating upgrade from version: " + version);
        installAndLaunchElevatorBuild(version);

        Assert.assertTrue(utility.getAppVersionName(APP_PACKAGE).equals(version),
                String.format("Expected - Version '%s' must be installed as a baseline to proceed with the test", version));

        login(disneyAccount.get(), false);
        dismissChromecastOverlay();
        closeAppForRelaunch();
        utility.installApp(this.latestBuild);
        activityAndPackageLaunch();

        Assert.assertTrue(utility.getAppVersionName(APP_PACKAGE).equals(latestVersionNum),
                String.format("Expected - Version upgrade to show correct number after upgrading from '%s'", version));

        Assert.assertTrue(initPage(DisneyPlusDiscoverPageBase.class).isOpened(),
                String.format("Expected - User to remain logged in after upgrade from version '%s'", version));

        commonPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusCommonPageBase.MenuItem.SEARCH.getText()));

        DisneyPlusSearchPageBase searchPageBase = initPage(DisneyPlusSearchPageBase.class);

        searchPageBase.searchForMedia(RandomStringUtils.randomAlphabetic(1));
        searchPageBase.openFirstSearchResultItem();

        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        String mediaTitle = mediaPageBase.getMediaTitle();

        LOGGER.info(String.format("Using media item '%s' for video player check...", mediaTitle));
        mediaPageBase.startPlayback();

        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);

       Assert.assertTrue(videoPageBase.isOpened(),
                String.format("Expected - Video Playback to open without issue after upgrading from '%s'", version));
    }
}
