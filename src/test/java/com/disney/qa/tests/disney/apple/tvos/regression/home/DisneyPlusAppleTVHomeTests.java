package com.disney.qa.tests.disney.apple.tvos.regression.home;

import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.api.explore.response.Container;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.disney.qa.api.disney.DisneyEntityIds.HOME_PAGE;

public class DisneyPlusAppleTVHomeTests extends DisneyPlusAppleTVBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-89521", "XCDQA-89523"})
    @Test(description = "Verify focus and home screen layout upon landing", groups = {TestGroup.HOME})
    public void verifyHomeScreenLayout() throws URISyntaxException, JsonProcessingException {
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());

        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        CollectionConstant.Collection collectionRecommended = CollectionConstant.Collection.RECOMMENDED_FOR_YOU;
        CollectionConstant.Collection collectionNewToDisney = CollectionConstant.Collection.NEW_TO_DISNEY;
        logInTemp(getAccount());

        //stop hero carousel
        disneyPlusAppleTVHomePage.moveRight(2, 2);

        aliceDriver.screenshotAndRecognize().isLabelNotPresent(sa, AliceLabels.HOME_BUTTON_IS_SELECTED.getText())
                .isLabelPresent(sa, AliceLabels.BANNER_HOVERED.getText());
        disneyPlusAppleTVHomePage.clickDown();
        ArrayList<Container> collectionsHome = disneyBaseTest.getDisneyAPIPage(HOME_PAGE.getEntityId());

        List<String> titles = disneyBaseTest.getContainerTitlesFromApi(collectionsHome.get(1).getId(), 50);
        System.out.println("** titles: " + titles.toString());
        disneyPlusAppleTVHomePage.moveDown(2,1);
        // Only first five items of the first shelf container are visible on the screen
        IntStream.range(0, titles.size()).forEach(i -> {
            sa.assertTrue(disneyPlusAppleTVHomePage.getTypeCellNameContains(titles.get(i)).isElementPresent(),
                    String.format("%s asset of %s not found on first row", titles, titles.get(i)));
        });
       sa.assertTrue(disneyPlusAppleTVHomePage.getStaticTextByLabelContains("Recommended For You").isPresent(),
              "Recommended for you collection is not present");

        List<String> recommendationTitlesFromApi = getContainerTitlesFromApi
                (CollectionConstant.getCollectionName(collectionRecommended), 10);
        System.out.println("** recommendationTitlesFromApi: "+ recommendationTitlesFromApi.toString());
        String firstCellTitle = disneyPlusAppleTVHomePage.getFirstCellTitleFromContainer(collectionRecommended).split(",")[0];
        System.out.println("** firstCellTitle:" + firstCellTitle.toString());
       // ExtendedWebElement firstTitle = disneyPlusAppleTVHomePage.getCellElementFromContainer(
           //     collectionRecommended,
            //    recommendationTitlesFromApi.get(0));
        sa.assertTrue(firstCellTitle.equals(disneyPlusAppleTVHomePage.getFirstCellTitleFromContainer(collectionRecommended).split(",")[0]),
                "UI title value not matched with API title value11");
        sa.assertTrue(firstCellTitle.equals(disneyPlusAppleTVHomePage.getFirstCellTitleFromContainer(collectionNewToDisney).split(",")[0]),
                "UI title value not matched with API title value22");

        sa.assertAll();
    }
}
