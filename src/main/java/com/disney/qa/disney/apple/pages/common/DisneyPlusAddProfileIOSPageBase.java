package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.utils.mobile.IMobileUtils;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.slf4j.*;

import java.lang.invoke.*;
import java.util.Map;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.ERROR_DUPLICATE_PROFILE_NAME;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusAddProfileIOSPageBase extends DisneyPlusApplePageBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @ExtendedFindBy(accessibilityId = "kidsProfileToggleCell")
    protected ExtendedWebElement kidsProfileToggleCell;

    @ExtendedFindBy(accessibilityId = "toggleView")
    protected ExtendedWebElement juniorModeToggle;

    @ExtendedFindBy(accessibilityId = "profileNameTextFieldIdentifier")
    protected ExtendedWebElement profileNameTextFieldIdentifier;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[$type='XCUIElementTypeImage' AND name='badgeIcon'$]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeImage[1]")
    protected ExtendedWebElement addProfileAvatar;

    @ExtendedFindBy(accessibilityId = "Cancel")
    private ExtendedWebElement cancelButton;

    @ExtendedFindBy(accessibilityId = "Skip")
    protected ExtendedWebElement skipBtn;

    @ExtendedFindBy(accessibilityId = "titleLabel")
    protected ExtendedWebElement titleLabel;

    @ExtendedFindBy(accessibilityId = "submitButtonCellIdentifier")
    private ExtendedWebElement saveButton;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`name == \"Choose content rating\"`]")
    private ExtendedWebElement chooseContentRating;

    @ExtendedFindBy(iosPredicate = "name == \"birthdateTextFieldIdentifier\" AND value == \"MM/DD/YYYY\"\n")
    protected ExtendedWebElement birthDateValue;

    private ExtendedWebElement kidsOnToggleButton = typeCellLabelContains.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.TOGGLE_ON.getText()));
    private String genderPreferNotToSay = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_PREFER_TO_NOT_SAY.getText());
    private String genderPlaceholder = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_PLACEHOLDER.getText());
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
        String addProfileHeader = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CREATE_PROFILE.getText());
        return titleLabel.getText().equalsIgnoreCase(addProfileHeader);
    }

    public boolean isAddProfileHeaderPresent(){
        String addProfileHeader = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CREATE_PROFILE.getText());
        return staticTextByLabel.format(addProfileHeader).isPresent();
    }

    public boolean isProfilePresent(String profileName) {
        ExtendedWebElement profileSelectionBtn = dynamicCellByLabel.format(
                getLocalizationUtils().formatPlaceholderString(
                        getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PROFILE.getText()), Map.of(USER_PROFILE, profileName)));
        return profileSelectionBtn.isPresent();
    }

    public boolean isProfileNameFieldPresent() {
        return profileNameTextFieldIdentifier.isPresent();
    }

    public void updateUserName(String userName) {
        if(profileNameTextFieldIdentifier.isElementNotPresent(10)) {
            swipeInContainer(null, Direction.DOWN, 200);
            profileNameTextFieldIdentifier.click();
            profileNameTextFieldIdentifier.type(userName);
        }
    }

    public boolean isKidsProfileToggleCellPresent() {
        return kidsProfileToggleCell.isPresent();
    }

    public String getKidsProfileToggleCellValue() {
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

    public void createProfile(String profileName, DateHelper.Month month, String day, String year) {
        enterProfileName(profileName);
        enterDOB(month, day, year);
        chooseGender();
        clickElementAtLocation(saveBtn, 50, 50);
    }

    //format: Month, day, year
    public void enterDOB(DateHelper.Month month, String day, String year) {
        getDynamicTextEntryFieldByName(BIRTHDATE_TEXT_FIELD).click();
        setBirthDate(DateHelper.localizeMonth(month, getLocalizationUtils()), day, year);
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
        String kidProfileSubCopy = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.KIDS_PROFILE_SUBCOPY.getText());
        return textViewByLabel.format(kidProfileSubCopy).isPresent();
    }

    /**
     * checks inline error for profile field if profile field is empty
     * @return true/false
     */
    public boolean isInlineErrorForProfileFieldPresent() {
        String inlineErrorForProfileField = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.FORM_ERROR_FOR_NAME.getText());
        return staticTextByLabel.format(inlineErrorForProfileField).isPresent();
    }

    /**
     * checks inline error for DOB field if DOB field is empty
     * @return true/false
     */
    public boolean isInlineErrorForDOBFieldPresent() {
        String inlineErrorForDOBField = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.FORM_ERROR_FOR_DOB.getText());
        return staticTextByLabel.format(inlineErrorForDOBField).isPresent();
    }

    /**
     * checks inline error for Gender field if Gender field is empty
     * @return true/false
     */
    public boolean isInlineErrorForGenderFieldPresent() {
        String inlineErrorForGenderField = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.FORM_ERROR_FOR_GENDER.getText());
        return staticTextByLabel.format(inlineErrorForGenderField).isPresent();
    }

    public boolean verifyHeadlineHeaderText() {
        String accessFullCatalogText = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.ADD_PROFILE_UPDATE_MATURITY_RATING_TITLE.getText());
        return headlineHeader.getText().equalsIgnoreCase(accessFullCatalogText);
    }

    public boolean isUpdateMaturityRatingActionDisplayed() {
        String maturityRatingInfo = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.MATURITY_RATING_SUBTITLE.getText());
        return staticTextByLabel.format(getLocalizationUtils().formatPlaceholderString(
                maturityRatingInfo, Map.of("highest_rating_value_image", "TV-MA"))).isPresent();
    }

    public boolean isMaturityRatingNotNowInfoDisplayed() {
        String maturityRatingInfo = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.MATURITY_RATING_NOT_NOW_INFO.getText());
        return staticTextByLabel.format(getLocalizationUtils().formatPlaceholderString(
                maturityRatingInfo, Map.of("current_rating_value_text", "TV-14"))).isPresent();
    }

    public boolean isDuplicateProfileNameErrorPresent() {
        String message = getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                ERROR_DUPLICATE_PROFILE_NAME.getText());
        LOGGER.info("Error Message by dictionary: {}", message);
        return staticTextLabelName.format(message).isPresent();
    }

    public ExtendedWebElement getChooseContentRating() {
        return chooseContentRating;
    }

    public String getBirthdateTextField(){
        return BIRTHDATE_TEXT_FIELD;
    }

    public ExtendedWebElement getValueFromDOB() {
        return birthDateValue;
    }

    public boolean isContentRatingDropdownEnabled(String value) {
          return getTypeButtonContainsLabel(value).getAttribute(IOSUtils.Attributes.ENABLED.getAttribute())
                  .equalsIgnoreCase(Boolean.TRUE.toString());
    }
}
