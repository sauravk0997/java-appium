package com.disney.qa.tests.disney.apple.tvos.regression.originals;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVOriginalsPage;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class DisneyPlusAppleTVOriginalsTest extends DisneyPlusAppleTVBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90978", "XCDQA-90980", "XCDQA-90982"})
    @Test(description = "Originals Appearance", groups = {TestGroup.SMOKE, TestGroup.DETAILS})
    public void originalsAppearance() {
        SoftAssert sa = new SoftAssert();
//        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
//        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVOriginalsPage disneyPlusAppleTVOriginalsPage = new DisneyPlusAppleTVOriginalsPage(getDriver());

        logInTemp(getAccount());

        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.ORIGINALS.getText());

        sa.assertTrue(disneyPlusAppleTVOriginalsPage.isOpened(), "Originals page is not open");
        sa.assertTrue(disneyPlusAppleTVOriginalsPage.isContentShownCertainNumberPerRow(3, 7), "Original items are not arranged 5 per row");
        List<String> originalContent = disneyPlusAppleTVOriginalsPage.getContentItems(3); //Originals begins at 3
        disneyPlusAppleTVOriginalsPage.moveRight(1, 1);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        sa.assertTrue(disneyPlusAppleTVOriginalsPage.isFocused(disneyPlusAppleTVHomePage.getDynamicCellByLabel(originalContent.get(2))), "Moving right did not focus the 2nd item in Series");
        disneyPlusAppleTVOriginalsPage.moveRight(1, 1);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        sa.assertTrue(disneyPlusAppleTVOriginalsPage.isFocused(disneyPlusAppleTVHomePage.getDynamicCellByLabel(originalContent.get(3))), "Moving right did not focus the 1st item in Series");
        disneyPlusAppleTVOriginalsPage.moveLeft(1, 1);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        sa.assertTrue(disneyPlusAppleTVOriginalsPage.isFocused(disneyPlusAppleTVHomePage.getDynamicCellByLabel(originalContent.get(2))), "Moving left did not focus the 1st item in Series");
        disneyPlusAppleTVOriginalsPage.moveDown(1, 1);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        sa.assertTrue(disneyPlusAppleTVOriginalsPage.isFocused(disneyPlusAppleTVHomePage.getDynamicCellByLabel(originalContent.get(8))), "Moving down did not focus the 1st item in Movies");
        disneyPlusAppleTVOriginalsPage.moveUp(1, 1);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        sa.assertTrue(disneyPlusAppleTVOriginalsPage.isFocused(disneyPlusAppleTVHomePage.getDynamicCellByLabel(originalContent.get(2))), "Moving up did not focus the 1st item in Series");

        sa.assertAll();
    }
}
