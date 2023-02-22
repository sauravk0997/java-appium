package com.disney.qa.disney.android.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.qaprosoft.carina.core.foundation.utils.android.AndroidService;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.awt.image.BufferedImage;
import java.util.*;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusMaturityPageBase extends DisneyPlusCommonPageBase{
    private static final String CONTENT_DESC = "content-desc";

    @FindBy(id = "setMaturityRatingRootView")
    private ExtendedWebElement maturityRatingRoot;

    @FindBy(id = "setMaturityRatingIntroContinueButton")
    private ExtendedWebElement continueButton;

    @FindBy(id = "setMaturityRatingNotNowButton")
    private ExtendedWebElement starNotNowBtn;

    @FindBy(id = "welcomeToStarTitle")
    private ExtendedWebElement starSetPinTitle;

    @FindBy(id = "maturityRatingTitle")
    private ExtendedWebElement starMaturityRatingTitle;

    @FindBy(id = "profileMaturitySwitch")
    private ExtendedWebElement maturityProfileSwitch;

    @FindBy(id = "setMaturityRatingBottomSheetContinueButton")
    private ExtendedWebElement starBottomContinueBtn;

    @FindBy(id = "maturityRatingConfirmationButton")
    private ExtendedWebElement maturityRatingConfirmationBtn;

    @FindBy(id = "addProfileButton")
    private ExtendedWebElement addProfileButton;

    @FindBy(id = "closeButton")
    private ExtendedWebElement addProfileCloseButton;

    @FindBy(id = "setMaturityRatingIntroTitle")
    private ExtendedWebElement migrationIntroTitle;

    @FindBy(id = "setMaturityRatingIntroDescription")
    private ExtendedWebElement migrationIntroDescription;

    @FindBy(id = "setToTextView")
    private ExtendedWebElement setToText;

    //Migration Full Catalog disclaimer items

    @FindBy(id = "forgotPwdPinTitle")
    private ExtendedWebElement forgotPwdTitle;

    @FindBy(id = "setMaturityRatingTextView")
    private ExtendedWebElement maturityRatingSubHeader;

    @FindBy(id = "setMaturityRatingHeader")
    private ExtendedWebElement starRatingsHeader;

    @FindBy(id = "setMaturityRatingBodyTextView")
    private ExtendedWebElement maturityRatingsBody;

    //Add Profile Items
    @FindBy(id = "addProfileMaturityRatingContainer")
    private ExtendedWebElement addProfileMaturityContainer;

    @FindBy(id = "addProfileMaturityRatingAvatar")
    private ExtendedWebElement addProfileMaturityAvatar;

    @FindBy(id = "addProfileMaturityRatingHeader")
    private ExtendedWebElement addProfileRatingsHeader;

    @FindBy(id = "addProfileMaturityRatingProfileName")
    private ExtendedWebElement addProfileMaturityName;

    @FindBy(id = "addProfileMaturityRatingTextView")
    private ExtendedWebElement addProfileMaturityRatingSubtitle;

    @FindBy(id = "addProfileMaturityRatingBodyTextView")
    private ExtendedWebElement addProfileRatingMainBody;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/addProfileMaturityRatingContinueButton']/*/*/*")
    private ExtendedWebElement addProfileMaturityContinueButton;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/addProfileMaturityRatingNotNowButton']/*/*/*")
    private ExtendedWebElement addProfileMaturityNotNowButton;

    @FindBy(id = "addProfileMaturityRatingFooter")
    private ExtendedWebElement addProfileMaturityFooter;

    @FindBy(id = "profileInfoAvatarImageView")
    private ExtendedWebElement profileAvatar;

    @FindBy(id = "profileInfoAvatar")
    private ExtendedWebElement additionalProfileAvatar;

    @FindBy(id = "profileInfoName")
    private ExtendedWebElement profileInfoName;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/profilesRecyclerView']/*/*[@resource-id='com.disney.disneyplus:id/profileInfoName']")
    private ExtendedWebElement updateProfilesExtraProfiles;

    @FindBy(xpath = "//*[@text='%s']/following-sibling::android.widget.Switch")
    private ExtendedWebElement updateProfileRatingToggle;

    @FindBy(id = "confirmPasswordLayout")
    private ExtendedWebElement confirmPasswordScreen;

    @FindBy(id = "confirmPasswordTitle")
    private ExtendedWebElement profilePasswordTitle;

    @FindBy(id = "confirmPasswordSubTitle")
    private ExtendedWebElement profilePasswordSubtitle;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/confirmPasswordConfirmButton']/*/*")
    private ExtendedWebElement profilePasswordContinueButton;

    @FindBy(id = "confirmPasswordForgotButton")
    private ExtendedWebElement forgotPasswordLink;

    @FindBy(id = "inputErrorTextView")
    private ExtendedWebElement passwordErrorText;

    //Pin Setting Items
    @FindBy(id = "starPinEntryRoot")
    private ExtendedWebElement pinPageContainer;

    @FindBy(id = "profileInfoMaturityRating")
    private ExtendedWebElement pinPageMaxRatingImage;

    @FindBy(id = "secureYourProfile")
    private ExtendedWebElement pinPageHeader;

    @FindBy(id = "profilePinDescription")
    private ExtendedWebElement pinPageDescription;

    @FindBy(id = "digitNumber")
    private ExtendedWebElement enterPinTile;

    @FindBy(id = "pinCodeErrorTextView")
    private ExtendedWebElement pinErrorText;

    @FindBy(id = "primaryCta")
    private ExtendedWebElement positiveActionButton;

    @FindBy(id = "secondaryCta")
    private ExtendedWebElement createProfileNotNowBtn;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/primaryCta']/*/*/*")
    private ExtendedWebElement createProfilePinText;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/secondaryCta']/*/*/*")
    private ExtendedWebElement createProfileNotNowText;

    @FindBy(id = "profilePinReminder")
    private ExtendedWebElement pinReminderText;

    @FindBy(id = "enterPinPromptText")
    private ExtendedWebElement pinEntryTitle;

    @FindBy(id = "pinCodeEditText")
    private ExtendedWebElement pinTypeEditText;

    @FindBy(id = "profileInfoMaturityRating")
    private ExtendedWebElement profileRatingImage;

    @FindBy(id = "welcomeToStarTitle")
    private ExtendedWebElement migrationPinTitle;

    @FindBy(id = "maturityRatingDescription")
    private ExtendedWebElement migrationSubtitle;

    //Not Now sheet elements
    @FindBy(id = "setMaturityRatingBottomSheetCloseButton")
    private ExtendedWebElement notNowCloseBtn;

    @FindBy(id = "setMaturityRatingBottomSheetHeader")
    private ExtendedWebElement notNowHeader;

    @FindBy(id = "continueWithoutMatureDescription")
    private ExtendedWebElement notNowDescription;

    @FindBy(id = "messageText")
    private ExtendedWebElement pinToast;

    //Default Rating sheet elements
    @FindBy(id = "maturityRatingConfirmationHeader")
    private ExtendedWebElement defaultRatingHeader;

    @FindBy(id = "maturityRatingConfirmationSubcopy")
    private ExtendedWebElement defaultRatingBody;

    public DisneyPlusMaturityPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return maturityRatingRoot.isElementPresent();
    }

    public void clickContinueButton() {
        continueButton.click();
    }

    public boolean isStarNotNowBtnPresent() {
        return starNotNowBtn.isElementPresent();
    }

    public void clickStarNotNowBtn(){
        starNotNowBtn.click();
    }

    public void clickForgotPasswordLink(){
        forgotPasswordLink.click();
    }

    //Used for the create-pin prompt in Add Profile or Profile Migration. Needs to be manually submitted.
    public void createProfilePin(String pin) {
        enterPinTile.click();
        LOGGER.info("Entering PIN value of {}", pin);
        AndroidTVUtils tvUtils = new AndroidTVUtils(getDriver());
        tvUtils.sendInput(pin);
        AndroidService.getInstance().hideKeyboard();
        clickPositiveActionButton();
    }

    //Used for entering the pin prompt for accessing the profile. Auto navigates in the app
    public void enterProfilePin(String pin){
        enterPinTile.click();
        AndroidTVUtils tvUtils = new AndroidTVUtils(getDriver());
        tvUtils.sendInput(pin);
    }

    public boolean isPinErrorTextVisible(){
        return pinErrorText.isElementPresent(SHORT_TIMEOUT);
    }

    public String getPinErrorText(){
        return pinErrorText.getText();
    }

    public boolean isMaturityRatingPageOpened(){
        return addProfileMaturityContainer.isElementPresent();
    }

    public boolean isMaturitySettingProfileImageVisible(){
        return addProfileMaturityAvatar.isElementPresent();
    }

    public BufferedImage getMaturitySettingProfileImage(){
        return new UniversalUtils().getElementImage(addProfileMaturityAvatar);
    }

    public String getMaturitySettingProfileName(){
        return addProfileMaturityName.getText();
    }

    public String getMaturitySettingHeaderText(){
        return addProfileRatingsHeader.getText();
    }

    public String getMaturitySettingSubtitleText(){
        return addProfileMaturityRatingSubtitle.getAttribute(CONTENT_DESC);
    }

    public String getMaturityContinueButtonText(){
        return addProfileMaturityContinueButton.getText();
    }

    public void clickMaturityContinueButton(){
        addProfileMaturityContinueButton.click();
    }

    public void proceedThroughWelchLogin(String userPass){
        clickMaturityContinueButton();
        getEditTextByClass().click();
        getEditTextByClass().type(userPass);
        new AndroidUtilsExtended().hideKeyboard();
        clickPasswordContinueButton();
    }

    public String getMaturityNotNowButtonText(){
        return addProfileMaturityNotNowButton.getText();
    }

    public void clickMaturityNotNowButton(){
        addProfileMaturityNotNowButton.click();
    }

    public void clickMaturityNotNowBtn(
            DisneyPlusCommonPageBase commonPageBase,
            DisneyLocalizationUtils localizationUtils) {
        commonPageBase.genericTextElement.format(
                localizationUtils.getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.WELCH,
                        DictionaryKeys.BTN_NOT_NOW.getText())).click();
    }

    public void clickFullCatalogBtn(
            DisneyPlusCommonPageBase commonPageBase,
            DisneyLocalizationUtils localizationUtils) {
        commonPageBase.genericTextElement.format(
                localizationUtils.getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.WELCH,
                        DictionaryKeys.BTN_FULL_CATALOG.getText())).click();
    }

    public void clickGotItBtn(
            DisneyPlusCommonPageBase commonPageBase,
            DisneyLocalizationUtils localizationUtils) {
        commonPageBase.genericTextElement.format(
                localizationUtils.getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.WELCH,
                        DictionaryKeys.BTN_GOT_IT.getText())).click();
    }

    public String getMaturitySettingsFooterText(){
        return addProfileMaturityFooter.getText();
    }

    public boolean isConfirmPasswordOpen(){
        return confirmPasswordScreen.isElementPresent(SHORT_TIMEOUT);
    }

    public String getPasswordTitleText(){
        return profilePasswordTitle.getText();
    }

    public boolean isProfileInfoAvatarDisplayed(){
        return profileAvatar.isElementPresent();
    }

    public String getPasswordSubtitleText(){
        return profilePasswordSubtitle.getText();
    }

    public boolean isPasswordContinueButtonPresent(){
        return profilePasswordContinueButton.isElementPresent();
    }

    public void clickPasswordContinueButton(){
        profilePasswordContinueButton.click();
    }

    public boolean isForgotPasswordLinkPresent(){
        return forgotPasswordLink.isElementPresent();
    }

    public boolean isPinPageOpen(){
        return pinPageContainer.isElementPresent();
    }

    public String getProfileName(){
        return profileInfoName.getText();
    }

    public String getPinHeaderText(){
        return pinPageHeader.getText();
    }

    public String getPinPageDescriptionText(){
        return pinPageDescription.getText();
    }

    public String getPinReminderText(){
        return pinReminderText.getText();
    }

    public boolean isPinInputDisplayCorrect(){
        return findExtendedWebElements(enterPinTile.getBy()).size() == 4;
    }

    public String getPinPageMaxRatingImageName(){
        String[] textValue = pinPageMaxRatingImage.getText().split("/");
        return textValue[textValue.length-1].trim();
    }

    public boolean isCreatePinButtonPresent() { return positiveActionButton.isElementPresent(); }

    public void clickPositiveActionButton() { positiveActionButton.click(); }

    public String getCreatePinButtonText() {
        return createProfilePinText.getText();
    }

    public boolean isPinNotNowButtonPresent(){
        return createProfileNotNowBtn.isElementPresent();
    }

    public void clickPinNotNowButton(){
        createProfileNotNowBtn.click();
    }

    public String getPinNotNowButtonText() {
        return createProfileNotNowText.getText();
    }

    public boolean isEnterPinTitleDisplayed() {
        return pinEntryTitle.isElementPresent();
    }

    public boolean isMigrationTitleDisplayed() {
        return migrationIntroTitle.isElementPresent();
    }

    public String getMigrationTitleText() {
        return migrationIntroTitle.getText();
    }

    public boolean isMigrationDescriptionDisplayed() {
        return migrationIntroDescription.isElementPresent();
    }

    public String getMigrationDescriptionText() {
        return migrationIntroDescription.getText();
    }

    public boolean isProfileMigrationCatalogHeaderPresent() {
        return starRatingsHeader.isElementPresent();
    }

    public String getProfileMigrationCatalogHeaderText() {
        return starRatingsHeader.getText();
    }

    public boolean isProfileMigrationCatalogSubHeaderPresent() {
        return maturityRatingSubHeader.isElementPresent();
    }

    public String getProfileMigrationCatalogSubHeaderText() {
        return maturityRatingSubHeader.getAttribute(CONTENT_DESC);
    }

    public boolean isProfileMigrationCatalogBodyPresent() {
        return maturityRatingsBody.isElementPresent();
    }

    public String getProfileMigrationBodyText() {
        return maturityRatingsBody.getText();
    }

    public boolean isNotNowCloseButtonPresent() {
        return notNowCloseBtn.isElementPresent();
    }

    public void clickNotNowCloseBtn() {
        notNowCloseBtn.click();
    }

    public boolean isNotNowHeaderPresent() {
        return notNowHeader.isElementPresent();
    }

    public String getNotNowHeaderText() {
        return notNowHeader.getText();
    }

    public boolean isNotNowDescriptionPresent() {
        return notNowDescription.isElementPresent();
    }

    public String getNotNowDescriptionBody() {
        return notNowDescription.getText();
    }

    public boolean isPasswordErrorTextDisplayed() {
        return passwordErrorText.isElementPresent();
    }

    public String getPasswordErrorText() {
        return passwordErrorText.getText();
    }

    public String getMigrationPinTitleText() {
        return migrationPinTitle.getText();
    }

    public String getMigrationSubtitle() {
        return migrationSubtitle.getText();
    }

    public boolean isProfileRatingImageVisible() {
        return profileRatingImage.isElementPresent();
    }

    public boolean isPinEntryPresent() {
        return pinTypeEditText.isElementPresent();
    }

    public String getPinToastText() {
        return pinToast.getText();
    }

    public boolean isOtherProfilesMaturityPageOpen() {
        return starMaturityRatingTitle.isElementPresent();
    }

    public String getMaturityRatingsTitleText() {
        return starMaturityRatingTitle.getText();
    }

    public String getMaturityRatingsSetToText() {
        String text = setToText.getText();
        text = StringUtils.substringBefore(text, "file").trim();
        return text;
    }

    public boolean isAdditionalProfileAvatarVisible() {
        return additionalProfileAvatar.isElementPresent();
    }

    public int getAdditionalAvatarsCount() {
        return findExtendedWebElements(additionalProfileAvatar.getBy()).size();
    }

    public List<String> getDisplayedOtherProfileNames() {
        List<String> names = new ArrayList<>();
        findExtendedWebElements(updateProfilesExtraProfiles.getBy()).forEach(name -> names.add(name.getText()));
        return names;
    }

    public boolean isAdditionalProfileNameDisplayed(String profileName) {
        return getDisplayedOtherProfileNames().contains(profileName);
    }

    public boolean isUpdateProfileTogglePresent(String profile) {
        return updateProfileRatingToggle.format(profile).isElementPresent();
    }

    public void clickProfileRatingToggle(String profile) {
        updateProfileRatingToggle.format(profile).click();
    }

    public String getDefaultRatingHeader() {
        return defaultRatingHeader.getText();
    }

    public String getDefaultRatingBody() {
        return defaultRatingBody.getText();
    }

    public void clickMaturityRatingConfirmationButton() { maturityRatingConfirmationBtn.click(); }

    public Map<Integer, Boolean> enterPinSequence(int maxInputOfFour) {
        Map<Integer, Boolean> inputResults = new TreeMap<>();
        String badPin = "";
        for (int i = 0; i < maxInputOfFour; i++) {
            badPin = badPin.concat(String.valueOf(i));
            initPage(DisneyPlusCommonPageBase.class).editTextByClass.type(badPin);
            inputResults.put(i, isPinErrorTextVisible());
        }
        return inputResults;
    }
}
