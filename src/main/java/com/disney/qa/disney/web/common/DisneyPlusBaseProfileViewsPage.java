package com.disney.qa.disney.web.common;

import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.entities.ProfileEligibility;
import com.disney.qa.disney.web.entities.WebConstant;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

public class DisneyPlusBaseProfileViewsPage extends DisneyPlusBasePage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//*[text()='%s']")
    private ExtendedWebElement genericEqualsText;

    @FindBy(xpath = "//button[contains(text(), '%s')]")
    private ExtendedWebElement buttonContainText;

    @FindBy(xpath = "//span[contains(text(), '%s')]")
    private ExtendedWebElement spanContainsText;

    @FindBy(xpath = "//p[contains(text(), '%s')]")
    private ExtendedWebElement paraContainsText;

    @FindBy(xpath = "//div[contains(text(), '%s')]")
    private ExtendedWebElement divContainsText;

    @FindBy(css = ".slick-track")
    private ExtendedWebElement avatarSelectionTrack;

    @FindBy(css = ".slick-track>div")
    private List<ExtendedWebElement> avatarSelection;

    @FindBy(id = "dropdown-option_add-profile")
    private ExtendedWebElement addProfileButton;

    @FindBy(xpath = "//*[@data-testid='dropdown-option-0-add-profile']")
    private ExtendedWebElement addProfileFromAccount;

    @FindBy(xpath = "//*[@data-testid='add-profile-cta']")
    private ExtendedWebElement addProfileButtonOnPage;

    @FindBy(id = "active-profile")
    private ExtendedWebElement activeProfile;

    @FindBy(xpath = "//*[@data-testid='dropdown-option-1-edit-profiles']")
    private ExtendedWebElement accountEditProfile;

    @FindBy(xpath = " //*[contains(@data-testid, 'dropdown-option') and contains(@data-testid, 'logout')]")
    private ExtendedWebElement accountDropdownlogout;

    //Awaiting proper data-testid https://jira.disneystreaming.com/browse/DMGZWEB-16807
    @FindBy(xpath = "//button[@data-gv2elementkey='log_out']")
    private ExtendedWebElement accountLogoutMobile;

    @FindBy(xpath = "//*[@data-testid='asset-wrapper-1-%s']")
    public ExtendedWebElement kidProfileTiles;

    @FindBy(id = "search-input")
    private ExtendedWebElement defaultSearchBarText;

    @FindBy(xpath = "//*[@data-testid='episodes-button']")
    private ExtendedWebElement episodesButton;

    @FindBy(xpath = "//*[@data-testid='extras-button']")
    private ExtendedWebElement extrasButton;

    @FindBy(xpath = "//*[@data-testid='details-button']")
    private ExtendedWebElement detailsButton;

    @FindBy(xpath = "//*[@data-testid='suggested-button']")
    private ExtendedWebElement suggestedButton;

    @FindBy(xpath = "//*[@data-testid='navigation-item-0-HOME']")
    private ExtendedWebElement homeMenuOption;

    @FindBy(xpath = "//*[@data-testid='edit-profiles-button']")
    private ExtendedWebElement editProfileOnPage;

    @FindBy(xpath = "//*[@data-testid='add-profile-save-button']")
    private ExtendedWebElement saveButton;

    @FindBy(xpath = "//*[@data-testid='add-profile-cancel']")
    private ExtendedWebElement cancelButton;

    @FindBy(xpath = "//*[@data-testid='edit-profiles-done']")
    private ExtendedWebElement editProfilesDoneButton;

    @FindBy(xpath = "//*[@data-testid='edit-profile-done']")
    private ExtendedWebElement editProfileDoneButton;

    @FindBy(xpath = "//*[@data-testid='manage-account-cta']")
    private ExtendedWebElement dropdownAccountMobile;

    @FindBy(xpath = "//*[@data-testid='dropdown-option-3-account']")
    private ExtendedWebElement dropdownAccount;

    @FindBy(xpath = "//*[@data-testid='dropdown-option-4-help']")
    private ExtendedWebElement dropdownHelp;

    @FindBy(xpath = "//*[@data-testid='avatar-skip-button']")
    private ExtendedWebElement skipButton;

    @FindBy(xpath = "//*[@data-testid='dropdown-option-2-app-settings']")
    private ExtendedWebElement dropdownAppSettings;

    @FindBy(xpath = "//*[@data-testid='profile-avatar-%s']")
    private ExtendedWebElement profileOnEditPage;

    @FindBy(xpath = "//*[@data-testid='dropdown-profile-%s']")
    private ExtendedWebElement dropdownProfile;

    @FindBy(xpath = "//*[@data-testid='gender-picker-dropdown-menu']/div[1]/div[1]/div[1]/div")
    public List<ExtendedWebElement> genderDropdownOptions;

    @FindBy(xpath = "//*[@data-testid='dropdown-selector-indicator']")
    public ExtendedWebElement genderDropdownBtn;

    @FindBy(xpath = "//*[@data-testid='gender-picker-selector-clear-indicator']")
    public ExtendedWebElement genderDropdownClearBtn;

    @FindBy(xpath = "//*[@data-testid='dob-input']/input")
    public ExtendedWebElement dobField;

    @FindBy(xpath = "//*[@id='react-select-gender-picker-dropdown-option-0-%s']")
    public ExtendedWebElement genderDropdownOption;

    @FindBy(xpath = "//*[@data-testid='profile-avatar-0']")
    public ExtendedWebElement primaryProfileBtn;

    @FindBy(xpath = "//*[@data-testid='profile-avatar-0']/h3")
    public ExtendedWebElement primaryProfileBtnFieldValue;

    @FindBy(xpath = "//*[@data-testid='btn-consent-accept']")
    private ExtendedWebElement agreeBtn;

    @FindBy(xpath = "//*[@data-testid='btn-consent-decline']")
    private ExtendedWebElement declineBtn;

    @FindBy(linkText = "Log out")
    private ExtendedWebElement profileLogoutBtn;

    @FindBy(xpath = "//*[@data-testid='gender-picker-disabled']")
    private ExtendedWebElement genderDropdownDisabled;

    @FindBy(xpath = "//*[@data-testid='gender-picker-enabled']")
    private ExtendedWebElement genderDropdownEnabled;

    @FindBy(xpath = "//*[@data-testid='gender-picker-enabled']//*[contains(text(),'This field is required')]")
    private ExtendedWebElement updateProfileGenderErrorLabel;

    @FindBy(xpath = "//*[@data-testid='text-input-error']")
    private ExtendedWebElement profileNameErrorLabel;

    @FindBy(xpath = "//*[@data-testid='dropdown-option-0-exit-kids-profile']")
    private ExtendedWebElement dropdownExitJuniorProfile;

    @FindBy(xpath = "//label/p[contains(text(), 'Junior Mode')]")
    private ExtendedWebElement juniorModeLabel;

    @FindBy(xpath = "//*[@data-testid='delete-profile-button']")
    private ExtendedWebElement deleteProfileBtn;

    public DisneyPlusBaseProfileViewsPage(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getSelectedProfileOnEditPage(int profileIndexOnEditPage) {
        return profileOnEditPage.format(profileIndexOnEditPage);
    }

    public ExtendedWebElement getSelectedDropdownProfile(int selectDropdownProfile) {
        waitFor(dropdownProfile.format(selectDropdownProfile));
        return dropdownProfile.format(selectDropdownProfile);
    }

    public ExtendedWebElement getGenericEqualsText(String text) {
        return genericEqualsText.format(text);
    }

    public ExtendedWebElement getTextFromParaElement(String text) {
        return paraContainsText.format(text);
    }

    public ExtendedWebElement getTextFromDivElement(String text) {
        return divContainsText.format(text);
    }

    public ExtendedWebElement getButtonContainText(String text) {
        return buttonContainText.format(text);
    }

    public ExtendedWebElement getAddButtonFromAccount() {
        waitFor(addProfileFromAccount);
        return addProfileFromAccount;
    }

    public ExtendedWebElement getActiveProfile() {
        waitFor(activeProfile, EXPLICIT_TIMEOUT);
        activeProfile.hover();
        return activeProfile;
    }

    public ExtendedWebElement getActiveProfileNoHover() {
        waitFor(activeProfile);
        return activeProfile;
    }

    public String getActiveProfileTextAttribute() {
        return getActiveProfileNoHover().getText();
    }

    public ExtendedWebElement getSkipButton() {
        waitFor(skipButton);
        return skipButton;
    }

    public ExtendedWebElement getAddProfileButtonOnPage() {
        waitFor(addProfileButtonOnPage);
        return addProfileButtonOnPage;
    }

    public ExtendedWebElement getAccountLogout() {
        waitFor(accountDropdownlogout);
        return accountDropdownlogout;
    }

    public ExtendedWebElement getAccountLogoutMobile() {
        waitFor(accountLogoutMobile);
        return accountLogoutMobile;
    }

    public ExtendedWebElement getAccountProfileEdit() {
        waitFor(accountEditProfile);
        return accountEditProfile;
    }

    public ExtendedWebElement getDropdownAccountMobile() {
        waitFor(dropdownAccountMobile, EXPLICIT_TIMEOUT);
        return dropdownAccountMobile;
    }

    public ExtendedWebElement getDropdownAccount() {
        waitFor(dropdownAccount, EXPLICIT_TIMEOUT);
        return dropdownAccount;
    }

    public ExtendedWebElement getDropdownAppSettings() {
        return dropdownAppSettings;
    }

    public ExtendedWebElement getAccountDropdownHelp() {
        waitFor(dropdownHelp);
        return dropdownHelp;
    }

    public ExtendedWebElement getEditProfileDoneButton() {
        waitFor(editProfileDoneButton);
        return editProfileDoneButton;
    }

    public ExtendedWebElement getEditProfilesDoneButton() {
        waitFor(editProfilesDoneButton);
        return editProfilesDoneButton;
    }

    public ExtendedWebElement getExitJuniorProfile(){
        return dropdownExitJuniorProfile;
    }

    public void clickOnPrimaryProfileOnEditPage() {
        LOGGER.info("Select Primary Profile on Edit Page");
        profileOnEditPage.format(0).click();
    }

    public void clickOnSecondaryProfileOnEditPage() {
        LOGGER.info("Select Secondary Profile on Edit Page");
        waitForPageToFinishLoading();
        getSelectedProfileOnEditPage(1).click(5);
    }

    public void clickOnDropDownProfileFirst() {
        LOGGER.info("Click on First Dropdown profile");
        getSelectedDropdownProfile(0).click();
    }

    public void clickOnAddProfileButtonOnPage() {
        LOGGER.info("Click on 'Add Profile' button on page");
        addProfileButtonOnPage.click();
    }

    public void clickOnHomeMenuOption() {
        LOGGER.info("Click On Home Menu");
        homeMenuOption.click();
    }

    public boolean isHomeMenuOptionPresent() {
        LOGGER.info("Verify Home Menu");
        return homeMenuOption.isElementPresent();
    }

    public DisneyPlusBaseProfileViewsPage clickOnEditProfilesDoneButton() {
        LOGGER.info("Click On Done in who's watching page");
        getEditProfilesDoneButton().click();
        return this;
    }

    public void clickOnEditProfileDoneButton() {
        LOGGER.info("Click On Done in edit profile page");
        getEditProfileDoneButton().clickByJs();
    }

    public DisneyPlusBaseProfileViewsPage clickOnEditProfile() {
        LOGGER.info("Click on 'Edit Profile' on dropdown");
        getAccountProfileEdit().hover();
        getAccountProfileEdit().clickByJs(5);
        return this;
    }

    public DisneyPlusBaseProfileViewsPage clickOnEditProfileFromPage() {
        LOGGER.info("Click on 'Edit Profile' on page");
        editProfileOnPage.click(5);
        return this;
    }

    public boolean isEditProfileFromPagePresent() {
        LOGGER.info("Verify 'Edit Profile' button on page");
        return editProfileOnPage.isElementPresent();
    }

    public boolean isSecondaryProfileFromEditProfilePageVisible() {
        return getSelectedProfileOnEditPage(1).isVisible();
    }


    public void clickOnSkip() {
        LOGGER.info("Click on 'Skip' button");
        getSkipButton().click();
    }

    public void clickOnCancelButton() {
        LOGGER.info("Click on 'Cancel'");
        cancelButton.click(5);
    }

    public DisneyPlusBaseProfileViewsPage clickOnAddProfileFromAccount(){
        LOGGER.info("Hover to active profile and click add button from account");
        getActiveProfile();
        getAddButtonFromAccount().clickByJs();
        return this;
    }

    public DisneyPlusBaseProfileViewsPage clickOnSaveButton() {
        LOGGER.info("Click on 'Save' button");
        saveButton.click(5);
        return this;
    }

    public void clickOnLogout() {
        LOGGER.info("Click Log out on account dropdown");
        getAccountLogout().clickByJs(5);
        pause(DELAY); // Needed after log out
    }

    public void clickOnLogoutMobile() {
        LOGGER.info("Click on 'Log Out' button");
        getAccountLogoutMobile().click();
        pause(DELAY); // Needed after log out
    }

    public void clickOnLogoutWebOrMobile(boolean isMobile) {
        if (isMobile) {
            clickOnLogoutMobile();
        } else {
            clickOnLogout();
        }
    }

    public void selectAvatarForNewProfile() {
        avatarSelectionTrack.hover();
        if (!avatarSelection.isEmpty()) {
            LOGGER.info("Selecting Avatar to create new profile");
            avatarSelection.get(0).click();
        }
    }

    public boolean isAccountDropdownAppSettingsVisible() {
        return getDropdownAppSettings().isVisible();
    }

    public boolean isAccountDropdownLogoutVisible() {
        return getAccountLogout().isVisible();
    }

    public boolean isAccountDropdownAddProfileVisible() {
        return getAddButtonFromAccount().isVisible();
    }

    public boolean isAccountDropdownHelpVisible() {
        return getAccountDropdownHelp().isVisible();
    }

    public boolean isEditProfileBtnVisible() {
        return getAccountProfileEdit().isVisible();
    }

    public boolean isAccountDropdownAccountBtnVisible() {
        return getDropdownAccount().isVisible();
    }

    public boolean isDropdownProfileFirstPresent() {
        return getSelectedDropdownProfile(0).isElementPresent();
    }

    @Override
    public boolean isOpened() {
        LOGGER.info("Verifying base profile view page is opened");
        waitFor(getAddButtonFromAccount());
        getActiveProfile();
        return getAddButtonFromAccount().isElementPresent();
    }

    public boolean isOpened(int timeout) {
        LOGGER.info("Verifying base profile view page is opened");
        waitFor(getAddButtonFromAccount());
        return getAddButtonFromAccount().isElementPresent(timeout);
    }

    public boolean verifyKidProfileTile(int tileNumber, String tileValue) {
        LOGGER.info("Verify '" + tileValue + "' is present for kid profile.");
        waitFor(kidProfileTiles.format(tileNumber), 5);
        return kidProfileTiles.format(tileNumber).isElementPresent();
    }

    public void searchFor(String textToSearch) {
        LOGGER.info("Searching for: {}", textToSearch);
        defaultSearchBarText.type(textToSearch);
    }

    public void verifyEpisodesTab() {
        if (episodesButton.isElementPresent()) {
            PAGEFACTORY_LOGGER.info("'Episode' Tab is present");
            episodesButton.click();
        }
    }

    public void verifyExtrasTab() {
        if (extrasButton.isElementPresent()) {
            PAGEFACTORY_LOGGER.info("'Extras' Tab is present");
            extrasButton.click();
        }
    }

    public void verifyDetailsTab() {
        if (detailsButton.isElementPresent()) {
            PAGEFACTORY_LOGGER.info("'Details' Tab is present");
            detailsButton.click();
        }
    }

    public void verifySuggestedTab() {
        if (suggestedButton.isElementPresent()) {
            PAGEFACTORY_LOGGER.info("'Suggested' Tab is present");
            suggestedButton.click();
        }
    }

    public DisneyPlusBaseProfileViewsPage selectGender() {
        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        genderDropdownOptions.get(appExUtil.generateRandomNumber(genderDropdownOptions.size())).click();
        return this;
    }

    public DisneyPlusBaseProfileViewsPage clickOnGenderDropdown() {
        LOGGER.info("Click on the Gender dropdown in gender-identity page");
        genderDropdownBtn.click();
        return this;
    }

    public DisneyPlusBaseProfileViewsPage clickOnGenderClearBtn() {
        LOGGER.info("Click on the Gender dropdown clear button");
        genderDropdownClearBtn.click();
        return this;
    }

    public DisneyPlusBaseProfileViewsPage clickOnPrimaryProfileBtn() {
        LOGGER.info("Click on the primary profile button in Select Profile page");
        primaryProfileBtn.click(3);
        return this;
    }

    public String getDOBValue() {
        LOGGER.info("Get date of birth value while editing profile initially");
        return dobField.getAttribute("value");
    }

    public String getPrimaryProfileName() {
        LOGGER.info("Get the primary profile name in edit-profiles page");
        return primaryProfileBtnFieldValue.getText();
    }

    public DisneyPlusBaseProfileViewsPage clickOnAgreeBtn() {
        LOGGER.info("Click on the Agree button");
        agreeBtn.click(5);
        return this;
    }

    public DisneyPlusBaseProfileViewsPage clickOnDeclineBtn() {
        LOGGER.info("Click on the Decline button");
        declineBtn.click(5);
        return this;
    }

    public DisneyPlusBaseProfileViewsPage clickOnDOBField() {
        LOGGER.info("Click on DOB field in add-dob page");
        dobField.click();
        return this;
    }

    public DisneyPlusBaseProfileViewsPage clickOnProfileLogoutBtn() {
        LOGGER.info("Click on the Logout button the DOB collection page");
        profileLogoutBtn.click();
        return this;
    }

    public boolean verifyProfileLogoutBtnIsVisible() {
        LOGGER.info("Verify that Log out button is visible in Complete-account-info page");
        return profileLogoutBtn.isVisible();
    }

    public DisneyPlusBaseProfileViewsPage enterDOB(ProfileEligibility profileEligibility) {
        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        dobField.type(appExUtil.formatDate(WebConstant.DATE_FORMAT_TWO, appExUtil.generateDOB(profileEligibility.getEligibilityAge()), WebConstant.DATE_FORMAT_ONE));
        return this;
    }

    public boolean isGenderDropdownDisabled() {
        LOGGER.info("Verify gender dropdown is disabled");
        return genderDropdownDisabled.isElementPresent();
    }

    public boolean isGenderDropdownEnabled() {
        LOGGER.info("Verify gender dropdown is enabled");
        return genderDropdownEnabled.isElementPresent();
    }

    public boolean isAgreeBtnPresent() {
        LOGGER.info("Verify the minor consent agree button is present");
        return agreeBtn.isElementPresent();
    }

    public boolean isDeclineBtnPresent() {
        LOGGER.info("Verify the minor consent agree button is present");
        return declineBtn.isElementPresent();
    }

    public boolean isUpdateProfileGenderErrorVisible() {
        LOGGER.info("Verify error label is displayed for missing gender field value");
        return updateProfileGenderErrorLabel.isVisible();
    }

    public String getProfileNameErrorLabelValue() {
        LOGGER.info("Get the error label displayed for profile name");
        return profileNameErrorLabel.getText();
    }

    public boolean isJuniorModeVisible() {
        LOGGER.info("Verify Junior Mode text is visible");
        return getGenericEqualsText("Junior Mode").isVisible();
    }

    public boolean isExitJuniorModePresent() {
        LOGGER.info("Verify exit junior mode option is present");
        return dropdownExitJuniorProfile.isElementPresent();
    }

    public String getExitJuniorModeDropdownLabel() {
        LOGGER.info("Get the label of exit junior mode option");
        return dropdownExitJuniorProfile.getText();
    }

    public DisneyPlusBaseProfileViewsPage clickOnExitJuniorMode() {
        LOGGER.info("Click on exit junior mode");
        dropdownExitJuniorProfile.click();
        return this;
    }

    public boolean isTextPresentInParaElement(String text) {
        LOGGER.info("Verify if {} is present in para element", text);
        return getTextFromParaElement(text).isElementPresent();
    }

    public boolean isTextPresentInDivElement(String text) {
        LOGGER.info("Verify if {} is present in div element", text);
        return getTextFromDivElement(text).isElementPresent();
    }

    public boolean isJuniorModeLabelPresent() {
        LOGGER.info("Verify Junior Mode label is present");
        return juniorModeLabel.isElementPresent();
    }

    public boolean isTextPresent(String text) {
        LOGGER.info("Verify 'text' is present in the page");
        return getGenericEqualsText(text).isElementPresent();
    }

    public boolean isDeleteProfileBtnPresent() {
        LOGGER.info("Verify Delete Profile button is present");
        return deleteProfileBtn.isElementPresent();
    }
}
