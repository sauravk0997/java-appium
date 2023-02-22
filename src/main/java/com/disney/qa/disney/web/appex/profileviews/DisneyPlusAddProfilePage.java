package com.disney.qa.disney.web.appex.profileviews;

import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.entities.WebConstant;
import com.disney.qa.disney.web.entities.ProfileConstant;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusAddProfilePage extends DisneyPlusCreateProfilePage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//div[@class='slick-track']")
    private ExtendedWebElement avatarSelectionTrack;

    @FindBy(xpath = "//div[@class='slick-track']/div")
    private List<ExtendedWebElement> avatarSelection;

    @FindBy(xpath = "//div[@class='slick-track']//a")
    private List<ExtendedWebElement> avatarSelectionAttribute;

    @FindBy(id = "addProfile")
    private ExtendedWebElement addProfileNameTextBox;

    @FindBy(id = "active-profile")
    private ExtendedWebElement activeProfile;

    @FindBy(xpath = "//div[contains(@class,'profile-avatar')]/..")
    private ExtendedWebElement selectedAvatar;

    @FindBy(xpath = "//div[contains(@class, 'input-error')]")
    private ExtendedWebElement duplicateNameErrorMessage;

    @FindBy(xpath = "//*[@id='select-avatar']/../header")
    private ExtendedWebElement chooseAvatarText;

    @FindBy(xpath = "//div[@class='gv2-asset']/a")
    private List<ExtendedWebElement> avatarNameCollection;

    @FindBy(xpath = "//div[@data-testid='collection-%s']//*[@class='slick-track']")
    private ExtendedWebElement slickTrackAvatarCollection;

    @FindBy(xpath = "//div[@data-testid='collection-%s']//*[@data-index='%s']")
    public ExtendedWebElement avatarInCollection;

    @FindBy(xpath = "//*[@data-testid='arrow-right']")
    public ExtendedWebElement scrollRightArrow;

    @FindBy(xpath = "//*[@data-testid='arrow-left']")
    public ExtendedWebElement scrollLeftArrow;

    @FindBy(xpath = "//*[@data-testid='gender-field']")
    public ExtendedWebElement addProfileGenderBtn;

    @FindBy(xpath = "//*[@data-testid='add-profile-save-button']")
    private ExtendedWebElement addProfileSaveBtn;

    @FindBy(xpath = "//*[@data-testid='personal-info-section']//*[contains(text(),'This field is required')]")
    public ExtendedWebElement addProfileGenderFieldRequiredError;

    private String attributeName = "data-testid";

    public DisneyPlusAddProfilePage(WebDriver driver) {
        super(driver);
    }

    public List<ExtendedWebElement> getSlickTrackAvatarCollection(int collectionTrack) {
        LOGGER.info("Get the track collection based of the 'collectionTrack'");
        return slickTrackAvatarCollection.format(collectionTrack).findExtendedWebElements(By.xpath(".//div[@class='slick-slide']"));
    }

    public ExtendedWebElement getAvatarInCollection(int collectionTrack, int avatarIndex) {
        return avatarInCollection.format(collectionTrack, avatarIndex);
    }

    @Override
    public void selectAvatarForProfile() {
        LOGGER.info("Selecting Avatar");
        avatarSelectionTrack.hover();
        if(!avatarSelection.isEmpty()){
            LOGGER.info("Avatar selection is not empty");
            avatarSelection.get(0).click();
        }
    }

    @Override
    public void addProfileName(String profileName) {
        LOGGER.info("Adding profile name: {}", profileName);
        addProfileNameTextBox.doubleClick();
        addProfileNameTextBox.sendKeys(Keys.DELETE);
        addProfileNameTextBox.type(profileName);
    }

    public String verifyProfileSelectedMatchOnHomePage(){
        LOGGER.info("Verify Selected Profile Name Match on Homepage");
        waitFor(activeProfile);
        activeProfile.hover();
        return activeProfile.getText();
    }

    public boolean verifyChooseAvatarTextPresent() {
        LOGGER.info("Verify 'Choose Icon' text on page");
        return chooseAvatarText.isElementPresent();
    }

    public boolean verifyCorrectIconDisplayed() {
        LOGGER.info("Verify correct icon is displayed");
        avatarSelectionTrack.hover();
        if (!avatarSelectionAttribute.isEmpty()) {
            String avatar = avatarSelectionAttribute.get(0).getAttribute(attributeName);
            selectAvatarForProfile();
            String avatarSelected = selectedAvatar.getAttribute(attributeName);
            if(avatarSelected.contains(avatar)){
                LOGGER.info("Correct avatar selected");
                return true;
            }
        }
        LOGGER.info("Incorrect avatar selected");
        return false;
    }

    public boolean verifySelectedIconDoesNotExistOnPage(String selectedProfileName) {
        avatarSelectionTrack.hover();
        if(!avatarSelectionAttribute.isEmpty()){
            LOGGER.info("Verify selected Icon Does Not Exist On Page");
           String avatarSelectionAttributeValue = avatarSelectionAttribute.get(0).getAttribute(attributeName);
           if(!avatarSelectionAttributeValue.contains(selectedProfileName)) {
               LOGGER.info("Selected avatar does not reappear on page");
               return true;
           }
        }
        LOGGER.info("Selected avatar reappear for selection again");
        return false;
    }

    public boolean verifyFirstAvatarIsVisible(int collectionTrack) {
        LOGGER.info("Verify the first avatar in collection is visible in horizontal scroll");
        return (getAvatarInCollection(collectionTrack, 0).getAttribute("aria-hidden").equalsIgnoreCase("false"));
    }

    public boolean verifyLastAvatarIsVisible(int collectionTrack) {
        LOGGER.info("Verify the last avatar in collection is visible in horizontal scroll:{}", collectionTrack);
        return (getAvatarInCollection(collectionTrack, getSlickTrackAvatarCollection(collectionTrack).size()-1).getAttribute("aria-hidden").equalsIgnoreCase("false"));
    }

    public String getSelectedAvatarName() {
        LOGGER.info("Get the name of the selected avatar in add-profile page");
        return selectedAvatar.getAttribute(attributeName);
    }

    public boolean verifyUsedAvatarIsNotVisible(String usedAvatarName) {
        LOGGER.info("Verify avatar used for another profile is not visible in edit profile page");
        for(ExtendedWebElement avatar: avatarNameCollection) {
            if(avatar.getAttribute(attributeName).equalsIgnoreCase(usedAvatarName))
                return false;
        }
        return true;
    }

    public boolean verifySelectAvatarPageVerticalScroll() {
        LOGGER.info("Verify that the page is scrollable vertically in select-avatar page");
        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        JavascriptExecutor j = (JavascriptExecutor) driver;
        util.scrollToBottom();
        waitForSeconds(1);
        Long d = (Long) j.executeScript(WebConstant.SCROLL_VERTICAL_PIXELS);
        util.scrollToBottom();
        waitForSeconds(1);
        while((Long) j.executeScript(WebConstant.SCROLL_VERTICAL_PIXELS) > d)
        {
            d = (Long) j.executeScript(WebConstant.SCROLL_VERTICAL_PIXELS);
            util.scrollToBottom();
            waitForSeconds(1);
        }
        appExUtil.scrollToTop();
        waitForSeconds(1);
        Long v = (Long) j.executeScript(WebConstant.SCROLL_VERTICAL_PIXELS);
        return (v == 0);
    }

    public boolean verifySelectAvatarPageHorizontalScroll() {
        LOGGER.info("Scroll to right in select-avatar page");
        while(!verifyLastAvatarIsVisible(1)) {
            scrollRightArrow.click();
            waitForSeconds(1);
        }
        while(!verifyFirstAvatarIsVisible(1)) {
            scrollLeftArrow.click();
            waitForSeconds(1);
        }
        JavascriptExecutor j = (JavascriptExecutor) driver;
        Long d = (Long) j.executeScript(WebConstant.SCROLL_HORIZONTAL_PIXELS);
        return (d == 0);
    }

    public String getAvatarName(int avatarIndex) {
        LOGGER.info("Get the avatar name");
        return avatarSelectionAttribute.get(avatarIndex).getAttribute(attributeName);
    }

    public DisneyPlusAddProfilePage updateProfile() {
        LOGGER.info("Updating profile with profile name and gender");
        addProfileName(ProfileConstant.PROFILE);  
        clickOnGenderDropdown().selectGender();
        addProfileSaveBtn.click();
        return this;
    }

    public boolean isGenderRequiredErrorVisible() {
        LOGGER.info("Verify the gender field required error message is visible");
        return addProfileGenderFieldRequiredError.isVisible();
    }
}
