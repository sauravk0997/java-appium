package com.disney.qa.tests.disney.apple.tvos.regression.home;

import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.api.client.requests.content.CollectionRequest;
import com.disney.qa.api.client.responses.content.ContentCollection;
import com.disney.qa.api.client.responses.content.ContentSet;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.search.DisneySearchApi;
import com.disney.qa.api.search.assets.DisneyStandardCollection;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class DisneyPlusAppleTVHomeTests extends DisneyPlusAppleTVBaseTest {
    private static final String PERSONALIZED_COLLECTION = "PersonalizedCollection";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-89521", "XCDQA-89523"})
    @Test(description = "Verify focus and home screen layout upon landing", groups = {TestGroup.HOME}, enabled = false)
    public void verifyHomeScreenLayout() {
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());

        logInTemp(entitledUser);

        //stop hero carousel
        disneyPlusAppleTVHomePage.moveRight(2, 2);

        aliceDriver.screenshotAndRecognize().isLabelNotPresent(sa, AliceLabels.HOME_BUTTON_IS_SELECTED.getText())
                .isLabelPresent(sa, AliceLabels.BANNER_HOVERED.getText());
        disneyPlusAppleTVHomePage.clickDown();
        List<ContentSet> allSets = getSearchApi().getAllSetsInHomeCollection(entitledUser, getCountry(), getLanguage(), PERSONALIZED_COLLECTION);
        CollectionRequest collectionRequest = CollectionRequest.builder().region(getCountry()).collectionType(PERSONALIZED_COLLECTION).account(entitledUser).language(getLanguage()).slug(DisneyStandardCollection.HOME.getSlug()).contentClass(DisneyStandardCollection.HOME.getSlug()).build();
        ContentCollection collection = getSearchApi().getCollection(collectionRequest);
        List<String> brands = DisneySearchApi.parseValueFromJson(collection.getJsonNode().toString(), "$..[?(@.type == 'GridContainer')]..items..text..full..content");
        disneyPlusAppleTVHomePage.traverseAndVerifyHomepageLayout(allSets, brands, sa);

        sa.assertAll();
    }
}
