package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.client.responses.profile.DisneyProfile;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusEditProfileIOSPageBase extends DisneyPlusAddProfileIOSPageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    protected static final String USER_RATING_KEY = "profile_rating_restriction";

    //TODO Refactor english hardcoded values to reference dictionary keys
    //LOCATORS

    @ExtendedFindBy(accessibilityId = "editProfile")
    protected ExtendedWebElement editProfileView;

    @ExtendedFindBy(accessibilityId = "badgeIcon")
    protected ExtendedWebElement badgeIcon;

    private ExtendedWebElement deleteProfileButton = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, BTN_DELETE_PROFILE.getText()));
    private final ExtendedWebElement editProfileTitle = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.EDIT_PROFILE_TITLE.getText()));

    @ExtendedFindBy(accessibilityId = "autoplayToggleCell")
    private ExtendedWebElement autoplayToggleCell;

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
    protected ExtendedWebElement sharePlayHyperLink;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"SharePlay\"`]")
    private ExtendedWebElement sharePlay;

    @ExtendedFindBy(accessibilityId = "saveProfileButton")
    private ExtendedWebElement doneBtn;

    @ExtendedFindBy(accessibilityId = "serviceEnrollmentAccessFullCatalog")
    private ExtendedWebElement serviceEnrollmentAccessFullCatalogPage;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"serviceEnrollmentSetPIN\"`]")
    private ExtendedWebElement serviceEnrollmentSetPIN;

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

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Content Rating\"`]")
    private ExtendedWebElement contentRatingTitle;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`name == \"subtitleLabel\"`]")
    private ExtendedWebElement subtitleLabel;

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

    public ExtendedWebElement getDoneButton() {
        String button = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, BTN_PROFILE_SETTINGS_DONE.getText());
        return dynamicBtnFindByLabel.format(button);
    }

    private String genderTitle = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SETTINGS_GENDER.getText());

    //FUNCTIONS

    public DisneyPlusEditProfileIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return editProfileTitle.isPresent(SHORT_TIMEOUT);
    }

    public ExtendedWebElement getSharePlay() {
        return sharePlay;
    }

    public ExtendedWebElement getBadgeIcon() {
        return badgeIcon;
    }

    public ExtendedWebElement getSharePlayTooltip() {
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

    public boolean verifyProfileSettingsMaturityRating(String rating) {
        String contentRatingText = getDictionary().formatPlaceholderString(
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.MATURITY_RATING_DESCRIPTION_VALUE.getText()),
                Map.of(USER_RATING_KEY, rating));
        swipe(staticTextByLabel.format(contentRatingText), 1);
        return staticTextByLabel.format(contentRatingText).isPresent();

    }

    public void clickEditModeProfile(String profile) {
        ExtendedWebElement profileIcon = dynamicCellByLabel.format(
                getDictionary().formatPlaceholderString(
                        getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                                DictionaryKeys.EDIT_PROFILE_EDIT.getText()),
                        Map.of(USER_PROFILE, profile)));
        profileIcon.click();
    }

    public boolean isBirthdateHeaderDisplayed() {
        String birthdateHeader = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, SETTINGS_DATE_OF_BIRTH.getText());
        return staticTextByLabel.format(birthdateHeader).isPresent();
    }

    public boolean isBirthdateDisplayed(DisneyProfile profile) {
        String dob = profile.getAttributes().getDateOfBirth();
        String monthName = getMonthName(dob.split("-")[1]);
        String date = dob.split("-")[2].startsWith("0") ? dob.split("-")[2].replace("0", "") : dob.split("-")[2];
        String displayedDOB = monthName + " " + date + "," + " " + dob.split("-")[0];
        return staticTextByLabel.format(displayedDOB).isPresent();
    }

    public boolean isProfileTitlePresent(String username) {
        return getDynamicAccessibilityId(username).isElementPresent();
    }

    public String getMonthName(String num) {
        String monthName = "";
        for (DateHelper.Month e : DateHelper.Month.values()) {
            if (e.getNum().equals(num)) {
                monthName = e.getText();
                break;
            }
        }
        return monthName;
    }

    public void selectInfoHyperlink() {
        sharePlayHyperLink.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.GROUPWATCH_SHAREPLAY_SETTINGS_SUBHEADER.getText())).click();
    }

    public ExtendedWebElement getLearnMoreLink() {
        String learnMoreText = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, JUNIOR_MODE_LEARN_MORE.getText());
        return customHyperlinkByLabel.format(learnMoreText);
    }

    public boolean isLearnMoreLinkPresent() {
        ExtendedWebElement learnMoreLink = getLearnMoreLink();
        swipe(learnMoreLink);
        return learnMoreLink.isPresent(SHORT_TIMEOUT);
    }

    public void clickJuniorModeLearnMoreLink() {
        ExtendedWebElement learnMoreLink = getLearnMoreLink();
        swipe(learnMoreLink);
        learnMoreLink.click(SHORT_TIMEOUT);
    }

    public DisneyPlusMoreMenuIOSPageBase clickBackBtn() {
        getBackArrow().click();
        return initPage(DisneyPlusMoreMenuIOSPageBase.class);
    }

    public void clickDoneBtn() {
        clickElementAtLocation(doneBtn, 50, 50);
    }

    public boolean isEditTextFieldPresent() {
        return textEntryField.isElementPresent();
    }


    public boolean isServiceEnrollmentAccessFullCatalogPagePresent() {
        return serviceEnrollmentAccessFullCatalogPage.isElementPresent();
    }

    public boolean isServiceEnrollmentSetPINPresent() {
        return serviceEnrollmentSetPIN.isElementPresent();
    }

    public boolean isProfilePinActionDisplayed() {
        return staticTextByLabel.format(getDictionary().
                getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.SECURE_PROFILE_PIN_ACTION.getText())).isPresent();
    }

    public boolean isProfilePinDescriptionDisplayed() {
        return staticTextByLabel.format(getDictionary().
                getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.SECURE_PROFILE_PIN_DESCRIPTION.getText())).isPresent();
    }

    public boolean isProfilePinReminderDisplayed() {
        return staticTextByLabel.format(getDictionary().
                getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.SECURE_PROFILE_PIN_REMINDER.getText())).isPresent();
    }

    /**
     * This method toggles the 'autoplay' switch to the requested state for the
     * given profile name and brings back the UI to the 'Edit profile' screen
     * where the bottom tab bar is visible
     **/

    public void toggleAutoplay(String profileName, String requestedState) {
        isOpened();
        clickEditModeProfile(profileName);
        toggleAutoplayButton(requestedState);
        pause(4);
        clickDoneBtn();
        if (!initPage(DisneyPlusHomeIOSPageBase.class).isOpened()) {
            clickBackBtn();
        }
    }

    public boolean isUpdatedToastPresent() {
        return staticTextByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.PROFILE_SETTINGS_GENERIC_TOAST.getText())).isPresent();
    }

    public void toggleAutoplayButton(String newState) {
        String currentState = autoplayToggleCell.getText();
        LOGGER.info("Current state of autoplay: {}, requested state: {}", currentState, newState);
        if (!currentState.equalsIgnoreCase(newState)) {
            autoplayToggleCell.getElement().findElement(By.name("toggleView")).click();
        }
    }

    public String getAutoplayState() {
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

    /**
     * click gender button to select gender value
     */
    public void clickGenderButton() {
        staticTextByLabel.format(genderTitle).click();
    }

    public boolean isGenderButtonPresent() {
        return staticTextByLabel.format(genderTitle).isElementPresent();
    }

    public boolean isGenderValuePresent(DisneyProfile profile) {
        String genderValue = getDynamicCellByName("Gender Selection").getText().split(",")[1].toLowerCase().replace(" ", "");
        return genderValue.equalsIgnoreCase(profile.getAttributes().getGender().toLowerCase());
    }

    public boolean isEditTitleDisplayed() {
        return staticTextByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.EDIT_PROFILE_TITLE_2.getText())).isPresent();
    }

    public boolean isProfileIconDisplayed(String avatarID) {
        return dynamicCellByName.format(avatarID).isPresent();
    }
    public boolean isPersonalInformationSectionDisplayed() {
        return staticTextByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                PROFILE_SETTINGS_PERSONAL_INFORMATION_HEADER.getText())).isPresent();
    }

    public boolean isPlayBackSettingsSectionDisplayed() {
        return staticTextByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                PLAYBACK_LANGUAGE_HEADER.getText())).isPresent() &&
                staticTextByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        CREATE_PROFILE_AUTOPLAY.getText())).isPresent() &&
                staticTextByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        AUTOPLAY_SUBCOPY.getText())).isPresent() &&
                staticTextByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        UI_LANGUAGE_SETTING.getText())).isPresent();

    }

    public boolean isFeatureSettingsSectionDisplayed() {
        ExtendedWebElement sharePlaySubheader = textViewByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                GROUPWATCH_SHAREPLAY_SETTINGS_SUBHEADER.getText()));
        swipePageTillElementPresent(sharePlaySubheader, 3, null, Direction.UP, 500);
        return staticTextByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                GROUPWATCH_FEATURE_SETTINGS.getText())).isPresent() &&
                staticTextByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        GROUPWATCH_SHAREPLAY_SETTINGS_HEADER.getText())).isPresent() &&
                sharePlaySubheader.isPresent();
    }

    public boolean isParentalControlSectionDisplayed() {
        ExtendedWebElement kidsProofExit = staticTextByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                PROFILE_SETTINGS_KIDPROOF_EXIT_DESCRIPTION.getText()));
        swipePageTillElementPresent(kidsProofExit, 3, null, Direction.UP, 500);
        return staticTextByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                PROFILE_SETTINGS_HEADER.getText())).isPresent() &&
                textViewByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        KIDS_PROFILE_SUBCOPY.getText())).isPresent() &&
                isLearnMoreLinkPresent() &&
                staticTextByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        PROFILE_SETTINGS_KIDPROOF_EXIT_LABEL.getText())).isPresent() &&
                kidsProofExit.isPresent();
    }

    public boolean isMaturityRatingSectionDisplayed() {
        ExtendedWebElement profilePinLabel = staticTextByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                PROFILE_SETTINGS_ENTRY_PIN_DESCRIPTION.getText()));
        return staticTextByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                PROFILE_SETTINGS_MATURITY_RATING_LABEL.getText())).isPresent() &&
                verifyProfileSettingsMaturityRating("TV-MA")&&
                staticTextByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        PROFILE_SETTINGS_ENTRY_PIN_LABEL.getText())).isPresent() &&
                profilePinLabel.isPresent();
    }
}
