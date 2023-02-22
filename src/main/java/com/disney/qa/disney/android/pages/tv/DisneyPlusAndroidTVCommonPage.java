package com.disney.qa.disney.android.pages.tv;

import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.api.search.sets.DisneyCollectionSet;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.common.DisneyPlusCommonPageBase;
import com.disney.qa.disney.android.pages.tv.globalnav.DisneyPlusAndroidTVSettingsPageBase;
import com.disney.qa.disney.android.pages.tv.utility.navhelper.NavHelper;
import com.qaprosoft.carina.core.foundation.utils.android.AndroidService;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TV, parentClass = DisneyPlusCommonPageBase.class)
public class DisneyPlusAndroidTVCommonPage extends DisneyPlusCommonPageBase {

    private static final String SETTINGS = DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.SETTINGS.getText();
    private static final String BOUNDS = "bounds";
    private boolean takeScreenshot = false;

    @FindBy(id = "landingPageTextView")
    protected ExtendedWebElement landingPageTextView;

    @FindBy(xpath = "//*[@resource-id = 'com.disney.disneyplus:id/tabLayoutRecyclerView']/*")
    private ExtendedWebElement tabs;

    @FindBy(id = "titleView")
    private ExtendedWebElement titleView;

    @FindBy(id = "sideMenuBackground")
    private ExtendedWebElement menuBox;

    @FindBy(id = "com.disney.disneyplus:id/iconLayout")
    private ExtendedWebElement iconMenu;

    @FindBy(xpath = "//*[contains(@resource-id, 'id/menuIcon')]")
    private List<ExtendedWebElement> thumbnailIcons;

    @FindBy(id = "profileImage")
    protected ExtendedWebElement profileImage;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/shelfContainer'][%d]/*/*/*")
    protected ExtendedWebElement shelfContainerChildren;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/shelfContainer'][%d]/*/*[%d]")
    protected ExtendedWebElement frameLayoutFromShelfContainer;

    @FindBy(id = "menuTitle")
    private ExtendedWebElement menuTile;

    @FindBy(xpath = "//*[@resource-id = 'com.disney.disneyplus:id/shelfItemLayout']")
    protected ExtendedWebElement mediaPoster;

    @FindBy(xpath = "//*[contains(@resource-id,  'id/iconLayout')]/*")
    private ExtendedWebElement globalNavOptions;

    @FindBy(xpath = "//*[@resource-id = 'com.disney.disneyplus:id/menuIndicator']")
    private List<ExtendedWebElement> globalNavMenuIndicator;

    @FindBy(id = "profileName")
    private ExtendedWebElement globalNavProfileName;

    @FindBy(id = "poster")
    protected ExtendedWebElement poster;

    @FindBy(id = "message")
    protected ExtendedWebElement message;

    private AndroidTVUtils androidTVUtils;

    public DisneyPlusAndroidTVCommonPage(WebDriver driver) {
        super(driver);
        androidTVUtils = new AndroidTVUtils(getDriver());
    }

    public String typeUsingKeyEvents(String text) {
        androidTVUtils.sendInput(text);
        UniversalUtils.captureAndUpload(getCastedDriver());
        return text;
    }

