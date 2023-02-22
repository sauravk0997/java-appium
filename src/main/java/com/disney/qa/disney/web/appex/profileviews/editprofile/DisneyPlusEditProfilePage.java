package com.disney.qa.disney.web.appex.profileviews.editprofile;

import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.common.DisneyPlusBaseProfileViewsPage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusEditProfilePage extends DisneyPlusBaseProfileViewsPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private String randomLocale;
    private String randomLanguage;

    public DisneyPlusEditProfilePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(css = "#logo")
    private ExtendedWebElement disneyPlusLogo;

    @FindBy(xpath = "//div[@class =  'slick-track']")
    private ExtendedWebElement avatarSelectionTrack;

    @FindBy(css = "#addProfile")
    private ExtendedWebElement addProfileNameTextBox;

    @FindBy(id = "editProfile")
    private ExtendedWebElement editProfileNameField;

    @FindBy(xpath = "//*[@data-testid='dob-field']/div[1]/div[1]/p")
    public ExtendedWebElement editProfileDOBFieldValue;

    @FindBy(xpath = "//*[@data-testid='dob-field']")
    public ExtendedWebElement editProfileDOBField;

    @FindBy(xpath = "//*[@data-testid='gender-field']")
    public ExtendedWebElement editProfileGenderBtn;

    @FindBy(xpath = "//*[@data-testid='gender-field']/div/div[2]/p")
    public ExtendedWebElement editProfileGenderValue;

    @FindBy(xpath = "//*[@aria-label=\"arrowDown\"][@aria-hidden=\"true\"]")
    private ExtendedWebElement dropdownArrow;

    @FindBy(xpath = "//*[contains(text(), '%s')]")
    private ExtendedWebElement genericContainText;

    @FindBy(xpath = "//*[@id='backgroundVideo']//following::div[1]")
    private ExtendedWebElement backgroundVideoExplainer;

    @FindBy(xpath = "//*[@data-testid='confirm-dob']")
    private ExtendedWebElement addDOBConfirmBtn;

    @FindBy(xpath = "//*[@data-testid='help-center-link']")
    private ExtendedWebElement disneyPlusHelpCentreLink;

    @FindBy(xpath = "//*[@data-testid='profile-name-input']")
    private ExtendedWebElement profileNameField;

    @FindBy(xpath = "//*[@data-testid='edit-gender-identity-save-button']")
    public ExtendedWebElement editGenderSaveBtn;

    @FindBy(xpath = "//*[@data-testid='edit-gender-identity-cancel-button']")
    public ExtendedWebElement editGenderCancelBtn;

    @FindBy(xpath = "//*[@data-testid='edit-gender-identity-form']//*[contains(text(),'This field is required')]")
    public ExtendedWebElement editGenderFieldRequiredError;

    @FindBy(xpath = "//*[@data-testid='edit-gender-identity-form']/div[1]/div[1]/div[1]/div[1]/div[1]")
    public ExtendedWebElement editGenderDropdownValue;

    @FindBy(xpath = "//*[@data-testid='flash-notification-message']")
    public ExtendedWebElement updateConfirmationMsg;

    @FindBy(xpath = "//*[@data-testid='gender-picker-dropdown-control']/div[1]/div[1]")
    public ExtendedWebElement profileGenderDropdownValue;

    @FindBy(xpath = "//*[@data-testid='personal-info-disclaimer-link']")
    public ExtendedWebElement profilePersonalInfoDisclaimerLink;

    @FindBy(xpath = "//*[@data-testid='selected-avatar-image']")
    public ExtendedWebElement editProfileSelectAvatar;

    @FindBy(xpath = "//*[@data-testid='edit-profile-done']")
    public ExtendedWebElement editProfileDoneBtn;

    @FindBy(xpath = "//div[@data-testid='input-error-label']")
    public ExtendedWebElement dobInputErrorLabel;

    @FindBy(xpath = "//*[@data-testid='-title']")
    public ExtendedWebElement notEligibleToUseServiceHeaderText;

    @FindBy(xpath = "//*[@data-testid='-button']")
    public ExtendedWebElement helpCentreBtn;

    @FindBy(xpath = "//*[@data-testid='-secondary-button']")
    public ExtendedWebElement dismissBtn;

    @FindBy(xpath = "//form[@name='dssLogin']")
    public ExtendedWebElement confirmPasswordModalForm;

    @FindBy(linkText = "Log out")
    public ExtendedWebElement logoutBtn;

    @FindBy(xpath = "//*[@data-testid='maturityRating']")
    private ExtendedWebElement contentRating;

    @FindBy(xpath = "//*[@id='maturityRating']/p")
    private ExtendedWebElement contentRatingHoverMessage;

    @FindBy(css = "#groupWatch + span")
    private ExtendedWebElement groupWatchToggle;

    @FindBy(xpath = "//*[@id='groupWatch-toggle-tooltip']/p")
    private ExtendedWebElement groupWatchHoverMessage;

    private static final String PROFILE_NAME = "DARTH VADE...";

    //Commented out items aren't supported through xml test runner
    public enum AppLangDropdownItems {
        //DEUTSCH("Deutsch"),
        ENGLISH_UK("English (UK)"),
        ENGLISH_US("English (US)"),
        ESPANOL("Español"),
        //ESPANOL_LATINOAMERICANO("Español (Latinoamericano)"),
        //FRANCAIS("Français"),
        FRANCAIS_CANADIEN("Français (Canada)"),
        //ITALIANO("Italiano"),
        NEDERLANDS("Nederlands");

        private String language;

        AppLangDropdownItems(String language) {
            this.language = language;
        }

        public String getLanguage() {
            return language;
        }
    }

    public boolean isLogoPresent() {
        LOGGER.info("Verify Logo");
        waitFor(disneyPlusLogo);
        return disneyPlusLogo.isElementPresent();
    }

    public DisneyPlusEditProfilePage selectProfileName(String profileText) {
        LOGGER.info("Selecting Profile From Profile Selection Page");
        getGenericEqualsText(profileText).hover();
        getGenericEqualsText(profileText).click(7);
        return this;
    }

    public void nameOfNewProfile(String nameOfNewProfile) {
        waitFor(addProfileNameTextBox);
        addProfileNameTextBox.type(nameOfNewProfile);
    }

    public DisneyPlusEditProfilePage nameOfEditProfile(String nameOfEditProfile) {
        waitFor(editProfileNameField);
        editProfileNameField.click();
        editProfileNameField.type("");
        waitForSeconds(1);
        editProfileNameField.type(nameOfEditProfile);
        return this;
    }

    public String verifySelectedProfileName() {
        LOGGER.info("Verify Selected Profile Name");
        return getGenericEqualsText(PROFILE_NAME).getText();
    }

    public boolean isProfileNameFieldPresent() {
        return editProfileNameField.isElementPresent();
    }

    public DisneyPlusEditProfilePage getDropdown() {
        waitFor(dropdownArrow);
        dropdownArrow.click();
        return this;
    }

    public List<String> getAppLangDropdownList() {
        List<String> appLangList = new ArrayList<>();
        for (AppLangDropdownItems item : AppLangDropdownItems.values()) {
            appLangList.add(item.getLanguage());
        }
        return appLangList;
    }

    public boolean verifyDropdownItemsPresent(List dropdown) {
        int count = 0;
        for (int i = 0; i < dropdown.size(); i++) {
            LOGGER.info("Checking dropdown for item: {}", dropdown.get(i));
            if (getGenericEqualsText(dropdown.get(i).toString()).isElementPresent()) {
                count++;
            } else LOGGER.info("{} missing from dropdown", dropdown.get(i));
        }
        return count == dropdown.size();
    }

    public boolean isLangDisplayedLocalized(String locale) {
        AppLangDropdownItems dropdownItem = AppLangDropdownItems.ENGLISH_US;
        switch (locale) {
            case "US":
                dropdownItem = AppLangDropdownItems.ENGLISH_US;
                break;
            case "GB":
                dropdownItem = AppLangDropdownItems.ENGLISH_UK;
                break;
            case "ES":
                dropdownItem = AppLangDropdownItems.ESPANOL;
                break;
            case "CA":
                dropdownItem = AppLangDropdownItems.FRANCAIS_CANADIEN;
                break;
            case "NL":
                dropdownItem = AppLangDropdownItems.NEDERLANDS;
                break;
            default:
                LOGGER.error("Unrecognized 'locale'");
        }
        return getGenericEqualsText(dropdownItem.getLanguage()).isElementPresent();
    }

    public String getRandomLocale() {
        return randomLocale;
    }

    public String getRandomLanguage() {
        return randomLanguage;
    }

    public DisneyPlusEditProfilePage pickRandomLanguage() {
        String newRandomLocale = null;
        String newRandomLang = null;
        String randomDropdownLanguage = getAppLangDropdownList().get(new SecureRandom().nextInt(getAppLangDropdownList().size()));
        LOGGER.info("Setting random language to {}", randomDropdownLanguage);
        getGenericEqualsText(randomDropdownLanguage).click();
        switch (randomDropdownLanguage) {
            case "Deutsch":
                LOGGER.info("Language unsupported");
                break;
            case "English (UK)":
                newRandomLocale = "GB";
                newRandomLang = "en-GB";
                break;
            case "English (US)":
                newRandomLocale = "US";
                newRandomLang = "en";
                break;
            case "Español":
            case "Español (Latinoamericano)":
                newRandomLocale = "ES";
                newRandomLang = "es-ES";
                break;
            case "Français":
            case "Français (Canada)":
                newRandomLocale = "CA";
                newRandomLang = "fr-CA";
                break;
            case "Italiano":
                LOGGER.info("Language unsupported");
                break;
            case "Nederlands":
                newRandomLocale = "NL";
                newRandomLang = "nl";
                break;
            default:
                LOGGER.error("Unrecognized language");
        }
        this.randomLocale = newRandomLocale;
        this.randomLanguage = newRandomLang;
        return this;
    }

    public boolean isBackgroundVideoExplainerPresent() {
        return backgroundVideoExplainer.isElementPresent();
    }

    public boolean verifyDOBFieldNotEditable() {
        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        return appExUtil.isAttributePresent(editProfileDOBField, "disabled");
    }

    public DisneyPlusEditProfilePage clickOnConfirmBtn() {
        addDOBConfirmBtn.click(5);
        return this;
    }

    public DisneyPlusEditProfilePage clickOnGenderBtn() {
        LOGGER.info("Click on the Gender button");
        editProfileGenderBtn.click();
        return this;
    }

    public boolean isGenderFieldRequiredErrorVisible() {
        LOGGER.info("Verify the gender field required error message is visible");
        return editGenderFieldRequiredError.isVisible();
    }

    public String getGender() {
        LOGGER.info("Get the gender value in edit profile page");
        return editProfileGenderValue.getText();
    }

    public String getEditGenderDropdownValue() {
        LOGGER.info("Get the gender value in edit gender page");
        return editGenderDropdownValue.getText();
    }

    public DisneyPlusEditProfilePage clickOnEditGenderCancelBtn() {
        LOGGER.info("Click on cancel button in edit gender page");
        editGenderCancelBtn.click();
        return this;
    }

    public boolean isUpdateConfirmationMsgVisible() {
        LOGGER.info("Verify 'Updated' confirmation message is visible");
        return updateConfirmationMsg.isVisible();
    }

    public DisneyPlusEditProfilePage clickOnEditGenderSaveBtn() {
        LOGGER.info("Click on Save button in edit gender page");
        editGenderSaveBtn.click();
        return this;
    }

    public boolean isGenderDropdownOptionsVisible() {
        LOGGER.info("Verify gender dropdown options are visible");
        for (int i = 0; i < genderDropdownOptions.size(); i++) {
            if (genderDropdownOption.format(i).isVisible()) {
                if (i == genderDropdownOptions.size() - 1)
                    return true;
            } else
                return false;
        }
        return false;
    }

    public String getProfileGenderValue() {
        LOGGER.info("Get the gender value in 'Lets update your profile' page");
        return profileGenderDropdownValue.getText();
    }

    public String getEditProfileDOBValue() {
        LOGGER.info("Get the date of birth value in Edit profile page");
        return editProfileDOBFieldValue.getText();
    }

    public DisneyPlusEditProfilePage clickOnAvatarImage() {
        LOGGER.info("Click on the avatar image in edit-profile page to select a different avatar");
        editProfileSelectAvatar.click();
        return this;
    }

    public boolean isAvatarVisible() {
        LOGGER.info("Verify the avatar image is visible");
        return editProfileSelectAvatar.isVisible();
    }

    public String getEditProfileNameFieldValue() {
        LOGGER.info("Get the profile name field value in edit-profile page");
        return editProfileNameField.getAttribute("value");
    }

    public DisneyPlusEditProfilePage clickOnEditProfileDoneBtn() {
        LOGGER.info("Click on Done button in edit-profile page");
        editProfileDoneBtn.click();
        return this;
    }

    public String getDOBInputErrorLabelValue() {
        LOGGER.info("Get the error label displayed for invalid DOB");
        return dobInputErrorLabel.getText();
    }

    public String getNotEligibleToUseServiceText() {
        LOGGER.info("Get the header text related to ineligibility");
        return notEligibleToUseServiceHeaderText.getText();
    }

    public DisneyPlusEditProfilePage clickOnDismissBtn() {
        LOGGER.info("Click on Dismiss button in account-blocked page");
        dismissBtn.click();
        return this;
    }

    public DisneyPlusEditProfilePage clickOnHelpCentreBtn() {
        LOGGER.info("Click on help centre button in account-blocked page");
        helpCentreBtn.click();
        return this;
    }

    public boolean isConfirmWithPasswordModalVisible() {
        LOGGER.info("Verify the Confirm with password modal is visible");
        return confirmPasswordModalForm.isVisible();
    }

    public boolean isEditProfileDOBFieldPresent() {
        LOGGER.info("Verify presence of DOB field in edit profile page");
        return editProfileDOBField.isElementPresent();
    }

    public boolean isEditProfileDOBFieldValuePresent() {
        LOGGER.info("Verify presence of DOB field value in edit profile page");
        return editProfileDOBFieldValue.isElementPresent();
    }

    public boolean isEditProfileGenderFieldPresent() {
        LOGGER.info("Verify presence of Gender field in edit profile page");
        return editProfileGenderBtn.isElementPresent();
    }

    public boolean isEditProfileGenderFieldValuePresent() {
        LOGGER.info("Verify presence of Gender field value in edit profile page");
        return editProfileGenderValue.isElementPresent();
    }

    public DisneyPlusEditProfilePage clickOnLogoutBtn() {
        LOGGER.info("Click on the Logout button in Complete-account-info page");
        logoutBtn.click();
        return this;
    }

    public boolean isLogoutBtnVisible() {
        LOGGER.info("Verify that Log out button is visible in Complete-account-info page");
        return logoutBtn.isVisible();
    }

    public DisneyPlusEditProfilePage clearProfileNameField() {
        LOGGER.info("Clear the profile name field in update profile page");
        int length = profileNameField.getAttribute("value").length();
        for (int i = 0; i < length; i++) {
            profileNameField.sendKeys(Keys.BACK_SPACE);
        }
        return this;
    }

    public DisneyPlusEditProfilePage enterProfileName(String profileName) {
        LOGGER.info("Clear the profile name field in update profile page");
        profileNameField.doubleClick();
        profileNameField.type("");
        profileNameField.type(profileName);
        return this;
    }

    public boolean isDisneyHelpLinkPresent() {
        LOGGER.info("Verify Disney+ Help Centre link is present");
        return disneyPlusHelpCentreLink.isElementPresent();
    }

    public String getContentRatingHoverMessage() {
        LOGGER.info("Get content rating hover message");
        contentRating.hover();
        waitForSeconds(3);
        return contentRatingHoverMessage.getText();
    }

    public String getGroupWatchHoverMessage() {
        LOGGER.info("Get group watch hover message");
        groupWatchToggle.hover();
        return groupWatchHoverMessage.getText();
    }

    public boolean isGroupWatchToggleDisabled() {
        LOGGER.info("Verify if groupwatch toggle is disabled");
        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        return appExUtil.isAttributePresent(groupWatchToggle, "disabled");
    }
}
