package com.disney.qa.tests.disney.apple.tvos.regression.originals;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVOriginalsPage;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusAppleTVOriginalsTest extends DisneyPlusAppleTVBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90978", "XCDQA-90980", "XCDQA-90982"})
    @Test(groups = {TestGroup.SMOKE, TestGroup.DETAILS_PAGE, US})
    public void originalsAppearance() {
        int originalTitleIndex = 3;
        SoftAssert sa = new SoftAssert();
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVOriginalsPage originalsPage = new DisneyPlusAppleTVOriginalsPage(getDriver());

        logInTemp(getAccount());

        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.
                globalNavigationMenu.ORIGINALS.getText());

//        originalsPage.waitForPresenceOfAnElement(originalsPage.getOriginalsTitle());
        Assert.assertTrue(originalsPage.isOpened(), "Originals page is not open");
        sa.assertTrue(originalsPage.isContentShownCertainNumberPerRow(3, 7),
                "Original items are not arranged 5 per row");
        List<String> originalContent = originalsPage.getContentItems(originalTitleIndex); //Originals begins at 3
        originalsPage.moveRight(1, 1);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        sa.assertTrue(originalsPage.isFocused(homePage.getDynamicCellByLabel(
                originalContent.get(2))), "Moving right did not focus the 2nd item in Series");
        originalsPage.moveRight(1, 1);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        sa.assertTrue(originalsPage.isFocused(homePage.getDynamicCellByLabel(
                originalContent.get(3))), "Moving right did not focus the 3rd item in Series");
        originalsPage.moveLeft(2, 1);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        sa.assertTrue(originalsPage.isFocused(homePage.getDynamicCellByLabel(
                originalContent.get(1))), "Moving left did not focus the 1st item in Series");
        originalsPage.moveDown(1, 1);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        //First 6 cells visible in Series collection, so 7th is 1st item in Movies
        sa.assertTrue(originalsPage.isFocused(homePage.getDynamicCellByLabel(
                originalContent.get(7))), "Moving down did not focus the 1st item in Movies");
        originalsPage.moveUp(1, 1);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        sa.assertTrue(originalsPage.isFocused(homePage.getDynamicCellByLabel(
                originalContent.get(1))), "Moving up did not focus the 1st item in Series");
        sa.assertAll();
    }
}
