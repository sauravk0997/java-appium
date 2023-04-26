package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.MobileUtilsExtended;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusAddProfileIOSPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(accessibilityId = "kidsProfileToggleCell")
    protected ExtendedWebElement kidsProfileToggleCell;

    @ExtendedFindBy(accessibilityId = "toggleView")
    protected ExtendedWebElement juniorModeToggle;

    @ExtendedFindBy(accessibilityId = "profileNameTextFieldIdentifier")
    protected ExtendedWebElement profileNameTextFieldIdentifier;

    @FindBy(xpath = "//XCUIElementTypeImage[@name='badgeIcon']/preceding-sibling::*")
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

    @ExtendedFindBy(accessibilityId = "saveProfileButton")
    private ExtendedWebElement saveButton;

    @ExtendedFindBy(accessibilityId = "genderFormButtonCellIdentifier")
    private ExtendedWebElement genderFormButtonCellIdentifier;

    private ExtendedWebElement kidsOnToggleButton = typeCellLabelContains.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.TOGGLE_ON.getText()));
    private ExtendedWebElement addProfileHeader = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CREATE_PROFILE.getText()));

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

    public ExtendedWebElement getAddProfileAvatar() {
        return addProfileAvatar;
    }

    public void enterProfileNameOnLocalizedKeyboard(String name) {
        enterText(name);
        keyPressTimes(getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
    }

    public void clickKidsOnToggleBtn() {kidsOnToggleButton.click();}

    public boolean isAddProfilePageOpened() {
        return addProfileHeader.isPresent();
    }

    public boolean isProfilePresent(String profileName) {
        ExtendedWebElement profileSelectionBtn = dynamicCellByLabel.format(
                getDictionary().replaceValuePlaceholders(
                        getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PROFILE.getText()), profileName));
        return profileSelectionBtn.isPresent();
    }

    public boolean profileNameTextFieldIdentifierFocused() {
        return isFocused(profileNameTextFieldIdentifier);
    }

    public boolean kidsProfileToggleCellFocused() {
        return isFocused(kidsProfileToggleCell);
    }

    public void tapJuniorModeToggle() {
        juniorModeToggle.click();
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
        new MobileUtilsExtended().clickElementAtLocation(saveBtn, 50, 50);
        //Save button is not tappable
        //saveBtn.click();
    }

    //format: Month, day, year
    public void enterDOB(DateHelper.Month month, String day, String year) {
        getDynamicTextEntryFieldByName(BIRTHDATE_TEXT_FIELD).click();
        new IOSUtils().setBirthDate(DateHelper.localizeMonth(month, getDictionary()), day, year);
        dismissPickerWheelKeyboard();
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

    public void tapCancelButton() {
        cancelButton.click();
    }
}
