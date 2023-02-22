package com.disney.qa.tests.disney.android.mobile.navigationandorientation;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.android.pages.common.DisneyPlusCommonPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusMoreMenuPageBase;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusAndroidNavigationAndOrientationTest extends BaseDisneyTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67343"})
    @Test(description = "Verify navigation bar UI", groups = {"Navigation"})
    public void testCheckNavigationBarUI() {
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);

        SoftAssert sa = new SoftAssert();

        login(disneyAccount.get(), true);
        commonPageBase.dismissTravelingDialog();

        commonPageBase.navigateToPage(
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                        DisneyPlusCommonPageBase.MenuItem.MORE.getText()));

        sa.assertTrue(moreMenuPageBase.isOpened(), "More Menu is not displayed");

        sa.assertFalse(
                commonPageBase.isMenuItemSelected(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                DisneyPlusCommonPageBase.MenuItem.DISCOVER.getText())),
                "Home icon is in a selected state");

        sa.assertFalse(
                commonPageBase.isMenuItemSelected(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                DisneyPlusCommonPageBase.MenuItem.DOWNLOADS.getText())),
                "Downloads icon is in a selected state");

        sa.assertFalse(
                commonPageBase.isMenuItemSelected(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                DisneyPlusCommonPageBase.MenuItem.SEARCH.getText())),
                "Search icon is in a selected state");

        sa.assertTrue(
                commonPageBase.isMenuItemSelected(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                                DisneyPlusCommonPageBase.MenuItem.MORE.getText())),
                "More Menu is in a un-selected state");

        for (int i = 0; i < DisneyPlusMoreMenuPageBase.getMenuOrder().length; i++) {
            String text =
                    languageUtils.get().getDictionaryItem(
                            DisneyDictionaryApi.ResourceKeys.APPLICATION,
                            DisneyPlusMoreMenuPageBase.getMenuOrder()[i].getText());

            sa.assertTrue((moreMenuPageBase.isMenuItemListedCorrectly(i, text)),
                    text + " was not populated correctly in slot " + (i + 1));
        }
        sa.assertAll();
    }
}
