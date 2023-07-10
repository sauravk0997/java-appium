package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.MobileUtilsExtended;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.Map;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusEditProfileIOSPageBase extends DisneyPlusAddProfileIOSPageBase {

    //TODO Refactor english hardcoded values to reference dictionary keys
    //LOCATORS

    @ExtendedFindBy(accessibilityId = "editProfile")
    protected ExtendedWebElement editProfileView;

    private ExtendedWebElement deleteProfileButton = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, BTN_DELETE_PROFILE.getText()));
    private final ExtendedWebElement editProfileTitle = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.EDIT_PROFILE_TITLE.getText()));

    @ExtendedFindBy(accessibilityId = "autoplayToggleCell")
    private ExtendedWebElement autoplayToggleCell;

    @ExtendedFindBy(accessibilityId = "groupWatchTooggleCell")
    private ExtendedWebElement groupWatchTooggleCell;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label == \"Add Profile\"`]")
    private ExtendedWebElement addProfileBtn;

    @FindBy(xpath = "//XCUIElementTypeCell[@name=\"kidsProfileToggleCell\"]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2]")
    private ExtendedWebElement kidsProfileToggleSwitch;

    @FindBy(xpath = "//XCUIElementTypeCell[@name=\"kidProofExitToggleCell\"]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther")
    private ExtendedWebElement kidProofExitLabel;

    @FindBy(xpath = "//XCUIElementTypeCell[@name=\"kidProofExitToggleCell\"]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2]")
    private ExtendedWebElement kidProofExitToggleSwitch;

    //Visibility set to false
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Add Profile\"`]")
    private ExtendedWebElement addProfileBtnTitle;

    @ExtendedFindBy(accessibilityId = "Skip")
    private ExtendedWebElement skipBtn;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextField[`value == \"Profile Name\"`]")
    private ExtendedWebElement profileNameTextField;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextView[`label == \"%s\"`]/XCUIElementTypeLink")
    protected ExtendedWebElement groupWatchHyperLink;

    @ExtendedFindBy(accessibilityId = "saveProfileButton")
    private ExtendedWebElement doneBtn;

    @ExtendedFindBy(accessibilityId = "serviceEnrollmentAccessFullCatalog")
    private ExtendedWebElement serviceEnrollmentAccessFullCatalogPage;

    @ExtendedFindBy(accessibilityId = "backgroundVideoToggleCell")
    private ExtendedWebElement backgroundVideoToggleCell;

    @ExtendedFindBy(accessibilityId = "appLanguageCell")
    private ExtendedWebElement appLanguageCell;

    @ExtendedFindBy(accessibilityId = "parentalControlsSelectorCell")
    private ExtendedWebElement parentalControlsSelectorCell;

    @ExtendedFindBy(accessibilityId = "deleteButton")
    private ExtendedWebElement deleteButton;

    @ExtendedFindBy(accessibilityId = "alertAction:destructiveButton")
    private ExtendedWebElement confirmProfileDeleteButton;

    private ExtendedWebElement pinSettingsCell = xpathNameOrName.format(getDictionary()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                            DictionaryKeys.PROFILE_SETTINGS_ENTRY_PIN_LABEL.getText()),
            DictionaryKeys.PROFILE_SETTINGS_ENTRY_PIN_LABEL.getText());

    private ExtendedWebElement contentRatingHeader = getDynamicXpath(String.format("//*[@name=\"%s\" or @name=\"%s\"]",
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, MATURITY_RATING_SETTINGS_LABEL.getText()),
            MATURITY_RATING_SETTINGS_LABEL.getText()));

    public ExtendedWebElement getDeleteProfileButton() {
        return deleteProfileButton;
    }

    //FUNCTIONS

    public DisneyPlusEditProfileIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return editProfileTitle.isPresent(SHORT_TIMEOUT);
    }

    public ExtendedWebElement getGroupWatchAndShareplay() {
        return xpathNameOrName.format(getDictionary()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                DictionaryKeys.GROUPWATCH_SHAREPLAY_SETTINGS_HEADER.getText()),
                DictionaryKeys.GROUPWATCH_SHAREPLAY_SETTINGS_HEADER.getText());
    }

    public ExtendedWebElement getGroupWatchAndShareplayTooltip() {
        String toastText = getDictionary()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.COVIEWING_ADS_TOOLTIP.getText());
        return getDynamicAccessibilityId(toastText);
    }


    public boolean isEditProfilesTitlePresent() {
        return collectionHeadlineTitle.isElementPresent();
    }

    public boolean isBackBtnPresent() {
        return getBackArrow().isElementPresent();
    }

    public boolean isEditModeProfileIconPresent(String username) {
        return dynamicCellByLabel.format(
                        getDictionary().formatPlaceholderString(
                                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                                        DictionaryKeys.EDIT_PROFILE_EDIT.getText()),
                                Map.of(USER_PROFILE, username)))
                .isElementPresent();
    }

    public void clickEditModeProfile(String profile) {
        ExtendedWebElement profileIcon = dynamicCellByLabel.format(
                getDictionary().formatPlaceholderString(
                        getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                                DictionaryKeys.EDIT_PROFILE_EDIT.getText()),
                        Map.of(USER_PROFILE, profile)));
        profileIcon.click();
    }

    public boolean isProfileTitlePresent(String username) {
        return getDynamicAccessibilityId(username).isElementPresent();
    }

    public void selectInfoHyperlink() {
        groupWatchHyperLink.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.GROUPWATCH_SHAREPLAY_SETTINGS_SUBHEADER.getText())).click();
    }

    public void clickJuniorModeLearnMoreLink() {
        String learnMoreText = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, JUNIOR_MODE_LEARN_MORE.getText());
        ExtendedWebElement learnMoreLink = customHyperlinkByLabel.format(learnMoreText);
        new IOSUtils().swipe(learnMoreLink);
        learnMoreLink.click(SHORT_TIMEOUT);
    }

    public DisneyPlusMoreMenuIOSPageBase clickBackBtn() {
        getBackArrow().click();
        return initPage(DisneyPlusMoreMenuIOSPageBase.class);
    }

    public void clickDoneBtn() {
        new MobileUtilsExtended().clickElementAtLocation(doneBtn, 50, 50);
    }

    public boolean isEditTextFieldPresent() {
        return textEntryField.isElementPresent();
    }


    public boolean isServiceEnrollmentAccessFullCatalogPagePresent() {
        return serviceEnrollmentAccessFullCatalogPage.isElementPresent();
    }

    /** This method toggles the 'autoplay' switch to the requested state for the
     * given profile name and brings back the UI to the 'Edit profile' screen
     * where the bottom tab bar is visible
     **/

    public void toggleAutoplay(String profileName,String requestedState) {
        isOpened();
        clickEditModeProfile(profileName);
        toggleAutoplayButton(requestedState);
        pause(4);
        clickDoneBtn();
        if (!initPage(DisneyPlusHomeIOSPageBase.class).isOpened()) {
            clickBackBtn();
        }
    }

    public boolean isUpdatedTextPresent() {
        return staticTextByLabel.format("Updated").isPresent();
    }

    public void toggleAutoplayButton(String newState) {
        String currentState = autoplayToggleCell.getText();
        LOGGER.info("Current state of autoplay: {}, requested state: {}", currentState, newState);
        if (!currentState.equalsIgnoreCase(newState)) {
            autoplayToggleCell.getElement().findElement(By.name("toggleView")).click();
        }
    }

    public void tapGroupWatchCell() {
        autoplayToggleCell.click();
    }

    public String getAutoplayState(){
        return autoplayToggleCell.getText();
    }

    public boolean isAutoplayToggleFocused() {
        return isFocused(autoplayToggleCell);
    }

    public boolean isBackgroundVideoFocused() {
        return isFocused(backgroundVideoToggleCell);
    }

    public boolean isAppLanguageCellFocused() {
        return isFocused(appLanguageCell);
    }

    public boolean isAppLanguageCellPresent() {
        return appLanguageCell.isElementPresent();
    }

    public boolean isParentalControlsCellFocused() {
        return isFocused(parentalControlsSelectorCell);
    }

    public void clickAppLanguage() {
        appLanguageCell.click();
    }

    public void clickParentalControls() {
        parentalControlsSelectorCell.click();
    }

    public void clickDeleteButton() {
        deleteButton.click();
    }

    public void clickConfirmDeleteButton() {
        confirmProfileDeleteButton.click();
    }

    public ExtendedWebElement getKidsProfileToggleSwitch() {
        return kidsProfileToggleSwitch;
    }

    public ExtendedWebElement getKidProofExitToggleSwitch() {
        return kidProofExitToggleSwitch;
    }

    public ExtendedWebElement getContentRatingHeader() {
        return contentRatingHeader;
    }

    public ExtendedWebElement getPinSettingsCell() {
        return pinSettingsCell;
    }

    public ExtendedWebElement getKidProofExitLabel() {
        return kidProofExitLabel;
    }
}