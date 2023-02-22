package com.disney.qa.common.google_play_store.android.pages.common;

import com.disney.qa.common.DisneyAbstractPage;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.qaprosoft.carina.core.foundation.utils.android.AndroidService;
import com.qaprosoft.carina.core.foundation.utils.resources.L10N;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import junit.framework.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GooglePlayHandler extends DisneyAbstractPage {

    @FindBy(xpath = "//*[@content-desc='{L10N:googleMenuButton}']")
    protected ExtendedWebElement hamburgerMenu;

    @FindBy(className = "android.widget.EditText")
    protected ExtendedWebElement playStoreInterfaceInput;

    @FindBy(className = "android.widget.EditText")
    protected ExtendedWebElement searchBarTextBox;

    @FindBy(xpath = "//android.widget.TextView[@text='%s']")
    protected ExtendedWebElement suggestedText;

    @FindBy(xpath = "//android.widget.Button[@text='{L10N:googleInstallButton}']")
    protected ExtendedWebElement installButton;

    @FindBy(xpath = "//android.widget.Button[@text='{L10N:googleOpenButton}']")
    protected ExtendedWebElement openButton;

    @FindBy(xpath = "//*[@text=\"%s\"]")
    protected ExtendedWebElement genericText;

    @FindBy(xpath = "//*[contains(@text, \"%s\")]")
    protected ExtendedWebElement genericTextContains;

    @FindBy(className = "android.widget.ImageButton")
    protected ExtendedWebElement backButton;

    @FindBy(xpath = "//android.widget.Button[@text=\"{L10N:googleRemoveButton}\"]")
    protected List<ExtendedWebElement> removeExpiredSubscriptionButtons;

    @FindBy(className = "android.widget.RadioButton")
    protected List<ExtendedWebElement> cancelOptions;

    @FindBy(className = "android.widget.RadioButton")
    protected ExtendedWebElement radioButton;

    @FindBy(xpath = "//android.widget.Button[@text='{L10N:googleContinueButton}']")
    protected ExtendedWebElement continueButton;

    @FindBy(xpath = "//android.widget.Button[@text='{L10N:googleNoThanks}']")
    protected ExtendedWebElement noThanksButton;

    @FindBy(xpath = "//android.widget.Button[@text='{L10N:googleNotNow}']")
    protected ExtendedWebElement notNowButton;

    @FindBy(className = "android.widget.Button")
    protected ExtendedWebElement purchaseInterfaceButton;

    @FindBy(className = "android.widget.TextView")
    protected ExtendedWebElement genericTextView;

    @FindBy(xpath = "//*[contains(@content-desc, '{L10N:googleAccountSettings}')]")
    protected ExtendedWebElement menuIcon;

    @FindBy(xpath = "//*[contains(@text, '{L10N:googleTryAgain}')]")
    protected ExtendedWebElement retryButton;

    @FindBy(xpath = "//*[contains(@content-desc, '%s')]")
    protected ExtendedWebElement contentDescContains;

    AndroidUtilsExtended util = new AndroidUtilsExtended();

    public GooglePlayHandler(WebDriver driver) {
        super(driver);
    }

    public enum localizationKeys {
        MENU_ICON("{L10NgoogleMenuButton}"),
        REMOVE_EXPIRED_SUB("{L10NgoogleRemoveButton}"),
        MENU_SUBSCRIPTION("{L10NgoogleMenuSubscriptionsItem}"),
        SUBMENU_SUBSCRIPTION("{L10NgoogleSubMenuSubscriptions}"),
        TEST_CARD_APPROVED("{L10NgoogleTestCardApproved}"),
        CANCEL_SUBSCRIPTION("{L10NgoogleCancelSubscription}"),
        PAUSE_TEXT("{L10NgooglePauseSubscription}"),
        CANCEL_BUTTON("{L10NgoogleCancelButton}"),
        NO_THANKS_TEXT("{L10NgoogleNoThanks}"),
        SEARCH_BAR_TEXT("{L10NgoogleSearchBar}"),
        CANCELED("{L10NgoogleCanceled}"),
        ADS("{L10NgoogleAds}");

        private String keyValue;

        localizationKeys(String key) {
            this.keyValue = key;
        }

        public String getTextKey() {
            String localizedText = this.keyValue;
            if (localizedText.contains("{L10N")) {
                String key = localizedText.replace("{L10N", "").replace("}", "");
                localizedText = L10N.getText(key);
            }
            return localizedText;
        }
    }

    public boolean isOpened(){
        return menuIcon.isElementPresent();
    }

    public boolean openPlayStore(){
        AndroidService androidService = AndroidService.getInstance();
        androidService.executeAdbCommand("shell pm clear com.android.vending");
        String output = "";
        int tries = 0;
        do {
            tries++;
            output = androidService.executeAdbCommand("shell am start -n com.android.vending/com.android.vending.AssetBrowserActivity");
        } while (output.isEmpty() && tries < 10);
        return isOpened() && verifyStoreConnection();
    }

    public boolean verifyStoreConnection() {
        int tries = 0;
        if(contentDescContains.format(localizationKeys.ADS.getTextKey()).isElementPresent()) {
            return true;
        } else {
            LOGGER.info("Suggested carousel was not displayed. Clicking Try Again button up to 3x...");
            try {
                do {
                    tries++;
                    retryButton.click();
                } while (tries < 3);
            } catch (NoSuchElementException e) {
                return true;
            }
            return contentDescContains.format(localizationKeys.ADS.getTextKey()).isElementPresent();
        }
    }

    //Cancels active subscriptions for provided app and all expired sub listings
    public void clearAppSubscriptionListings(String app){
        if(isAppSubscriptionActive(app)){
            cancelAppSubscription(app);
        } else if(areExpiredSubsVisible()){
            removeExpiredSubscriptions();
        }
    }

    public boolean isAppSubscriptionActive(String app){
        menuIcon.click();
        genericTextContains.format(localizationKeys.MENU_SUBSCRIPTION.getTextKey()).click();
        genericText.format(localizationKeys.SUBMENU_SUBSCRIPTION.getTextKey()).click();
        if(genericText.format(localizationKeys.CANCELED.getTextKey()).isElementPresent()) {
            return false;
        } else {
            return genericTextContains.format(app).isElementPresent() && removeExpiredSubscriptionButtons.isEmpty();
        }
    }

    public boolean areExpiredSubsVisible(){
        return !removeExpiredSubscriptionButtons.isEmpty();
    }

    //Double click due to Google Play using the same element for the popup prompt after selecting Remove
    public void removeExpiredSubscriptions(){
        for(ExtendedWebElement removeButton : removeExpiredSubscriptionButtons){
            LOGGER.info("Removing expired sub...");
            removeButton.click();
            ExtendedWebElement removeConfirm = genericText.format(localizationKeys.REMOVE_EXPIRED_SUB.getTextKey());
            waitUntil(ExpectedConditions.visibilityOfElementLocated(removeConfirm.getBy()), 60);
            removeConfirm.click();
        }
        returnToMainMenu();
    }

    public void cancelAppSubscription(String app){
        String cancelText = localizationKeys.CANCEL_SUBSCRIPTION.getTextKey();
        genericTextContains.format(app).click();
        util.swipe(genericText.format(cancelText));
        try {
            genericText.format("Cancel").click();
        } catch (NoSuchElementException nse) {
            genericText.format(cancelText).click();
        }
        if(genericText.format(localizationKeys.PAUSE_TEXT.getTextKey()).isElementPresent()){
            noThanksButton.click();
        }
        waitUntil(ExpectedConditions.visibilityOfElementLocated(radioButton.getBy()), LONG_TIMEOUT);
        cancelOptions.get(cancelOptions.size()-1).click();
        continueButton.click();
        Instant billingCycle = getTimeToBillingCycle();
        genericText.format(cancelText).click();
        returnToMainMenu();
        LOGGER.info("Subscription cancelled. Waiting for Google Play billing cycle ({} seconds)...", Duration.between(Instant.now(), billingCycle).toSeconds());

        while (Instant.now().isBefore(billingCycle)) {
            isOpened();
        }

        LOGGER.info("Cycle completed. Proceed with test.");
    }

    /**
     * Gets the billing cycle time as an Instant
     * Regex pattern looks for the date as displayed in Google Play (Sep 6, 2022, 11:35 AM EDT)
     * @return - Instant based on regex parse + 1 minute due to lack of seconds data
     */
    //TODO: This should be updated at some point to support various Local/Language settings and not just US English
    private Instant getTimeToBillingCycle() {
        Instant expiryCycle = Instant.now().plus(5, ChronoUnit.MINUTES);
        String regex = "(\\w+\\s+\\d*,+\\s+\\d{4},+\\s+\\d*:+\\d+\\s\\w{2}+\\s+\\w{3})";
        Matcher matcher = Pattern.compile(regex).matcher(genericTextContains.format("Your subscription will be canceled").getText());
        if(matcher.find()) {
            expiryCycle = LocalDateTime.parse(matcher.group(1), DateTimeFormatter.ofPattern("MMM d, yyyy, h:mm a z", Locale.US))
                    .atZone(ZoneId.of("America/New_York"))
                    .toInstant();
        } else {
            LOGGER.warn("Regex parse for billing cycle time failed. Defaulting to 5 minutes from now.");
        }
        return expiryCycle.plus(1, ChronoUnit.MINUTES);
    }

    public void installStoreBuild(String app){
        genericText.format(localizationKeys.SEARCH_BAR_TEXT.getTextKey()).click();
        playStoreInterfaceInput.type(app);
        suggestedText.format(app).click();
        genericText.format("BAMTECH LLC").click();
        installButton.click();

        waitUntil(ExpectedConditions.attributeToBe(openButton.getBy(), "enabled", "true"), 60);
    }

    public boolean verifyAccountIsWhitelisted(){
        return genericText.format(localizationKeys.TEST_CARD_APPROVED.getTextKey()).isElementPresent();
    }

    public void approvePurchase() {
        if (verifyAccountIsWhitelisted()) {
            purchaseInterfaceButton.click();
            radioButton.click();
            purchaseInterfaceButton.click();
            if (radioButton.isElementPresent()) {
                radioButton.click();
                purchaseInterfaceButton.click();
            }
        } else {
            Assert.fail("FAIL: WHITELIST CONFIRMATION NOT PRESENT.");
        }
    }

    public void returnToMainMenu(){
        while (backButton.isElementPresent()){
            backButton.click();
        }
    }

    public void clickNowNowBtnIfPresent() {
        notNowButton.clickIfPresent();
    }
}
