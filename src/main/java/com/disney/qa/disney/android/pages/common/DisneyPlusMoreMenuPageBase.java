package com.disney.qa.disney.android.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.disney.DisneyContentApiChecker;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.fasterxml.jackson.databind.JsonNode;
import com.qaprosoft.carina.core.foundation.utils.android.AndroidService;
import com.qaprosoft.carina.core.foundation.utils.resources.L10N;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.stream.IntStream;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusMoreMenuPageBase extends DisneyPlusCommonPageBase {

    private static String lion = "{L10N:";
    private static final String CHILD = "Child";

    @FindBy(id = "editProfilesButton")
    private ExtendedWebElement editProfilesButton;

    @FindBy(id = "options")
    private ExtendedWebElement menuList;

    @FindBy(id = "optionsRoot")
    private ExtendedWebElement moreMenuScreen;

    @FindBy(xpath = "//*[contains(@resource-id, 'optionsListRowRoot') and @index='%s']/*[contains(@resource-id, 'title')]")
    private ExtendedWebElement menuItem;

    @FindBy(xpath = "//*[contains(@text, \"%s\")]")
    private ExtendedWebElement customMenuItem;

    @FindBy(id = "backButton")
    private ExtendedWebElement backButton;

    @FindBy(id = "closeButton")
    private ExtendedWebElement closeButton;

    @FindBy(id = "emptyStateImage")
    private ExtendedWebElement plusIcon;

    @FindBy(id = "emptyStateTvDetails")
    private ExtendedWebElement noItemsTextBody;

    @FindBy(id = "emptyStateTvTitle")
    private ExtendedWebElement noItemsTextHeader;

    @FindBy(xpath = "//*[contains(@resource-id, 'sectionName') and @text=\"%s\"]")
    private ExtendedWebElement sectionHeader;

    @FindBy(xpath = "//*[@text=\"%s\"]/following-sibling::*/*[@text=\"%s\"]")
    private ExtendedWebElement sectionOption;

    @FindBy(xpath = "//*[@text=\"%s\"]/following-sibling::*[@text=\"%s\"]")
    private ExtendedWebElement sectionSetting;

    @FindBy(xpath = "//*[@text=\"%s\"]/following-sibling::*[contains(@resource-id, 'settingToggle')]")
    private ExtendedWebElement sectionOptionToggle;

    @FindBy(id = "storageType")
    private ExtendedWebElement appSettingsDeviceName;

    @FindBy(id = "indicatorBar")
    private ExtendedWebElement appSettingsStorageGraph;

    @FindBy(xpath = "//*[contains(@resource-id, 'accountSettingValue') and @text='%s']")
    private ExtendedWebElement accountCredential;

    @FindBy(xpath = "//*[contains(@resource-id, 'accountSettingValue') and @text='%s']/" +
            "following-sibling::*[contains(@resource-id, 'editButton')]")
    private ExtendedWebElement accountCredentialEditButton;

    @FindBy(id = "disneyLogoAccount")
    private ExtendedWebElement disneyPlusAccountLogo;

    @FindBy(id = "disneyLogoAccountDescription")
    private ExtendedWebElement disneyPlusAccountCTA;

    @FindBy(xpath = "//*[contains(@resource-id, 'subscriptionPeriodText') and @text=\"%s\"]")
    private ExtendedWebElement accountSubscriptionSectionItem;

    @FindBy(xpath = "//*[contains(@resource-id, 'legal_title') and @text=\"%s\"]")
    private ExtendedWebElement legalViewItem;

    @FindBy(id = "legalContentTextView")
    private ExtendedWebElement legalContentView;

    @FindBy(id = "optionsProgressBar")
    private ExtendedWebElement loadSpinner;

    //Logout Prompt elements
    @FindBy(id = "dialogLayout")
    private ExtendedWebElement logoutModal;

    //Webview elements
    @FindBy(xpath = "//*[contains(@resource-id, 'close_button')]")
    private ExtendedWebElement webviewClose;

    @FindBy(xpath = "//*[contains(@resource-id, 'url_bar')]")
    private ExtendedWebElement webviewHeader;

    //Edit Profile elements
    @FindBy(xpath = "//*[@text='%s']/preceding-sibling::*[@resource-id='com.disney.disneyplus:id/avatarImageView']")
    private ExtendedWebElement profileImage;

    @FindBy(id = "editButton")
    private ExtendedWebElement profileEditButton;

    @FindBy(id = "labelTextView")
    private ExtendedWebElement profileText;

    @FindBy(id = "poster")
    private ExtendedWebElement profilePoster;

    @FindBy(id = "shelfItemLayout")
    private ExtendedWebElement shelfItemLayout;

    @FindBy(id = "profileToggleSwitch")
    private ExtendedWebElement kidsProfileToggle;

    @FindBy(xpath = "//*[@text=\"%s\"]/following-sibling::android.widget.Switch")
    private ExtendedWebElement editProfileItemSwitch;

    @FindBy(id = "deleteButton")
    private ExtendedWebElement deleteProfileBtn;

    @FindBy(xpath = "//*[@content-desc='Add Profile']")
    private ExtendedWebElement addProfileButton;

    @FindBy(xpath = "//*[@text='NOT NOW']")
    private ExtendedWebElement notNowButton;

    @FindBy(xpath = "//*[@text='FULL CATALOG']")
    private ExtendedWebElement fullCatalogButton;

    //Watchlist Items
    @FindBy(id = "thumbnailImage")
    private ExtendedWebElement watchlistThumbnail;

    @FindBy(xpath = "//*[@text=\"%s\"]/following-sibling::android.widget.TextView")
    private ExtendedWebElement editProfileContentRating;

    //Download quality screen
    @FindBy(id = "optionSqTitle")
    private ExtendedWebElement standardDownloadQuality;

    @FindBy(id = "optionMqTitle")
    private ExtendedWebElement mediumDownloadQuality;

    @FindBy(id = "optionHqTitle")
    private ExtendedWebElement highDownloadQuality;

    @FindBy(id = "gender")
    protected ExtendedWebElement gender;

    @FindBy(id = "inputConstraintLayout")
    protected ExtendedWebElement genderSelector;

    private List<ExtendedWebElement> profiles = new ArrayList<>();

    private BufferedImage newProfileAvatar;

    public DisneyPlusMoreMenuPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened(){
        return moreMenuScreen.isVisible();
    }

    protected static final DisneyPlusMoreMenuPageBase.MenuList[] menuOrder = {DisneyPlusMoreMenuPageBase.MenuList.WATCHLIST,
            DisneyPlusMoreMenuPageBase.MenuList.SETTINGS, DisneyPlusMoreMenuPageBase.MenuList.ACCOUNT,
            DisneyPlusMoreMenuPageBase.MenuList.LEGAL, DisneyPlusMoreMenuPageBase.MenuList.HELP,
            DisneyPlusMoreMenuPageBase.MenuList.LOGOUT};

    public static MenuList[] getMenuOrder() {
        return menuOrder;
    }

    public enum MenuList{
        WATCHLIST("nav_watchlist_title"),
        SETTINGS("app_settings_title"),
        LEGAL("legalcenter_title"),
        ACCOUNT("nav_account"),
        HELP("nav_help"),
        LOGOUT("nav_log_out"),
        DEBUG("Debug About"),
        //MODAL TEXT
        LOGOUT_CONFIRMATION("log_out_confirmation_title"),
        LOGOUT_WITH_DOWNLOADS("log_out_confirmation_copy");

        private String menuTitle;

        MenuList(String title) {
            this.menuTitle = title;
        }

        public String getText() {
            String menuTitleText = this.menuTitle;
            if (menuTitleText.contains(lion)) {
                String key = menuTitleText.replace(lion, "").replace("}", "");
                menuTitleText = L10N.getText(key);
            }
            return menuTitleText;
        }
    }

    public enum AppSettingsList{
        SETTINGS_PAGE_HEADER("nav_settings_title"),
        APP_SETTINGS_TITLE("app_settings_title"),
        //Video Playback section
        VIDEO_PLAYBACK("video_playback_title"),
        STREAM_OVER_WIFI("settings_datausage_wifionly"),
        WIFI_DATA_USAGE("settings_wifidatausage_pageheader"),
        CELLULAR_DATA("cell_data_usage_title"),
        //Downloads section
        WIFI_SETTING("wifionly_label"),
        DOWNLOADS_TITLE("downloads_title"),
        DOWNLOAD_QUALITY("download_quality_title"),
        DOWNLOAD_LOCATION("app_settings_downloadlocation_label"),
        DELETE_DOWNLOADS("delete_downloads_label"),
        DEFAULT_QUALITY("download_quality_standard_title"),
        //Storage
        DEVICE_STORAGE("app_settings_storagelocation_label");

        private String subMenuItem;

        AppSettingsList(String title) {
            this.subMenuItem = title;
        }

        public String getText() {
            String menuTitleText = this.subMenuItem;
            if (menuTitleText.contains(lion)) {
                String key = menuTitleText.replace(lion, "").replace("}", "");
                menuTitleText = L10N.getText(key);
            }
            return menuTitleText;
        }
    }

    public enum DownloadQualityItems {
        HIGH("download_quality_high_title"),
        MEDIUM("download_quality_medium_title"),
        STANDARD("download_quality_standard_title");

        private String key;
        DownloadQualityItems(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public enum WatchlistItems{
        TITLE("watchlist_title"),
        EMPTY_LONG("watchlist_subcopy"),
        EMPTY_SHORT("watchlist_copy");

        private String textItem;

        WatchlistItems(String query){
            this.textItem = query;
        }

        public String getText() {
            String textValue = this.textItem;
            if(textValue.contains(lion)){
                String key = textValue.replace(lion, "").replace("}", "");
                textValue = L10N.getText(key);
            }
            return textValue;
        }
    }

    public enum AccountList{
        ACCOUNT_DETAILS("account_details"),
        SUBSCRIPTION("subscription"),
        BILLING_DETAILS("billing_details"),
        HIDDEN_PASSWORD("login_hide_password_asterisk");

        private String subMenuItem;

        AccountList(String title) {
            this.subMenuItem = title;
        }

        public String getText() {
            String menuTitleText = this.subMenuItem;
            if (menuTitleText.contains(lion)) {
                String key = menuTitleText.replace(lion, "").replace("}", "");
                menuTitleText = L10N.getText(key);
            }
            return menuTitleText;
        }
    }

    //Parameterized legal item queries for use in the event a specific item needs to be checked.
    public enum LegalList{
        SUBSCRIPTION("[?(@.documentCode=='GTOU_DPLUS_SUB_%s.DEFAULT.%s')].label"),
        PRIVACY("[?(@.documentCode=='ppV2.DEFAULT.%s')].label"),
        COOKIES("[?(@.documentCode=='DISNEY-EU-SHAREINFO-COOKIE-ACCEPT.DEFAULT.%s')].label"),
        EU_RIGHTS("[?(@.documentCode=='EU_PRIVACY_RIGHTS.DEFAULT.%s')].label"),
        AU_COLLECTION("[?(@.documentCode=='COLLECTION_STATEMENT.AU_bloc.en-US')].label");

        private String subMenuItem;

        LegalList(String title) {
            this.subMenuItem = title;
        }

        public String getText() {
            String menuTitleText = this.subMenuItem;
            if (menuTitleText.contains(lion)) {
                String key = menuTitleText.replace(lion, "").replace("}", "");
                menuTitleText = L10N.getText(key);
            }
            return menuTitleText;
        }
    }

    public enum AddProfileKeys{
        ADD_PROFILE("create_profile_add_profile"),
        CHOOSE_AVATAR("chooseprofileicon_title");

        private String key;
        AddProfileKeys(String key){
            this.key = key;
        }

        public String getKey(){
         return this.key;
        }
    }

    public enum EditProfileKeys{
        ALL_TITLES("profile_settings_maturity_rating_description_all"),
        CONTENT_RATING("maturity_rating_settings_label"),
        MATURITY_RATING_DESCRIPTION("profile_settings_maturity_rating_description_value"),
        RATING_14("rating_djctq_14"),
        TV_14_DESC("rating_mpaaandtvpg_tv-14_description"),
        TV_MA("rating_mpaaandtvpg_tv-ma"),
        RATING_18("rating_djctq_18"),
        TV_MA_DESC("rating_mpaaandtvpg_tv-ma_description");

        private String key;
        EditProfileKeys(String key){
            this.key = key;
        }

        public String getKey(){
            return this.key;
        }
    }

    public boolean isMenuItemListedCorrectly(int index, String item){
        return menuItem.format(Integer.toString(index)).getText().equals(item);
    }

    public void selectMenuItem(String menuText){
        waitUntil(ExpectedConditions.visibilityOfElementLocated(menuList.getBy()), 30);
        customMenuItem.format(menuText).click();
    }

    public boolean isEmptyWatchlistIconPresent(){
        return plusIcon.isElementPresent();
    }

    public boolean isBackButtonPresent(){
        return backButton.isElementPresent();
    }

    public void pressBackButton(){
        backButton.click();
    }

    public boolean isHeaderPresent(String headerText){
        return new AndroidUtilsExtended().scroll(headerText, menuList).isElementPresent();
    }

    public boolean isWatchlistEmptyTextTitlePresent(String text){
        return noItemsTextHeader.isElementPresent() && noItemsTextHeader.getText().equals(text);
    }

    public boolean isWatchlistEmptyTextBodyPresent(String text){
        return noItemsTextBody.isElementPresent() && noItemsTextBody.getText().equals(text);
    }

    public boolean isSectionHeaderPresent(String headerText){
        return sectionHeader.format(headerText).isElementPresent();
    }

    public boolean isSectionOptionPresent(String header, String option){
        return sectionOption.format(header, option).isElementPresent();
    }

    public boolean isOptionTogglePresent(String option){
        return sectionOptionToggle.format(option).isElementPresent();
    }

    public boolean isOptionSettingVisible(String option, String setting){
        return sectionSetting.format(option, setting).isElementPresent();
    }

    public boolean isAppSettingsStorageHeaderProperlyDisplayed(String text){
        return appSettingsDeviceName.isElementPresent() &&
                appSettingsDeviceName.getText().equals(text);
    }

    public boolean isAppSettingsStorageGraphDisplayed(){
        if (!appSettingsStorageGraph.isElementPresent()) {
            swipeUpOnScreen(2);
        }
        return appSettingsStorageGraph.isElementPresent();
    }

    public boolean isUserCredentialDisplayed(String credential, boolean email){
        if(email) {
            return accountCredential.format(credential.toLowerCase()).isElementPresent();
        } else {
            return accountCredential.format(credential).isElementPresent();
        }
    }

    public boolean checkEditButtonVisibility(String credential, boolean email){
        if(email) {
            return accountCredentialEditButton.format(credential.toLowerCase()).isElementPresent();
        } else {
            return accountCredentialEditButton.format(credential).isElementPresent();
        }
    }

    public boolean isAccountCtaTextVisible(){
        return disneyPlusAccountCTA.isElementPresent();
    }

    public boolean isLegalOptionVisible(String item){
        return legalViewItem.format(item).isElementPresent();
    }

    public boolean isLegalOptionExpandable(String item){
        legalViewItem.format(item).click();
        boolean expanded = isLegalContentViewVisible();
        legalViewItem.format(item).click();
        return expanded && !legalContentView.isElementPresent(SHORT_TIMEOUT);
    }

    public boolean isLegalContentViewVisible(){
        return legalContentView.isElementPresent();
    }

    public boolean isWebviewClosePresent(){
        return webviewClose.isElementPresent();
    }

    public boolean doesWebviewOpenCorrectAddress(String url){
        return webviewHeader.getText().contains(url);
    }

    public boolean isLogoutModalPresent(){
        return logoutModal.isElementPresent(SHORT_TIMEOUT);
    }

    public void cancelLogout(){
        clickCancelButton();
    }

    public void confirmLogout(){
        clickConfirmButton();
    }

    public void openEditProfile(){
        editProfilesButton.click();
    }

    public List<ExtendedWebElement> getProfiles() {
        if(profiles.isEmpty()) {
            waitUntil(ExpectedConditions.visibilityOfElementLocated(profileText.getBy()), 30);
            profiles.addAll(findExtendedWebElements(profileText.getBy()));
        }
        return profiles;
    }

    public String getAddProfileText(){
        return getProfiles().get(getProfiles().size()-1).getText();
    }

    public boolean testCheckDoneButton(){
        return getActionButton().isElementPresent();
    }

    /**
     * @deprecated - This needs to be replaced with the Welch-friendly method
     * and associated logic needs to be tested for stability after removal.
     */
    @Deprecated(forRemoval = false)
    public void makeNewProfile(String addProfile){
        genericTextElementExact.format(addProfile).click();
        profilePoster.click();
        editTextByClass.click();
        editTextByClass.type("Automation");
        new AndroidUtilsExtended().pressBack();
        clickActionButton();
        pause(1);
        genericTextElementExact.format("Automation").click();
    }

    /**
     * @deprecated - This needs to be replaced with the Welch-friendly method
     * and associated logic needs to be tested for stability after removal.
     */
    @Deprecated(forRemoval = false)
    public void makeNewChildProfile(String addProfile, String language){
        genericTextElementExact.format(addProfile).click();
        profilePoster.click();
        editTextByClass.click();
        editTextByClass.type(CHILD);
        AndroidService.getInstance().hideKeyboard();
        kidsProfileToggle.click();
        clickActionButton();
        if(!language.equals("en")) {
            manuallySetActiveProfileLanguage(CHILD, language);
        } else {
            genericTextElementExact.format(CHILD).click();
        }
    }

    /**
     * Creates a new Profile via in-app navigation. Saves profile image as class variable
     * @param profileName - Name to be entered
     * @param restricted - Toggles 'Kids' mode switch
     */
    public void addNewProfile(String profileName, boolean restricted, AndroidUtilsExtended androidUtilsExtended){
        getProfiles().get(getProfiles().size()-1).click();
        newProfileAvatar = androidUtilsExtended.getElementImage(profilePoster);
        profilePoster.click();
        editTextByClass.click();
        editTextByClass.type(profileName);
        Assert.assertTrue(isVirtualKeyboardVisible(androidUtilsExtended),
                "Virtual keyboard expected to be displayed");
        androidUtilsExtended.hideKeyboard();

        if(restricted){
            kidsProfileToggle.click();
        }
        clickActionButton();
    }

    /**
     * Creates new profile from more menu screen
     * Full catalog access not available if kidsMode is true
     * Explicitly bypass pin creation flow if fullCatalog is true
     * @param profileName - New profile name
     * @param passwordRequired - E.g. restricted profile type
     * @param kidsMode - Profile to be kids mode
     * @param dob - Date of Birth
     */
    public void addArielNewProfileFromMoreMenu(
            String profileName,
            boolean passwordRequired,
            boolean kidsMode,
            String dob,
            DisneyPlusLoginPageBase loginPageBase,
            DisneyAccount disneyAccount,
            AndroidUtilsExtended androidUtilsExtended) {
        addProfileButton.click();

        if (passwordRequired) {
            loginPageBase.logInWithPassword(disneyAccount.getUserPass());
        }

        profilePoster.click();
        editTextByClass.click();
        sendTextEditTextField(0, profileName);
        sendTextEditTextField(1, dob);
        androidUtilsExtended.hideKeyboard();
        genderSelector.click();
        gender.click();
        androidUtilsExtended.hideKeyboard();

        if (kidsMode) {
            kidsProfileToggle.click();
            clickActionButton();
            return;
        }
        clickActionButton();
    }

    public void sendTextEditTextField(int position, String text) {
        List<WebElement> list = getDriver().findElements(By.id("editFieldEditText"));
        try {
            list.get(position).sendKeys(text);
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            LOGGER.error("sentTextEditTextField: ", indexOutOfBoundsException);
            Assert.fail("indexOutOfBoundsExcept for Position " + position + " and unable to send keys to editFieldEditText.");
        }
    }

    /**
     * @param fullCatalog - Profile to have full TV-MA catalog access
     */
    public void fullCatalogLogIn(
            boolean fullCatalog,
            DisneyAccount disneyAccount,
            DisneyPlusCommonPageBase commonPageBase,
            DisneyPlusLoginPageBase loginPageBase,
            DisneyLocalizationUtils localizationUtils) {
        if (fullCatalog) {
            fullCatalogButton.click();
            loginPageBase.logInWithPassword(disneyAccount.getUserPass());
            commonPageBase.clickOnGenericTextElement(
                    localizationUtils.getDictionaryItem(
                            DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_NOT_NOW.getText()));
        } else {
            commonPageBase.clickOnGenericTextElement(
                    localizationUtils.getDictionaryItem(
                            DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_NOT_NOW.getText()));
        }
    }

    public void clickFirstAvailableProfilePoster(){
        profilePoster.click();
    }

    //Selects the given profile by passed name
    public void changeProfile(String profileName){
        waitForLoading();
        genericTextElement.format(profileName).click();
    }

    /**
     * @deprecated This is specific to Profiles with a hardcoded name. This needs to be replaced with the
     * new method that allows for configurable names.
     * @param dictionary
     */
    @Deprecated(forRemoval = false)
    public void deleteChildProfile(JsonNode dictionary){
        DisneyContentApiChecker apiChecker = new DisneyContentApiChecker();
        editProfilesButton.click();
        genericTextElementExact.format(CHILD).click();
        genericTextElementExact.format(apiChecker.getDictionaryItemValue(dictionary, "btn_delete_profile")).click();
        genericTextElementExact.format(apiChecker.getDictionaryItemValue(dictionary, "btn_delete")).click();
    }

    public boolean isWatchlistTitleVisible(String title){
        return shelfItemLayout.getAttribute("content-desc").equals(title);
    }

    //New profiles do not allow for language setting on creation. Specific navigation into profile edit is needed.
    public void manuallySetActiveProfileLanguage(String profileName, String language){
        DisneyContentApiChecker apiHandler = new DisneyContentApiChecker();
        JsonNode dictionary = apiHandler.getFullDictionaryBody(language);

        if(!initPage(DisneyPlusMoreMenuPageBase.class).isOpened()) {
            initPage(DisneyPlusCommonPageBase.class).navigateToPage(apiHandler.getDictionaryItemValue(
                    dictionary, DisneyPlusCommonPageBase.MenuItem.MORE.getText()));
        }

        editProfilesButton.click();
        genericTextElementExact.format(profileName).click();
        genericTextElementExact.format(apiHandler.getDictionaryItemValue(
                dictionary, "ui_language_setting")).click();

        selectLanguageFromList(language);
        clickActionButton();
        clickActionButton();
        genericTextElementExact.format(profileName).click();
    }

    //Dictionary defaults to 'en' if the language is invalid on the api call. This does the same.
    private void selectLanguageFromList(String language){
        switch(language){
            case "en":
                genericTextElementExact.format("English (US)").click();
                break;
            case "en-gb":
                genericTextElementExact.format("English (UK)").click();
                break;
            case "es-419":
                genericTextElementExact.format("Español (Latinoamericano)").click();
                break;
            case "fr-ca":
                genericTextElementExact.format("Français (Canadien)").click();
                break;
            case "nl":
                genericTextElementExact.format("Nederlands").click();
                break;
            default:
                genericTextElementExact.format("English (US)").click();
                break;
        }
    }

    public boolean areDownloadQualityOptionsPresent(String...downloadOptions){
        List<Boolean> assertions = new ArrayList<>();
        IntStream.range(0, downloadOptions.length-1).forEach(option -> assertions.add(genericTextElement.format(downloadOptions[option]).isElementPresent()));
        return !assertions.contains(false);
    }

    public void selectDownloadQuality(DownloadQualityItems downloadQualityItems) {
        switch(downloadQualityItems) {
            case HIGH:
                highDownloadQuality.click();
                break;
            case MEDIUM:
                mediumDownloadQuality.click();
                break;
            case STANDARD:
                standardDownloadQuality.click();
        }
    }

    //For manually setting the autoplay toggle in Edit Profiles
    public void setProfileAutoPlayStatus(boolean setStatus, JsonNode dictionary){
        LOGGER.info("Setting Auto Play to {}", setStatus);
        DisneyContentApiChecker apiChecker = new DisneyContentApiChecker();
        String autoPlayText = apiChecker.getDictionaryItemValue(dictionary, "create_profile_autoplay");
        if(editProfileItemSwitch.format(autoPlayText).getAttribute("checked").equals(Boolean.toString(setStatus))){
            LOGGER.info("Auto Play status already set. Proceeding with test.");
        } else {
            editProfileItemSwitch.format(autoPlayText).click();
            clickActionButton();
            LOGGER.info("Auto Play status set. Proceeding with test.");
        }
    }

    public BufferedImage getNewProfileAvatar(){
        return newProfileAvatar;
    }

    public String getProfileRatingText(String contentRating){
        AndroidUtilsExtended util = new AndroidUtilsExtended();
        util.scroll(contentRating, getAppRootDisplay());
        util.swipeUp(1, 250);
        return editProfileContentRating.format(contentRating).getText();
    }

    public String getSettingsStorageLocationText(DisneyLocalizationUtils languageUtils) {
        List<String> locations = Arrays.asList(AndroidService.getInstance().executeAdbCommand("shell df | grep storage | awk '{print $6}'").split(" "));
        LOGGER.info("Storage location is: {}, and size is: {}", locations, locations.size());
        if (locations.size() > 1) {
            LOGGER.info("External Storage device is mounted. Storage Header text will be against localized 'Internal Storage' instead of device name.");
            return languageUtils.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DEVICE_STORAGE_INTERNAL_LABEL.getText());
        } else {
            return languageUtils.replaceValuePlaceholders(languageUtils.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.AppSettingsList.DEVICE_STORAGE.getText()), new AndroidUtilsExtended().getDeviceModel());
        }
    }

    public void validateLegalScreenItems(SoftAssert sa) {
        Set<String> legalItems = getDictionary().getLegalHeaders();

        for (String item : legalItems) {
            boolean itemPresent = isLegalOptionVisible(item);

            LOGGER.info("Validating functions for: {}", item);
            sa.assertTrue(itemPresent,
                    item + " is not displayed");

            if (itemPresent) {
                sa.assertTrue(isLegalOptionExpandable(item),
                        item + " is not expandable/collapsable");
            }
        }
    }

    public void clickAddProfileButton() { addProfileButton.click(); }
}