    public static Document convertStringToXMLDocument(String xmlString) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = null;
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(xmlString)));
            doc.getDocumentElement().normalize();
            return doc;
        } catch (Exception e) {
            LOGGER.info("Failed to convert page source string to xml");
        }
        return doc;
    }

    public static String getXMLToJsonString(String source) {
        String jsonString = StringUtils.EMPTY;
        try {
            JSONObject xmlJSONObj = XML.toJSONObject(source);
            jsonString = xmlJSONObj.toString();
        } catch (JSONException e) {
            LOGGER.info("Unable to convert XML to JSON,failed with exception: " + e);
        }
        return jsonString;
    }

    private static HashMap<String, String> getNodes(Document node) {
        Element element;
        HashMap<String, String> result = new HashMap<>();
        if (node == null) {
            return null;
        } else {
            NodeList nodeList = node.getElementsByTagName("*");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node currentNode = nodeList.item(i);
                if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                    element = (Element) currentNode;
                    String temp = element.getAttribute("focused");
                    if (temp.equals("true")) {
                        result.put(BOUNDS, element.getAttribute(BOUNDS));
                        result.put("parentNode", currentNode.getParentNode().getNodeName());
                        break;
                    } else {
                        LOGGER.debug("Unrecognized attribute");
                    }
                }
            }
        }
        return result;
    }

    public ExtendedWebElement getGlobalNavOptions() {
        return globalNavOptions;
    }

    public String getProfileNameOnGlobalNav() {
        return globalNavProfileName.getText();
    }

    @Override
    public boolean isMenuDisplayed() {
        return menuBox.isElementPresent(SHORT_TIMEOUT) && menuTile.isElementPresent(SHORT_TIMEOUT);
    }

    public boolean isGlobalNavCollapsed() {
        if (takeScreenshot)
            UniversalUtils.captureAndUpload(getCastedDriver());
        return thumbnailIcons.size() == DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.values().length - 1 && profileImage.isElementNotPresent(SHORT_TIMEOUT);
    }

    public BufferedImage getProfileImage() {
        return new UniversalUtils().getElementImage(profileImage);
    }

    public boolean isNavSelectionFocused(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem navItem) {
        int index = DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.globalNavMap.get(navItem);
        List<ExtendedWebElement> elements = findExtendedWebElements(globalNavOptions.getBy());
        UniversalUtils.captureAndUpload(getCastedDriver());
        return androidTVUtils.isElementFocused(elements.get(index));
    }

    public static boolean isProd() {
        return DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()).equalsIgnoreCase("prod");
    }

    /**
     * A simple algorithm to ensure that the global navigation bar is opened with appropriate focus
     *
     * @return - index of the current focused item on the navigation bar
     */
    public int openGlobalNavAndFocus() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        FluentWait<WebDriver> wait = new FluentWait<>(getDriver())
                .withTimeout(Duration.ofSeconds(LONG_TIMEOUT * 5))
                .pollingEvery(Duration.ofSeconds(2))
                .withMessage("Unable to open global nav from home page")
                .ignoring(NoSuchElementException.class)
                .ignoring(TimeoutException.class)
                .ignoring(WebDriverException.class);
        wait.until(it -> {
            if(!isMenuDisplayed()) {
                // Avoid potentially exiting the app if the menu is already open...
                androidTVUtils.pressBack();
            } else {
                List<ExtendedWebElement> elements = findExtendedWebElements(globalNavOptions.getBy());
                for (int i = 0; i < elements.size(); i++) {
                    if (androidTVUtils.isElementFocused(elements.get(i))) {
                        atomicInteger.set(i);
                        return true;
                    }
                }
                if (initPage(DisneyPlusAndroidTVSettingsPageBase.class).isOpened())
                    pressRight(2);
                else
                    androidTVUtils.pressRight();
                return false;
            }
            return false;
        });
        UniversalUtils.captureAndUpload(getCastedDriver());
        return atomicInteger.get();
    }

    /**
     * @param navItem - Which navigation page you would like to select
     * @param index   - returned from {@link #openGlobalNavAndFocus()} which indicates the current focus on global navigation bar
     */
    public void navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem navItem, int index) {
        navigateNavBar(navItem, index);
        androidTVUtils.pressSelect();
    }

    public void navigateNavBarAndSelect(String navItem) {
        NavHelper navHelper = new NavHelper(getCastedDriver());
        navHelper.navigateNavBar(navItem);
        androidTVUtils.pressSelect();
    }

    public void navigateNavBar(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem navItem, int index) {
        int expectedIndex = DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.globalNavMap.get(navItem);
        int result = index - expectedIndex;

        if (result < 0) {
            pressDown(Math.abs(result));
        }
        if (result > 0) {
            pressUp(result);
        }
        UniversalUtils.captureAndUpload(getCastedDriver());
    }

    public boolean isGlobalNavFullyCollapsedOnHome() {
        return iconMenu.isElementNotPresent(SHORT_TIMEOUT);
    }

    public boolean isCollapsedNavThumbnailHighlighted(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem selectedItem) {
        return androidTVUtils.isElementSelected(thumbnailIcons.get(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.globalNavMap.get(selectedItem) - 1));
    }

    //TODO:Refactor this once Alice can recognize indicator
    public String beforeStateOfIndicator(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem selectedItem) {
        BufferedImage bufferedImage = new UniversalUtils().getElementImage(
                globalNavMenuIndicator.get(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.globalNavMap.get(selectedItem) - 1));

        return writeImage(bufferedImage);
    }

    public String writeImage(BufferedImage bufferedImage) {
        String randomId = UUID.randomUUID() + ".png";
        try {
            ImageIO.write(bufferedImage, "png", new File(randomId));
        } catch (IOException e) {
            LOGGER.error(String.format("Failed to write file %s with exception %s", randomId, e));
        }
        return randomId;
    }

    //TODO:Refactor this once Alice can recognize indicator
    public BufferedImage getIndicatorImage(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem selectedItem) {
        return new UniversalUtils().getElementImage(globalNavMenuIndicator.get(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.globalNavMap.get(selectedItem) - 1));
    }

    public boolean pressDownFromHeroCarousel() {
        androidTVUtils.pressDown();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isGlobalNavCollapsed();
    }

    public void pressDownAndSelect() {
        androidTVUtils.pressDown();
        androidTVUtils.pressSelect();
    }

    public String getCurrentPageLayout() {
        UniversalUtils.captureAndUpload(getCastedDriver());
        return AndroidService.getInstance().getCurrentFocusedApkDetails();
    }

    public boolean focusBrandTile(String brandName) {
        if (genericTextElement.format(SETTINGS).isElementPresent(DELAY)) {
            androidTVUtils.pressRight();
        }
        if (!androidTVUtils.isElementFocused(genericTextElement.format(brandName))) {
            androidTVUtils.pressDown();
        }
        UniversalUtils.captureAndUpload(getCastedDriver());
        return androidTVUtils.isElementFocused(genericTextElement.format(brandName));
    }

    public boolean isGlobalNavMainMenuHidden() {
        return genericTextElement.format(SETTINGS).isElementPresent(DELAY);
    }

    public void pressBackToActivateGlobalMenu() {
        androidTVUtils.pressBack();
        isMenuDisplayed();
    }

    public boolean closeKeyboard() {
        androidTVUtils.hideKeyboardIfPresent();
        return new AndroidUtilsExtended().isKeyboardShown();
    }

    public boolean closeKeyboard(int waitDelay) {
        androidTVUtils.hideKeyboardIfPresent(waitDelay);
        return new AndroidUtilsExtended().isKeyboardShown();
    }

    public void pressTabToMoveToTheNextField() {
        androidTVUtils.keyPressTimes(AndroidTVUtils::pressTab, 1, 2);
        androidTVUtils.hideKeyboardIfPresent();
    }

    public boolean openKeyboardWithSelect() {
        androidTVUtils.pressSelect();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return new AndroidUtilsExtended().isKeyboardShown();
    }

    public void selectFocusedElement() {
        androidTVUtils.pressSelect();
    }

    public void pressBackTimes(int times) {
        for (int i = 0; i < times; i++){
            androidTVUtils.keyPressTimes(AndroidTVUtils::pressBack, 1, ONE_SEC_TIMEOUT);
        }
    }

    public void pressDown(int times) {
        androidTVUtils.keyPressTimes(AndroidTVUtils::pressDown, times, ONE_SEC_TIMEOUT);
    }

    public void pressDelete(int deleteTimes) {
        androidTVUtils.keyPressTimes(AndroidTVUtils::pressDelete, deleteTimes, ONE_SEC_TIMEOUT);
    }

    public void pressUp(int times) {
        androidTVUtils.keyPressTimes(AndroidTVUtils::pressUp, times, ONE_SEC_TIMEOUT);
    }

    public void pressRight(int times) {
        androidTVUtils.keyPressTimes(AndroidTVUtils::pressRight, times, ONE_SEC_TIMEOUT);
    }

    public void pressLeft(int times) {
        androidTVUtils.keyPressTimes(AndroidTVUtils::pressLeft, times, ONE_SEC_TIMEOUT);
    }

    public void selectMediaItem(boolean isOriginalsPage) {
        if (isOriginalsPage) {
            getAndroidTVUtilsInstance().pressSelect();
        } else {
            pressDown(1);
            getAndroidTVUtilsInstance().pressSelect();
        }
    }

    public void selectMediaItem(int index) {
        List<ExtendedWebElement> mediaPosters = findExtendedWebElements(mediaPoster.getBy());
        findExtendedWebElement(mediaPosters.get(index).getBy()).isElementPresent();
        findMediaFromFirstRow(mediaPosters.get(index));
    }

    public List<ExtendedWebElement> getMediaPosters() {
        return findExtendedWebElements(mediaPoster.getBy());
    }

    private void findMediaFromFirstRow(ExtendedWebElement extendedWebElement) {
        androidTVUtils.pressRight();
        boolean isMovingRight = true;
        String previous = null;
        String focused = null;
        String current;
        String temp;
        Document doc;
        while (!androidTVUtils.isElementFocused(findExtendedWebElement(extendedWebElement.getBy()))) {
            moveFocus(isMovingRight);
            HashMap<String, String> result;
            temp = getCastedDriver().getPageSource();
            doc = convertStringToXMLDocument(temp);
            result = getNodes(doc);
            if (result != null) {
                focused = result.get(BOUNDS);
            } else {
                continue;
            }
            current = focused;
            if (current.equals(previous)) {
                androidTVUtils.pressDown();
                if (result.get("parentNode").contains("FrameLayout")) {
                    isMovingRight = !isMovingRight;
                }
                current = focused;
            }
            previous = current;
        }
        LOGGER.debug(String.format("Element %s was found", extendedWebElement.getBy().toString()));
        UniversalUtils.captureAndUpload(getCastedDriver());
        androidTVUtils.pressSelect();
    }

    private void moveFocus(boolean isMovingRight) {
        if (isMovingRight)
            androidTVUtils.pressRight();
        else
            androidTVUtils.pressLeft();
    }

    public void closeKeyboardOnscreen(ExtendedWebElement element) {
        if (new AndroidUtilsExtended().isKeyboardShown()) {
            DisneyPlusCommonPageBase.fluentWait(getDriver(), LONG_TIMEOUT, 2, "Unable to hide keyboard")
                    .until(it -> {
                        androidTVUtils.pressBack();
                        return element.isElementPresent(ONE_SEC_TIMEOUT);
                    });
            UniversalUtils.captureAndUpload(getCastedDriver());
        }
    }

    public boolean isErrorViewPresent() {
        return errorTextView.isElementPresent();
    }

    public void setTakeScreenshot(boolean takeScreenshot) {
        this.takeScreenshot = takeScreenshot;
    }

    public ExtendedWebElement getTabs() {
        return tabs;
    }

    public ExtendedWebElement getTitleView() {
        return titleView;
    }

    public AndroidTVUtils getAndroidTVUtilsInstance() {
        return androidTVUtils;
    }

    /**
     * Takes a parent element and returns the TextView Element inside.
     * This is useful when trying to get the text of a button.
     * @param element - the parent element containing the TextView element.
     * @return - the TextView child of the "passed in" element
     */
    public static ExtendedWebElement getTextViewInElement(ExtendedWebElement element) {
        return element.findExtendedWebElements(By.className("android.widget.TextView")).get(0);
    }

    public void focusFirstTile() {
        DisneyPlusCommonPageBase.fluentWait(getDriver(), LONG_TIMEOUT, SHORT_TIMEOUT,
                "Unable to focus first tile")
                .until(it -> {
                    if (getAndroidTVUtilsInstance().isFocused(mediaPoster)) return true;
                    pressDown(1);
                    return false;
                });
        UniversalUtils.captureAndUpload(getCastedDriver());
    }

    public boolean fourTilesPerRow(int rowsToCheck) {
        List<Boolean> eachRowContainsFour = new ArrayList<>();
        IntStream.range(0, rowsToCheck).forEach(i -> {
            List<ExtendedWebElement> recylerViewChildren = findExtendedWebElements(shelfContainerChildren.format(i + 1).getBy());
            boolean fourPerRow = recylerViewChildren.size() == 4;
            eachRowContainsFour.add(fourPerRow);
        });
        return eachRowContainsFour.stream().allMatch(value -> value);
    }

    public void verifyTabsAreSelectedAndFocused(ExtendedWebElement element, SoftAssert sa) {
        List<ExtendedWebElement> list = findExtendedWebElements(element.getBy());
        list.forEach(i -> {
            UniversalUtils.captureAndUpload(getCastedDriver());
            sa.assertTrue(getAndroidTVUtilsInstance().isFocused(i), "Tab not focused: " + i.getText());
            sa.assertTrue(getAndroidTVUtilsInstance().isSelected(i), "Tab not selected: " + i.getText());
            androidTVUtils.keyPressTimes(AndroidTVUtils::pressRight, 1, 1);
        });
    }

    public void navigateRowsAndTiles(SoftAssert sa) {
        //traversing two rows and 8 tiles
        IntStream.range(0, 2).forEach(i -> IntStream.range(0, 3).forEach(j -> {
            UniversalUtils.captureAndUpload(getCastedDriver());
            //After reaching end of first row press Down to move to next Row and left thrice to get to first tile
            if (i == 1 && j == 0) {
                pressDown(1);
                pressLeft(3);
                UniversalUtils.captureAndUpload(getCastedDriver());
            }
            sa.assertTrue(getAndroidTVUtilsInstance().isFocused(frameLayoutFromShelfContainer.format(i + 1, j + 1)),
                    String.format("Row: %d and tile: %d couldn't be focused", i, j));
            pressRight(1);
        }));
    }

    /**
     * Navigates down to the next set. Uses the layout type to determine how much to scroll down.
     * @param disneyCollectionSet - collection set containing info such as layout type
     * @param setSize - the size of the set
     */
    public void selectNextSet(DisneyCollectionSet disneyCollectionSet, int setSize){
        int downPressNumber;
        if (disneyCollectionSet.getLayoutType().equals("GridContainer")) {
            downPressNumber = setSize % 4 == 0 ? setSize / 4 : setSize / 4 + 1;
        } else {
            downPressNumber = 1;
        }
        pressDown(downPressNumber);
    }

    /**
     * If the set is a shelf, press right until the set scrolls. If the set is something else,
     * don't move as the set can't scroll.
     * @param disneyCollectionSet - the set you want to see scroll
     * @return - the number of right presses
     */
    public int scrollShelfSet(DisneyCollectionSet disneyCollectionSet) {
        int rightPressNumber = 0;
        if (disneyCollectionSet.getLayoutType().equals("ShelfContainer")) {
            rightPressNumber = 4;
        }
        pressRight(rightPressNumber);
        return rightPressNumber;
    }
}
