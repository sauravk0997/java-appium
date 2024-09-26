package com.disney.qa.tests.disney.apple.tvos.regression.home;

import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.api.client.requests.content.CollectionRequest;
import com.disney.qa.api.client.responses.content.ContentCollection;
import com.disney.qa.api.client.responses.content.ContentSet;
import com.disney.qa.api.explore.response.Container;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.search.DisneySearchApi;
import com.disney.qa.api.search.assets.DisneyStandardCollection;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.disney.qa.api.disney.DisneyEntityIds.HOME_PAGE;

public class DisneyPlusAppleTVHomeTests extends DisneyPlusAppleTVBaseTest {
    private static final String PERSONALIZED_COLLECTION = "PersonalizedCollection";
// this
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-89521", "XCDQA-89523"})
    @Test(description = "Verify focus and home screen layout upon landing", groups = {TestGroup.HOME})
    public void verifyHomeScreenLayout() throws URISyntaxException, JsonProcessingException {
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyOffer offer = new DisneyOffer();
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        CollectionConstant.Collection collectionRecommended = CollectionConstant.Collection.RECOMMENDED_FOR_YOU;
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

        sa.assertAll();
    }
}
