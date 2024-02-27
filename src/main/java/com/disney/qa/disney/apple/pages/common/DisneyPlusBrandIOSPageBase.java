package com.disney.qa.disney.apple.pages.common;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class DisneyPlusBrandIOSPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"brandLandingView\"`]")
    private ExtendedWebElement brandLandingView;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"brandLandingView\"`]/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeImage")
    protected ExtendedWebElement collectionBrandImageExpanded;

    @FindBy(xpath = "//XCUIElementTypeButton[@name=\"iconNavBack24Dark\"]/parent::XCUIElementTypeOther/following-sibling::XCUIElementTypeOther//XCUIElementTypeImage")
    protected ExtendedWebElement collectionBrandImageCollapsed;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"iconNavBack24LightActive\"`]")
    protected ExtendedWebElement collectionBackButtonLight;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"brandLandingView\"`]/XCUIElementTypeImage[1]")
    private ExtendedWebElement brandFeaturedImage;

    public DisneyPlusBrandIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return brandLandingView.isPresent();
    }

    public void clickFirstCarouselPoster() {
        clickContent(3, 1);
        pause(5);
    }

    public boolean isCollectionBrandImageExpanded() {
        return collectionBrandImageExpanded.isPresent() && !collectionBrandImageCollapsed.isPresent(SHORT_TIMEOUT);
    }

    public boolean isCollectionBrandImageCollapsed() {
        return collectionBrandImageCollapsed.isPresent() && !collectionBrandImageExpanded.isPresent(SHORT_TIMEOUT);
    }

    public void swipeInCollectionBrandPage(Direction direction) {
        swipeInContainer(brandLandingView, direction, 500);
    }

    public ExtendedWebElement getBrandFeaturedImage() {
        return brandFeaturedImage;
    }
}
