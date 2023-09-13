package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.DisneyAbstractPage;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.MobileUtilsExtended;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.appletv.IRemoteControllerAppleTV;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.LIVE_PROGRESS;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.LIVE_PROGRESS_TIME;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusApplePageBase extends DisneyAbstractPage implements IRemoteControllerAppleTV {

    private static final String DEVICE_TYPE = "capabilities.deviceType";
    private static final String TABLET = "Tablet";
    protected static final String USER_PROFILE = "user_profile";
    public static final String SEASON_NUMBER = "seasonNumber";
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
    private static ThreadLocal<DisneyLocalizationUtils> disneyLanguageUtils;
    @ExtendedFindBy(accessibilityId = "Clear")
    public ExtendedWebElement keyboardClear;
    @ExtendedFindBy(accessibilityId = "unlockedProfileCell")
    public ExtendedWebElement unlockedProfileCell;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"%s\"`]")
    protected ExtendedWebElement staticTextByLabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"%s\" or label == \"%s\"`]")
    protected ExtendedWebElement staticTextByLabelOrLabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label CONTAINS \"%s\"`]")
    protected ExtendedWebElement staticTextLabelContains;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label CONTAINS \"%s\"`]")
    protected ExtendedWebElement typeCellLabelContains;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeImage[`label CONTAINS \"%s\"`]")
    private ExtendedWebElement dynamicIosClassChainElementTypeImage;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextView[`value == '%s'`]")
    protected ExtendedWebElement staticTypeTextViewValue;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextView[`value == \"%s\"`]")
    protected ExtendedWebElement staticTypeTextViewValueDoubleQuotes;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextView[`label == \"%s\"`]")
    protected ExtendedWebElement textViewByLabel;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextView[`name == \"%s\"`]")
    protected ExtendedWebElement textViewByName;
    @ExtendedFindBy(accessibilityId = "logoImage")
    protected ExtendedWebElement logoImage;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell")
    protected ExtendedWebElement cell;
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

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextField[`name == \"%s\"`]")
    protected ExtendedWebElement dynamicTextEntryFieldByName;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextField[`label == \"%s\"`]")
    protected ExtendedWebElement dynamicTextEntryFieldByLabel;

    @ExtendedFindBy(accessibilityId = "secureTextFieldPassword")
    protected ExtendedWebElement passwordEntryField;
    protected ExtendedWebElement saveBtn = xpathNameOrName.format(getDictionary()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                            DictionaryKeys.BTN_ACCOUNT_CREATE_PASSWORD_SAVE.getText()),
            DictionaryKeys.BTN_ACCOUNT_CREATE_PASSWORD_SAVE.getText());
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
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`label == \"%s\" or label == \"%s\"`][%s]")
    protected ExtendedWebElement dynamicRowOtherLabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"%s\"`]")
    protected ExtendedWebElement dynamicBtnFindByLabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name == \"%s\"`]")
    protected ExtendedWebElement dynamicBtnFindByName;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"%s\"`]")
    private ExtendedWebElement dynamicOtherFindByName;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`label CONTAINS \"%s\"`]")
    private ExtendedWebElement dynamicOtherFindByLabelContains;
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
    @ExtendedFindBy(accessibilityId = "buttonShowHidePassword")
    protected ExtendedWebElement showHidePasswordIndicator;
    @ExtendedFindBy(accessibilityId = "collectionHeadlineTitle")
    protected ExtendedWebElement collectionHeadlineTitle;
    @ExtendedFindBy(accessibilityId = "textFieldEmail")
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
    @ExtendedFindBy(accessibilityId = "secondaryButton")
    protected ExtendedWebElement secondaryButton;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther")
    protected ExtendedWebElement typeOtherElements;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell")
    protected ExtendedWebElement typeCell;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeAlert")
    protected ExtendedWebElement typeSystemAlerts;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton")
    protected ExtendedWebElement typeButtons;
    @ExtendedFindBy(accessibilityId = "Keyboard")
    protected ExtendedWebElement keyboard;
    @FindBy(xpath = "//*[contains(@type, 'XCUIElementTypeKeyboard')]")
    protected ExtendedWebElement localizedKeyboard;
    @ExtendedFindBy(accessibilityId = "buttonLogout")
    protected ExtendedWebElement logoutButton;
    @ExtendedFindBy(accessibilityId = "customButton")
    protected ExtendedWebElement customButton;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextView")
    private ExtendedWebElement typeTextView;
    @FindBy(xpath = "//*[contains(@name, \"%s\")]")
    private ExtendedWebElement dynamicXpathContainsName;
    @FindBy(xpath = "//*[contains(@label, \"%s\")]")
    private ExtendedWebElement dynamicXpathContainslabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[%s]/XCUIElementTypeCell[%s]")
    private ExtendedWebElement dynamicRowColumnContent;
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

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"labelStepTitle\"`]/XCUIElementTypeStaticText")
    protected ExtendedWebElement stepTitle;

    @ExtendedFindBy(accessibilityId = "progressBar")
    private ExtendedWebElement progressBar;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`name CONTAINS \"%s\"`]")
    protected ExtendedWebElement staticTextNameContains;

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

    public static synchronized DisneyLocalizationUtils getDictionary() {
        return disneyLanguageUtils.get();
    }

    public static synchronized void setDictionary(DisneyLocalizationUtils dictionary) {
        if (disneyLanguageUtils == null) {
            disneyLanguageUtils = new ThreadLocal<>();
        } else {
            disneyLanguageUtils.remove();
        }

        disneyLanguageUtils.set(dictionary);
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

    public ExtendedWebElement getStaticTextByLabel(String label) {
        return staticTextByLabel.format(label);
    }

    public  ExtendedWebElement getStaticTextLabelName(String name) {
        return staticTextLabelName.format(name);
    }

    public ExtendedWebElement getTextViewByLabel(String label) {
        return textViewByLabel.format(label);
    }

    public ExtendedWebElement getTextViewByName(String name) {
        return textViewByName.format(name);
    }

    public ExtendedWebElement getStaticTextByLabelContains(String label) {
        return staticTextLabelContains.format(label);
    }

    public ExtendedWebElement getStaticTextByNameContains(String name) {
        return staticTextNameContains.format(name);
    }

    public ExtendedWebElement getTypeCellLabelContains(String label) {
        return typeCellLabelContains.format(label);
    }

    public String getTextFromStaticTextByLabel(String label) {
        return getStaticTextByLabel(label).getText();
    }

    public ExtendedWebElement findByAccessibilityId(DisneyDictionaryApi.ResourceKeys resourceKey, DictionaryKeys key) {
        return dynamicAccessibilityId.format(getDictionary().getDictionaryItem(resourceKey, key.getText()));
    }

    public ExtendedWebElement findByFallbackAccessibilityId(DisneyDictionaryApi.ResourceKeys resourceKey, DictionaryKeys key) {
        return dynamicAccessibilityId.format(getDictionary().getDictionaryItem(resourceKey, key.getText(), false));
    }

    public static synchronized List<String> getEnumValues(DictionaryKeys... dictionaryValues) {
        return Arrays.stream(dictionaryValues).map(DictionaryKeys::getText).collect(Collectors.toList());
    }

    public static synchronized FluentWait<WebDriver> fluentWait(WebDriver driver, long timeOut, int polling, String message) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeOut))
                .pollingEvery(Duration.ofSeconds(polling))
                .withMessage(message)
                .ignoring(NoSuchElementException.class)
                .ignoring(TimeoutException.class);
    }

    public static synchronized FluentWait<WebDriver> fluentWaitNoMessage(WebDriver driver, long timeOut, int polling) {
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
        return headlineSubtitle.isElementPresent();
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

    public String getErrorMessageLabelText() {
        String errorMessage = getElementText(labelError);
        UniversalUtils.captureAndUpload(getCastedDriver());
        return errorMessage;
    }

    public boolean isAIDElementPresentWithScreenshot(String id) {
        boolean isPresent = dynamicAccessibilityId.format(id).isPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
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
        ExtendedWebElement openButton =  dynamicBtnFindByLabel.format("Open");
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
     * @param endNum Specify ending range number
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
        UniversalUtils.captureAndUpload(getCastedDriver());
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

    public String getWebviewUrl() {
        return webviewUrlBar.getText();
    }

    public void waitUntilWebviewUrlContains(String expectedText) {
        fluentWait(getDriver(), LONG_TIMEOUT, SHORT_TIMEOUT, "Webview URL did not contain the expected text...").until(it -> getWebviewUrl().contains(expectedText));
    }

    // Will take you to continue button on tvOS on screen keyboard
    public void moveToContinueBtnKeyboardEntry() {
        boolean isClearBtnPresent = keyboardClear.isElementPresent(SHORT_TIMEOUT);
        fluentWait(getCastedDriver(), EXPLICIT_TIMEOUT, 0, "Unable to focus continue button on email Entry")
                .until(it -> {
                    if (isClearBtnPresent) {
                        clickRight();
                    } else {
                        clickDown();
                    }
                    return isFocused(keyboardContinue);
                });
        UniversalUtils.captureAndUpload(getCastedDriver());
    }

    public void moveToLocalizedKeyboard() {
        ExtendedWebElement keyboardContinueLocalized = getDynamicAccessibilityId(disneyLanguageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.KEYBOARD_CONTINUE.getText()).toLowerCase());
        UniversalUtils.captureAndUpload(getDriver(), "Email_Input_Screen");

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
        moveToContinueBtnKeyboardEntry();
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
        new MobileUtilsExtended().clickElementAtLocation(secondaryButton, 50, 50);
    }

    public void clickPrimaryButtonByCoordinates() {
        new MobileUtilsExtended().clickElementAtLocation(primaryButton, 50, 50);
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
        UniversalUtils.captureAndUpload(getCastedDriver());
        List<ExtendedWebElement> listOfTypeButtons = findExtendedWebElements(typeButtons.getBy());
        if (typeSystemAlerts.isElementPresent(15)) {
            IntStream.range(0, listOfTypeButtons.size()).forEach(i ->
                    moveDown(1, 1));
            clickSelect();
        }
    }

    public String getSystemAlertText(){
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
        saveProfileButton.click();
        UniversalUtils.captureAndUpload(getCastedDriver());
    }

    public void clickTypeButton() {
        typeButtons.click();
    }

    public void clickMoreTab() { moreTab.click(); }

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

    public boolean doesAiringBadgeContainLive() {
        return airingBadgeLabel.getText().toLowerCase().contains("live");
    }

    public boolean doesAttributeEqualTrue(ExtendedWebElement element, String name) {
        return element.getAttribute(name).equalsIgnoreCase("true");
    }

    public void dismissKeyboardIfIpadLandscapeDetected() {
        IOSUtils utils = new IOSUtils();
        if (utils.detectDevice(DeviceType.Type.IOS_TABLET) && utils.detectOrientation(ScreenOrientation.LANDSCAPE)) {
            hideKeyboard.clickIfPresent();
        }
    }

    public boolean isUpNextHeaderPresent() {
        return upNextHeaderLabel.isElementPresent();
    }

    public void dismissNotificationsPopUp() {
        if (notificationPopUp.isPresent()) {
            getStaticTextByLabel("Not Now").click();
        }
    }

    public boolean isThumbnailViewPresent() { return thumbnailView.isPresent(); }

    public void clickThumbnailView() { thumbnailView.click(); }

    public enum FooterTabs {
        HOME("homeTab"),
        SEARCH("searchTab"),
        DOWNLOADS("downloadsTab"),
        MORE_MENU("moreTab");

        String tabName;

        FooterTabs(String tabName) {
            this.tabName = tabName;
        }

        public String getLocator() {
            return this.tabName;
        }
    }

    public void goBackToDisneyAppFromSafari() {
        Dimension size = getDriver().manage().window().getSize();
        if (R.CONFIG.get(DEVICE_TYPE).equals("Phone")) {
            LOGGER.info("tapping on the left corner of the phone to go back to the Disney app");
            new IOSUtils().tapAtCoordinateNoOfTimes((int)(size.width * 0.2), (int)(size.height * 0.04), 1);
        } else {
            new IOSUtils().tapAtCoordinateNoOfTimes((int)(size.width * 0.04), (int)(size.height * 0.01), 1);
        }
    }

    public boolean  verifyTextOnWebView(String text) {
        return staticTextLabelContains.format(text).isPresent(SHORT_TIMEOUT);
    }

    public void dismissPickerWheelKeyboard() {
        if (R.CONFIG.get(DEVICE_TYPE).equals(TABLET)) {
            new IOSUtils().hideKeyboard();
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
        if (globalNavBarView.isElementPresent()) {
            Dimension size = globalNavBarView.getSize();
            int x = size.getWidth();
            LOGGER.info("Detecting if global nav is expanded..");
            UniversalUtils.captureAndUpload(getCastedDriver());
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

    /**
     *
     * @param min session to be kept alive for these many minutes
     * @param element check on this element to make sure session is alive
     *
     */
    public void keepSessionAlive(int min, ExtendedWebElement element) {
        LOGGER.info("pausing session for {} mins", min);
        int pauseInterval = 15;
        int upperbound = min * 60/ pauseInterval ;
        AtomicInteger count = new AtomicInteger(0);
        IntStream.range(0, upperbound).forEach(i -> {
            pause(pauseInterval);
            count.addAndGet(pauseInterval);
            Assert.assertTrue(element.isPresent(),
                    String.format("Element was not present after %d seconds elapsed.", count.get()));
        } );
    }

    public ExtendedWebElement getAiringBadgeLabel() {
        return airingBadgeLabel;
    }

    public ExtendedWebElement getProgressBar() { return progressBar; }

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
        new IOSUtils().swipe(override);
        return override;
    }

    public void disableFlexWelcomeConfig() {
        pause(5);
        if (getStaticTextByLabelContains("welcome is using its default value of true").isPresent()) {
            LOGGER.info("Disabling flex welcome config..");
            clickToggleView();
            Assert.assertTrue(getStaticTextByLabelContains("Set to: false").isPresent());
        }
    }

    public void enableFlexWelcomeConfig() {
        pause(5);
        if (getStaticTextByLabelContains("Override in use! Set to: false").isPresent()) {
            LOGGER.info("Enabling flex welcome config..");
            getTypeButtonByLabel("REMOVE OVERRIDE").click();
            Assert.assertTrue(getStaticTextByLabelContains("NO override in use!").isPresent());
        }
    }

    public void clickConfig(String appConfig) {
        clickItemWhileMovingDown(config.format(appConfig));
    }

    public void clickItemWhileMovingDown(ExtendedWebElement element) {
        fluentWait(getDriver(), 300L, 0, "Unable to find Config ").until(it-> {
            if (element.isVisible(1L)) {
                return true;
            } else {
                moveDown(5, 0);
                return false;
            }
        });
        element.click();
    }
}
