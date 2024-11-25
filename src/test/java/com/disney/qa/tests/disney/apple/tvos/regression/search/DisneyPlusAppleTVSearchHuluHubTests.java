package com.disney.qa.tests.disney.apple.tvos.regression.search;

import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.SEARCH;

public class DisneyPlusAppleTVSearchHuluHubTests extends DisneyPlusAppleTVBaseTest {

// move to search folder
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121510"})
    @Test(groups = {TestGroup.HULU_HUB, TestGroup.SEARCH, US})
    public void verifyHuluHubSearchContentWithStandaloneAccount() {
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        SoftAssert sa = new SoftAssert();
        String huluContent = "Only Murders in the Building";

        loginTVWitHULUStandaloneBasicAccount();
        home.isOpened();
        home.moveDownFromHeroTileToBrandTile();
        home.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        searchPage.isOpened();
        searchPage.typeInSearchField(huluContent);
        sa.assertTrue(searchPage.getStaticTextByLabelContains(huluContent).isPresent(), "Hulu movie is not present");
        sa.assertAll();
    }
}
