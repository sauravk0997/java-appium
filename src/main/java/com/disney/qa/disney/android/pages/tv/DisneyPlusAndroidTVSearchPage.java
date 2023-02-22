package com.disney.qa.disney.android.pages.tv;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.common.DisneyPlusSearchPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.FluentWait;
import java.time.Duration;
import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TV, parentClass = DisneyPlusSearchPageBase.class)
public class DisneyPlusAndroidTVSearchPage extends DisneyPlusSearchPageBase {

    @FindBy(id = "shelfItemLayout")
    private ExtendedWebElement shelfItems;

    @FindBy(id = "recyclerView")
    private ExtendedWebElement shelfContainer;

    @FindBy(id = "keyboardResultTextView")
    private ExtendedWebElement autoCompleteSuggestions;

    @FindBy(id = "searchEditText")
    private ExtendedWebElement searchBox;

    @FindBy(id = "contentRestrictedTitle")
    private ExtendedWebElement contentRestrictedTitle;

    @FindBy(id = "contentRestrictedText")
    private ExtendedWebElement contentRestrictedText;

    @FindBy(id = "filterRoot")
    private ExtendedWebElement searchFilterButton;

    @FindBy(id = "gridKeyboardView")
    private ExtendedWebElement searchKeyboard;

    @FindBy(id = "gridKeyboardDividerContainer")
    private ExtendedWebElement searchKeyboardDivider;

    @FindBy(id = "microphoneImageViewContainer")
    private ExtendedWebElement microphoneIcon;

    @FindBy(id = "keyboardSpaceButton")
    private ExtendedWebElement searchSpaceBarKey;

    @FindBy(id = "keyboardDeleteButton")
    private ExtendedWebElement searchBackKey;

    public DisneyPlusAndroidTVSearchPage(WebDriver driver) {
        super(driver);
    }

    public boolean isContentRestrictedTitleVisible() {
        return contentRestrictedTitle.isElementPresent();
    }
    public ExtendedWebElement getContentRestrictedTitleElement() {
        return contentRestrictedTitle;
    }

    public boolean isContentRestrictedTextVisible() {
        return contentRestrictedText.isElementPresent();
    }

    public ExtendedWebElement getContentRestrictedTextElement() {
        return contentRestrictedText;
    }

    public boolean isAutoCompleteSuggestionVisible() {
        return autoCompleteSuggestions.isElementPresent();
    }

    public List<ExtendedWebElement> getAutoCompleteSuggestionElements() {
        return findExtendedWebElements(autoCompleteSuggestions.getBy());
    }

    public List<ExtendedWebElement> getShelfSetElements() {
        return findExtendedWebElements(shelfItems.getBy());
    }

    public String getSearchBoxText() {
        return searchBox.getText();
    }

    public Point getKeyboardTopLeftCoordinate() {
        return searchKeyboard.getLocation();
    }

    public Point getSearchResultsTopLeftCoordinate() {
        return shelfContainer.getLocation();
    }

    public boolean isSearchKeyboardPresent() {
        return searchKeyboard.isElementPresent(5);
    }

