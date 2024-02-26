package com.disney.qa.tests.disney.apple.ios.regression.home;

import com.disney.qa.api.client.requests.content.CollectionRequest;
import com.disney.qa.api.client.responses.content.ContentCollection;
import com.disney.qa.api.client.responses.content.ContentSet;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.search.DisneySearchApi;
import com.disney.qa.api.search.assets.DisneyStandardCollection;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest.SUB_VERSION;

public class DisneyPlusHomeTest extends DisneyBaseTest {
    private static final String PERSONALIZED_COLLECTION = "PersonalizedCollection";

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62276"})
    @Test(description = "Home - Deeplink", groups = {"Home", TestGroup.PRE_CONFIGURATION})
    public void verifyHomeDeeplink() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        launchDeeplink(true, R.TESTDATA.get("disney_prod_home_deeplink"), 10);
        homePage.clickOpenButton();
        Assert.assertTrue(homePage.isOpened(), "Home page did not open via deeplink.");
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67371"})
    @Test(description = "Home - Home Screen UI Elements", groups = {"Home", TestGroup.PRE_CONFIGURATION})
    public void verifyHomeUIElements() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), "V1");
        setAppToHomeScreen(entitledUser);


        List<ContentSet> allSets = getSearchApi().getAllSetsInHomeCollection(entitledUser, getCountry(), getLanguage(), PERSONALIZED_COLLECTION);
        CollectionRequest collectionRequest = CollectionRequest.builder().region(getCountry()).collectionType(PERSONALIZED_COLLECTION).account(entitledUser).language(getLanguage()).slug(DisneyStandardCollection.HOME.getSlug()).contentClass(DisneyStandardCollection.HOME.getSlug()).build();
        ContentCollection collection = getSearchApi().getCollection(collectionRequest);
        List<String> brands = DisneySearchApi.parseValueFromJson(collection.getJsonNode().toString(), "$..[?(@.type == 'GridContainer')]..items..text..full..content");
        LOGGER.info("All sets size: " + allSets.size());
        LOGGER.info("Collection titles: " + collection.getCollectionTitle());
        LOGGER.info("Collection set info: " + collection.getCollectionSetsInfo());
        homePage.isOpened();
        //validate recommended for you, new to disney+
//        sa.assertTrue();

        homePage.verifyBrands(brands, sa);

        System.out.println(getDriver().getPageSource());

        homePage.traverseAndVerifyHomepageLayout(allSets, brands, sa);

        sa.assertAll();
    }
}
