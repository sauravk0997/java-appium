package com.disney.qa.disney.apple.pages.common;

import com.amazonaws.services.applicationautoscaling.model.ObjectNotFoundException;
import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.DisneyAbstractPage;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.appletv.IRemoteControllerAppleTV;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.DriverHelper;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusApplePageBase extends DisneyAbstractPage implements IRemoteControllerAppleTV, IOSUtils {
    public static final String BABY_YODA = "f11d21b5-f688-50a9-8b85-590d6ec26d0c";
    protected static final String BRAND_NAME = "brand_name";
    public static final String MICKEY_MOUSE = "442af7db-85f7-5e1d-96f0-b2c517be4085";
    public static final String RAYA = "edb6c80b-9f97-5bf2-9c8f-b861feb2062e";
    public static final String ONLY_MURDERS_IN_THE_BUILDING = "Only Murders in the Building";
    public static final String PREY = "Prey";
    public static final String DEUTSCH = "Deutsch";
    public static final String ITALIANO = "Italiano";
    public static final String ENGLISH = "English";
    public static final String ENGLISH_CC = "English [CC]";
    public static final String ENGLISH_AUDIO_DESCRIPTION = "English [Audio Description]";
    public static final String OFF = "Off";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String DEVICE_TYPE = "capabilities.deviceType";
    private static final String TABLET = "Tablet";
    protected static final String USER_PROFILE = "user_profile";
    public static final String SEASON_NUMBER = "seasonNumber";
    private static final String SAVE_OVERRIDE = "SAVE OVERRIDE";
    private static final String REMOVE_OVERRIDE = "REMOVE OVERRIDE";
    private static final String NO_OVERRIDE_IN_USE = "NO override in use!";
    private static final String UPDATE_LATER = "Update Later";
    private static final String UPDATE_AVAILABLE = "An update is available";
    private static final String SET_TO_TRUE = "Set to: true";
    private static final String SET_TO_FALSE = "Set to: false";
    private static final String APPLE = "apple";
    private static final String PARTNER = "disney";
    private static final String APAC = "apac";
    private static final String KMRB = "kmrb";
    private static final String MPAA_AND_TVPG = "mpaaandtvpg";
    @FindBy(xpath = "%s")
    protected ExtendedWebElement dynamicXpath;
    @FindBy(xpath = "//*[@name='%s' or @name='%s']")
    protected ExtendedWebElement xpathNameOrName;
    @FindBy(xpath = "//*[@name='%s']")
    protected ExtendedWebElement xpathName;
    @ExtendedFindBy(accessibilityId = "%s")
    private ExtendedWebElement dynamicAccessibilityId;
    @ExtendedFindBy(iosClassChain = "%s")
    private ExtendedWebElement dynamicClassChain;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label == '%s'`]")
    protected ExtendedWebElement staticCellByLabel;
    private static DisneyLocalizationUtils disneyLanguageUtils = null;
    @ExtendedFindBy(accessibilityId = "Clear")
    public ExtendedWebElement keyboardClear;
    @ExtendedFindBy(accessibilityId = "unlockedProfileCell")
    public ExtendedWebElement unlockedProfileCell;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"%s\"`]")
    protected ExtendedWebElement staticTextByLabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`name == \"%s\"`]")
    protected ExtendedWebElement staticTextByName;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"%s\" or label == \"%s\"`]")
    protected ExtendedWebElement staticTextByLabelOrLabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label CONTAINS \"%s\"`]")
    protected ExtendedWebElement staticTextLabelContains;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeImage[`label CONTAINS \"%s\"`]")
    protected ExtendedWebElement imageLabelContains;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label CONTAINS \"%s\"`]")
    protected ExtendedWebElement typeCellLabelContains;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`name CONTAINS \"%s\"`]")
    protected ExtendedWebElement typeCellNameContains;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeImage[`label CONTAINS \"%s\"`]")
    private ExtendedWebElement dynamicIosClassChainElementTypeImage;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextView[`value == '%s'`]")
    protected ExtendedWebElement staticTypeTextViewValue;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextView[`value == \"%s\"`]")
    protected ExtendedWebElement staticTypeTextViewValueDoubleQuotes;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextView[`label == \"%s\"`]")
    protected ExtendedWebElement textViewByLabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextView[`label CONTAINS \"%s\"`]")
    protected ExtendedWebElement textViewByLabelContains;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextView[`name == \"%s\"`]")
    protected ExtendedWebElement textViewByName;
    @ExtendedFindBy(accessibilityId = "logoImage")
    protected ExtendedWebElement logoImage;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell")
    protected ExtendedWebElement cell;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView")
    protected ExtendedWebElement collectionView;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeLink[`label == '%s'`]")
    protected ExtendedWebElement customHyperlinkByLabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeLink[`label == '%s'`][%s]")
    protected ExtendedWebElement typeLinkRowLabel;
    @ExtendedFindBy(iosPredicate = "label == \"Address\"")
    protected ExtendedWebElement webviewUrlBar;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeSecureTextField")
    protected ExtendedWebElement secureTextEntryField;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextField")
    protected ExtendedWebElement textEntryField;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextField[`value == \"%s\"`]")
    private ExtendedWebElement textFieldValue;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextField[`name == \"%s\"`]")
    protected ExtendedWebElement dynamicTextEntryFieldByName;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextField[`label == \"%s\"`]")
    protected ExtendedWebElement dynamicTextEntryFieldByLabel;

    @ExtendedFindBy(accessibilityId = "secureTextFieldPassword")
    protected ExtendedWebElement passwordEntryField;
    @ExtendedFindBy(accessibilityId = "Password")
    protected ExtendedWebElement passwordFieldHint;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name == \"saveProfileButton\"`]")
    protected ExtendedWebElement saveBtn;
    @ExtendedFindBy(accessibilityId = "dismissButton")
    protected ExtendedWebElement dismissBtn;
    @ExtendedFindBy(accessibilityId = "homeTab")
    protected ExtendedWebElement homeTab;
    @ExtendedFindBy(accessibilityId = "searchTab")
    protected ExtendedWebElement searchTab;
    @ExtendedFindBy(accessibilityId = "downloadsTab")
    protected ExtendedWebElement downloadTab;
    @ExtendedFindBy(accessibilityId = "moreTab")
    protected ExtendedWebElement moreTab;
    @ExtendedFindBy(accessibilityId = "profileTab")
    protected ExtendedWebElement profileTab;
    @ExtendedFindBy(accessibilityId = "settingsTab")
    protected ExtendedWebElement settingsTab;
    @FindBy(xpath = "//XCUIElementTypeStaticText[@name=\"%s\"]")
    protected ExtendedWebElement staticTextLabelName;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label == \"%s\"`]")
    protected ExtendedWebElement dynamicCellByLabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`name == \"%s\"`]")
    protected ExtendedWebElement dynamicCellByName;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"%s\"`][%s]")
    protected ExtendedWebElement dynamicRowButtonLabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name CONTAINS \"%s\"`]")
    protected ExtendedWebElement dynamicBtnFindByNameContains;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`label == \"%s\"`][%s]")
    protected ExtendedWebElement dynamicRowOtherLabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"%s\"`]")
    protected ExtendedWebElement dynamicBtnFindByLabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name == \"%s\"`]")
    protected ExtendedWebElement dynamicBtnFindByName;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"%s\"`]")
    private ExtendedWebElement dynamicOtherFindByName;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`label CONTAINS \"%s\"`]")
    protected ExtendedWebElement dynamicOtherFindByLabelContains;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name CONTAINS \"%s\"`]")
    protected ExtendedWebElement dynamicOtherFindByNameContains;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`label == \"%s\"`]")
    private ExtendedWebElement dynamicOtherFindByLabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label CONTAINS \"%s\"`]")
    protected ExtendedWebElement dynamicBtnFindByLabelContains;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"tabBarView\"`]")
    private ExtendedWebElement globalNavBarView;

    @ExtendedFindBy(accessibilityId = "buttonBack")
    protected ExtendedWebElement backArrow;
    @ExtendedFindBy(accessibilityId = "headlineHeader")
    protected ExtendedWebElement headlineHeader;
    @ExtendedFindBy(accessibilityId = "headlineSubtitle")
    protected ExtendedWebElement headlineSubtitle;
    @ExtendedFindBy(accessibilityId = "labelErrorMessage")
    protected ExtendedWebElement labelError;
    @ExtendedFindBy(accessibilityId = "hidePasswordDisneyAuth")
    protected ExtendedWebElement showHidePasswordIndicator;
    @ExtendedFindBy(accessibilityId = "showPasswordDisneyAuth")
    protected ExtendedWebElement showPasswordIndicator;
    @ExtendedFindBy(accessibilityId = "collectionHeadlineTitle")
    protected ExtendedWebElement collectionHeadlineTitle;
    @ExtendedFindBy(accessibilityId = "Email")
    protected ExtendedWebElement emailField;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeWindow")
    protected ExtendedWebElement windowType;
    @ExtendedFindBy(accessibilityId = "actionableAlertTitle")
    protected ExtendedWebElement actionableAlertTitle;
    @ExtendedFindBy(accessibilityId = "actionableAlertMessage")
    protected ExtendedWebElement actionableAlertMessage;
    @ExtendedFindBy(accessibilityId = "alertAction:secondaryButton")
    protected ExtendedWebElement systemAlertSecondaryBtn;
    @ExtendedFindBy(accessibilityId = "alertAction:defaultButton")
    protected ExtendedWebElement systemAlertDefaultBtn;
    @ExtendedFindBy(accessibilityId = "alertAction:destructiveButton")
    protected ExtendedWebElement systemAlertDestructiveButton;
    @ExtendedFindBy(accessibilityId = "alertAction:cancelButton")
    protected ExtendedWebElement systemAlertDismissBtn;
    @ExtendedFindBy(accessibilityId = "CANCEL")
    protected ExtendedWebElement systemAlertCancelBtn;
    @ExtendedFindBy(accessibilityId = "LOG OUT")
    protected ExtendedWebElement systemAlertLogoutBtn;
    @ExtendedFindBy(accessibilityId = "primaryButton")
    protected ExtendedWebElement primaryButton;
    @ExtendedFindBy(accessibilityId = "Continue")
    protected ExtendedWebElement continueButton;
    @ExtendedFindBy(accessibilityId = "secondaryButton")
    protected ExtendedWebElement secondaryButton;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther")
    protected ExtendedWebElement typeOtherElements;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell")
    protected ExtendedWebElement typeCell;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeAlert")
    protected ExtendedWebElement typeSystemAlerts;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeAlert[`label == \"%s\"`]")
    protected ExtendedWebElement typeAlertByLabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton")
    protected ExtendedWebElement typeButtons;
    @ExtendedFindBy(accessibilityId = "Keyboard")
    protected ExtendedWebElement keyboard;
    @FindBy(xpath = "//*[contains(@type, 'XCUIElementTypeKeyboard')]")
    protected ExtendedWebElement localizedKeyboard;
    @ExtendedFindBy(accessibilityId = "delete")
    private ExtendedWebElement iPadKeyboardDelete;
    @ExtendedFindBy(accessibilityId = "Delete")
    private ExtendedWebElement iPhoneKeyboardDelete;
    @ExtendedFindBy(accessibilityId = "buttonLogout")
    protected ExtendedWebElement logoutButton;
    @ExtendedFindBy(accessibilityId = "customButton")
    protected ExtendedWebElement customButton;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextView")
    protected ExtendedWebElement typeTextView;
    @FindBy(xpath = "//*[contains(@name, \"%s\")]")
    private ExtendedWebElement dynamicXpathContainsName;
    @FindBy(xpath = "//*[contains(@label, \"%s\")]")
    private ExtendedWebElement dynamicXpathContainslabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[%s]/XCUIElementTypeCell[%s]")
    protected ExtendedWebElement dynamicRowColumnContent;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"done\"`]")
    private ExtendedWebElement keyboardDone;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"continue\"`]")
    private ExtendedWebElement keyboardContinue;
    @ExtendedFindBy(accessibilityId = "saveProfileButton")
    private ExtendedWebElement saveProfileButton;
    @ExtendedFindBy(accessibilityId = "viewAlert")
    private ExtendedWebElement viewAlert;

    @ExtendedFindBy(accessibilityId = "airingBadgeLabel")
    private ExtendedWebElement airingBadgeLabel;

    @ExtendedFindBy(accessibilityId = "Hide keyboard")
    private ExtendedWebElement hideKeyboard;

    @ExtendedFindBy(accessibilityId = "upNextHeaderLabel")
    private ExtendedWebElement upNextHeaderLabel;

    @ExtendedFindBy(accessibilityId = "thumbnailView")
    private ExtendedWebElement thumbnailView;

    @ExtendedFindBy(accessibilityId = "toggleView")
    private ExtendedWebElement toggleView;

    @ExtendedFindBy(accessibilityId = "buttonBack")
    protected ExtendedWebElement backButton;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Want to stay in the loop?\"`]")
    protected ExtendedWebElement notificationPopUp;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"Ask App Not to Track\"`]")
    protected ExtendedWebElement trackingPopUp;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"labelStepTitle\"`]/XCUIElementTypeStaticText")
    protected ExtendedWebElement stepTitle;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`name == \"labelStepTitle\"`]")
    protected ExtendedWebElement stepTitleText;

    @ExtendedFindBy(accessibilityId = "progressBar")
    private ExtendedWebElement progressBar;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`name CONTAINS \"%s\"`]")
    protected ExtendedWebElement staticTextNameContains;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[`name == '%s'`]/XCUIElementTypeCell")
    protected ExtendedWebElement collectionCellNoRow;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[`name == '%s'`]")
    protected ExtendedWebElement collectionCell;

    @ExtendedFindBy(accessibilityId = "brandLandingView")
    protected ExtendedWebElement brandLandingView;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"iconNavBack24Dark\"`]")
    protected ExtendedWebElement collectionBackButton;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"brandLandingView\"`]/XCUIElementTypeImage[1]")
    protected ExtendedWebElement artworkBackground;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == 'Address'`]")
    protected ExtendedWebElement tabletWebviewAddressBar;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextField[`label == 'Address'`]")
    protected ExtendedWebElement phoneWebviewAddressBar;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Stay up to date\"`]")
    protected ExtendedWebElement stayUpToDatePopup;
    @ExtendedFindBy(iosPredicate = "type == \"XCUIElementTypeKeyboard\"")
    private ExtendedWebElement keyboardByPredicate;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[`name == '%s'`]/XCUIElementTypeCell[1]")
    private ExtendedWebElement firstCellElementFromCollection;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[`name == '%s'`]/XCUIElementTypeCell[$label CONTAINS '%s,'$]")
    private ExtendedWebElement cellElementFromCollection;

    @ExtendedFindBy(accessibilityId = "iconNavBack24LightActive")
    protected ExtendedWebElement navBackButton;
    @ExtendedFindBy(accessibilityId = "Clear text")
    private ExtendedWebElement clearText;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeKey[`label == \"%s\"`]")
    private ExtendedWebElement typeKey;

    @ExtendedFindBy(accessibilityId = "cancelBarButton")
    private ExtendedWebElement cancelButton;

    public DisneyPlusApplePageBase(WebDriver driver) {
        super(driver);
    }

    public void waitForPresenceOfAnElement(ExtendedWebElement element) {
        fluentWait(getDriver(), DELAY, SHORT_TIMEOUT, "Element is not present").until(it -> element.isPresent(ONE_SEC_TIMEOUT));
    }

    public ExtendedWebElement getDynamicIosClassChainElementTypeImage(String label) {
        return dynamicIosClassChainElementTypeImage.format(label);
    }

    public void clickToggleView() {
        toggleView.click();
    }

    public String getStepTitleText() {
        return stepTitle.getText();
    }

    public String getStepTitleTextLabel() {
        return stepTitleText.getText();
    }

    public void tapBackButton() {
        fluentWait(getDriver(), LONG_TIMEOUT, SHORT_TIMEOUT, "Back button is not present").until(it -> backButton.isElementPresent(ONE_SEC_TIMEOUT));
        backButton.click();
    }

    public ExtendedWebElement getSystemAlertDestructiveButton() {
        return systemAlertDestructiveButton;
    }

    public ExtendedWebElement getXpathName(String name) {
        return xpathName.format(name);
    }

    public enum contentType {
        MOVIE, SERIES, EXTRAS
    }

    public static DisneyLocalizationUtils getDictionary() {
        return Objects.requireNonNull(disneyLanguageUtils);
    }

    public static void setDictionary(DisneyLocalizationUtils dictionary) {
        disneyLanguageUtils = dictionary;
    }

    public ExtendedWebElement getDynamicAccessibilityId(String id) {
        return dynamicAccessibilityId.format(id);
    }

    public ExtendedWebElement getDynamicClassChain(String classChain) {
        return dynamicClassChain.format(classChain);
    }

    public boolean isDynamicAccessibilityIDElementPresent(String id) {
        return dynamicAccessibilityId.format(id).isPresent();
    }

    public ExtendedWebElement getDynamicCellByLabel(String label) {
        return dynamicCellByLabel.format(label);
    }

    public ExtendedWebElement getDynamicCellByName(String name) {
        return dynamicCellByName.format(name);
    }

    public ExtendedWebElement getDynamicXpath(String path) {
        return dynamicXpath.format(path);
    }

    public ExtendedWebElement getDynamicXpathContainsName(String name) {
        return dynamicXpathContainsName.format(name);
    }

    public ExtendedWebElement getDynamicXpathContainsLabel(String label) {
        return dynamicXpathContainslabel.format(label);
    }

    public ExtendedWebElement getTypeButtonByLabel(String label) {
        return dynamicBtnFindByLabel.format(label);
    }

    public ExtendedWebElement getTypeButtonByName(String name) {
        return dynamicBtnFindByName.format(name);
    }

    public ExtendedWebElement getTypeOtherByName(String name) {
        return dynamicOtherFindByName.format(name);
    }

    public ExtendedWebElement getTypeOtherByLabel(String label) {
        return dynamicOtherFindByLabel.format(label);
    }

    public ExtendedWebElement getTypeButtonContainsLabel(String label) {
        return dynamicBtnFindByLabelContains.format(label);
    }

    public ExtendedWebElement getTypeOtherContainsLabel(String label) {
        return dynamicOtherFindByLabelContains.format(label);
    }

    public ExtendedWebElement getTypeOtherContainsName(String name) {
        return dynamicOtherFindByNameContains.format(name);
    }

    public ExtendedWebElement getStaticTextByLabel(String label) {
        return staticTextByLabel.format(label);
    }

    public ExtendedWebElement getStaticTextByName(String name) {
        return staticTextByName.format(name);
    }

    public ExtendedWebElement getStaticCellByLabel(String label) {
        return staticCellByLabel.format(label);
    }

    public ExtendedWebElement getStaticTextLabelName(String name) {
        return staticTextLabelName.format(name);
    }

    public ExtendedWebElement getTextViewByLabel(String label) {
        return textViewByLabel.format(label);
    }

    public ExtendedWebElement getTextViewByLabelContains(String label) {
        return textViewByLabelContains.format(label);
    }

    public ExtendedWebElement getTextViewByName(String name) {
        return textViewByName.format(name);
    }

    public ExtendedWebElement getStaticTextByLabelContains(String label) {
        return staticTextLabelContains.format(label);
    }
    public ExtendedWebElement getImageLabelContains(String label) {
        return imageLabelContains.format(label);
    }

    public ExtendedWebElement getStaticTextByNameContains(String name) {
        return staticTextNameContains.format(name);
    }

    public ExtendedWebElement getTypeCellLabelContains(String label) {
        return typeCellLabelContains.format(label);
    }

    public ExtendedWebElement getTypeCellNameContains(String name) {
        return typeCellNameContains.format(name);
    }

    public String getTextFromStaticTextByLabel(String label) {
        return getStaticTextByLabel(label).getText();
    }

    public String getHourMinFormatForDuration(int duration) {
        long hours = TimeUnit.MILLISECONDS.toHours(duration) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60;
        return String.format("%dh %dm", hours, minutes);
    }

    public ExtendedWebElement findByAccessibilityId(DisneyDictionaryApi.ResourceKeys resourceKey, DictionaryKeys key) {
        return dynamicAccessibilityId.format(getDictionary().getDictionaryItem(resourceKey, key.getText()));
    }

    public ExtendedWebElement findByFallbackAccessibilityId(DisneyDictionaryApi.ResourceKeys resourceKey, DictionaryKeys key) {
        return dynamicAccessibilityId.format(getDictionary().getDictionaryItem(resourceKey, key.getText(), false));
    }

    public static List<String> getEnumValues(DictionaryKeys... dictionaryValues) {
        return Arrays.stream(dictionaryValues).map(DictionaryKeys::getText).collect(Collectors.toList());
    }

    public static FluentWait<WebDriver> fluentWait(WebDriver driver, long timeOut, int polling, String message) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeOut))
                .pollingEvery(Duration.ofSeconds(polling))
                .withMessage(message)
                .ignoring(NoSuchElementException.class)
                .ignoring(TimeoutException.class);
    }

    public static FluentWait<WebDriver> fluentWaitNoMessage(WebDriver driver, long timeOut, int polling) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeOut))
                .pollingEvery(Duration.ofSeconds(polling))
                .ignoring(NoSuchElementException.class)
                .ignoring(TimeoutException.class);
    }

    public String getCollectionHeadlineTitleText() {
        return collectionHeadlineTitle.getText();
    }

    public String getActionableAlertTitle() {
        return actionableAlertTitle.getText();
    }

    public boolean isActionAlertTitlePresent() {
        return actionableAlertTitle.isElementPresent();
    }

    public boolean isHeadlineHeaderPresent() {
        return headlineHeader.isElementPresent();
    }

    public boolean isHeadlineSubtitlePresent() {
        return getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_CHANGE_EMAIL_BODY.getText())).isElementPresent();
    }

    public String getActionableAlertMessage() {
        return actionableAlertMessage.getText();
    }

    public ExtendedWebElement getDynamicRowButtonLabel(String label, int rowNum) {
        return dynamicRowButtonLabel.format(label, rowNum);
    }

    public ExtendedWebElement dynamicTypeLinkRowLabel(String label, int num) {
        return typeLinkRowLabel.format(label, num);
    }

    //Nav Items
    public ExtendedWebElement getHomeNav() {
        return homeTab;
    }

    public ExtendedWebElement getSearchNav() {
        return searchTab;
    }

    public ExtendedWebElement getProfileNav() {
        return profileTab;
    }

    public void clickProfileTab() {
        profileTab.click();
    }

    public ExtendedWebElement getDownloadNav() {
        return downloadTab;
    }

    public void clickSettingsTab() {
        settingsTab.click();
    }

    public ExtendedWebElement getOkButton() {
        return dynamicBtnFindByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.OK_BTN.getText()));
    }

    public void enterText(String text) {
        typeTextView.type(text);
    }

    public String getHeadlineHeaderText() {
        return headlineHeader.getText();
    }

    public boolean isHeadlineHeaderTextPresent(){
        return getStaticTextByLabelContains(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, MY_DISNEY_ENTER_EMAIL_HEADER.getText())).isPresent();
    }

    public String getErrorMessageLabelText() {
        String errorMessage = getElementText(labelError);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return errorMessage;
    }

    public boolean isAttributeValidationErrorMessagePresent() {
        return getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.ATTRIBUTE_VALIDATION.getText())).isPresent();
    }

    public boolean isAIDElementPresentWithScreenshot(String id) {
        boolean isPresent = dynamicAccessibilityId.format(id).isPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    /**
     * @return always returns false as this class does not represent an actual page in the app
     */
    @Override
    public boolean isOpened() {
        return false;
    }

    public ExtendedWebElement getBackArrow() {
        return backArrow;
    }

    public boolean isFocused(ExtendedWebElement element) {
        return element.getAttribute("focused").equalsIgnoreCase("true");
    }

    public void clickOpenButton() {
        ExtendedWebElement openButton = dynamicBtnFindByLabel.format("Open");
        openButton.isElementPresent(10);
        openButton.click();
    }

    public void keyPressTimes(Consumer<IRemoteControllerAppleTV> action, int times, int timeout) {
        IntStream.range(0, times).forEach(i -> {
            action.accept(this);
            pause(timeout);
        });
    }

    /**
     * @param startNum Specify beginning range number
     * @param endNum   Specify ending range number
     * @return - Returns how many cells per row
     */
    public boolean isContentShownCertainNumberPerRow(int startNum, int endNum) {
        List<Integer> pointList = new ArrayList<>();
        List<ExtendedWebElement> titles = findExtendedWebElements(cell.getBy());
        IntStream.range(startNum, endNum).forEach(i -> pointList.add(titles.get(i).getLocation().y));
        // removing first item from list since first item is hovered and that changes the Y co-ord value
        pointList.remove(0);
        return pointList.stream().distinct().count() <= 1;
    }

    public int getNumberOfItemsByCell() {
        List<ExtendedWebElement> titles = findExtendedWebElements(cell.getBy());
        cell.findExtendedWebElements(cell.getBy());
        return titles.size();
    }

    public List<String> getContentItems(int startNum) {
        waitForPresenceOfAnElement(cell);
        List<ExtendedWebElement> titlesElements = findExtendedWebElements(cell.getBy());
        List<String> titles = new ArrayList<>();
        IntStream.range(startNum, titlesElements.size()).forEach(i -> titles.add(titlesElements.get(i).getText()));
        return titles;
    }

    public List<String> getTextViewItems(int startNum) {
        List<ExtendedWebElement> titlesElements = findExtendedWebElements(typeTextView.getBy());
        List<String> titles = new ArrayList<>();
        IntStream.range(startNum, titlesElements.size()).forEach(i -> titles.add(titlesElements.get(i).getText()));
        return titles;
    }

    /**
     * Returns a list of all Cell element types that have a text value, not including those from the navigation
     * tray as they are a constant display with few exceptions.
     * Hardcoded 3 second pause is to give the app time to actually render cells on screen before gathering them.
     *
     * @return - The list of cell elements with Labels.
     */
    public List<ExtendedWebElement> getCellsWithLabels() {
        pause(SHORT_TIMEOUT);
        List<ExtendedWebElement> cells = findExtendedWebElements(cell.getBy());
        cells.removeIf(labeledCell -> labeledCell.getText().isEmpty()
                || Arrays.asList(getTabs()).contains(labeledCell.getAttribute("name")));
        return cells;
    }

    public void clickMenuTimes(int times, int timeout) {
        keyPressTimes(IRemoteControllerAppleTV::clickMenu, times, timeout);
    }

    public void moveDown(int times, int timeout) {
        keyPressTimes(IRemoteControllerAppleTV::clickDown, times, timeout);
    }

    public void moveUp(int times, int timeout) {
        keyPressTimes(IRemoteControllerAppleTV::clickUp, times, timeout);
    }

    public void moveLeft(int times, int timeout) {
        keyPressTimes(IRemoteControllerAppleTV::clickLeft, times, timeout);
    }

    public void moveRight(int times, int timeout) {
        keyPressTimes(IRemoteControllerAppleTV::clickRight, times, timeout);
    }

    public void clickContent(int carouselColumn, int tileRow) {
        dynamicRowColumnContent.format(carouselColumn, tileRow).click();
    }

    public boolean isDownloadInProgressDisplayed(DisneyLocalizationUtils dictionary, int numDownloads) {
        String downloadString;
        if (numDownloads > 1) {
            downloadString = dictionary.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.DOWNLOADS_IN_PROGRESS.getText());
        } else {
            downloadString = dictionary.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.DOWNLOAD_IN_PROGRESS_SINGLE.getText());
        }
        downloadString = downloadString.replace("${number_of_active_downloads}", String.valueOf(numDownloads));
        return dynamicXpathContainsName.format(downloadString).isElementPresent();
    }

    public boolean isStaticTextPresentWithScreenShot(String text) {
        boolean isPresent = (staticTextByLabel.format(text).isElementPresent() || textViewByLabel.format(text).isElementPresent());
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    public void clickHomeIcon() {
        getHomeNav().click();
    }

    public boolean isSearchDisplayed() {
        return getSearchNav().isElementPresent();
    }

    public void clickSearchIcon() {
        getSearchNav().click();
    }

    public boolean isDownloadsDisplayed() {
        return getDownloadNav().isElementPresent();
    }

    public void clickDownloadsIcon() {
        LOGGER.info("Wait for Downloads icon and click");
        getDownloadNav().clickIfPresent(30);
    }

    public void dismissChromecastAlert() {
        LOGGER.info("Checking for Chromecast alert");
        getOkButton().clickIfPresent();
    }

    public boolean isWebviewOpen() {
        return webviewUrlBar.isElementPresent();
    }

    public boolean continueButtonPresent() {
        return getTypeButtonByLabel("Continue").isElementPresent();
    }

    public void clickContinueBtn() {
        continueButton.click();
    }

    public String getWebviewUrl() {
        return webviewUrlBar.getText();
    }

    public void waitUntilWebviewUrlContains(String expectedText) {
        fluentWait(getDriver(), LONG_TIMEOUT, SHORT_TIMEOUT, "Webview URL did not contain the expected text...").until(it -> getWebviewUrl().contains(expectedText));
    }

    // Will take you to continue or done button on tvOS on screen keyboard
    public void moveToContinueOrDoneBtnKeyboardEntry() {
        keyPressTimes(getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
    }

    public ExtendedWebElement getManageWithMyDisneyButton() {
        return getStaticTextByLabelContains(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_MANAGE.getText()));
    }
    public void clickManageWithMyDisneyButton() {
        getManageWithMyDisneyButton().click();
    }

    public void moveToLocalizedKeyboard() {
        ExtendedWebElement keyboardContinueLocalized = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.KEYBOARD_CONTINUE.getText()).toLowerCase());
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE,"Email_Input_Screen");
        List<ExtendedWebElement> listOfOtherElements = findExtendedWebElements(typeOtherElements.getBy());
        if (keyboardContinueLocalized.isPresent()) {
            IntStream.range(0, listOfOtherElements.size()).forEach(i ->
                    moveDown(1, 1));
            clickSelect();
        }
    }

    public void selectLocalizedContinueBtnKeyboardEntry() {
        moveToLocalizedKeyboard();
        clickSelect();
    }

    public void selectContinueBtnOnKeyboardEntry() {
        moveToContinueOrDoneBtnKeyboardEntry();
        clickSelect();
    }

    public String[] getTabs() {
        return new String[]{FooterTabs.HOME.tabName, FooterTabs.SEARCH.tabName, FooterTabs.DOWNLOADS.tabName, FooterTabs.MORE_MENU.tabName};
    }

    public boolean isSaveBtnPresent() {
        return saveBtn.isElementPresent();
    }

    public void clickSaveBtn() {
        saveBtn.click();
    }

    public boolean isCancelBtnPresent() {
        return dismissBtn.isElementPresent();
    }

    public void clickCancelBtn() {
        dismissBtn.click();
    }

    public void clickAlertConfirm() {
        systemAlertDefaultBtn.click();
    }

    public boolean isPrimaryButtonPresent() {
        return primaryButton.isElementPresent();
    }

    public boolean isViewAlertPresent() {
        return viewAlert.isElementPresent();
    }

    public String getPrimaryButtonText() {
        return primaryButton.getText();
    }

    public void clickPrimaryButton() {
        primaryButton.click();
    }

    public void clickSecondaryButton() {
        secondaryButton.click();
    }

    /*
     * If element cannot be interacted with because of Welsh Pt 2.
     */
    public void clickSecondaryButtonByCoordinates() {
        clickElementAtLocation(secondaryButton, 50, 50);
    }

    public void clickPrimaryButtonByCoordinates() {
        clickElementAtLocation(primaryButton, 50, 50);
    }

    public boolean isAlertDefaultBtnPresent() {
        return systemAlertDefaultBtn.isElementPresent();
    }

    public void clickDefaultAlertBtn() {
        systemAlertDefaultBtn.click();
    }

    public boolean isAlertCancelBtnPresent() {
        return systemAlertCancelBtn.isElementPresent();
    }

    public void dismissUnexpectedErrorAlert() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        List<ExtendedWebElement> listOfTypeButtons = findExtendedWebElements(typeButtons.getBy());
        if (typeSystemAlerts.isElementPresent(15)) {
            IntStream.range(0, listOfTypeButtons.size()).forEach(i ->
                    moveDown(1, 1));
            clickSelect();
        }
    }

    public String getSystemAlertText() {
        return typeSystemAlerts.getText();
    }

    public Consumer<IRemoteControllerAppleTV> getClickActionBasedOnLocalizedKeyboardOrientation() {
        localizedKeyboard.isPresent();
        return localizedKeyboard.getSize().getWidth() > 1000 ?
                IRemoteControllerAppleTV::clickDown : IRemoteControllerAppleTV::clickRight;
    }

    public boolean isLocalizedPageWithPrimaryButtonOpened() {
        //Many localized pages have primaryButton, use this method since typical isOpened does not suffice on localized version
        return primaryButton.isElementPresent(15);
    }

    public boolean isCustomButtonPresent() {
        return customButton.isPresent();
    }

    public boolean isAlertDismissBtnPresent() {
        return systemAlertDismissBtn.isElementPresent();
    }

    public void clickAlertDismissBtn() {
        systemAlertDismissBtn.click();
    }

    public void clickCancelAlertBtn() {
        systemAlertCancelBtn.click();
    }

    public void clickLogoutAlertBtn() {
        systemAlertLogoutBtn.click();
    }

    public void clickLogoutButtonIfHasFocus() {
        if (isFocused(logoutButton)) {
            logoutButton.click();
        }
    }

    public String getParsedString(ExtendedWebElement element, String part, String regex) {
        String[] elementParts = element.getText().split(regex);
        return elementParts[Integer.parseInt(part)];
    }

    public void clickLogoutButton() {
        logoutButton.click();
    }

    public boolean isLogOutBtnPresent() {
        return logoutButton.isElementPresent();
    }

    public void clickSaveProfileButton() {
        dynamicBtnFindByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, BTN_ADD_PROFILE_SAVE.getText())).click();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
    }

    public void clickTypeButton() {
        typeButtons.click();
    }

    public void clickMoreTab() {
        moreTab.click();
    }

    public By getTypeButtonBy() {
        return typeButtons.getBy();
    }

    public boolean isPasswordEntryFieldPresent(long timeout) {
        return passwordEntryField.isElementPresent(timeout);
    }

    public void enterPassword(DisneyAccount account) {
        passwordEntryField.type(account.getUserPass());
        clickPrimaryButton();
    }

    public void enterPasswordNoAccount(String password) {
        passwordEntryField.type(password);
        clickPrimaryButton();
    }

    public boolean doesAiringBadgeContainLive() {
        return airingBadgeLabel.getText().toLowerCase().contains("live");
    }

    public boolean doesAttributeEqualTrue(ExtendedWebElement element, String name) {
        return element.getAttribute(name).equalsIgnoreCase("true");
    }

    public void dismissKeyboardIfIpadLandscapeDetected() {
        if (detectDevice(DeviceType.Type.IOS_TABLET) && detectOrientation(ScreenOrientation.LANDSCAPE)) {
            hideKeyboard.clickIfPresent();
        }
    }

    public boolean isUpNextHeaderPresent() {
        return upNextHeaderLabel.isElementPresent();
    }

    public void dismissNotificationsPopUp() {
        if (notificationPopUp.isPresent(5) || stayUpToDatePopup.isPresent(5)) {
            getStaticTextByLabel("Not Now").click();
        }
    }

    public void dismissAppTrackingPopUp() {
        trackingPopUp.clickIfPresent();
    }

    public void dismissAppTrackingPopUp(int timeout) {
        trackingPopUp.clickIfPresent(timeout);
    }

    public boolean isThumbnailViewPresent() {
        return thumbnailView.isPresent();
    }

    public void clickThumbnailView() {
        thumbnailView.click();
    }

    public enum FooterTabs {
        HOME("homeTab"),
        SEARCH("searchTab"),
        DOWNLOADS("downloadsTab"),
        MORE_MENU("moreTab");

        private final String tabName;

        FooterTabs(String tabName) {
            this.tabName = tabName;
        }

        public String getLocator() {
            return this.tabName;
        }
    }

    public void goBackToDisneyAppFromSafari() {
        Dimension size = getDriver().manage().window().getSize();
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase("Phone")) {
            LOGGER.info("tapping on the left corner of the phone to go back to the Disney app");
            pause(1);
            tapAtCoordinateNoOfTimes((int) (size.width * 0.2), (int) (size.height * 0.04), 1);
        } else {
            tapAtCoordinateNoOfTimes((int) (size.width * 0.04), (int) (size.height * 0.01), 1);
        }
    }

    public boolean verifyTextOnWebView(String text) {
        return staticTextLabelContains.format(text).isPresent(SHORT_TIMEOUT);
    }

    public void dismissPickerWheelKeyboard() {
        if (R.CONFIG.get(DEVICE_TYPE).equals(TABLET)) {
            hideKeyboard();
        } else {
            getTypeButtonByLabel("Done").click();
        }
    }

    public void clickSystemAlertSecondaryBtn() {
        systemAlertSecondaryBtn.click();
    }

    public ExtendedWebElement getSystemAlertDefaultBtn() {
        return systemAlertDefaultBtn;
    }

    public boolean isGlobalNavPresent() {
        return globalNavBarView.isElementPresent();
    }

    public boolean isGlobalNavExpanded() {
        if (globalNavBarView.isElementPresent(DELAY)) {
            Dimension size = globalNavBarView.getSize();
            int x = size.getWidth();
            LOGGER.info("Detecting if global nav is expanded..");
            Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
            return x > 200;
        }
        return false;
    }

    public ExtendedWebElement getStaticTextByLabelOrLabel(String value, String dictionaryKeyValue) {
        return staticTextByLabelOrLabel.format(value, dictionaryKeyValue);
    }

    public ExtendedWebElement getDynamicTextEntryFieldByName(String name) {
        return dynamicTextEntryFieldByName.format(name);
    }

    public ExtendedWebElement getTextEntryField() {
        return textEntryField;
    }

    /**
     * @param min     session to be kept alive for these many minutes
     * @param element check on this element to make sure session is alive
     */
    public void keepSessionAlive(int min, ExtendedWebElement element) {
        LOGGER.info("pausing session for {} mins", min);
        int pauseInterval = 15;
        int upperbound = min * 60 / pauseInterval;
        AtomicInteger count = new AtomicInteger(0);
        IntStream.range(0, upperbound).forEach(i -> {
            pause(pauseInterval);
            count.addAndGet(pauseInterval);
            Assert.assertTrue(element.isPresent(),
                    String.format("Element was not present after %d seconds elapsed.", count.get()));
        });
    }

    public ExtendedWebElement getAiringBadgeLabel() {
        return airingBadgeLabel;
    }

    public ExtendedWebElement getProgressBar() {
        return progressBar;
    }

    public void validateLiveProgress(SoftAssert sa) {
        if (getStaticTextByLabelContains("Started").isPresent()) {
            String[] liveProgressMinutes = getStaticTextByLabelContains("Started").getText().split("Started ");
            String[] minutes = liveProgressMinutes[1].split(" ");
            String liveProgress = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, LIVE_PROGRESS.getText()),
                    Map.of("x", Integer.valueOf(minutes[0])));
            sa.assertTrue(getDynamicAccessibilityId(liveProgress).isPresent(), "'Live Progress' was not present");
        } else if (getStaticTextByLabelContains("Started at").isPresent()) {
            String[] liveProgressTimeMinutes = getStaticTextByLabelContains("Started at").getText().split("at");
            String liveProgressTime = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, LIVE_PROGRESS_TIME.getText()),
                    Map.of("x", Integer.valueOf(liveProgressTimeMinutes[1])));
            sa.assertTrue(getDynamicAccessibilityId(liveProgressTime).isPresent(), "'Live Progress Time' was not present.");
        }
    }

    /**
     * Below are identifiers / methods to support temp setup of iOS/tvOS tests by disabling flexWelcomeConfig
     * To be deprecated when IOS-7629 is fixed
     */
    private static final String COMPATIBLE_DISNEY = "Compatible Disney+";
    @FindBy(xpath = "//XCUIElementTypeStaticText[@label=\"%s\"]/ancestor::XCUIElementTypeCell")
    private ExtendedWebElement config;

    public boolean isCompatibleDisneyTextPresent() {
        return staticTextLabelContains.format(COMPATIBLE_DISNEY).isElementPresent();
    }

    public ExtendedWebElement scrollToItem(String item) {
        ExtendedWebElement override = getStaticTextByLabel(item);
        swipe(override);
        return override;
    }

    public void saveDomainIdentifier(String value) {
        pause(5);
        if (getStaticTextByLabelContains("Current override set to: ").isPresent()) {
            LOGGER.info("Domain identifier override is already set..");
        } else {
            textEntryField.type(value);
            getTypeButtonByLabel(SAVE_OVERRIDE).click();
            Assert.assertTrue(getStaticTextByLabelContains("Current override set to: ").isPresent());
        }
    }

    public void removeDomainIdentifier() {
        pause(5);
        if (getStaticTextByLabelContains("No override set").isPresent()) {
            LOGGER.info("Domain identifier override is already removed..");
        } else {
            getTypeButtonByLabel(REMOVE_OVERRIDE).click();
            Assert.assertTrue(getStaticTextByLabelContains("No override set").isPresent());
        }
    }

    public void enableOneTrustConfig() {
        pause(5);
        if (getStaticTextByLabelContains("default value of true").isPresent() //to accommodate jarvis bug
                || getStaticTextByLabelContains(SET_TO_TRUE).isPresent()) {
            LOGGER.info("isEnabledV2 is already enabled to true..");
        } else {
            LOGGER.info("Enabling oneTrustConfig isEnableV2 config..");
            clickToggleView();
            Assert.assertTrue(getStaticTextByLabelContains(SET_TO_TRUE).isPresent());
        }
    }

    public void disableOneTrustConfig() {
        pause(5);
        if (getStaticTextByLabelContains(NO_OVERRIDE_IN_USE).isPresent()) {
            LOGGER.info("oneTrustConfig isEnabledV2 config does not have any override in use..");
        } else {
            LOGGER.info("Disabling oneTrustConfig isEnableV2 config..");
            getTypeButtonByLabel(REMOVE_OVERRIDE).click();
            Assert.assertTrue(getStaticTextByLabelContains(NO_OVERRIDE_IN_USE).isPresent());
        }
    }

    public void disableBrazeConfig() {
        Assert.assertTrue(getTypeButtonByLabel("brazeConfig").isPresent(), "Braze config not found");
        if (getStaticTextByLabelContains("default value of true").isPresent(SHORT_TIMEOUT) //to accommodate jarvis bug
                || getStaticTextByLabelContains(SET_TO_TRUE).isPresent(SHORT_TIMEOUT)) {
            LOGGER.info("disabling brazeConfig isEnable config..");
            clickToggleView();
            Assert.assertTrue(getStaticTextByLabelContains(SET_TO_FALSE).isPresent());
        } else {
            LOGGER.info("brazeConfig is already disabled..");
        }
    }

    public void clickConfig(String appConfig) {
        clickItemWhileMovingDown(config.format(appConfig));
    }

    public void clickItemWhileMovingDown(ExtendedWebElement element) {
        fluentWait(getDriver(), 300L, 0, "Unable to find Config ").until(it -> {
            if (element.isVisible(1L)) {
                return true;
            } else {
                moveDown(5, 0);
                return false;
            }
        });
        element.click();
    }

    public ExtendedWebElement getElementTypeCellByLabel(String labelText) {
        String cellFormatLocator = "type == 'XCUIElementTypeCell' and label contains '%s'";
        return findExtendedWebElement(AppiumBy.iOSNsPredicateString(String.format(cellFormatLocator, labelText)));
    }

    public void detectAppleUpdateAndClickUpdateLater() {
        if (staticTextLabelContains.format(UPDATE_AVAILABLE).isPresent(5)) {
            LOGGER.info("Dismissing Apple Update alert by clicking {}", UPDATE_LATER);
            moveDown(2, 1);
            LOGGER.info("Is {} focused? {}", UPDATE_LATER, isFocused(dynamicBtnFindByLabelContains.format(UPDATE_LATER)));
            clickSelect();
        }
    }

    public boolean isElementEnabled(ExtendedWebElement element) {
        try {
            String locator = element.getBy().toString();
            DisneyPlusApplePageBase.fluentWait(getDriver(), DriverHelper.EXPLICIT_TIMEOUT, 1, String.format("Element [%s] is NOT enabled", locator))
                    .until(it -> element.getElement().isEnabled());
        } catch (TimeoutException ex) {
            return false;
        }
        return true;
    }

    public ExtendedWebElement getUnavailableOkButton() {
        return dynamicBtnFindByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_ERROR_MEDIAUNAVAILABLE.getText()));
    }

    public ExtendedWebElement getUnavailableContentError() {
        return typeAlertByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.ERROR_COLLECTION_UNAVAILABLE.getText()));
    }

    public ExtendedWebElement getTypeAlertByLabel(String label){
        return typeAlertByLabel.format(label);
    }
    /**
     * Select random tile, scroll to specific collection, then selects random tile
     *
     * @param collection gets collection name from enum Collection
     * @param count      swipe collection for number of times
     * @param direction  Up or Down homeContentView
     */
    public void clickRandomCollectionTile(CollectionConstant.Collection collection, int count, ExtendedWebElement container, Direction direction) {
        swipeTillCollectionPresent(collection, count, container, direction);
        getAllCollectionCells(collection).get(new SecureRandom().nextInt(getAllCollectionCells(collection).size())).click();
    }

    public List<ExtendedWebElement> getAllCollectionCells(CollectionConstant.Collection collection) {
        return findExtendedWebElements(collectionCellNoRow.format(CollectionConstant.getCollectionName(collection)).getBy());
    }


    /**
     * Navigate to collection and clicks a tile in collection.
     *
     * @param collection gets collection name from enum Collection
     * @param count      number of times to swipe
     * @param container  container view - input 'null' if desire to be left empty.
     * @param direction  Up or Down
     */
    public void swipeTillCollectionPresent(CollectionConstant.Collection collection, int count, ExtendedWebElement container, Direction direction) {
        while (collectionCell.format(CollectionConstant.getCollectionName(collection)).isElementNotPresent(SHORT_TIMEOUT) && count >= 0) {
            swipeInContainer(container, direction, 1, 1200);
            count--;
        }
    }

    public void swipeTillCollectionPresent(CollectionConstant.Collection collection, int count) {
        swipeTillCollectionPresent(collection, count, brandLandingView, Direction.UP);
    }

    /**
     * Navigate to collection and clicks a tile in collection.
     * @param collection gets collection name from enum Collection
     * @param num        select a tile starting with 0. Typically only first 3 are visible on handset, or first 4-5 on tablet.
     */
    public void clickCollectionTile(CollectionConstant.Collection collection, int num) {
        List<ExtendedWebElement> tiles = findExtendedWebElements(collectionCellNoRow.format(CollectionConstant.getCollectionName(collection)).getBy());
        clickElementAtLocation(tiles.get(num), 50, 50);
    }

    public boolean isCollectionPresent(CollectionConstant.Collection collection) {
        return getCollection(collection).isPresent();
    }

    public ExtendedWebElement getUnavailableContentErrorPreview() {
        return typeAlertByLabel.format("Sorry, content you are trying to access is not currently available. You will be redirected to Disney+ Home.");
    }

    public void swipeInHuluBrandPage(Direction direction) {
        swipeInContainer(brandLandingView, direction, 500);
    }


    public ExtendedWebElement getCollection(CollectionConstant.Collection collection) {
        return collectionCell.format(CollectionConstant.getCollectionName(collection));
    }

    public ExtendedWebElement getCollection(String collectionId) {
        return collectionCell.format(collectionId);
    }

    public void swipeLeftInCollection(ExtendedWebElement element) {
        Point elementLocation = element.getLocation();
        Dimension elementDimensions = element.getSize();
        int endY;
        int startY = endY = elementLocation.getY() + Math.round(elementDimensions.getHeight() / 2.0F);
        int startX = (int) (elementLocation.getX() + Math.round(0.8 * elementDimensions.getWidth()));
        int endX = (int) (elementLocation.getX() + Math.round(0.25 * elementDimensions.getWidth()));
        this.swipe(startX, startY, endX, endY, 500);
    }

    public void swipeLeftInCollection(CollectionConstant.Collection collection) {
        ExtendedWebElement collectionElement = getCollection(collection);
        Point elementLocation = collectionElement.getLocation();
        Dimension elementDimensions = collectionElement.getSize();

        int endY;
        int startY = endY = elementLocation.getY() + Math.round(elementDimensions.getHeight() / 2.0F);
        int startX = (int) (elementLocation.getX() + Math.round(0.8 * elementDimensions.getWidth()));
        int endX = (int) (elementLocation.getX() + Math.round(0.25 * elementDimensions.getWidth()));

        this.swipe(startX, startY, endX, endY, 500);
    }

    public void swipeLeftInCollectionNumOfTimes(int number, CollectionConstant.Collection collection) {
        int count = number;
        while (count >= 0) {
            swipeLeftInCollection(collection);
            count--;
        }
    }

    public void swipeRightInCollection(CollectionConstant.Collection collection) {
        ExtendedWebElement collectionElement = getCollection(collection);
        Point elementLocation = collectionElement.getLocation();
        Dimension elementDimensions = collectionElement.getSize();

        int startY;
        int endY = startY = elementLocation.getY() + Math.round(elementDimensions.getHeight() / 2.0F);
        int startX = (int) (elementLocation.getX() + Math.round(0.25 * elementDimensions.getWidth()));
        int endX = (int) (elementLocation.getX() + Math.round(0.8 * elementDimensions.getWidth()));


        this.swipe(startX, startY, endX, endY, 500);
    }

    public void swipeRightInCollectionNumOfTimes(int number, CollectionConstant.Collection collection) {
        int count = number;
        while (count >= 0) {
            swipeRightInCollection(collection);
            count--;
        }
    }

    public boolean validateScrollingInCollections(CollectionConstant.Collection collection) {
        swipePageTillElementPresent(getCollection(collection), 10, brandLandingView, Direction.UP, 500);
        List<ExtendedWebElement> titles1 = getAllCollectionCells(collection);
        swipeLeftInCollection(collection);
        List<ExtendedWebElement> titles2 = getAllCollectionCells(collection);
        return titles1 != titles2;
    }

    public boolean isBackButtonPresent() {
        return collectionBackButton.isPresent();
    }

    public boolean isArtworkBackgroundPresent() {
        return artworkBackground.isPresent();
    }

    public void clickOnCollectionBackButton() {
        collectionBackButton.click();
    }

    public ExtendedWebElement getBackButton() { return backButton; }

    public void clickBackArrow() {
         backArrow.click();
    }

    public boolean isDownloadsTabDisplayed() { return downloadTab.isPresent(); }

    public ExtendedWebElement getUnavailableContentErrorPopUpMessage() {
        // This element has hardcoded the text in the app and there is not a dictionary key with the same content
        return  findExtendedWebElement(AppiumBy.iOSClassChain("**/XCUIElementTypeTextView[`label == \"Sorry, this content is unavailable. If the problem continues, visit our Help Center at disneyplus.com/content-unavailable.\"`]"));
    }

    public boolean isUnavailableContentErrorPopUpMessageIsPresent() {
        return getUnavailableContentErrorPopUpMessage().isPresent();
    }

    public ExtendedWebElement getKeyboardDelete() {
        if (iPhoneKeyboardDelete.isPresent()) {
            return iPhoneKeyboardDelete;
        } else {
            return iPadKeyboardDelete;
        }
    }

    public ExtendedWebElement getPinProtectedProfileIcon(String name) {
        return getDynamicAccessibilityId(
                getDictionary().formatPlaceholderString(
                        getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PIN_PROFILE.getText()), Map.of(USER_PROFILE, name)));
    }

    public ExtendedWebElement getCellPinProtectedProfileIcon(String name) {
        return dynamicCellByLabel.format(
                getDictionary().formatPlaceholderString(
                        getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PIN_PROFILE.getText()), Map.of(USER_PROFILE, name)));
    }

    public boolean isPinProtectedProfileIconPresent(String name) {
        if (getCellPinProtectedProfileIcon(name).isPresent(SHORT_TIMEOUT)) {
            return getCellPinProtectedProfileIcon(name).isPresent();
        } else {
            return getPinProtectedProfileIcon(name).isPresent();
        }
    }

    public ExtendedWebElement getKeyboardByPredicate() {
        return keyboardByPredicate;
    }

    public ExtendedWebElement getMoreMenuTab() {
        return moreTab;
    }

    public String getFirstCellTitleFromContainer(CollectionConstant.Collection collection) {
        return firstCellElementFromCollection.format(CollectionConstant.getCollectionName(collection)).getText();
    }

    public ExtendedWebElement getCellElementFromContainer(CollectionConstant.Collection collection, String title){
        return cellElementFromCollection.format(CollectionConstant.getCollectionName(collection), title);
    }

    public boolean isRatingPresent(String rating) {
        return getStaticTextByLabelContains(rating).isPresent();
    }

    public ExtendedWebElement getNavBackArrow() {
        return navBackButton;
    }
    public boolean isCollectionViewScreenScrollableVertically(ExtendedWebElement firstCollection, ExtendedWebElement secondCollection, ExtendedWebElement container) {
        List<ExtendedWebElement> titles1 = findExtendedWebElements(firstCollection.getBy(), SHORT_TIMEOUT);
        swipePageTillElementPresent(secondCollection, 3, container, Direction.UP, 500);
        List<ExtendedWebElement> titles2 = findExtendedWebElements(secondCollection.getBy(), SHORT_TIMEOUT);
        return !titles1.equals(titles2);
    }

    public boolean isCollectionViewScrollableHorizontally(int startNum, int index) {
        List<String> titles1 = getContentItems(startNum);
        swipeLeftInCollection(getCollectionRowInView(index));
        List<String> titles2 = getContentItems(startNum);
        return !titles1.equals(titles2);
    }

    public List<ExtendedWebElement> getCollectionViews() {
        List<ExtendedWebElement> collectionViews;
        if (collectionView.isPresent()) {
            collectionViews = findExtendedWebElements(collectionView.getBy());
        } else {
            throw new ObjectNotFoundException("Collection view not present.");
        }
        return collectionViews;
    }

    public ExtendedWebElement getCollectionRowInView(int index) {
        ExtendedWebElement collectionRowInView = null;
        try {
            collectionRowInView = getCollectionViews().get(index);
        } catch (IndexOutOfBoundsException e) {
            Assert.fail(String.format("Index out of bounds: %s", e));
        }
        return collectionRowInView;
    }

    public ExtendedWebElement getTypeKey(String num) {
        return typeKey.format(num);
    }

    //format: Month, day, year
    public void enterDOB(DateHelper.Month month, String day, String year) {
        setBirthDate(DateHelper.localizeMonth(month, getDictionary()), day, year);
        dismissPickerWheelKeyboard();
    }

    public ExtendedWebElement getClearTextBtn() {
        return clearText;
    }

    public ExtendedWebElement getTextFieldValue(String value) {
        return textFieldValue.format(value);
    }

    public void clickCancelButton() {
        cancelButton.click();
    }
}
