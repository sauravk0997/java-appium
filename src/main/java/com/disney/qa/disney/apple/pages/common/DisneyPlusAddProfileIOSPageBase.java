package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.utils.mobile.IMobileUtils;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.Map;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusAddProfileIOSPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(accessibilityId = "kidsProfileToggleCell")
    protected ExtendedWebElement kidsProfileToggleCell;

    @ExtendedFindBy(accessibilityId = "toggleView")
    protected ExtendedWebElement juniorModeToggle;

    @ExtendedFindBy(accessibilityId = "profileNameTextFieldIdentifier")
    protected ExtendedWebElement profileNameTextFieldIdentifier;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[$type='XCUIElementTypeImage' AND name='badgeIcon'$]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeImage[1]")
    protected ExtendedWebElement addProfileAvatar;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label == \"Add Profile\"`]")
    protected ExtendedWebElement addProfileBtn;

    @ExtendedFindBy(accessibilityId = "Cancel")
    private ExtendedWebElement cancelButton;

    //Visibility set to false
    //dict key: create_profile_add_profile
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Add Profile\"`]")
    private ExtendedWebElement addProfileBtnTitle;

    @ExtendedFindBy(accessibilityId = "Skip")
    protected ExtendedWebElement skipBtn;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextField[`value == \"Profile Name\"`]")
    private ExtendedWebElement profileNameTextField;

    @ExtendedFindBy(accessibilityId = "editProfile")
    private ExtendedWebElement editProfile;

    @ExtendedFindBy(accessibilityId = "titleLabel")
    protected ExtendedWebElement titleLabel;

    @ExtendedFindBy(accessibilityId = "submitButtonCellIdentifier")
    private ExtendedWebElement saveButton;

    @ExtendedFindBy(accessibilityId = "genderFormButtonCellIdentifier")
    private ExtendedWebElement genderFormButtonCellIdentifier;

    private ExtendedWebElement kidsOnToggleButton = typeCellLabelContains.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.TOGGLE_ON.getText()));

    private String genderWoman = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_WOMAN.getText());
    private String genderMan = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_MAN.getText());
    private String genderNonBinary = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_NON_BINARY.getText());
    private String genderPreferNotToSay = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_PREFER_TO_NOT_SAY.getText());
    private String genderPlaceholder = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_PLACEHOLDER.getText());
    private static final String BIRTHDATE_TEXT_FIELD = "birthdateTextFieldIdentifier";

    //Functions
    public DisneyPlusAddProfileIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return addProfileAvatar.isPresent();
    }

    public ExtendedWebElement getAddProfileAvatar() {
        return addProfileAvatar;
    }

    public void enterProfileNameOnLocalizedKeyboard(String name) {
        enterText(name);
        keyPressTimes(getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
    }

    public void clickKidsOnToggleBtn() {kidsOnToggleButton.click();}

    public boolean isAddProfilePageOpened() {
        String addProfileHeader = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CREATE_PROFILE.getText());
        return titleLabel.getText().equalsIgnoreCase(addProfileHeader);
    }

    public boolean isAddProfileHeaderPresent(){
        String addProfileHeader = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CREATE_PROFILE.getText());
        return staticTextByLabel.format(addProfileHeader).isPresent();
    }

    public boolean isProfilePresent(String profileName) {
        ExtendedWebElement profileSelectionBtn = dynamicCellByLabel.format(
                getDictionary().formatPlaceholderString(
                        getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PROFILE.getText()), Map.of(USER_PROFILE, profileName)));
        return profileSelectionBtn.isPresent();
    }

    public boolean isProfileNamefieldPresent() {
        return profileNameTextFieldIdentifier.isPresent();
    }

    public boolean profileNameTextFieldIdentifierFocused() {
        return isFocused(profileNameTextFieldIdentifier);
    }

    public void updateUserName(String userName) {
        if(profileNameTextFieldIdentifier.isElementNotPresent(10)) {
            swipeInContainer(null, Direction.DOWN, 200);
            profileNameTextFieldIdentifier.click();
            profileNameTextFieldIdentifier.type(userName);
        }
    }
    public boolean kidsProfileToggleCellFocused() {
        return isFocused(kidsProfileToggleCell);
    }

    public boolean iskidsProfileToggleCellPresent() {
        return kidsProfileToggleCell.isPresent();
    }

    public String getkidsProfileToggleCellValue() {
        return kidsProfileToggleCell.getAttribute("value");
    }

    public void tapJuniorModeToggle() {
        juniorModeToggle.click();
    }

    public void tapSaveButton() {
        if (!saveButton.isPresent()) {
            swipeInContainer(null, IMobileUtils.Direction.UP, 1200);
        }
        saveButton.click();
    }

    public ExtendedWebElement getSaveButton() {
        return saveButton;
    }

    public void clickSkipBtn() {
        skipBtn.click();
    }

    public void enterProfileName(String name) {
        textEntryField.type(name);
    }

    public void createProfile(String name) {
        enterProfileName(name);
        saveBtn.click();
    }

    public void createProfileForNewUser(String profileName) {
        enterProfileName(profileName);
        chooseGender();
        tapSaveButton();
    }

    public void createProfile(String profileName, DateHelper.Month month, String day, String year) {
        enterProfileName(profileName);
        enterDOB(month, day, year);
        chooseGender();
        clickElementAtLocation(saveBtn, 50, 50);
        //Save button is not tappable
        //saveBtn.click();
    }

    //format: Month, day, year
    public void enterDOB(DateHelper.Month month, String day, String year) {
        getDynamicTextEntryFieldByName(BIRTHDATE_TEXT_FIELD).click();
        setBirthDate(DateHelper.localizeMonth(month, getDictionary()), day, year);
        dismissPickerWheelKeyboard();
    }

    public boolean isDateOfBirthFieldPresent(){
        return getDynamicTextEntryFieldByName(BIRTHDATE_TEXT_FIELD).isPresent();
    }

    public void chooseGender() {
        clickGenderDropDown();
        dynamicBtnFindByLabel.format(genderPreferNotToSay).click();
    }

    public boolean isJuniorModeTextPresent() {
        return staticTextByLabel.format("Junior Mode").isPresent();
    }

    public void clickGenderDropDown() {
        dynamicBtnFindByLabel.format(genderPlaceholder).click();
    }

    public void clickSave() {
        dynamicBtnFindByNameContains.format("Save").click();
    }

    /**
     * checks if gender field is enabled or not
     * @return true/false, return the enabled attribute value
     */
    public boolean isGenderFieldEnabled() {
        return dynamicBtnFindByLabel.format(genderPlaceholder).getAttribute("enabled").equals("true");
    }

    public boolean isGenderFieldPresent() {
        return dynamicBtnFindByLabel.format(genderPlaceholder).isPresent();
    }

    public void tapCancelButton() {
        cancelButton.click();
    }

    public boolean isCancelButtonPresent(){
        return cancelButton.isPresent();
    }
    /**
     * checks if Kid Profile sub copy present or not
     * @return true/false
     */
    public boolean isKidProfileSubCopyPresent() {
        String kidProfileSubCopy = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.KIDS_PROFILE_SUBCOPY.getText());
        return textViewByLabel.format(kidProfileSubCopy).isPresent();
    }

    /**
     * checks inline error for profile field if profile field is empty
     * @return true/false
     */
    public boolean isInlineErrorForProfileFieldPresent() {
        String inlineErrorForProfileField = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.FORM_ERROR_FOR_NAME.getText());
        return staticTextByLabel.format(inlineErrorForProfileField).isPresent();
    }

    /**
     * checks inline error for DOB field if DOB field is empty
     * @return true/false
     */
    public boolean isInlineErrorForDOBFieldPresent() {
        String inlineErrorForDOBField = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.FORM_ERROR_FOR_DOB.getText());
        return staticTextByLabel.format(inlineErrorForDOBField).isPresent();
    }

    /**
     * checks inline error for Gender field if Gender field is empty
     * @return true/false
     */
    public boolean isInlineErrorForGenderFieldPresent() {
        String inlineErrorForGenderField = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.FORM_ERROR_FOR_GENDER.getText());
        return staticTextByLabel.format(inlineErrorForGenderField).isPresent();
    }

    public boolean verifyHeadlineHeaderText() {
        String accessFullCatalogText = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.ADD_PROFILE_UPDATE_MATURITY_RATING_TITLE.getText());
        return headlineHeader.getText().equalsIgnoreCase(accessFullCatalogText);
    }

    public boolean isUpdateMaturityRatingActionDisplayed() {
        String maturityRatingInfo = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.MATURITY_RATING_SUBTITLE.getText());
        return staticTextByLabel.format(getDictionary().formatPlaceholderString(
                maturityRatingInfo, Map.of("highest_rating_value_image", "TV-MA"))).isPresent();
    }

    public boolean isMaturityRatingNotNowInfoDisplayed() {
        String maturityRatingInfo = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.MATURITY_RATING_NOT_NOW_INFO.getText());
        return staticTextByLabel.format(getDictionary().formatPlaceholderString(
                maturityRatingInfo, Map.of("current_rating_value_text", "TV-14"))).isPresent();
    }
}
