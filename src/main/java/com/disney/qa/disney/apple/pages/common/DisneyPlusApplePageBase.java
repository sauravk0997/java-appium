package com.disney.qa.disney.apple.pages.common;

import com.amazonaws.services.applicationautoscaling.model.ObjectNotFoundException;
import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.pojos.*;
import com.disney.qa.common.DisneyAbstractPage;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.common.utils.helpers.IAPIHelper;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.appletv.IRemoteControllerAppleTV;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.awt.image.BufferedImage;
import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusApplePageBase extends DisneyAbstractPage implements IRemoteControllerAppleTV, IOSUtils, IAPIHelper {
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
    protected static final String PLACEHOLDER_E = "E";
    protected static final String DEVICE = "DEVICE";
    public static final String HULU_SERVICE_ATTRIBUTION_MESSAGE = "Included with your Hulu subscription";

    @FindBy(xpath = "%s")
    protected ExtendedWebElement dynamicXpath;
    @FindBy(xpath = "//*[@name='%s' or @name='%s']")
    protected ExtendedWebElement xpathNameOrName;
    @FindBy(xpath = "//*[@name='%s']")
    protected ExtendedWebElement xpathName;
    @ExtendedFindBy(accessibilityId = "%s")
    private ExtendedWebElement dynamicAccessibilityId;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label == '%s'`]")
    protected ExtendedWebElement staticCellByLabel;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell")
    protected ExtendedWebElement cell;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell")
    protected ExtendedWebElement typeCell;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label CONTAINS \"%s\"`]")
    protected ExtendedWebElement typeCellLabelContains;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`name CONTAINS \"%s\"`]")
    protected ExtendedWebElement typeCellNameContains;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label == \"%s\"`]")
    protected ExtendedWebElement dynamicCellByLabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`name == \"%s\"`]")
    protected ExtendedWebElement dynamicCellByName;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"%s\"`]")
    protected ExtendedWebElement staticTextByLabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"%s\" or label == \"%s\"`]")
    protected ExtendedWebElement staticTextByLabelOrLabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label CONTAINS \"%s\"`]")
    protected ExtendedWebElement staticTextLabelContains;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label MATCHES \"%s\"`]")
    protected ExtendedWebElement staticTextLabelMatches;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`value CONTAINS \"%s\"`]")
    private ExtendedWebElement staticTextValueContains;
    @FindBy(xpath = "//XCUIElementTypeStaticText[@name=\"%s\"]")
    protected ExtendedWebElement staticTextLabelName;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`name == \"%s\"`]")
    protected ExtendedWebElement staticTextByName;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`name CONTAINS \"%s\"`]")
    protected ExtendedWebElement staticTextNameContains;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton")
    protected ExtendedWebElement typeButtons;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"%s\"`][%s]")
    protected ExtendedWebElement dynamicRowButtonLabel;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"%s\"`]")
    protected ExtendedWebElement dynamicBtnFindByLabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"%s\" or label == \"%s\"`]")
    protected ExtendedWebElement dynamicBtnFindByLabelOrLabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label CONTAINS \"%s\"`]")
    protected ExtendedWebElement dynamicBtnFindByLabelContains;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name == \"%s\"`]")
    protected ExtendedWebElement dynamicBtnFindByName;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name CONTAINS \"%s\"`]")
    protected ExtendedWebElement dynamicBtnFindByNameContains;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"done\"`]")
    private ExtendedWebElement keyboardDone;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"continue\"`]")
    private ExtendedWebElement keyboardContinue;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name == \"saveProfileButton\"`]")
    protected ExtendedWebElement saveBtn;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"iconNavBack24Dark\"`]")
    protected ExtendedWebElement collectionBackButton;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"Ask App Not to Track\"`]")
    protected ExtendedWebElement trackingPopUp;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == 'Address'`]")
    protected ExtendedWebElement tabletWebviewAddressBar;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeImage[`label CONTAINS \"%s\"`]")
    protected ExtendedWebElement imageLabelContains;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeImage[`label CONTAINS \"%s\"`]")
    private ExtendedWebElement dynamicIosClassChainElementTypeImage;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextView[`value == '%s'`]")
    protected ExtendedWebElement staticTypeTextViewValue;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextView[`value CONTAINS \"%s\"`]")
    protected ExtendedWebElement staticTypeTextViewValueContains;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextView[`value == \"%s\"`]")
    protected ExtendedWebElement staticTypeTextViewValueDoubleQuotes;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextView[`label == \"%s\"`]")
    protected ExtendedWebElement textViewByLabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextView[`label CONTAINS \"%s\"`]")
    protected ExtendedWebElement textViewByLabelContains;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextView[`name == \"%s\"`]")
    protected ExtendedWebElement textViewByName;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView")
    protected ExtendedWebElement collectionView;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeLink[`label == '%s'`]")
    protected ExtendedWebElement customHyperlinkByLabel;
    @ExtendedFindBy(iosPredicate = "label == \"Address\"")
    protected ExtendedWebElement webviewUrlBar;
    @ExtendedFindBy(accessibilityId = "logoImage")
    protected ExtendedWebElement logoImage;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeSecureTextField")
    protected ExtendedWebElement secureTextEntryField;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextField")
    protected ExtendedWebElement textEntryField;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextField[`value == \"%s\"`]")
    private ExtendedWebElement textFieldValue;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextField[`name == \"%s\"`]")
    protected ExtendedWebElement dynamicTextEntryFieldByName;
    @ExtendedFindBy(accessibilityId = "secureTextFieldPassword")
    protected ExtendedWebElement passwordEntryField;
    @ExtendedFindBy(accessibilityId = "Password")
    protected ExtendedWebElement passwordFieldHint;
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

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"%s\"`]")
    private ExtendedWebElement dynamicOtherFindByName;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`label CONTAINS \"%s\"`]")
    protected ExtendedWebElement dynamicOtherFindByLabelContains;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name CONTAINS \"%s\"`]")
    protected ExtendedWebElement dynamicOtherFindByNameContains;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`label == \"%s\"`]")
    private ExtendedWebElement dynamicOtherFindByLabel;
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
    @ExtendedFindBy(accessibilityId = "actionableAlertTitle")
    protected ExtendedWebElement actionableAlertTitle;
    @ExtendedFindBy(accessibilityId = "alertAction:secondaryButton")
    protected ExtendedWebElement systemAlertSecondaryBtn;
    @ExtendedFindBy(accessibilityId = "alertAction:defaultButton")
    protected ExtendedWebElement systemAlertDefaultBtn;
    @ExtendedFindBy(accessibilityId = "alertAction:destructiveButton")
    protected ExtendedWebElement systemAlertDestructiveButton;
    @ExtendedFindBy(accessibilityId = "alertAction:cancelButton")
    protected ExtendedWebElement systemAlertDismissBtn;
    @ExtendedFindBy(accessibilityId = "primaryButton")
    protected ExtendedWebElement primaryButton;
    @ExtendedFindBy(accessibilityId = "Continue")
    protected ExtendedWebElement continueButton;
    @ExtendedFindBy(accessibilityId = "secondaryButton")
    protected ExtendedWebElement secondaryButton;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther")
    protected ExtendedWebElement typeOtherElements;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeAlert")
    protected ExtendedWebElement typeSystemAlerts;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeAlert[`label == \"%s\"`]")
    protected ExtendedWebElement typeAlertByLabel;
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
    protected ExtendedWebElement dynamicXpathContainsName;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[%s]/XCUIElementTypeCell[%s]")
    protected ExtendedWebElement dynamicRowColumnContent;
    @ExtendedFindBy(accessibilityId = "saveProfileButton")
    private ExtendedWebElement saveProfileButton;
    @ExtendedFindBy(accessibilityId = "viewAlert")
    protected ExtendedWebElement viewAlert;
    @ExtendedFindBy(accessibilityId = "buttonForgotPassword")
    protected ExtendedWebElement forgotPasswordBtn;

    @ExtendedFindBy(accessibilityId = "airingBadgeLabel")
    private ExtendedWebElement airingBadgeLabel;

    @ExtendedFindBy(accessibilityId = "headerViewTitleLabel")
    protected ExtendedWebElement headerViewTitleLabel;

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

    @FindBy(xpath = "//XCUIElementTypeStaticText[@label=\"%s\"]/ancestor::XCUIElementTypeCell")
    private ExtendedWebElement config;

    @ExtendedFindBy(accessibilityId = "progressBar")
    private ExtendedWebElement progressBar;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[`name == '%s'`]/XCUIElementTypeCell")
    protected ExtendedWebElement collectionCellNoRow;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[`name == '%s'`]")
    protected ExtendedWebElement collectionCell;

    @ExtendedFindBy(accessibilityId = "highEmphasisView")
    protected ExtendedWebElement brandLandingView;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"highEmphasisView\"`]/XCUIElementTypeImage[1]")
    protected ExtendedWebElement artworkBackground;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextField[`label == 'Address'`]")
    protected ExtendedWebElement phoneWebviewAddressBar;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Stay up to date\"`]")
    protected ExtendedWebElement stayUpToDatePopup;
    @ExtendedFindBy(iosPredicate = "type == \"XCUIElementTypeKeyboard\"")
    private ExtendedWebElement keyboardByPredicate;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[`name == '%s'`]/XCUIElementTypeCell[1]")
    protected ExtendedWebElement firstCellElementFromCollection;
    @ExtendedFindBy(iosClassChain =
            "**/XCUIElementTypeCollectionView[`name == '%s'`]/XCUIElementTypeCell[1]/" +
                    "**/XCUIElementTypeOther[`name == 'progressBar'`]")
    private ExtendedWebElement firstCellElementFromCollectionProgressBar;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[`name == '%s'`]/XCUIElementTypeCell[1]/" +
            "XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[1]/" +
            "XCUIElementTypeOther[1]/XCUIElementTypeImage[1]")
    private ExtendedWebElement firstCellElementFromCollectionAssetImage;
    @ExtendedFindBy(iosClassChain =
            "**/XCUIElementTypeCollectionView[`name == '%s'`]/XCUIElementTypeCell[1]/" +
                    "**/XCUIElementTypeImage[`name == 'playIcon'`]")
    private ExtendedWebElement firstCellElementFromCollectionPlayIcon;
    @ExtendedFindBy(iosClassChain =
            "**/XCUIElementTypeCollectionView[`name == '%s'`]/XCUIElementTypeCell[1]/" +
                    "**/XCUIElementTypeStaticText[`value CONTAINS '%s'`]")
    private ExtendedWebElement firstCellElementFromCollectionDynamicStaticText;
    @ExtendedFindBy(iosClassChain =
            "**/XCUIElementTypeCollectionView[`name == '43a35f2b-3788-4449-a54d-cd37263f0940'`]/" +
                    "XCUIElementTypeCell[1]/**/XCUIElementTypeStaticText[`value MATCHES '.*S.+:E.+'`]")
    private ExtendedWebElement firstCellElementFromCollectionEpisodeMetadata;
    @ExtendedFindBy(iosClassChain =
            "**/XCUIElementTypeCollectionView[`name == '%s'`]/XCUIElementTypeCell[1]/" +
                    "**/XCUIElementTypeStaticText[`name == 'airingBadgeLabel'`]")
    private ExtendedWebElement firstCellElementFromCollectionAiringBadge;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[`name == '%s'`]/XCUIElementTypeCell[$label " +
            "CONTAINS \"%s,\"$]")
    private ExtendedWebElement cellElementFromCollection;
    @ExtendedFindBy(accessibilityId = "iconNavBack24LightActive")
    protected ExtendedWebElement navBackButton;
    @ExtendedFindBy(accessibilityId = "Clear text")
    private ExtendedWebElement clearText;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeKey[`label == \"%s\"`]")
    private ExtendedWebElement typeKey;
    @ExtendedFindBy(accessibilityId = "disneyAuthCheckboxUnchecked")
    private ExtendedWebElement checkboxUnchecked;
    @ExtendedFindBy(accessibilityId = "disneyAuthCheckboxChecked")
    private ExtendedWebElement checkboxChecked;
    @ExtendedFindBy(accessibilityId = "cancelBarButton")
    private ExtendedWebElement cancelButton;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeImage[`name CONTAINS \"kidsBackgroundGradient\"`]")
    private ExtendedWebElement kidThemeBackgroundUI;
    @ExtendedFindBy(iosClassChain =
            "**/XCUIElementTypeCell[`name == 'downloadsTab'`]/**/XCUIElementTypeButton[`name MATCHES '\\\\d+'`]")
    protected ExtendedWebElement downloadsTabNotificationBadge;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeImage[`name == 'loader'`]")
    private ExtendedWebElement loader;

    public DisneyPlusApplePageBase(WebDriver driver) {
        super(driver);
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

    public void waitForPresenceOfAnElement(ExtendedWebElement element) {
        fluentWait(getDriver(), FIFTEEN_SEC_TIMEOUT, THREE_SEC_TIMEOUT,
                "Element is not present").until(it -> element.isPresent(ONE_SEC_TIMEOUT));
    }

    public ExtendedWebElement getDynamicIosClassChainElementTypeImage(String label) {
        return dynamicIosClassChainElementTypeImage.format(label);
    }

    public void clickToggleView() {
        toggleView.click();
    }

    public void tapBackButton() {
        fluentWait(getDriver(), SIXTY_SEC_TIMEOUT, THREE_SEC_TIMEOUT, "Back button is not present").until(it -> backButton.isElementPresent(ONE_SEC_TIMEOUT));
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

    public ExtendedWebElement getDynamicAccessibilityId(String id) {
        return dynamicAccessibilityId.format(id);
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

    public ExtendedWebElement getStaticTextViewValueContains(String value) {
        return staticTypeTextViewValueContains.format(value);
    }

    public ExtendedWebElement getViewAlert() {
        return viewAlert;
    }

    public String getErrorMessageString() {
        return labelError.getText();
    }

    public String getHourMinFormatForDuration(int duration) {
        long hours = TimeUnit.MILLISECONDS.toHours(duration) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60;
        return String.format("%dh %dm", hours, minutes);
    }

    public ExtendedWebElement findByAccessibilityId(DisneyDictionaryApi.ResourceKeys resourceKey, DictionaryKeys key) {
        return dynamicAccessibilityId.format(getLocalizationUtils().getDictionaryItem(resourceKey, key.getText()));
    }

    public ExtendedWebElement findByFallbackAccessibilityId(DisneyDictionaryApi.ResourceKeys resourceKey, DictionaryKeys key) {
        return dynamicAccessibilityId.format(getLocalizationUtils().getDictionaryItem(resourceKey, key.getText(), false));
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

    public boolean isActionAlertTitlePresent() {
        return actionableAlertTitle.isElementPresent();
    }

    public boolean isHeadlineHeaderPresent() {
        return headlineHeader.isElementPresent();
    }

    public boolean isHeadlineSubtitlePresent() {
        return getDynamicAccessibilityId(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_CHANGE_EMAIL_BODY.getText())).isElementPresent();
    }

    public ExtendedWebElement getDynamicRowButtonLabel(String label, int rowNum) {
        return dynamicRowButtonLabel.format(label, rowNum);
    }

    //Nav Items
    public ExtendedWebElement getHomeNav() {
        return homeTab;
    }

    public ExtendedWebElement getSearchNav() {
        return searchTab;
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
        return dynamicBtnFindByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.OK_BTN.getText()));
    }

    public void enterText(String text) {
        typeTextView.type(text);
    }

    public String getHeadlineHeaderText() {
        return headlineHeader.getText();
    }

    public boolean isHeadlineHeaderTextPresent() {
        return getStaticTextByLabelContains(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, MY_DISNEY_ENTER_EMAIL_HEADER.getText())).isPresent();
    }

    public boolean isAttributeValidationErrorMessagePresent() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.ATTRIBUTE_VALIDATION.getText())).isPresent();
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
        pause(THREE_SEC_TIMEOUT);
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

    public boolean isStaticTextPresentWithScreenShot(String text) {
        boolean isPresent = (staticTextByLabel.format(text).isElementPresent() || textViewByLabel.format(text).isElementPresent());
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    public void clickHomeIcon() {
        getHomeNav().click();
    }

    public void clickSearchIcon() {
        getSearchNav().click();
    }

    public void clickDownloadsIcon() {
        LOGGER.info("Wait for Downloads icon and click");
        getDownloadNav().clickIfPresent(30);
    }

    public boolean isWebviewOpen() {
        return webviewUrlBar.isElementPresent();
    }

    public String getWebviewUrl() {
        return webviewUrlBar.getText();
    }

    public ExtendedWebElement getWebviewUrlBar() {
        return webviewUrlBar;
    }

    public boolean continueButtonPresent() {
        return getTypeButtonByLabel("Continue").isElementPresent();
    }

    public void clickContinueBtn() {
        continueButton.click();
    }

    // Will take you to continue or done button on tvOS on screen keyboard
    public void moveToContinueOrDoneBtnKeyboardEntry() {
        keyPressTimes(getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
    }

    public ExtendedWebElement getManageWithMyDisneyButton() {
        return getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                DictionaryKeys.MY_DISNEY_MANAGE.getText()));
    }

    public void clickManageWithMyDisneyButton() {
        getManageWithMyDisneyButton().click();
    }

    public ExtendedWebElement getKeyboardDoneButton() {
        return keyboardDone;
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

    public boolean isViewAlertPresent() {
        return viewAlert.isElementPresent();
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

    public ExtendedWebElement getSystemAlert() {
        return typeSystemAlerts;
    }

    public Consumer<IRemoteControllerAppleTV> getClickActionBasedOnLocalizedKeyboardOrientation() {
        localizedKeyboard.isPresent();
        return localizedKeyboard.getSize().getWidth() > 1000 ?
                IRemoteControllerAppleTV::clickDown : IRemoteControllerAppleTV::clickRight;
    }

    public boolean isAlertDismissBtnPresent() {
        return systemAlertDismissBtn.isElementPresent();
    }

    public void clickAlertDismissBtn() {
        systemAlertDismissBtn.click();
    }

    public String getParsedString(ExtendedWebElement element, String part, String regex) {
        String[] elementParts = element.getText().split(regex);
        return elementParts[Integer.parseInt(part)];
    }

    public void clickSaveProfileButton() {
        dynamicBtnFindByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, BTN_ADD_PROFILE_SAVE.getText())).click();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
    }

    public void clickMoreTab() {
        moreTab.click();
    }

    public By getTypeButtonBy() {
        return typeButtons.getBy();
    }

    public void enterPassword(DisneyAccount account) {
        passwordEntryField.type(account.getUserPass());
        clickPrimaryButton();
    }

    public void enterPassword(UnifiedAccount account) {
        passwordEntryField.type(account.getUserPass());
        clickPrimaryButton();
    }

    public void enterPasswordNoAccount(String password) {
        passwordEntryField.type(password);
        clickPrimaryButton();
    }

    public boolean doesAttributeEqualTrue(ExtendedWebElement element, String name) {
        return element.getAttribute(name).equalsIgnoreCase("true");
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

    public void dismissATVAppTrackingPopUp(int timeout) {
        if (isAlertPresent()) {
            LOGGER.info("Dismissing App Track popup by clicking {}", "Ask App not to Track");
            moveDown(1, 1);
            clickSelect();
        }
    }

    public void goBackToDisneyAppFromSafari() {
        Dimension size = getDriver().manage().window().getSize();
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase("Phone")) {
            LOGGER.info("tapping on the left corner of the phone to go back to the Disney app");
            pause(1);
            tap((int) (size.width * 0.2), (int) (size.height * 0.04));
        } else {
            tap((int) (size.width * 0.04), (int) (size.height * 0.01));
        }
    }

    public boolean verifyTextOnWebView(String text) {
        return staticTextLabelContains.format(text).isPresent(THREE_SEC_TIMEOUT);
    }

    public void dismissPickerWheelKeyboard() {
        String editProfilesDone = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                EDIT_PROFILE_DONE_BUTTON.getText());
        if (R.CONFIG.get(DEVICE_TYPE).equals(TABLET)) {
            hideKeyboard();
        }
        getTypeButtonByLabel(editProfilesDone).click();
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
        if (globalNavBarView.isElementPresent(TEN_SEC_TIMEOUT)) {
            Dimension size = globalNavBarView.getSize();
            int x = size.getWidth();
            LOGGER.info("Detecting if global nav is expanded..");
            Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
            return x > 200;
        }
        return false;
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
        if (getStaticTextByLabelContains("default value of true").isPresent(THREE_SEC_TIMEOUT) //to accommodate jarvis bug
                || getStaticTextByLabelContains(SET_TO_TRUE).isPresent(THREE_SEC_TIMEOUT)) {
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

    public ExtendedWebElement getUnavailableOkButton() {
        return dynamicBtnFindByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_ERROR_MEDIAUNAVAILABLE.getText()));
    }

    public ExtendedWebElement getUnavailableContentError() {
        return typeAlertByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.ERROR_COLLECTION_UNAVAILABLE.getText()));
    }

    public List<ExtendedWebElement> getAllCollectionCells(CollectionConstant.Collection collection) {
        return findExtendedWebElements(collectionCellNoRow.format(CollectionConstant.getCollectionName(collection)).getBy());
    }

    public void swipeTillCollectionTappable
            (CollectionConstant.Collection collection, Direction direction, int count) {
        ExtendedWebElement element = collectionCell.format(CollectionConstant.getCollectionName(collection));

        swipePageTillElementTappable(element, count, null, direction, 900);
    }

    public void swipeUpTillCollectionCompletelyVisible
            (CollectionConstant.Collection collection, int count) {
        ExtendedWebElement element = collectionCell.format(CollectionConstant.getCollectionName(collection));
        int screenHeight = getDriver().manage().window().getSize().getHeight();

        swipe(element, Direction.UP, count, 900);
        int elementCurrentYMaxBoundary = element.getLocation().getY() + element.getSize().getHeight();
        if (elementCurrentYMaxBoundary > screenHeight) {
            swipeUp(900);
        }
    }

    public boolean isCollectionVisibleAfterSwiping
            (CollectionConstant.Collection collection, Direction direction, int count) {
        ExtendedWebElement element = collectionCell.format(CollectionConstant.getCollectionName(collection));
        return swipe(element, direction, count, 900);
    }

    public boolean isCollectionPresent(CollectionConstant.Collection collection) {
        return getCollection(collection).isPresent();
    }

    public boolean isCollectionPresent(CollectionConstant.Collection collection, int timeout) {
        return getCollection(collection).isPresent(timeout);
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

        dragFromToForDuration(startX, startY, endX, endY, 0.5);
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

        dragFromToForDuration(startX, startY, endX, endY, 0.5);
    }

    public void dragFromToForDuration(int startX, int startY, int endX, int endY, double duration) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        Map<String, Object> params = new HashMap<>();
        params.put("duration", duration);
        params.put("fromX", startX);
        params.put("fromY", startY);
        params.put("toX", endX);
        params.put("toY", endY);
        js.executeScript("mobile: dragFromToForDuration", params);
    }

    public void swipeRightInCollectionNumOfTimes(int number, CollectionConstant.Collection collection) {
        int count = number;
        while (count >= 0) {
            swipeRightInCollection(collection);
            count--;
        }
    }

    public boolean validateScrollingInCollections(CollectionConstant.Collection collection) {
        DisneyPlusHuluIOSPageBase huluIOSPageBase = initPage(DisneyPlusHuluIOSPageBase.class);
        ExtendedWebElement huluOriginalsLabel = getHuluOriginals();
        swipePageTillElementPresent(huluOriginalsLabel, 5, brandLandingView, Direction.UP, 1000);
        Assert.assertTrue(huluIOSPageBase.getTypeOtherContainsName("Hulu Originals").isPresent(),
                "Hulu Originals collection was not found");
        huluIOSPageBase.waitForLoaderToDisappear(5);
        huluIOSPageBase.swipeLeftInCollectionNumOfTimes(1, collection);
        BufferedImage recommendedForYouLastTileInView = getElementImage(
                huluIOSPageBase.getCollection(collection));
        huluIOSPageBase.swipeRightInCollectionNumOfTimes(1, collection);
        BufferedImage recommendedForYouFirstTileInView = getElementImage(
                huluIOSPageBase.getCollection(collection));

        return areImagesDifferent(recommendedForYouFirstTileInView, recommendedForYouLastTileInView);
    }

    public ExtendedWebElement getHuluOriginals() {
        return staticTextByLabel.format("Hulu Originals");
    }

    public boolean isBackButtonPresent() {
        return collectionBackButton.isPresent();
    }

    public boolean isArtworkBackgroundPresent() {
        return artworkBackground.isPresent();
    }

    public ExtendedWebElement getBackButton() {
        return backButton;
    }

    public ExtendedWebElement getUnavailableContentErrorPopUpMessage() {
        // This element has hardcoded the text in the app and there is not a dictionary key with the same content
        return getStaticTextByLabelContains("**/XCUIElementTypeTextView[`label == \"Sorry, this content is " +
                "unavailable. If the problem continues, visit our Help Center at disneyplus.com/content-unavailable.");
    }

    public boolean isUnavailableContentErrorPopUpMessageIsPresent() {
        return getUnavailableContentErrorPopUpMessage().isPresent();
    }

    public ExtendedWebElement getParentalControlMediaNotAllowedErrorPopUpMessage() {
        //This error message is hardcoded text in the app, once it available in dictionary need to get it from dict
        String notAllowedParentalControlMesssgae = "This title cannot be accessed because it exceeds your profile's " +
                "parental control settings. You will be re-directed to Disney+ Home.";
        return getStaticTextByLabel(notAllowedParentalControlMesssgae);
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
                getLocalizationUtils().formatPlaceholderString(
                        getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PIN_PROFILE.getText()), Map.of(USER_PROFILE, name)));
    }

    public ExtendedWebElement getCellPinProtectedProfileIcon(String name) {
        return dynamicCellByLabel.format(
                getLocalizationUtils().formatPlaceholderString(
                        getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESS_PIN_PROFILE.getText()), Map.of(USER_PROFILE, name)));
    }

    public boolean isPinProtectedProfileIconPresent(String name) {
        if (getCellPinProtectedProfileIcon(name).isPresent(THREE_SEC_TIMEOUT)) {
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

    public ExtendedWebElement getCellElementFromContainer(CollectionConstant.Collection collection, String title) {
        return cellElementFromCollection.format(CollectionConstant.getCollectionName(collection), title);
    }

    public boolean isRatingPresent(String rating) {
        return getStaticTextByLabelContains(rating).isPresent();
    }

    public boolean isRatingPresent(DictionaryKeys rating) {
        return waitUntil(ExpectedConditions.visibilityOfElementLocated(getStaticTextByLabelContains(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.RATINGS,
                        rating.getText())).getBy()), TEN_SEC_TIMEOUT);
    }

    public ExtendedWebElement getNavBackArrow() {
        return navBackButton;
    }

    public void clickNavBackBtn() {
        navBackButton.click();
    }

    public boolean isCollectionViewScreenScrollableVertically(ExtendedWebElement firstCollection, ExtendedWebElement secondCollection, ExtendedWebElement container) {
        List<ExtendedWebElement> titles1 = findExtendedWebElements(firstCollection.getBy(), THREE_SEC_TIMEOUT);
        swipePageTillElementPresent(secondCollection, 3, container, Direction.UP, 500);
        List<ExtendedWebElement> titles2 = findExtendedWebElements(secondCollection.getBy(), THREE_SEC_TIMEOUT);
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
        setBirthDate(DateHelper.localizeMonth(month, getLocalizationUtils()), day, year);
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

    public ExtendedWebElement getTravelAlertTitle() {
        return getStaticTextByLabelContains(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.PCON, TRAVEL_MESSAGE_TITLE.getText()));
    }

    public boolean isTravelAlertTitlePresent() {
        return getTravelAlertTitle().isPresent();
    }

    public boolean isTravelAlertBodyPresent() {
        return getStaticTextByLabelContains(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, TRAVEL_MESSAGE_BODY.getText())).isPresent();
    }

    public ExtendedWebElement getTravelAlertOk() {
        return getTypeButtonContainsLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, BTN_TRAVEL_MESSAGE_OK.getText()));
    }

    public void clickLogoutAllDevices() {
        checkboxUnchecked.click();
    }

    public boolean isLogoutAllDevicesChecked() {
        return checkboxChecked.isPresent();
    }

    public boolean isLogoutAllDevicesUnchecked() {
        return checkboxUnchecked.isPresent();
    }

    public boolean isKidThemeBackgroudUIDisplayed() {
        return kidThemeBackgroundUI.isPresent();
    }

    public ExtendedWebElement getFirstCellFromCollectionAssetImage(String collectionName) {
        return firstCellElementFromCollectionAssetImage.format(collectionName);
    }

    public boolean isFirstCellFromCollectionAssetImagePresent(String collectionName) {
        return firstCellElementFromCollectionAssetImage.format(collectionName).isPresent();
    }

    public boolean isFirstCellFromCollectionPlayIconPresent(String collectionName) {
        return firstCellElementFromCollectionPlayIcon.format(collectionName).isPresent();
    }

    public boolean isFirstCellFromCollectionProgressBarPresent(String collectionName) {
        return firstCellElementFromCollectionProgressBar.format(collectionName).isPresent();
    }

    public boolean isFirstCellFromCollectionEpisodeMetadataPresent(
            String collectionName, String seasonNumber, String episodeNumber, String episodeTitle) {
        String episodeMetadata = String.format("S%s:E%s %s", seasonNumber, episodeNumber, episodeTitle);
        return firstCellElementFromCollectionDynamicStaticText.format(collectionName, episodeMetadata).isPresent();
    }

    public boolean isFirstCellFromCollectionStaticTextPresent(
            String collectionName, String expectedContainedText) {
        return firstCellElementFromCollectionDynamicStaticText.format(collectionName, expectedContainedText)
                .isPresent();
    }

    public ExtendedWebElement getFirstCellFromCollectionEpisodeMetadataElement(String collectionName) {
        return firstCellElementFromCollectionEpisodeMetadata.format(collectionName);
    }

    public ExtendedWebElement getAiringBadgeOfFirstCellElementFromCollection(String collectionName) {
        return firstCellElementFromCollectionAiringBadge.format(collectionName);
    }

    public ExtendedWebElement getFirstCellFromCollection(String collectionName) {
        return firstCellElementFromCollection.format(collectionName);
    }

    public ExtendedWebElement getTextElementValue(String collectionName) {
        return staticTextValueContains.format(collectionName);
    }

    public int getFirstCellRemainingTimeInMinutesFromCollection(String collectionName) {
        String remainingTimePrompt = firstCellElementFromCollectionDynamicStaticText
                .format(collectionName, "remaining").getText();
        String[] remainingTimePromptParts = remainingTimePrompt.split(" ");
        int totalMinutes = 0;

        for (String remainingTimePromptPart : remainingTimePromptParts) {
            if (remainingTimePromptPart.endsWith("h")) {
                int hours = Integer.parseInt(remainingTimePromptPart.replace("h", ""));
                totalMinutes += hours * 60;
            } else if (remainingTimePromptPart.endsWith("m")) {
                int minutes = Integer.parseInt(remainingTimePromptPart.replace("m", ""));
                totalMinutes += minutes;
            }
        }
        return totalMinutes;
    }

    public boolean isCollectionTitleDisplayed() {
        return getTypeCellLabelContains(
                getLocalizationUtils().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                        DictionaryKeys.CONTENT_TILE_INTERACT.getText())).isDisplayed();
    }

    public void moveDownUntilCollectionContentIsFocused(String collectionName, int count) {
        LOGGER.info("Moving down until desired collection content is focused");
        ExtendedWebElement firstCellFromCollection = getFirstCellFromCollection(collectionName);
        if (firstCellFromCollection.isPresent(ONE_SEC_TIMEOUT) && isFocused(firstCellFromCollection)) {
            LOGGER.info("Desired collection content was already focused");
            return;
        }
        while (count > 0) {
            moveDown(1, 1);
            if (firstCellFromCollection.isPresent(THREE_SEC_TIMEOUT) &&
                    isFocused(firstCellFromCollection)) {
                LOGGER.info("Reached desired collection");
                return;
            }
            count--;
        }
        throw new NoSuchElementException("Desired collection was not focused");
    }

    public void moveRightUntilElementIsFocused(ExtendedWebElement element, int count) {
        LOGGER.info("Moving right until desired collection content is focused");
        if (element.isPresent(ONE_SEC_TIMEOUT) && isFocused(element)) {
            LOGGER.info("Desired element was already focused");
            return;
        }
        while (count > 0) {
            moveRight(1, 1);
            if (element.isPresent(ONE_SEC_TIMEOUT) &&
                    isFocused(element)) {
                LOGGER.info("Reached desired element");
                return;
            }
            count--;
        }
        throw new NoSuchElementException("Desired element was not focused after '" + count + "' retries");
    }

    public void waitForLoaderToDisappear(int timeout) {
        LOGGER.info("Waiting for loader to disappear");
        fluentWait(getDriver(), timeout, THREE_SEC_TIMEOUT, "Loader was still visible")
                .until(it -> !loader.isVisible(THREE_SEC_TIMEOUT));
    }

    public void waitUntilElementIsFocused(ExtendedWebElement element, int timeout) {
        fluentWait(getDriver(), timeout, THREE_SEC_TIMEOUT,
                String.format("Element was not focused after %s seconds", timeout))
                .until(it -> isFocused(element));
    }

    public void waitForElementToDisappear(ExtendedWebElement element, int timeout) {
        LOGGER.info("Waiting for element to disappear");
        fluentWait(getDriver(), timeout, THREE_SEC_TIMEOUT, "Given element was still present")
                .until(it -> !element.isPresent(THREE_SEC_TIMEOUT));
    }

    public ExtendedWebElement getCancelButton() {
        String cancelButtonText = getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.CANCEL.getText());
        return getTypeButtonByLabel(cancelButtonText);
    }

    public ExtendedWebElement getDownloadsTabNotificationBadge() {
        return downloadsTabNotificationBadge;
    }

    public ExtendedWebElement getBrandLandingView() {
        return brandLandingView;
    }
}
