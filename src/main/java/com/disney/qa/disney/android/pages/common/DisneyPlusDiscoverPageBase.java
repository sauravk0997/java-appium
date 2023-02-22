package com.disney.qa.disney.android.pages.common;

import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.common.utils.MobileUtilsExtended;
import com.qaprosoft.carina.core.foundation.utils.mobile.IMobileUtils;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusDiscoverPageBase extends DisneyPlusCommonPageBase{

    @FindBy(id = "welcomeLogo")
    private ExtendedWebElement welcomeLogo;

    @FindBy(id = "poster")
    protected ExtendedWebElement mediaPoster;

    @FindBy(id = "collectionRecyclerView")
    private ExtendedWebElement discoverPageContainer;

    @FindBy(id = "cast_featurehighlight_help_text_header_view")
    private ExtendedWebElement chromecastPopup;

    @FindBy(id = "castButton")
    private ExtendedWebElement chromecastIcon;

    //Elements for post-IAP landing
    @FindBy(id = "freeTrialBackground")
    private ExtendedWebElement signupSuccessBackground;

    @FindBy(id = "startWatchingButton")
    private ExtendedWebElement startWatchingButton;

    @FindBy(xpath = "//*[contains(@content-desc, \"%s\")]")
    private ExtendedWebElement genericContentDescElement;

    @FindBy(xpath = "//*[@text='%s']/following-sibling::*[@resource-id='com.disney.disneyplus:id/shelfRecyclerView']")
    private ExtendedWebElement contentShelf;

    @FindBy(id = "heroItemContainer")
    private ExtendedWebElement heroCarousel;

    //Star Add Profiles banner
    @FindBy(id = "closeButton")
    private ExtendedWebElement addProfileCloseButton;

    @FindBy(id  = "textAddProfileHeader")
    private ExtendedWebElement addProfilesHeader;

    @FindBy(id  = "textAddProfilesSubCopy")
    private ExtendedWebElement addProfilesSubHeader;

    public DisneyPlusDiscoverPageBase(WebDriver driver){
        super(driver);
    }

    private String[] brandsList = new String[]{Brands.DISNEY.getText(), Brands.PIXAR.getText(), Brands.MARVEL.getText(),
        Brands.STAR_WARS.getText(), Brands.NAT_GEO.getText()};

    public String[] getBrands() {
        return brandsList;
    }

    public enum Brands {
        DISNEY("disney"),
        PIXAR("pixar"),
        MARVEL("marvel"),
        STAR_WARS("star-wars"),
        NAT_GEO("national-geographic");

        private String brandMenu;

        Brands(String navItemText) {
            this.brandMenu = navItemText;
        }

        public String getText() {
            return this.brandMenu;
        }
    }

    @Override
    public boolean isOpened(){
        return welcomeLogo.isElementPresent() && discoverPageContainer.isElementPresent();
    }

    public void selectVisibleMediaPosterByIndex(Integer index) {
        List<WebElement> mediaPosterIndex = getDriver().findElements(By.id("poster"));
        if (index > mediaPosterIndex.size()) {
            Assert.fail("Index " + index + " is not selectable");
        }
        mediaPosterIndex.get(index).click();
    }

    public boolean isChromecastPopupDisplayed(){
        return chromecastPopup.isElementPresent(DELAY);
    }

    public boolean isChromecastAvailable() {
        return chromecastIcon.isElementPresent(SHORT_TIMEOUT);
    }

    public void waitForChromecastToClose(){
        waitUntil(ExpectedConditions.invisibilityOfElementLocated(chromecastPopup.getBy()), 30);
    }

    public DisneyPlusCommonPageBase dismissChromecastNotification(){
        LOGGER.info("Checking for Chromecast popup...");
        if(isChromecastPopupDisplayed()){
            LOGGER.info("Popup is present. Dismissing...");
            waitForSuccessPopup();
            navigateToPage(DisneyPlusCommonPageBase.MenuItem.DISCOVER.getText());
        } else {
            LOGGER.info("Popup not present. Proceeding with test.");
        }
        return initPage(DisneyPlusCommonPageBase.class);
    }

    public void openBrand(String brandName){
        genericContentDescElement.format(brandName).click();
    }

    public boolean isSignupSuccessOverlayDisplayed(){
        return signupSuccessBackground.isElementPresent() && startWatchingButton.isElementPresent();
    }

    public void dismissTrialOverlay(){
        if(isSignupSuccessOverlayDisplayed()){
            startWatchingButton.click();
        }
    }

    public boolean focusHeroTile() {
        return false;
    }

    public void selectGenericContentDescElement(String menuOption) {
        if (genericContentDescElement.format(menuOption).isElementPresent()) {
            genericContentDescElement.format(menuOption).click();
        }
    }

    /**
     * Method swipes down to desired header text and returns the number of movements made
     * @param header - String of the carousel header to navigate to
     * @return - Number of swipes made
     *
     * utils method used to end swipes if bottom of the screen is reached before the text is found.
     */
    public int swipesToHeaderText(String header){
        int swipes = 0;
        LOGGER.info("Swiping to '{}'", header);
        MobileUtilsExtended mobileUtilsExtended = new MobileUtilsExtended();
        while(!genericTextElement.format(header).isElementPresent() && mobileUtilsExtended.swipeAndCompareBeforeAndAfterPlacement()){
            swipes++;
        }
        return swipes;
    }

    public boolean isHeroVisible(){
        return heroCarousel.isElementPresent(SHORT_TIMEOUT);
    }

    public void returnToHero(){
        LOGGER.info("Returning to Hero...");
        new AndroidUtilsExtended().swipe(heroCarousel, IMobileUtils.Direction.DOWN);
    }

    /**
     *
     * @param header - Header text of the carousel being swiped in
     * @param swipes - Number of swipes being done in the desired carousel
     */
    public void swipeInShelf(String header, int swipes){
        MobileUtilsExtended mobileUtilsExtended = new MobileUtilsExtended();

        if(!contentShelf.format(header).isElementPresent()){
            mobileUtilsExtended.swipeUp(1000);
        }

        for (int i = 0; i < swipes; i++) {
            mobileUtilsExtended.swipeInContainer(contentShelf.format(header), IMobileUtils.Direction.LEFT, 1000);
        }
    }

    public void dismissAddProfilesPopup(){
        addProfileCloseButton.clickIfPresent();
    }

    public boolean isAddProfilesCloseBtnPresent() {
        return addProfileCloseButton.isElementPresent();
    }

    public boolean isAddProfilesBannerVisible() {
        return addProfilesHeader.isElementPresent();
    }

    public String getAddProfileHeaderText() {
        return addProfilesHeader.getText();
    }

    public String getAddProfileSubHeaderTeaxt() {
        return addProfilesSubHeader.getText();
    }

    public ExtendedWebElement getHeroCarousel(){
        return heroCarousel;
    }
}
