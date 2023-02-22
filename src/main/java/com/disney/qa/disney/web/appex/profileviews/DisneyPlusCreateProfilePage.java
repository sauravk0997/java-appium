package com.disney.qa.disney.web.appex.profileviews;

import com.disney.qa.disney.web.DisneyWebKeys;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.common.DisneyPlusBaseProfileViewsPage;
import com.disney.qa.disney.web.entities.ProfileEligibility;
import com.fasterxml.jackson.databind.JsonNode;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusCreateProfilePage extends DisneyPlusBaseProfileViewsPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//button[contains(text(), '%s')]")
    private ExtendedWebElement buttonContainText;

    @FindBy(xpath = "//div[contains(@class,'add-profile')]")
    private ExtendedWebElement addProfileBtn;

    @FindBy(xpath = "//div[contains(@class,'profile-avatar')]")
    private ExtendedWebElement primaryProfileBtn;

    @FindBy(xpath = "//span[contains(text(), '%s')]")
    private ExtendedWebElement spanContainsText;

    @FindBy(xpath = "//*[text() = '%s']")
    private ExtendedWebElement genericEqualsText;

    @FindBy(id = "email")
    private ExtendedWebElement emailTextBox;

    @FindBy(xpath = "//span[@for='kidsMode']")
    private ExtendedWebElement juniorToggleButtonSpan;

    @FindBy(id = "password")
    private ExtendedWebElement passTextBox;

    @FindBy(xpath = "//div[@class = 'slick-track']")
    private ExtendedWebElement avatarSelectionTrack;

    @FindBy(xpath = "//div[contains(@class,'profile-avatar')]//h3[contains(text(),'%s')]")
    private ExtendedWebElement profileAvatarSplash;

    @FindBy(xpath = "//div[@class = 'slick-track']/div")
    private List<ExtendedWebElement> avatarSelection;

    @FindBy(id = "account-dropdown")
    private ExtendedWebElement profileDropDown;

    @FindBy(xpath = "//div[@id='account-dropdown']//a[@role='menuitem' and contains(@aria-label,'%s')]")
    private ExtendedWebElement selectProfile;

    @FindBy(xpath = "//*[@id ='account-dropdown']/ul/li[5]")
    private ExtendedWebElement accountButton;

    @FindBy(id = "addProfile")
    private ExtendedWebElement addProfileNameTextBox;

    @FindBy(xpath = "//div[@id = 'app_scene_content']//ul")
    private ExtendedWebElement allProfile;

    @FindBy(xpath = "//div[@id = 'app_scene_content']//ul/div//h3")
    private List<ExtendedWebElement> avatarNamesOnProfileSectionPage;

    @FindBy (id = "cancel")
    private ExtendedWebElement cancelButton;

    @FindBy(id = "dropdown-option_account")
    private ExtendedWebElement accountDropdownAccountNavId;

    @FindBy(xpath = "//*[@data-testid='myservices-link-bundle_activate_hulu']")
    private ExtendedWebElement huluServicesIcon;

    public DisneyPlusCreateProfilePage(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getProfileAvatarSplashByName(String text) {
        return profileAvatarSplash.format(text);
    }

    public ExtendedWebElement getAccountDropdownAccountNavId() {
        return accountDropdownAccountNavId;
    }

    public ExtendedWebElement getHuluServicesIcon() {
        return huluServicesIcon;
    }

    public void enterEmail(String email) {
        emailTextBox.type(email);
    }

    public boolean isProfileBtnVisible() {
        return profileDropDown.isVisible();
    }

    public ExtendedWebElement clickOnSelectedProfile(String profileName) {
        return selectProfile.format(profileName);
    }

    public void hoverOnProfile() {
        profileDropDown.hover();
    }

    public String getAccountAttribute(String attribute) {
        return profileDropDown.getAttribute(attribute);
    }

    public void clickOnAddSplashProfileBtn() {
        addProfileBtn.clickByJs();
    }

    public void clickOnPrimarySplashProfileBtn() {
        primaryProfileBtn.click();
    }

    public void clickJuniorToggleBtn() {
        juniorToggleButtonSpan.click();
    }

    public void selectAvatarForProfile() {
        avatarSelectionTrack.hover();
        if (!avatarSelection.isEmpty()) {
            LOGGER.info("Selecting Avatar");
            avatarSelection.get(1).click();
        }
    }

    public void selectProfile(String profileText) {
        LOGGER.info("Selecting Profile From Profile Selection Page");
        waitFor(getGenericEqualsText(profileText));
        getGenericEqualsText(profileText).hover();
        getGenericEqualsText(profileText).click();
        LOGGER.info("After Selecting Profile: " + getCurrentUrl());
    }

    public void addProfileName(String profileName) {
        waitFor(addProfileNameTextBox);
        addProfileNameTextBox.type(profileName);
    }

    public ExtendedWebElement getAppSceneContentAvatar() {
        return allProfile;
    }

    public void clickAccountDropdownAccountNavId() {
        getAccountDropdownAccountNavId().clickByJs();
    }

    public void clickHuluServicesIcon() {
        getHuluServicesIcon().click();
    }

    public void verifyNumberOfProfileForNewProfileCreation(SoftAssert softAssert, String urlText) {
        allProfile.hover();
        if (avatarNamesOnProfileSectionPage.size() > 1) {
            LOGGER.info("New profile created");
            verifyUrlText(softAssert, urlText);
        } else {
            LOGGER.info("After new profile was added it did not navigate to proper url: " + getCurrentUrl());
        }
    }

    //Dictionary Keys

    public void clickOnNavAccountKey(JsonNode dictionary) {
        getDictionaryElement(dictionary,DisneyWebKeys.NAV_ACCOUNT.getText()).click();
        LOGGER.info("'Account' link in nav is clicked");
    }

    public boolean isAccountDetailsPresentKey(JsonNode dictionary){
        return getDictionaryElement(dictionary,DisneyWebKeys.ACCOUNT_DETAILS.getText()).isElementPresent();
    }

    public void clickOnEmailChangeTitleKey(JsonNode dictionary){
        getDictionaryElement(dictionary,DisneyWebKeys.EMAIL_CHANGE_TITLE.getText()).click();
        LOGGER.info("Change Email link is clicked");
    }

    public boolean isEmailChangeTitlePresentKey(JsonNode dictionary){
        return getDictionaryElement(dictionary,DisneyWebKeys.EMAIL_CHANGE_TITLE.getText()).isElementPresent();
    }

    public void clickOnCancelButtonKey(JsonNode dictionary){
        getDictionaryElement(dictionary,DisneyWebKeys.BTN_CANCEL.getText()).click();
        LOGGER.info("Cancel button is clicked");
    }

    public void clickOnChangePasswordKey(JsonNode dictionary){
        getDictionaryContainsIgnoreCaseElement(dictionary,DisneyWebKeys.CHANGE_PASSWORD.getText()).click();
        LOGGER.info("Change Password link is clicked");
    }

    public boolean isChangePasswordTitlePresentKey(JsonNode dictionary){
        return getDictionaryContainsIgnoreCaseElement(dictionary,DisneyWebKeys.CHANGE_PASSWORD.getText()).isElementPresent();
    }

    public void clickOnBillingDetailsKey(JsonNode dictionary){
        getDictionaryElement(dictionary,DisneyWebKeys.BILLING_DETAILS.getText()).click();
        LOGGER.info("Billing Details link is clicked");
    }

    public boolean isBillingDetailsPresentKey(JsonNode dictionary){
        return getDictionaryElement(dictionary,DisneyWebKeys.BILLING_DETAILS.getText()).isElementPresent();
    }

    public void clickOnCancelSubscriptionKey(JsonNode dictionary){
        getDictionaryElement(dictionary,DisneyWebKeys.SUBSCRIPTION_CANCEL_COPY.getText()).click();
        LOGGER.info("Cancel Subscription link is clicked");
    }

    public boolean isCancelSubscriptionTitlePresentKey(JsonNode dictionary){
        return getDictionaryElement(dictionary,DisneyWebKeys.SUBSCRIPTIONCANCEL_CONFIRMCANCELMSG_COPY.getText()).isElementPresent();
    }

    public void clickOnGoBackButton(JsonNode dictionary){
        getDictionaryElement(dictionary,DisneyWebKeys.BTN_SUBSCRIPTION_GOBACK.getText()).click();
        LOGGER.info("Go Back button is clicked");
    }

    public void clickOnChangePaymentInfoKey(JsonNode dictionary){
        getDictionaryElement(dictionary,DisneyWebKeys.SUBSCRIPTION_CHANGEPAYMENT_COPY.getText()).click();
        LOGGER.info("Change Payment Info link is clicked");
    }

    public boolean isChangePaymentInfoTitlePresentKey(JsonNode dictionary){
        return getDictionaryElement(dictionary,DisneyWebKeys.SUBSCRIPTION_CHANGEPAYMENT_COPY.getText()).isElementPresent();
    }

    public void clickOnBackToAccountKey(JsonNode dictionary){
        getDictionaryElement(dictionary,DisneyWebKeys.BTN_SUBSCRIPTION_BACKTOACCOUNT.getText()).click();
        LOGGER.info("Back To Account link is clicked");
    }

    public void addSplashProfileClick(JsonNode dictionary){
        getDictionaryElement(dictionary,DisneyWebKeys.ACCOUNT_ADD_BTN.getText()).click();
        LOGGER.info("Add Profile Button is clicked");
    }

    //Flow Methods

    public void createNewProfile(String profileName) {
        getActiveProfile();
        clickOnAddProfileFromAccount();
        selectAvatarForProfile();
        addProfileName(profileName);
        enterDOB(ProfileEligibility.ELIGIBLE_DOB);
        clickOnGenderDropdown();
        selectGender();
        clickOnSaveButton();
        new DisneyPlusCommercePage(getDriver()).clickCancelButton();
    }

    public String getProfileNameValue() {
        LOGGER.info("Get the value of profile name from profile name text box");
        return addProfileNameTextBox.getAttribute("value");
    }
}
