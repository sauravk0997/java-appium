package com.disney.qa.disney.apple.pages.common;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusHuluIOSPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(accessibilityId = "brandLandingView")
    protected ExtendedWebElement brandLandingView;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"brandLandingView\"`]/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeImage")
    protected ExtendedWebElement huluBrandImageExpanded;

    @FindBy(xpath = "//XCUIElementTypeButton[@name=\"iconNavBack24Dark\"]/parent::XCUIElementTypeOther/following-sibling::XCUIElementTypeOther//XCUIElementTypeImage")
    protected ExtendedWebElement huluBrandImageCollapsed;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"iconNavBack24Dark\"`]")
    protected ExtendedWebElement backButton;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"brandLandingView\"`]/XCUIElementTypeImage[1]")
    protected ExtendedWebElement artworkBackground;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[`name == \"917351f3-45cf-4251-b425-c8fd1b18434d\"`]")
    private ExtendedWebElement studiosAndNetworkCollection;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[`name == \"a55ccd44-a52f-486c-9aaa-29a19297aab4\"`]")
    private ExtendedWebElement huluOriginalsCollection;

    public DisneyPlusHuluIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return isStudiosAndNetworkPresent();
    }

    public ExtendedWebElement getStudiosAndNetwork() {
        return staticTextByLabel.format("Studios and Networks");
    }

    public boolean isStudiosAndNetworkPresent() {
        ExtendedWebElement studiosLabel = getStudiosAndNetwork();
        swipePageTillElementPresent(studiosLabel, 3, brandLandingView, Direction.UP, 500);
        return studiosLabel.isPresent();
    }

    public boolean isHuluBrandImageExpanded() {
        return huluBrandImageExpanded.isPresent() && !huluBrandImageCollapsed.isPresent(SHORT_TIMEOUT);
    }

    public boolean isHuluBrandImageCollapsed() {
        return huluBrandImageCollapsed.isPresent() && !huluBrandImageExpanded.isPresent(SHORT_TIMEOUT);
    }

    public boolean isBackButtonPresent() {
        return backButton.isPresent();
    }

    public boolean isArtworkBackgroundPresent() {
        return artworkBackground.isPresent();
    }

    public void swipeInHuluBrandPage(Direction direction) {
        swipeInContainer(brandLandingView, direction, 500);
    }

    public void clickOnBackButton() {
        backButton.click();
    }

    public boolean isNetworkLogoPresent(String logoName) {
        if (!typeCellLabelContains.format(logoName).isPresent(SHORT_TIMEOUT)) {

            // studiosAndNetworkCollection element has not visible attribute in false. This is a workaround
            // swipeInContainer(studiosAndNetworkCollection, Direction.LEFT, 500);
            swipeLeftInCollection(studiosAndNetworkCollection);
        }
        return typeCellLabelContains.format(logoName).isPresent(SHORT_TIMEOUT);
    }

    public void swipeLeftInCollection(ExtendedWebElement collection) {
        Point elementLocation = collection.getLocation();
        Dimension elementDimensions = collection.getSize();

        int endY;
        int startY = endY = elementLocation.getY() + Math.round((float) elementDimensions.getHeight() / 2.0F);
        int startX = (int) ((long) elementLocation.getX() + Math.round(0.8 * (double) elementDimensions.getWidth()));
        int endX = (int) ((long) elementLocation.getX() + Math.round(0.25 * (double) elementDimensions.getWidth()));

        this.swipe(startX, startY, endX, endY, 500);
    }

    public boolean validateScrollingInCollections() {
        swipePageTillElementPresent(huluOriginalsCollection, 3, brandLandingView, Direction.UP, 500);
        List<ExtendedWebElement> titles1 = getHuluTitlesInCollection();
        swipeLeftInCollection(huluOriginalsCollection);
        List<ExtendedWebElement> titles2 = getHuluTitlesInCollection();
        return !(titles1 == titles2);
    }

    public List<ExtendedWebElement> getHuluTitlesInCollection() {
        return huluOriginalsCollection.findExtendedWebElements(AppiumBy.iOSClassChain("**/XCUIElementTypeCell"));
    }
}
