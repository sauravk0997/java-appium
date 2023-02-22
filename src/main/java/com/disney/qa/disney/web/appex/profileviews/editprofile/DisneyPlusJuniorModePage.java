package com.disney.qa.disney.web.appex.profileviews.editprofile;

import com.disney.qa.api.disney.DisneyContentApiChecker;
import com.disney.qa.disney.web.DisneyWebKeys;
import com.disney.qa.disney.web.common.DisneyPlusBaseProfileViewsPage;
import com.disney.qa.disney.web.entities.WebConstant;
import com.fasterxml.jackson.databind.JsonNode;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusJuniorModePage extends DisneyPlusBaseProfileViewsPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    DisneyContentApiChecker apiHandler = new DisneyContentApiChecker();

    @FindBy(xpath = "//div[@class =  'slick-track']")
    private ExtendedWebElement avatarSelectionTrack;

    @FindBy(xpath = "//div[@class =  'slick-track']/div")
    private List<ExtendedWebElement> avatarSelection;

    @FindBy(xpath = "//*[@data-testid='profile-name-input']")
    private ExtendedWebElement primaryProfile;

    @FindBy(css = "#kidsMode + span")
    private ExtendedWebElement juniorModeToggle;

    @FindBy(xpath = "//input[@id='kidsMode']")
    private ExtendedWebElement juniorModeToggleStatus;

    @FindBy(css = "#addProfile")
    private ExtendedWebElement addProfileNameTextBox;

    @FindBy(xpath = "//*[@data-testid='junior-mode-learn-more-link']")
    private ExtendedWebElement juniorModeLearnMoreLink;

    @FindBy(css = "#kidProofExit + span")
    private ExtendedWebElement kidProofExitToggle;

    @FindBy(xpath = "//*[@id='kidProofExit-toggle-tooltip']/p")
    private ExtendedWebElement kidProofExitHoverMessage;

    @FindBy(xpath = "//*[@role='dialog']//*[contains(text(), '%s')]")
    private ExtendedWebElement turnOffJuniorModeText;

    @FindBy(xpath = "//*[@data-testid='container-1']//a")
    private List<ExtendedWebElement> characterCategoryTilesCollection;

    @FindBy(xpath = "//*[@data-testid='asset-wrapper-1-%s']/div")
    private ExtendedWebElement characterCategoryTileName;

    @FindBy(xpath = "//*[@data-testid='asset-wrapper-1-%s']")
    private ExtendedWebElement characterCategoryTile;

    private static final String SELECT_DARTH_VADER = "Darth Vade...";
    private static final String JUNIOR_PROFILE_TEXT = "Junior Profile";

    public DisneyPlusJuniorModePage(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getTurnOffJuniorModeText(String text) {
        return turnOffJuniorModeText.format(text);
    }

    public ExtendedWebElement getCharacterCategoryTileName(int tileIndex) {
        return characterCategoryTileName.format(tileIndex);
    }

    public ExtendedWebElement getCharacterCategoryTile(int i) {
        return characterCategoryTile.format(i);
    }

    public enum CharacterCategoryTiles {
        MICKEY_MOUSE_AND_FRIENDS(0, "Mickey and Friends", "mickey-and-friends"),
        PRINCESSES_AND_FAIRY_TALES(1, "Princesses and Fairy Tales", "princesses-and-fairy-tales"),
        DISNEY_JUNIOR(2, "Junior", "junior"),
        SUPER_HEROES(3, "Heroes", "heroes"),
        ACTION_ADVENTURE(4, "Adventures", "adventures"),
        ANIMALS_AND_NATURE(5, "Animals and Nature", "animals-and-nature");

        int position;
        String name;
        String url;

        CharacterCategoryTiles(int position, String name, String url) {
            this.position = position;
            this.name = name;
            this.url = url;
        }
        public String getNames() {
            return name;
        }

        public int getPosition() {
            return position;
        }

        public String getUrl() {
            return url;
        }
    }

    public DisneyPlusJuniorModePage selectProfileToEditFromEditProfilePage(String profileToBeSelectedToEdit) {
        LOGGER.info("Click on Profile to Edit");
        getGenericEqualsText(profileToBeSelectedToEdit).click();
        return this;
    }

    public String getPrimaryProfileText() {
        LOGGER.info("Verify Primary Profile Text Message");
        return primaryProfile.getAttribute("value");
    }

    public boolean verifyJuniorProfileOptionUnavailable() {
        LOGGER.info("Verify 'Kid's Profile' option does not exist");
        return getGenericEqualsText(JUNIOR_PROFILE_TEXT).isElementPresent();
    }

    public DisneyPlusJuniorModePage selectAvatarForProfile() {
        avatarSelectionTrack.hover();
        if (!avatarSelection.isEmpty()) {
            LOGGER.info("Selecting Avatar");
            avatarSelection.get(0).click();
            return this;
        }
        return this;
    }

    public DisneyPlusJuniorModePage clickOnJuniorModeToggle() {
        LOGGER.info("Click on Toggle to turn junior-profile on or off");
        waitFor(juniorModeToggle);
        juniorModeToggle.click();
        return this;
    }

    public boolean verifyJuniorToggleStatus(String checked) {
        LOGGER.info("Verify junior toggle button: {} ", checked);
        waitFor(juniorModeToggleStatus);
        return juniorModeToggleStatus.getAttribute("aria-checked").equals(checked);
    }

    public DisneyPlusJuniorModePage nameOfNewProfile(String nameOfNewProfile) {
        waitFor(addProfileNameTextBox);
        addProfileNameTextBox.type(nameOfNewProfile);
        return this;
    }

    public void clickOnEditProfileFromSelectProfilePage(JsonNode dictionary) {
        LOGGER.info("Click on Edit Profile on Select Profile Page");
        waitFor(getGenericEqualsText(apiHandler.getDictionaryItemValue(dictionary, DisneyWebKeys.PAGE_EDIT_BTN.getText())));
        getGenericEqualsText(apiHandler.getDictionaryItemValue(dictionary, DisneyWebKeys.PAGE_EDIT_BTN.getText())).click();
    }

    public DisneyPlusJuniorModePage createAdultProfile() {
        clickOnAddProfileFromAccount();
        selectAvatarForProfile();
        nameOfNewProfile("Darth Vader");
        clickOnSaveButton();
        selectProfileToEditFromEditProfilePage(SELECT_DARTH_VADER);
        getActiveProfile();
        clickOnLogout();
        return this;
    }

    public DisneyPlusJuniorModePage clickOnLearnMoreLink() {
        LOGGER.info("Click on junior mode learn more link");
        juniorModeLearnMoreLink.click(5);
        return this;
    }

    public DisneyPlusJuniorModePage clickOnKidProofExitToggle() {
        LOGGER.info("Click on kid-proof exit toggle");
        kidProofExitToggle.click();
        return this;
    }

    public String getKidProofExitHoverMessage() {
        LOGGER.info("Verify kid proof exit hover message is present");
        kidProofExitToggle.hover();
        return kidProofExitHoverMessage.getText();
    }

    public boolean isTurnOffJuniorModeTextPresent(String text) {
        LOGGER.info("Verify if {} text is present", text);
        return getTurnOffJuniorModeText(text).isElementPresent();
    }

    public int getCharacterCategoryTilesCount() {
        LOGGER.info("Get the number of character category tiles in Junior Mode home page");
        return characterCategoryTilesCollection.size();
    }

    public boolean verifyCharacterCategoryTileName() {
        LOGGER.info("Verify character category tiles are displayed");
        for(CharacterCategoryTiles tiles: CharacterCategoryTiles.values()) {
            if(!getCharacterCategoryTileName(tiles.getPosition()).getAttribute(WebConstant.ARIA_LABEL).contains(tiles.getNames()))
                return false;
        }
        return true;
    }

    public boolean verifyTilesCollectionPageURL() {
        LOGGER.info("Verify corresponding tile collection page URL is displayed");
        for(CharacterCategoryTiles tiles: CharacterCategoryTiles.values()) {
            getCharacterCategoryTile(tiles.getPosition()).click();
            waitForSeconds(1);
            if(!getCurrentUrl().contains(tiles.getUrl()))
                return false;
            clickOnHomeMenuOption();
        }
        return true;
    }
}