    public boolean isASearchTileFocused() {
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());
        return androidTVUtils.isElementFocused(shelfItems);
    }

    public boolean isSearchResultVisible() {
        boolean isVisible = shelfItems.isVisible();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isVisible;
    }

    public String getAutoCompleteSuggestionText(int index) {
        List<ExtendedWebElement> list = findExtendedWebElements(autoCompleteSuggestions.getBy());
        return list.get(index).getText();
    }

    public void selectAutoCompleteSuggestion(int index) {
        List<ExtendedWebElement> list = findExtendedWebElements(autoCompleteSuggestions.getBy());
        list.get(index).click();
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = searchBox.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public boolean isMicrophoneIconVisible() {
        boolean isVisible = microphoneIcon.isVisible();
        LOGGER.info("Checking if microphone icon is visible");
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isVisible;
    }

    public boolean isSearchEditBoxVisible() {
        boolean isVisible = searchBox.isVisible();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isVisible;
    }

    public boolean verifyShelfElementText(String expectedText, int index) {
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());

        List<ExtendedWebElement> list = findExtendedWebElements(shelfItems.getBy());
        String desc = androidTVUtils.getContentDescription(list.get(index));

        return expectedText.equals(desc);
    }

    public boolean verifySuggestionContainsSearchText(String suggestedText) {
        return suggestedText.contains(searchBox.getText());
    }

    public void downUntilElementVisible(ExtendedWebElement target) {
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());

        DisneyPlusAndroidTVCommonPage.fluentWait(getDriver(), 120, 1, "Did not locate target element...")
                .until(it -> {
                    if (target.isElementPresent(1)) {
                        return true;
                    }
                    androidTVUtils.pressDown();
                    return false;
                });
    }

    public void selectFirstSearchedItem() {
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());

        focusFirstSearchedItem();
        androidTVUtils.pressSelect();
    }

    public void focusFirstSearchedItem() {
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());
        DisneyPlusAndroidTVCommonPage.fluentWait(getDriver(), 120, 1, "Unable to focus first search result")
                .until(it -> {
                    if (androidTVUtils.isElementFocused(shelfItems))
                        return true;
                    androidTVUtils.pressRight();
                    if (androidTVUtils.isElementFocused(searchFilterButton))
                        androidTVUtils.pressDown();
                    return false;
                });
    }

    public void selectCollectionFromFirstRow() {
        List<ExtendedWebElement> list = findExtendedWebElements(shelfItems.getBy());
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());
        FluentWait<WebDriver> wait = new FluentWait<>(getDriver())
                .withTimeout(Duration.ofSeconds(LONG_TIMEOUT))
                .pollingEvery(Duration.ofSeconds(ONE_SEC_TIMEOUT))
                .withMessage("Unable to focus selection on first row")
                .ignoring(NoSuchElementException.class)
                .ignoring(TimeoutException.class)
                .ignoring(WebDriverException.class);
        wait.until(it -> {
            //Maximum of 3 titles in a row
            for (int i = 0; i < 4; i++) {
                if (androidTVUtils.isElementFocused(list.get(i))) {
                    UniversalUtils.captureAndUpload(getCastedDriver());
                    androidTVUtils.pressSelect();
                    return true;
                }
            }
            androidTVUtils.pressRight();
            return false;
        });
    }

    public void typeInSearchBox(String text) {
        searchBox.isElementPresent(LONG_TIMEOUT);
        searchBox.type(text);
    }


    /**
     *
     * @param key - Search keyboard keys, example "a" or "Number 1"
     */
    public ExtendedWebElement getKeyInKeyboard(String key) {
        String xpath = String.format("//android.widget.TextView[@content-desc='%s']", key);
        return searchKeyboard.findExtendedWebElements(By.xpath(xpath)).get(0);
    }

    public List<String> getKeyboardKeys() {
        String xpath = "//android.widget.TextView";
        String keyboardKeys = searchKeyboard.findExtendedWebElements(By.xpath(xpath)).toString();

        // Remove '[' and ']' from end of string and split into list of strings
        keyboardKeys = keyboardKeys.substring(1, keyboardKeys.length() - 1);

        return List.of(keyboardKeys.split(", "));
    }
    
    public boolean isKeyFocused(String key) {
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());
        return androidTVUtils.isFocused(getKeyInKeyboard(key));
    }

    public boolean isBackSpaceVisible() {
        boolean isVisible = searchBackKey.isVisible();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isVisible;
    }

    public boolean isSpaceBarVisible() {
        boolean isVisible = searchSpaceBarKey.isVisible();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isVisible;
    }

    public boolean isKeyboardDividerVisible() {
        boolean isVisible = searchKeyboardDivider.isVisible();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isVisible;
    }

}
