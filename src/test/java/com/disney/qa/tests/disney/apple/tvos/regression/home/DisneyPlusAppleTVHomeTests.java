package com.disney.qa.tests.disney.apple.tvos.regression.home;

import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.api.explore.response.Container;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.disney.qa.api.disney.DisneyEntityIds.HOME_PAGE;

public class DisneyPlusAppleTVHomeTests extends DisneyPlusAppleTVBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-89521"})
    @Test(description = "Verify focus and home screen layout upon landing", groups = {TestGroup.HOME})
    public void verifyHomeScreenLayout() throws URISyntaxException, JsonProcessingException {
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        SoftAssert sa = new SoftAssert();

        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        logInTemp(getAccount());

        //stop hero carousel
        disneyPlusAppleTVHomePage.moveRight(2, 2);

        aliceDriver.screenshotAndRecognize().isLabelNotPresent(sa, AliceLabels.HOME_BUTTON_IS_SELECTED.getText())
                .isLabelPresent(sa, AliceLabels.BANNER_HOVERED.getText());
        disneyPlusAppleTVHomePage.clickDown();
        ArrayList<Container> collectionsHome = disneyBaseTest.getDisneyAPIPage(HOME_PAGE.getEntityId());

        List<String> titles = disneyBaseTest.getContainerTitlesFromApi(collectionsHome.get(1).getId(), 50);
        disneyPlusAppleTVHomePage.moveDown(2,1);
        // Items related to brands are visible on the screen
        IntStream.range(0, titles.size()).forEach(i -> {
            sa.assertTrue(disneyPlusAppleTVHomePage.getTypeCellNameContains(titles.get(i)).isElementPresent(),
                    String.format("%s asset of %s not found", titles, titles.get(i)));
        });
        disneyPlusAppleTVHomePage.moveDown(1,1);

        // Validate first containers
        IntStream.range(2, 4).forEach(i -> {
            List<String> container = disneyBaseTest.getContainerTitlesFromApi(collectionsHome.get(i).getId(), 5);
            IntStream.range(0, 5).forEach(j -> {
                sa.assertTrue(disneyPlusAppleTVHomePage.getTypeCellNameContains(container.get(j)).isElementPresent(),
                        "Title not found");});
            if(i == 3) { disneyPlusAppleTVHomePage.moveDown(1, 1); }
        });

        sa.assertAll();
    }
}
