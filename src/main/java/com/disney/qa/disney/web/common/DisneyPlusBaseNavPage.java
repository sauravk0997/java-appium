package com.disney.qa.disney.web.common;

import com.disney.qa.disney.web.DisneyWebKeys;
import com.fasterxml.jackson.databind.JsonNode;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class DisneyPlusBaseNavPage extends DisneyPlusBasePage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public DisneyPlusBaseNavPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "account-dropdown")
    private ExtendedWebElement accountDropdownBtn;

    @FindBy(xpath = "//div[@id='account-dropdown' and @aria-expanded='%s']")
    private ExtendedWebElement accountDropdownExpandedBtn;

    @FindBy(id = "expanding-menu")
    private ExtendedWebElement expandingMenu;

    @FindBy(id = "expanding-menu")
    private  ExtendedWebElement moreItemIcn;

    @FindBy(id = "subnav")
    private ExtendedWebElement subNavId;

    @FindBy(xpath = "//button[@aria-label='SAVE']")
    private ExtendedWebElement dPlusNavPageSaveBtn;

    @FindBy(xpath = "//*[@data-testid='navigation-item-0-HOME']")
    private ExtendedWebElement dPlusBaseNavHomeBtn;

    @FindBy(xpath = "//a[@data-route='HOME']//span")
    private ExtendedWebElement dPlusBaseNavHomeLogoBtn;

    @FindBy(xpath = "//*[@data-testid='navigation-item-1-SEARCH']")
    private ExtendedWebElement dPlusBaseNavSearchBtn;

    @FindBy(xpath = "//*[@data-testid='navigation-item-1-SEARCH']//span")
    private ExtendedWebElement dPlusBaseNavSearchLogoBtn;

    @FindBy(xpath = "//*[@data-testid='navigation-item-2-WATCHLIST']")
    private ExtendedWebElement dPlusBaseNavWatchlistBtn;

    @FindBy(xpath = "//*[contains(@data-testid, 'navigation-item-2')]")
    private ExtendedWebElement dPlusBaseNavWatchListLocalizationBtn;

    @FindBy(xpath = "//*[@data-testid='navigation-item-2-WATCHLIST']//span")
    private ExtendedWebElement dPlusBaseNavWatchlistLogoBtn;

    @FindBy(xpath = "(//*[@data-testid='navigation-item-3-ORIGINALS'])[1]")
    private ExtendedWebElement dPlusBaseNavOriginalsBtn;

    @FindBy(xpath = "(//*[@data-testid='navigation-item-4-MOVIES'])[1]")
    private ExtendedWebElement dPlusBaseNavMoviesBtn;

    @FindBy(xpath = "(//*[@data-testid='navigation-item-5-SERIES'])[1]")
    private ExtendedWebElement dPlusBaseNavSeriesBtn;

    @FindBy(xpath = "//div[@id='subnav']//a[@data-testid='navigation-item-3-ORIGINALS']")
    private ExtendedWebElement dPlusBaseNavMoreOriginalsBtn;

    @FindBy(xpath = "//div[@id='subnav']//a[@data-testid='navigation-item-4-MOVIES']")
    private ExtendedWebElement dPlusBaseNavMoreMoviesBtn;

    @FindBy(xpath = "//div[@id='subnav']//a[@data-testid='navigation-item-5-SERIES']")
    private ExtendedWebElement dPlusBaseNavMoreSeriesBtn;

    @FindBy(id = "app_navigation")
    private ExtendedWebElement dPlusBaseAppNavHeader;

    @FindBy(xpath = "//div[@class='slick-list']")
    private ExtendedWebElement dPlusBaseSlickList;

    @FindBy(id = "section_index")
    private ExtendedWebElement dPlusBaseSectionIndex;

    //Account Global Nav Elements

    @FindBy(id = "account-dropdown")
    private ExtendedWebElement dPlusBaseNavAccountDropDownBtn;

    @FindBy(xpath = "//div[@id='account-dropdown']//ul[@role='menu']")
    private ExtendedWebElement dPlusBaseNavAccountDropDownMenu;

    @FindBy(xpath = "//*[@id='active-profile']//p")
    private  ExtendedWebElement dPlusBaseNavActiveProfile;

    @FindBy(xpath = "//li[@aria-label='Edit Profiles']")
    private ExtendedWebElement dPlusBaseNavAccountEditProfiles;

    @FindBy(xpath = "//li[@aria-label='Account']")
    private ExtendedWebElement dPlusBaseNavAccountAccount;

    @FindBy(xpath = "//li[@aria-label='Help']")
    private ExtendedWebElement dPlusBaseNavAccountHelp;

    @FindBy(xpath = "//li[@aria-label='Log out']")
    private ExtendedWebElement dPlusBaseNavAccountLogout;

    //Extended Web Element Getter Methods

    public ExtendedWebElement getNavViewAccountDropdownBtn() {
        return accountDropdownBtn;
    }

    public ExtendedWebElement getNavViewAccountDropdownExpandedBtn() {
        return accountDropdownExpandedBtn;
    }

    public ExtendedWebElement getNavViewExpandingMenuLbl() {
        return expandingMenu;
    }

    public ExtendedWebElement getNavViewMoreMenuIcn() {
        return moreItemIcn;
    }

    public ExtendedWebElement getNavViewSubNav() {
        return subNavId;
    }

    public ExtendedWebElement getDplusNavPageSaveBtn() {
        return dPlusNavPageSaveBtn;
    }

    public ExtendedWebElement getDplusBaseNavHomeBtn() {
        return dPlusBaseNavHomeBtn;
    }

    public ExtendedWebElement getDplusBaseNavHomeLogoBtn() {
        return dPlusBaseNavHomeLogoBtn;
    }

    public ExtendedWebElement getDplusBaseNavSearchBtn() {
        return dPlusBaseNavSearchBtn;
    }

    public ExtendedWebElement getDplusBaseNavSearchLogoBtn() {
        return dPlusBaseNavSearchLogoBtn;
    }

    public ExtendedWebElement getDplusBaseNavWatchListBtn() {
        return dPlusBaseNavWatchlistBtn;
    }

    public ExtendedWebElement getdPlusBaseNavWatchListLocalizationBtn() {
        return dPlusBaseNavWatchListLocalizationBtn;
    }

    public ExtendedWebElement getDplusBaseNavWatchlistLogoBtn() {
        return dPlusBaseNavWatchlistLogoBtn;
    }

    public ExtendedWebElement getDplusBaseNavOriginalsBtn() {
        return dPlusBaseNavOriginalsBtn;
    }

    public ExtendedWebElement getDplusBaseNavMoviesBtn() {
        return dPlusBaseNavMoviesBtn;
    }

    public ExtendedWebElement getDplusBaseNavSeriesBtn() {
        return dPlusBaseNavSeriesBtn;
    }

    public ExtendedWebElement getDplusBaseNavMoreOriginalsBtn() {
        return dPlusBaseNavMoreOriginalsBtn;
    }

    public ExtendedWebElement getDplusBaseNavMoreMoviesBtn() {
        return dPlusBaseNavMoreMoviesBtn;
    }

    public ExtendedWebElement getDplusBaseNavMoreSeriesBtn() {
        return dPlusBaseNavMoreSeriesBtn;
    }

    public ExtendedWebElement getDplusBaseAppNavHeader() {
        return dPlusBaseAppNavHeader;
    }

    public ExtendedWebElement getDplusBaseSlickList() {
        return dPlusBaseSlickList;
    }

    public ExtendedWebElement getdPlusBaseSectionIndex() {
        return dPlusBaseSectionIndex;
    }

    public ExtendedWebElement getdPlusBaseActiveProfile() {
        return dPlusBaseNavActiveProfile;
    }

    public ExtendedWebElement getdPlusBaseNavAccountEditProfiles() {
        return dPlusBaseNavAccountEditProfiles;
    }

    public ExtendedWebElement getdPlusBaseNavAccountAccount() {
        return dPlusBaseNavAccountAccount;
    }

    public ExtendedWebElement getdPlusBaseNavAccountLogOut() {
        return dPlusBaseNavAccountLogout;
    }

    public ExtendedWebElement getdPlusBaseNavAccountHelp() {
        return dPlusBaseNavAccountHelp;
    }

    //Booleans

    public boolean isAppNavHeaderVisible() {
        return getDplusBaseAppNavHeader().isVisible();
    }

    //Hover Elements

    public void hoverMoreMenuIcon() {
        getNavViewMoreMenuIcn().hover();
    }

    //Click Elements

    public void clickNavSearchBtn() {
        getDplusBaseNavSearchBtn().click();
    }

    public void clickNavHomeBtn() {
        getDplusBaseNavHomeBtn().click();
    }

    public void clickNavWatchListBtn() {
        getDplusBaseNavWatchListBtn().click();
    }

    public void clickNavWatchListLocalizationBtn() {
        getdPlusBaseNavWatchListLocalizationBtn().click();
    }

    public void clickNavOriginalsBtn() {
        getDplusBaseNavOriginalsBtn().click();
    }

    public void clickNavMoviesBtn() {
        getDplusBaseNavMoviesBtn().click();
    }

    public void clickNavSeriesBtn() {
        getDplusBaseNavSeriesBtn().click();
    }

    //End Click Elements

    public ExtendedWebElement accountDropdownStatus(String visible) {
        return accountDropdownExpandedBtn.format(visible);
    }

    public ExtendedWebElement getdPlusBaseNavAccountDropdown() {
        return dPlusBaseNavAccountDropDownBtn;
    }

    public ExtendedWebElement getdPlusBaseNavAccountDropdownMenu() {
        return dPlusBaseNavAccountDropDownMenu;
    }

    public String getActiveProfileAsString() {
        return dPlusBaseNavActiveProfile.getText();
    }

    public String navPageUrlAssertTrue(String actualValue, String expectedValue) {
        return String.format("Expected %s to be present in url after clicking %s, Actual: %s", expectedValue, expectedValue ,actualValue);
    }

    public String navPageUrlAssertFalse(String actualValue, String selectedValue, String expectedValue) {
        return String.format("URl Redirected to unexpected value. Actual Value taken: %s, Clicked Value: %s, Expected Value contains: %s", actualValue, selectedValue, expectedValue);
    }

    public boolean isNavMenuSearchIsVisible() {
        return getDplusBaseNavSearchBtn().isVisible();
    }

    public boolean isNavMenuHomeIsVisible() {
       return getDplusBaseNavHomeBtn().isVisible();
    }

    public boolean isNavMenuWatchListIsVisible() {
        return getDplusBaseNavWatchListBtn().isVisible();
    }

    public boolean isNavMenuOriginalsIsVisible() {
        return getDplusBaseNavOriginalsBtn().isVisible();
    }

    public boolean isNavMenuSeriesIsVisible() {
        return getDplusBaseNavSeriesBtn().isVisible();
    }

    public boolean isNavMenuMoviesIsVisible() {
        return getDplusBaseNavMoviesBtn().isVisible();
    }

    public void saveButtonClick(JsonNode dictionary){
        getDictionaryElement(dictionary,DisneyWebKeys.SAVE_BTN.getText()).click();
        LOGGER.info("Save Button is clicked");

    }
}
