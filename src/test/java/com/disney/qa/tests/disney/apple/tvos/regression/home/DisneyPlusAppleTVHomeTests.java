package com.disney.qa.tests.disney.apple.tvos.regression.home;

import com.disney.qa.api.explore.response.*;
import com.disney.qa.api.utils.*;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.core.*;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.*;
import org.slf4j.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.*;
import java.net.*;
import java.util.*;

import static com.disney.qa.api.disney.DisneyEntityIds.HOME_PAGE;

public class DisneyPlusAppleTVHomeTests extends DisneyPlusAppleTVBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-89521"})
    @Test(groups = {TestGroup.HOME})
    public void verifyHomeScreenLayout() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        SoftAssert sa = new SoftAssert();

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        logInWithoutHomeCheck(getAccount());
        collapseGlobalNav();

        Assert.assertTrue(homePage.isOpened(),
                "Home page did not launch for single profile user after logging in");

        //stop hero carousel
        homePage.moveRight(2, 2);
        homePage.clickDown();

        ArrayList<Container> homeCollections = getCollectionsHome();

        verifyBrandDetails(homeCollections.get(1).getId(), sa);

        homePage.moveDown(1, 1);
        homePage.moveLeft(4, 1);
        homePage.moveRight(2, 1);

        verifyHomeCollectionsAndContent(homeCollections, sa);
        sa.assertAll();
    }

    private ArrayList<Container> getCollectionsHome() {
        ArrayList<Container> homeCollections;
        try {
            homeCollections = getDisneyAPIPage(HOME_PAGE.getEntityId(),
                    getLocalizationUtils().getLocale(),
                    getLocalizationUtils().getUserLanguage());
        } catch (URISyntaxException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return homeCollections;
    }

    private void verifyBrandDetails(String brandCollectionID, SoftAssert sa) {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        List<Item> brandsCollection = getExploreAPIItemsFromSet(brandCollectionID, 10);
        brandsCollection.forEach(item -> {
            sa.assertTrue(homePage.isFocused(homePage.getTypeCellNameContains(item.getVisuals().getTitle())),
                    "The following brand tile was not focused: " + item);
            homePage.moveRight(1, 1);
        });
    }

    private void verifyHomeCollectionsAndContent(ArrayList<Container> homeCollections, SoftAssert sa) {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());

        //This removes first 2 collections from the home collection
        homeCollections.subList(0, 2).clear();

        LOGGER.info("Home collection size: {}", homeCollections.size());

        homeCollections.forEach(homeCollectionId -> {
            //Verify shelf title
            String shelfTitle = homeCollectionId.getVisuals().getName();
            if(!Arrays.asList("Home","My Watchlist").contains(shelfTitle)) {
                sa.assertTrue(homePage.getStaticTextByLabelContains(shelfTitle).isPresent(SHORT_TIMEOUT),
                        "Shelf title not found: " + shelfTitle);
            }
            if (!homeCollectionId.getItems().isEmpty()) {
                String firstContentTitle = homeCollectionId.getItems().get(0).getVisuals().getTitle();
                LOGGER.info("Content Title: {} for Shelf: {}", firstContentTitle, shelfTitle);
                //Verify content title
                sa.assertTrue(homePage.getTypeCellNameContains(firstContentTitle).isPresent(),
                        "Content title not found: " + firstContentTitle);
            }
            homePage.moveDown(1, 1);
        });
    }
}
