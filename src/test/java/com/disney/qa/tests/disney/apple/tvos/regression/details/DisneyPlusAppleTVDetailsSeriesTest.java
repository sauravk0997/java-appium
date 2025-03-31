package com.disney.qa.tests.disney.apple.tvos.regression.details;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.api.explore.response.*;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.*;
import com.zebrunner.agent.core.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.annotations.*;
import org.testng.asserts.*;

import java.lang.invoke.MethodHandles;
import java.util.*;

import static com.disney.qa.api.disney.DisneyEntityIds.LOKI;
import static com.disney.qa.common.constant.IConstantHelper.DETAILS_PAGE_NOT_DISPLAYED;
import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.SEARCH;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVDetailsSeriesTest extends DisneyPlusAppleTVBaseTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String SEARCH_PAGE_ERROR_MESSAGE = "Search page did not open";
    private static final String CONTENT_ERROR_MESSAGE = "Content is not found";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-64981"})
    @Test(groups = {TestGroup.DETAILS_PAGE, US})
    public void verifySeriesDetailsPageSuggestedTab() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        SoftAssert sa = new SoftAssert();
        String series = LOKI.getTitle();

        logIn(getUnifiedAccount());

        homePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        sa.assertTrue(searchPage.isOpened(), SEARCH_PAGE_ERROR_MESSAGE);
        searchPage.typeInSearchField(series);
        Assert.assertTrue(searchPage.getStaticTextByLabelContains(series).isPresent(), CONTENT_ERROR_MESSAGE);
        searchPage.getSearchResults(series).get(0).click();
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);

        detailsPage.moveDown(1, 1);
        detailsPage.moveRight(1, 1);
        Assert.assertTrue(detailsPage.isFocused(detailsPage.getSuggestedTab()), "Suggested tab was not focussed");
        detailsPage.moveDown(1, 1);

        ArrayList<Container> lokiPageDetails = getDisneyAPIPage(LOKI.getEntityId(), false);
        List<Item> list = lokiPageDetails.stream()
                .filter(container -> container.getVisuals().getName().equals("SUGGESTED"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("container not present in API response"))
                .getItems().subList(0, 1);

        list.forEach(i -> {
            Assert.assertTrue(detailsPage.isFocused(detailsPage.getTypeCellLabelContains(i.getVisuals().getTitle())),
                    "Suggested title was not focussed");
            detailsPage.moveRight(1, 1);
        });
        detailsPage.clickSelect();
        detailsPage.waitForDetailsPageToOpen();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
    }
}
