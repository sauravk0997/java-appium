package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.client.responses.profile.Profile;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
    protected static final String EMPTY_PROFILE_NAME_ERROR = "Enter profile name";
    private static final String R21 = "R21";

    //TODO Refactor english hardcoded values to reference dictionary keys
    //LOCATORS

    @ExtendedFindBy(accessibilityId = "editProfile")
    protected ExtendedWebElement editProfileView;

    @ExtendedFindBy(accessibilityId = "badgeIcon")
    protected ExtendedWebElement badgeIcon;

    @ExtendedFindBy(accessibilityId = "kidProofExitToggleCell")
    protected ExtendedWebElement kidProofExitToggleCell;

    @ExtendedFindBy(accessibilityId = "kidsProfileToggleCell")
    protected ExtendedWebElement juniorModeToggleCell;

    @ExtendedFindBy(accessibilityId = "maturityRatingCell")
    protected ExtendedWebElement maturityRatingCell;

    private final ExtendedWebElement deleteProfileButton = getDynamicAccessibilityId(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, BTN_DELETE_PROFILE.getText()));

    private final String editProfileTitle =
            getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                    DictionaryKeys.EDIT_PROFILE_TITLE.getText());

    @ExtendedFindBy(accessibilityId = "autoplayToggleCell")
    private ExtendedWebElement autoplayToggleCell;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label == \"Add Profile\"`]")
    private ExtendedWebElement addProfileBtn;

    @FindBy(xpath = "//XCUIElementTypeCell[@name=\"kidsProfileToggleCell\"]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2]")
    private ExtendedWebElement kidsProfileToggleSwitch;

    @FindBy(xpath = "//XCUIElementTypeCell[@name=\"kidProofExitToggleCell\"]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther")
    private ExtendedWebElement kidProofExitLabel;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`name == \"kidsProfileToggleCell\"`]/**/XCUIElementTypeOther[`name == \"toggleView\"`]")
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

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`name == \"unlockedProfileCell\"`]/**/XCUIElementTypeImage[1]")
    private ExtendedWebElement editProfileImage;


    private final ExtendedWebElement pinSettingsCell = staticTextByLabelOrLabel.format(getLocalizationUtils()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                            DictionaryKeys.PROFILE_SETTINGS_ENTRY_PIN_LABEL.getText()),
            DictionaryKeys.PROFILE_SETTINGS_ENTRY_PIN_LABEL.getText());

    private final ExtendedWebElement contentRatingHeader = getStaticTextByLabel(
            getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.MATURITY_RATING_SETTINGS_LABEL.getText()));

    public ExtendedWebElement getDeleteProfileButton() {
        return deleteProfileButton;
    }

    public boolean isDeleteProfileButtonPresent() {
        swipeInContainer(null, Direction.UP, 2500);
        return deleteProfileButton.isPresent();
    }

    public ExtendedWebElement getDoneButton() {
        String button = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, BTN_PROFILE_SETTINGS_DONE.getText());
        return dynamicBtnFindByLabel.format(button);
    }

    private final String genderTitle = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SETTINGS_GENDER.getText());

    //FUNCTIONS

    public DisneyPlusEditProfileIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return getStaticTextByLabel(editProfileTitle).isPresent(SHORT_TIMEOUT);
    }

    public ExtendedWebElement getSharePlay() {
        return sharePlay;
    }

    public ExtendedWebElement getBadgeIcon() {
        return badgeIcon;
    }

    public ExtendedWebElement getSharePlayTooltip() {
        String toastText = getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.COVIEWING_ADS_TOOLTIP.getText());
        return getDynamicAccessibilityId(toastText);
    }

    public ExtendedWebElement getAddProfileBtn() {
        return addProfileBtn;
    }

    public boolean isEditProfilesTitlePresent() {
        return collectionHeadlineTitle.getText().equals(editProfileTitle);
    }

    public boolean isBackBtnPresent() {
        return getBackArrow().isElementPresent();
    }

    public boolean isEditModeProfileIconPresent(String username) {
        return dynamicCellByLabel.format(
                        getLocalizationUtils().formatPlaceholderString(
                                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                                        DictionaryKeys.EDIT_PROFILE_EDIT.getText()),
                                Map.of(USER_PROFILE, username)))
                .isElementPresent();
    }

    public boolean verifyProfileSettingsMaturityRating(String rating) {
        String contentRatingText = getLocalizationUtils().formatPlaceholderString(
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.MATURITY_RATING_DESCRIPTION_VALUE.getText()),
                Map.of(USER_RATING_KEY, rating));
        swipe(staticTextByLabel.format(contentRatingText), 1);
        return staticTextByLabel.format(contentRatingText).isPresent();

    }

    public void clickEditModeProfile(String profile) {
        ExtendedWebElement profileIcon = dynamicCellByLabel.format(
                getLocalizationUtils().formatPlaceholderString(
                        getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                                DictionaryKeys.EDIT_PROFILE_EDIT.getText()),
                        Map.of(USER_PROFILE, profile)));
        profileIcon.click();
    }

    public boolean isBirthdateHeaderDisplayed() {
        String birthdateHeader = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, SETTINGS_DATE_OF_BIRTH.getText());
        return staticTextByLabel.format(birthdateHeader).isPresent();
    }

    public boolean isBirthdateDisplayed(Profile profile) {
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
        sharePlayHyperLink.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.GROUPWATCH_SHAREPLAY_SETTINGS_SUBHEADER.getText())).click();
    }

    public ExtendedWebElement getLearnMoreLink() {
        String learnMoreText = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, JUNIOR_MODE_LEARN_MORE.getText());
        return customHyperlinkByLabel.format(learnMoreText);
    }

    public boolean isLearnMoreLinkPresent() {
        ExtendedWebElement learnMoreLink = getLearnMoreLink();
        swipe(learnMoreLink);
        return learnMoreLink.isPresent(THREE_SEC_TIMEOUT);
    }

    public void clickJuniorModeLearnMoreLink() {
        ExtendedWebElement learnMoreLink = getLearnMoreLink();
        swipe(learnMoreLink);
        learnMoreLink.click(THREE_SEC_TIMEOUT);
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

    public boolean isEmptyProfileNameErrorDisplayed() {
        return staticTextByLabel.format(EMPTY_PROFILE_NAME_ERROR).isPresent();
    }

    public boolean isServiceEnrollmentAccessFullCatalogPagePresent() {
        return serviceEnrollmentAccessFullCatalogPage.isElementPresent();
    }

    public boolean isServiceEnrollmentSetPINPresent() {
        return serviceEnrollmentSetPIN.isElementPresent();
    }

    public boolean isProfilePinActionDisplayed() {
        return staticTextByLabel.format(getLocalizationUtils().
                getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.SECURE_PROFILE_PIN_ACTION.getText())).isPresent();
    }

    public boolean isProfilePinDescriptionDisplayed() {
        return staticTextByLabel.format(getLocalizationUtils().
                getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.SECURE_PROFILE_PIN_DESCRIPTION.getText())).isPresent();
    }

    public boolean isProfilePinReminderDisplayed() {
        return staticTextByLabel.format(getLocalizationUtils().
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
        return staticTextByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.PROFILE_SETTINGS_GENERIC_TOAST.getText())).isPresent();
    }

    public void waitForUpdatedToastToDisappear() {
        ExtendedWebElement updatedToast = staticTextByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.PROFILE_SETTINGS_GENERIC_TOAST.getText()));
        fluentWait(getDriver(), SIXTY_SEC_TIMEOUT, THREE_SEC_TIMEOUT, "Download complete text is not present")
                .until(it -> updatedToast.isElementNotPresent(THREE_SEC_TIMEOUT));
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

    public String getKidProofExitToggleValue() {
        swipe(kidProofExitToggleCell);
        return kidProofExitToggleCell.getText();
    }

    public String getJuniorModeToggleValue() {
        return juniorModeToggleCell.getText();
    }

    public ExtendedWebElement getMaturityRatingCell() {
        return maturityRatingCell;
    }

    public void toggleKidsProofExit() {
        kidProofExitToggleCell.getElement().findElement(By.name("toggleView")).click();
    }

    public void toggleJuniorMode() {
        LOGGER.info("tapping on junior mode toggle");
        WebElement juniorModeToggle = juniorModeToggleCell.getElement().findElement(By.name("toggleView"));
        juniorModeToggle.click();
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

    public ExtendedWebElement getLiveAndUnratedToggleCell() {
        String liveUnratedHeader = getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        PROFILE_SETTINGS_LIVE_UNRATED_HEADER.getText());
        return getTypeCellLabelContains(liveUnratedHeader);
    }

    public void tapLiveAndUnratedToggle() {
        getLiveAndUnratedToggleCell().getElement().findElement(By.name("toggleView")).click();
    }

    public ExtendedWebElement getPinSettingsCell() {
        return pinSettingsCell;
    }

    public ExtendedWebElement getKidProofExitLabel() {
        swipeInContainer(null, Direction.UP, 2500);
        return kidProofExitLabel;
    }

    public ExtendedWebElement getKidProofDescription() {
        swipeInContainer(null, Direction.UP, 2500);
        return staticTextByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                PROFILE_SETTINGS_KIDPROOF_EXIT_DESCRIPTION.getText()));
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

    public boolean isGenderValuePresent(Profile profile) {
        String genderValue = getDynamicCellByName("Gender Selection").getText().split(",")[1].toLowerCase().replace(" ", "");
        return genderValue.equalsIgnoreCase(profile.getAttributes().getGender().toLowerCase());
    }

    public boolean isEditTitleDisplayed() {
        return staticTextByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.EDIT_PROFILE_TITLE_2.getText())).isPresent();
    }

    public ExtendedWebElement getPrimaryProfileExplainer() {
        return staticTextByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.PRIMARY_PROFILE_EXPLAINER.getText()));
    }

    public boolean isProfileIconDisplayed(String avatarID) {
        return dynamicCellByName.format(avatarID).isPresent();
    }

    public boolean isPersonalInformationSectionDisplayed() {
        return staticTextByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                PROFILE_SETTINGS_PERSONAL_INFORMATION_HEADER.getText())).isPresent();
    }

    public boolean isPlayBackSettingsSectionDisplayed() {
        return staticTextByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                PLAYBACK_LANGUAGE_HEADER.getText())).isPresent() &&
                staticTextByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        CREATE_PROFILE_AUTOPLAY.getText())).isPresent() &&
                staticTextByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        AUTOPLAY_SUBCOPY.getText())).isPresent() &&
                staticTextByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        UI_LANGUAGE_SETTING.getText())).isPresent();

    }

    public boolean isFeatureSettingsSectionDisplayed() {
        ExtendedWebElement sharePlaySubheader = textViewByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                GROUPWATCH_SHAREPLAY_SETTINGS_SUBHEADER.getText()));
        swipePageTillElementPresent(sharePlaySubheader, 3, null, Direction.UP, 500);
        return staticTextByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                GROUPWATCH_FEATURE_SETTINGS.getText())).isPresent() &&
                staticTextByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        GROUPWATCH_SHAREPLAY_SETTINGS_HEADER.getText())).isPresent() &&
                sharePlaySubheader.isPresent();
    }

    public ExtendedWebElement getJuniorModeDescription() {
        return textViewByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                KIDS_PROFILE_SUBCOPY.getText()));
    }

    public boolean isParentalControlHeadingDisplayed() {
        swipeInContainer(null, Direction.UP, 2500);
        return staticTextByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                PROFILE_SETTINGS_HEADER.getText())).isPresent();
    }

    public boolean isParentalControlSectionDisplayed() {
        ExtendedWebElement kidsProofExit = getKidProofDescription();
        swipePageTillElementPresent(kidsProofExit, 3, null, Direction.UP, 500);
        return isParentalControlHeadingDisplayed() &&
                getJuniorModeDescription().isPresent() &&
                isLearnMoreLinkPresent() &&
                getKidProofExitLabel().isPresent() &&
                kidsProofExit.isPresent();
    }

    public ExtendedWebElement getProfilePinHeader() {
        return staticTextByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                PROFILE_SETTINGS_ENTRY_PIN_LABEL.getText()));
    }

    public ExtendedWebElement getProfilePinDescription() {
        return staticTextByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                PROFILE_SETTINGS_ENTRY_PIN_DESCRIPTION.getText()));
    }

    public ExtendedWebElement getMaturityRatingLabel() {
        return staticTextByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                PROFILE_SETTINGS_MATURITY_RATING_LABEL.getText()));
    }

    public boolean isMaturityRatingSectionDisplayed(String rating) {
        return getMaturityRatingLabel().isPresent() &&
                verifyProfileSettingsMaturityRating(rating) &&
                getProfilePinHeader().isPresent() &&
                getProfilePinDescription().isPresent();
    }

    public ExtendedWebElement getDeleteProfileCancelButton() {
        return getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CANCEL_BTN_NORMAL.getText()));
    }

    public ExtendedWebElement getDeleteProfileDeleteButton() {
        return getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_DELETE.getText()));
    }

    public ExtendedWebElement getDeleteProfileTitle(String profileName) {
        String deleteProfileTitle = getLocalizationUtils().formatPlaceholderString(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DELETE_PROFILE_TITLE.getText()), Map.of("user_profile", profileName));
        return getStaticTextByLabel(deleteProfileTitle);
    }

    public ExtendedWebElement getDeleteProfileCopy() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DELETE_PROFILE_COPY.getText()));
    }

    public ExtendedWebElement getEditProfilePinSettingLabel() {
        return getStaticTextByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.PROFILE_SETTINGS_ENTRY_PIN_LABEL.getText()));
    }

    public boolean isProfileNameCharacterLimitErrorPresent() {
        return labelError.isPresent();
    }

    public boolean isR21MaturitySliderPresent() {
        String ratingDescriptionText = getElementTypeCellByLabel(R21).getText();
        String dictRatingDescription = getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.RATING_MDA_R21_DESCRIPTION.getText());
        return ratingDescriptionText.toLowerCase().contains(dictRatingDescription.toLowerCase());
    }

    public boolean isEditProfileImageDisplayed() {
        return editProfileImage.isPresent();
    }

    public boolean isDateFieldNotRequiredLabelPresent() {
        String fieldNotRequired = getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, FIELD_NOT_REQUIRED.getText());
        return staticTextByLabel.format(fieldNotRequired).isPresent();
    }

    public ExtendedWebElement getProfileSettingLiveUnratedHeader() {
        String unratedHeader = getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        PROFILE_SETTINGS_LIVE_UNRATED_HEADER.getText());
        LOGGER.info("Expected live and unrated header: {}", unratedHeader);
        return getStaticTextByLabel(unratedHeader);
    }

    public ExtendedWebElement getProfileSettingLiveUnratedDesc() {
        return getStaticTextViewValueContains(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        PROFILE_SETTINGS_LIVE_UNRATED_DESCRIPTION.getText()));
    }

    public ExtendedWebElement getProfileSettingLiveUnratedHelpLink() {
        return getDynamicAccessibilityId(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        PROFILE_SETTINGS_LIVE_UNRATED_HELP_LINK.getText()));
    }

    public ExtendedWebElement getProfileSettingLiveUnratedTooltip() {
        return getDynamicAccessibilityId(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        PROFILE_SETTINGS_LIVE_UNRATED_TOOL_TIP.getText()));
    }

    public String getLiveAndUnratedToggleState() {
        return getLiveAndUnratedToggleCell().getText();
    }

}
