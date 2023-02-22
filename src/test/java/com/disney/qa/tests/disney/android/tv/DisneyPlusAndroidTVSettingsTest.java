
package com.disney.qa.tests.disney.android.tv;

import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage;
import com.disney.qa.disney.android.pages.tv.globalnav.DisneyPlusAndroidTVSettingsPageBase;
import com.disney.qa.disney.android.pages.tv.utility.navhelper.NavHelper;
import com.disney.util.disney.ZebrunnerXrayLabels;
import io.appium.java_client.android.nativekey.AndroidKey;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.tests.disney.DisneyPlusBaseTest.STA;


public class DisneyPlusAndroidTVSettingsTest extends DisneyPlusAndroidTVBaseTest {

    private static final String SETTINGS_OPEN_ERROR_MESSAGE = "Settings page should be open";
    private static final String ACCOUNT_OPEN_ERROR_MESSAGE = "Account page should be open";
    private static final String PROFILE_OPEN_ERROR_MESSAGE = "Profile page should be open";

    @Test
    public void settingsOpenAccountPage() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(STA, country, "XCDQA-103081"));
        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());

        // Go to Settings page
        NavHelper navHelper = new NavHelper(this.getCastedDriver());
        navHelper.keyUntilElementFocused(() -> disneyPlusAndroidTVDiscoverPage.get().getNavHome(), AndroidKey.BACK);
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().getNavHome().isVisible(),"Nav bar should be visible.");
        navHelper.keyUntilElementDescContains(() -> disneyPlusAndroidTVDiscoverPage.get().getFocusedNavItem(),
                AndroidKey.DPAD_DOWN, String.valueOf(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.SETTINGS));
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();

        sa.assertTrue(disneyPlusAndroidTVSettingsPageBase.get().isOpened(), SETTINGS_OPEN_ERROR_MESSAGE);

        // XCDQA-103081
        sa.assertTrue(disneyPlusAndroidTVSettingsPageBase.get().getSettingsOption(
                DisneyPlusAndroidTVSettingsPageBase.Settings.HELP.getText()).isElementPresent(),
                "Help button should be present");
        disneyPlusAndroidTVSettingsPageBase.get().selectSettingsOption(
                DisneyPlusAndroidTVSettingsPageBase.Settings.ACCOUNT.getText(), AndroidKey.DPAD_DOWN);
        sa.assertTrue(disneyPlusAndroidTVSettingsPageBase.get().isSettingsAccountOpened(), ACCOUNT_OPEN_ERROR_MESSAGE);
        sa.assertTrue(disneyPlusAndroidTVSettingsPageBase.get().getSubscriptionTitle().isElementPresent(),
                "Subscription Title should be present");
        sa.assertTrue(disneyPlusAndroidTVSettingsPageBase.get().getSubscriptionCopy().isElementPresent(),
                "Subscription Copy should be present");
        sa.assertAll();
    }

    @Test
    public void settingsNavToProfiles() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(STA, country, "XCDQA-104789", "XCDQA-102648"));
        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());

        // Go to Settings page
        NavHelper navHelper = new NavHelper(this.getCastedDriver());
        navHelper.keyUntilElementFocused(() -> disneyPlusAndroidTVDiscoverPage.get().getNavHome(), AndroidKey.BACK);
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().getNavHome().isVisible(),"Nav bar should be visible.");
        navHelper.keyUntilElementDescContains(() -> disneyPlusAndroidTVDiscoverPage.get().getFocusedNavItem(),
                AndroidKey.DPAD_DOWN, String.valueOf(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.SETTINGS));
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();

        sa.assertTrue(disneyPlusAndroidTVSettingsPageBase.get().isOpened(), SETTINGS_OPEN_ERROR_MESSAGE);

        // XCDQA-102648
        for (int i = 0; i < DisneyPlusAndroidTVSettingsPageBase.Settings.values().length; i++) {
            sa.assertTrue(disneyPlusAndroidTVSettingsPageBase.get().getSettingsOption(
                    DisneyPlusAndroidTVSettingsPageBase.Settings.values()[i].getText()).isElementPresent(),
                    i + " button should be present");
        }
        // XCDQA-104789
        disneyPlusAndroidTVSettingsPageBase.get().selectSettingsOption(
                DisneyPlusAndroidTVSettingsPageBase.Settings.PROFILES.getText(), AndroidKey.DPAD_DOWN);
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_OPEN_ERROR_MESSAGE);

        sa.assertAll();
    }
}
