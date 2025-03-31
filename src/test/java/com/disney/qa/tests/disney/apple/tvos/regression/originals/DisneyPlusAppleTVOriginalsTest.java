package com.disney.qa.tests.disney.apple.tvos.regression.originals;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVOriginalsPage;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.disney.qa.common.constant.IConstantHelper.US;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVOriginalsTest extends DisneyPlusAppleTVBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-67316", "XCDQA-90980", "XCDQA-90982"})
    @Test(groups = {TestGroup.SMOKE, TestGroup.DETAILS_PAGE, US})
    public void originalsAppearance() {
        int originalTitleIndex = 4;
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVOriginalsPage originalsPage = new DisneyPlusAppleTVOriginalsPage(getDriver());
        logIn(getUnifiedAccount());

        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.
                globalNavigationMenu.ORIGINALS.getText());
        List<String> originalContent = originalsPage.getContentItems(originalTitleIndex);
        Assert.assertTrue(originalsPage.isOpened(), "Originals page is not open");
        sa.assertTrue(originalsPage.isContentShownCertainNumberPerRow(3, 7),
                "Original items are not arranged 5 per row");

        originalsPage.moveRight(1, 2);
        sa.assertTrue(originalsPage.isFocused(homePage.getTypeCellLabelContains(
                originalsPage.getFormattedOriginalsTitle(originalContent,1))), "Moving right did not focus the 2nd item in Series");
        originalsPage.moveRight(2, 2);
        sa.assertTrue(originalsPage.isFocused(homePage.getTypeCellLabelContains(
                originalsPage.getFormattedOriginalsTitle(originalContent,4))), "Moving right 2 did not focus the 3rd item in Series");
        originalsPage.moveLeft(3, 2);
        sa.assertTrue(originalsPage.isFocused(homePage.getTypeCellLabelContains(
                originalsPage.getFormattedOriginalsTitle(originalContent,0))), "Moving left 3 times did not focus the 1st item in Series");
        originalsPage.moveDown(1, 2);
        //First 6 cells visible in Series collection, so 7th is 1st item in Movies
        sa.assertTrue(originalsPage.isFocused(homePage.getTypeCellLabelContains(
                originalsPage.getFormattedOriginalsTitle(originalContent, 7))), "Moving down did not focus the 1st item in Movies");
        originalsPage.moveUp(1, 2);
        sa.assertTrue(originalsPage.isFocused(homePage.getTypeCellLabelContains(
                originalsPage.getFormattedOriginalsTitle(originalContent, 0))), "Moving up did not focus the 1st item in Series");
        sa.assertAll();
    }
}
