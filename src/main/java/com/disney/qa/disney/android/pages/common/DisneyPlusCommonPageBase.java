package com.disney.qa.disney.android.pages.common;

import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.common.DisneyAbstractPage;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.resources.L10N;
import com.qaprosoft.carina.core.foundation.webdriver.Screenshot;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.List;

import static com.qaprosoft.carina.core.foundation.utils.mobile.IMobileUtils.Direction.DOWN;
import static com.qaprosoft.carina.core.foundation.utils.mobile.IMobileUtils.Direction.UP;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public abstract class DisneyPlusCommonPageBase extends DisneyAbstractPage {

    private static final int ANDROID_SWIPE_UP_DURATION_MILLIS = 5000;

    protected static final String PLATFORM = "android";
    protected static final String PARTNER = "partner";

    @FindBy(className = "android.widget.TextView")
    public ExtendedWebElement genericTextItemType;

    @FindBy(xpath = "//*[contains(@text, \"%s\")]")
    public ExtendedWebElement genericTextElement;

    @FindBy(id = "shelfTitle")
    protected ExtendedWebElement shelfTitle;

    @FindBy(id= "appLogo")
    private ExtendedWebElement disneyPlusLogo;

    @FindBy(xpath = "//*[@text='%s']")
    public ExtendedWebElement genericTextElementExact;

    @FindBy(id = "menuNavigation")
    private ExtendedWebElement menuBox;

    @FindBy(xpath = "//*[contains(@content-desc, \"%s\")]/android.widget.ImageView")
    private ExtendedWebElement navIcon;

    @FindBy(id = "profileImageFocus")
    private ExtendedWebElement moreMenunButton;

    @FindBy(xpath = "//*[@content-desc=\"%s\"]")
    private ExtendedWebElement contentDescExact;

    @FindBy(id = "inputErrorTextView")
    protected ExtendedWebElement errorTextView;

    @FindBy(xpath = "//*[contains(@content-desc, '%s')]")
    protected ExtendedWebElement contentDescContains;

    @FindBy(id = "successTitle")
    private ExtendedWebElement launchBanner;

    @FindBy(xpath = "//*[contains(@text, \"%s\")]")
    private ExtendedWebElement genericTextSnippet;

    @FindBy(id = "backButton")
    private ExtendedWebElement genericBackButton;

    @FindBy(id = "blackBackButton")
    private ExtendedWebElement blackBackButton;

    @FindBy(id = "com.disney.disneyplus:id/action_bar_root")
    private ExtendedWebElement appRootDisplay;

    @FindBy(id = "title")
    protected ExtendedWebElement title;

    @FindBy(id = "standardButtonContainer")
    protected ExtendedWebElement standardButton;

    @FindBy(id="disneyLogo")
    private ExtendedWebElement disneyLogo;

    @FindBy(id = "noConnectionRetry")
    public ExtendedWebElement retryButton;

    @FindBy(id = "bottomClickView")
    public ExtendedWebElement bottomClickView;

    @FindBy(id = "positiveButton")
    protected ExtendedWebElement confirmButton;

    @FindBy(id = "positive_button")
    private ExtendedWebElement errorDialogConfirmButton;

    @FindBy(id = "neutralButton")
    private ExtendedWebElement neutralButton;

    @FindBy(id = "negativeButton")
    private ExtendedWebElement cancelButton;

    @FindBy(id = "titleDialog")
    private ExtendedWebElement errorDialog;

    @FindBy(id = "dialogLayout")
    public ExtendedWebElement errorDialogBox;

    @FindBy(id = "messageDialog")
    protected ExtendedWebElement errorDialogMessage;

    @FindBy(id = "tier2DialogTitle")
    protected ExtendedWebElement tierTwoTitle;

    @FindBy(id = "tier2DialogSubtitle")
    protected ExtendedWebElement tierTwoSubtitle;

    @FindBy(id = "tier2DialogFirstButton")
    protected ExtendedWebElement tierTwoButtonOne;

    @FindBy(id = "tier2DialogSecondButton")
    protected ExtendedWebElement tierTwoSecondButton;

    @FindBy(className = "android.widget.EditText")
    public ExtendedWebElement editTextByClass;

    @FindBy(id = "editFieldEditText")
    protected ExtendedWebElement editTextField;

    @FindBy(id = "inputHintTextView")
    protected ExtendedWebElement textViewEditText;

    @FindBy(className = "android.widget.ProgressBar")
    private ExtendedWebElement loadSpinner;

    @FindBy(id = "cast_button")
    private ExtendedWebElement externalChromecastButton;

    @FindBy(className = "android.widget.Button")
    public ExtendedWebElement commonGenericButton;

    @FindBy(xpath = "//*[contains(@text, \"%s\")]/following-sibling::*[@class='android.widget.TextView']")
    private ExtendedWebElement metaData;

    @FindBy(id = "continueWatchingItemContainer")
    protected ExtendedWebElement continueWatchingItemContainer;

    @FindBy(id = "downloadBadge")
    protected ExtendedWebElement downloadBadge;

    @FindBy(id = "actionButton")
    private ExtendedWebElement actionButton;

    @FindBy(id = "lockImageView")
    private ExtendedWebElement lockIcon;

    @FindBy(xpath = "//*[contains(@content-desc, \"%s\")]/android.widget.TextView")
    private ExtendedWebElement whosWatchingProfileName;

    @FindBy(xpath = "//*[contains(@content-desc, \"%s\")]/*[@resource-id='com.disney.disneyplus:id/lockIconImage']")
    private ExtendedWebElement whosWatchingProfileLock;

    @ExtendedFindBy(accessibilityId = "%s")
    private ExtendedWebElement genericAccessibilityidElement;

    @FindBy(id = "inputShowPwdImageView")
    private ExtendedWebElement showPwdToggle;

    @FindBy(id = "inputDescriptionTextView")
    private ExtendedWebElement caseSensitiveSubcopy;

    @FindBy(id = "forgotPwdPinTitle")
    private ExtendedWebElement forgotPwdHeader;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/pinCodeConstraintLayout']/android.widget.TextView")
    private ExtendedWebElement forgotPwdNumbers;

    @FindBy(id = "content_title")
    protected ExtendedWebElement contentTitle;

    @FindBy(id = "positive_button")
    protected ExtendedWebElement positiveButton;

    @FindBy(id = "positiveButton")
    protected ExtendedWebElement travelingDialogOkButton;

    @FindBy(id = "shelfItemLayout")
    protected ExtendedWebElement shelfItem;

    @FindBy(id = "titleTextSwitcher")
    private ExtendedWebElement titleTextSwitcher;

    private static ThreadLocal<DisneyLocalizationUtils> languageUtils;

    public static final String CONTENT_DESC = "content-desc";

    public DisneyPlusCommonPageBase(WebDriver driver) {
        super(driver);
    }

    public static DisneyLocalizationUtils getDictionary() {
        return languageUtils.get();
    }

    public static void setDictionary(DisneyLocalizationUtils dictionary) {
        if (languageUtils == null) {
            languageUtils = new ThreadLocal<>();
        } else {
            languageUtils.remove();
        }

        languageUtils.set(dictionary);
    }

    @Override
    public boolean isOpened() {
        return false;
    }

    public boolean isTitleTextSwitcherPresent() { return titleTextSwitcher.isElementPresent(); }

    public void clickTitleTextSwitcherTextView() { titleTextSwitcher.click(); }

    public enum MenuItem{
        DISCOVER("nav_home"),
        DOWNLOADS("nav_downloads"),
        SEARCH("nav_search"),
        MORE("mobilenav_more");

        private String menuTitle;

        MenuItem(String title) {
            this.menuTitle = title;
        }

        public String getText() {
            String menu = this.menuTitle;
            if (menu.contains("{L10N:")) {
                String key = menu.replace("{L10N:", "").replace("}", "");
                menu = L10N.getText(key);
            }
            return menu;
        }
    }

    public enum WhosWatchingKeys{
        WHOS_WATCHING_TITLE("choose_profile_title"),
        EDIT_PROFILES("edit_profile_title"),
        ADD_PROFILE("create_profile_add_profile"),
        ACCESS_PROFILE("accessibility_whoswatching_selectprofile"),
        ACCESS_PIN_PROTECTED_PROFILE("accessibility_whoswatching_selectprofile_pin");

        private String key;
        WhosWatchingKeys(String key){
            this.key = key;
        }

        public String getKey(){
            return this.key;
        }
    }

    public void waitForLoading(){
        waitForLoading(30, false);
    }

    public void waitForLoading(int timeout){
        waitForLoading(timeout, false);
    }

    public void waitForLoading(boolean withLoadDelay){
        waitForLoading(30, withLoadDelay);
    }

    public void waitForLoading(int timeout, boolean withLoadDelay){
        if(withLoadDelay){
            waitUntil(ExpectedConditions.visibilityOfElementLocated(loadSpinner.getBy()), timeout);
        }
        waitUntil(ExpectedConditions.invisibilityOfElementLocated(loadSpinner.getBy()), timeout);
    }

    public boolean isLoadSpinnerDisplayed() {
        return loadSpinner.isElementPresent();
    }

    public boolean isMenuDisplayed(){
        return menuBox.isElementPresent();
    }

    public void clickOnMenuBox(){
        menuBox.click();
    }

    public void tapAwayToCloseModal() {
        bottomClickView.click();
    }

    public boolean isMenuItemSelected(String menuItem){
        return (getCastedDriver().findElement(navIcon.format(menuItem).getBy()).isSelected());
    }

    public void navigateToPage(String page){
         waitForSuccessPopup();
         navIcon.format(page).click();
    }

    public void navigateToSettings(){
        waitForSuccessPopup();
        moreMenunButton.click();
    }

    public void waitForSuccessPopup(){
        waitUntil(ExpectedConditions.invisibilityOfElementLocated(launchBanner.getBy()), DELAY);
    }

    public boolean isTextElementPresent(String text){
        return genericTextElement.format(text).isElementPresent();
    }

    public boolean isTextElementExactPresent(String text){
        return genericTextElementExact.format(text).isElementPresent(DELAY);
    }

    public boolean isTextSnippetPresent(String text){
        return genericTextSnippet.format(text).isElementPresent();
    }

    public void clickOnGenericTextElement(String text){
        genericTextElement.format(text).click();
    }

    public void clickOnContentDescContains(String text){
        contentDescContains.format(text).click();
    }

    public void clickOnContentDescExact(String text){
        contentDescExact.format(text).click();
    }

    public boolean isContentDescElementPresent(String contentDesc){
        return contentDescExact.format(contentDesc).isElementPresent();
    }

    public boolean isDisneyPlusLogoPresent(){
        return disneyPlusLogo.isElementPresent();
    }

    public void clickOnGenericTextExactElement(String text){
        //Default timeout value of 30 seconds
        clickOnGenericTextExactElement(text, 30);
    }

    public void clickOnGenericTextExactElement(String text, int timeout){
        waitUntil(ExpectedConditions.visibilityOfElementLocated(genericTextElementExact.format(text).getBy()), timeout);
        genericTextElementExact.format(text).click();
    }

    public boolean isGenericBackButtonPresent(){
        return genericBackButton.isElementPresent();
    }

    public void clickBackButton() {
        genericBackButton.click();
    }

    public void tapBlackBackButton(){
        blackBackButton.click();
    }

    public boolean isAppVisible(){
        return appRootDisplay.isElementPresent();
    }

    public boolean isDisneyLogoPresent(){
        Screenshot.capture(getDriver()," ");
        return disneyLogo.isElementPresent();
    }

    public boolean isEditTextFieldPresent(){
        return editTextField.isElementPresent();
    }

    public String getTextFieldText(){
        return getElementText(editTextField);
    }

    public String getEditTextFieldText(){
        return editTextField.getText();
    }

    public boolean scrollAndClickGenericTextElement(String text){
       boolean textFound = false;
       if(new AndroidUtilsExtended().swipe(genericTextElement.format(text))){
           genericTextElement.format(text).click();
           textFound = true;
       }

       return textFound;
    }

    public void swipeToItem(String header){
        LOGGER.info("Swiping to '{}'", header);
        new AndroidUtilsExtended().swipe(genericTextElement.format(header));
    }

    public void swipeToContentDesc(String contentDesc) {
        LOGGER.info("Swiping to '{}'", contentDesc);
        new AndroidUtilsExtended().swipe(contentDescExact.format(contentDesc));
    }

    public void swipeUpOnScreen(int swipeCount) {
        LOGGER.info("Performing '{}' up swipes on screen", swipeCount);
        new AndroidUtilsExtended().swipeInContainer(appRootDisplay, UP, swipeCount, ANDROID_SWIPE_UP_DURATION_MILLIS);
    }

    public void swipeDownOnScreen(int swipeCount) {
        LOGGER.info("Performing '{}' up swipes on screen", swipeCount);
        new AndroidUtilsExtended().swipeInContainer(appRootDisplay, DOWN, swipeCount, ANDROID_SWIPE_UP_DURATION_MILLIS);
    }

    public boolean isErrorButtonPresent(){
        return errorDialogConfirmButton.isElementPresent();
    }

    public boolean isStandardButtonPresent() {
        return standardButton.isElementPresent();
    }

    public boolean isTextViewStringDisplayed(String text) {
        List<WebElement> textViewList = getDriver().findElements(By.className("android.widget.TextView"));
        for (WebElement textViewElement : textViewList) {
            if (textViewElement.getText().equals(text)) {
                return true;
            }
        }
        LOGGER.info("{} TextView text is not displayed", text);
        return false;
    }

    public void clickConfirmButton(){
        confirmButton.click();
    }

    public String getConfirmButtonText() {
        return getElementText(confirmButton);
    }

    public void clickConfirmIfPresent() {
        waitForLoading(true);
        standardButton.clickIfPresent();
    }

    public String getCancelButtonText(){
        return getElementText(cancelButton);
    }

    public void clickCancelButton(){
        cancelButton.click();
    }

    public String getNeutralButtonText(){
        return neutralButton.getText();
    }

    public void clickNeutralButton(){
        neutralButton.click();
    }

    public boolean isErrorDialogPresent(){
        return errorDialog.isElementPresent();
    }

    public boolean isErrorPresent(){
        return isErrorPresent(30);
    }

    public boolean isErrorPresent(int delay) {
        return errorDialogBox.isElementPresent(delay);
    }

    public boolean isStar() {
        return R.CONFIG.get(PARTNER).equals("star");
    }

    public String getErrorDialogText(){
        return getElementText(errorDialog);
    }

    public String getErrorMessageText(){
        return getElementText(errorDialogMessage);
    }

    public String getTierTwoButtonText(){
        return tierTwoSecondButton.getText();
    }

    public boolean isDownloadBadgePresent(){
        return downloadBadge.isElementPresent();
    }

    public String getDownloadBadgeValue() {
        return downloadBadge.getText();
    }

    public String getStandardMediaListingMetadataRating(String title){
        new AndroidUtilsExtended().scroll(title, appRootDisplay);
        return metaData.format(title).getText().split(" ")[0];
    }

    public void dismissSmartLock(){
        if(genericTextElement.format("Smart lock").isElementPresent()){
            genericTextElement.format("NEVER").click();
        }
    }

    public void stopCasting(){
        externalChromecastButton.click();
        commonGenericButton.click();
    }

    public void clickStandardButton(){
        standardButton.click();
    }

    public ExtendedWebElement getMetaData(){
        return metaData;
    }

    public ExtendedWebElement getShelfItem() {
        return shelfItem;
    }

    public boolean isContinueWatchingDisplayed() { return continueWatchingItemContainer.isVisible(); }

    public ExtendedWebElement getAppRootDisplay(){
        return appRootDisplay;
    }

    public ExtendedWebElement getEditTextByClass() {
        return editTextByClass;
    }

    public boolean isGenericTextPresent(String text){
        return genericTextElement.format(text).isElementPresent();
    }

    public boolean isProfileVisible(String profileName){
        return whosWatchingProfileName.format(profileName).isElementPresent();
    }

    public boolean isProfileLockVisible(String profileName){
        return whosWatchingProfileLock.format(profileName).isElementPresent(SHORT_TIMEOUT);
    }

    public void selectWhosWatchingProfile(String profileName){
        whosWatchingProfileName.format(profileName).click();
    }

    public ExtendedWebElement getActionButton() {
        return actionButton;
    }

    public String getActionButtonText(){
        return actionButton.getText();
    }

    public void clickActionButton(){
        actionButton.click();
    }

    public boolean isLockIconVisible(){
        return lockIcon.isElementPresent();
    }

    public ExtendedWebElement getGenericAccessibilityidElement(String accessibilityId) {
        return genericAccessibilityidElement.format(accessibilityId);
    }

    public ExtendedWebElement getShowPwdToggle() {
        return showPwdToggle;
    }

    public ExtendedWebElement getCaseSensitiveSubcopy() {
        return caseSensitiveSubcopy;
    }

    public ExtendedWebElement getForgotPwdHeader() {
        return forgotPwdHeader;
    }

    public int getForgotPwdInputTotal() {
        return findExtendedWebElements(forgotPwdNumbers.getBy()).size();
    }

    public boolean isTierTwoTitleOpen(){
        return tierTwoTitle.isElementPresent();
    }

    public boolean isStandardButtonContainerDisplayed() { return standardButton.isElementPresent(); }

    public String invalidEmailInputErrorMessage() { return errorTextView.getText(); }

    public void clickPositiveButton() { positiveButton.click(); }

    public void dismissTravelingDialog() { travelingDialogOkButton.clickIfPresent(FORTY_FIVE_SEC_TIMEOUT); }

    public static FluentWait<WebDriver> fluentWait(WebDriver driver, long timeOut, int polling, String message) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeOut))
                .pollingEvery(Duration.ofSeconds(polling))
                .withMessage(message)
                .ignoring(NoSuchElementException.class)
                .ignoring(TimeoutException.class);
    }

    public boolean isVirtualKeyboardVisible(AndroidUtilsExtended androidUtilsExtended) {
        return fluentWait(getDriver(), HALF_TIMEOUT, ONE_SEC_TIMEOUT, "Error attempting to detect virtual keyboard")
                .until(it -> androidUtilsExtended.isKeyboardShown());
    }
}
