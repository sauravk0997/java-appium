package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.common.constant.CollectionConstant;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusHuluIOSPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"brandLandingView\"`]/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeImage")
    protected ExtendedWebElement huluBrandImageExpanded;

    @FindBy(xpath = "//XCUIElementTypeButton[@name=\"iconNavBack24Dark\"]/parent::XCUIElementTypeOther/following-sibling::XCUIElementTypeOther//XCUIElementTypeImage")
    protected ExtendedWebElement huluBrandImageCollapsed;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"iconNavBack24Dark\"`]")
    protected ExtendedWebElement collectionBackButton;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"brandLandingView\"`]/XCUIElementTypeImage[1]")
    protected ExtendedWebElement artworkBackground;

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
        return collectionBackButton.isPresent();
    }

    public boolean isArtworkBackgroundPresent() {
        return artworkBackground.isPresent();
    }

    public void clickOnBackButton() {
        collectionBackButton.click();
    }

    public boolean isNetworkLogoPresent(String logoName) {
        if (!typeCellLabelContains.format(logoName).isPresent(SHORT_TIMEOUT)) {
            // studiosAndNetworkCollection element has visible attribute in false. This is a workaround
            swipeLeftInCollection(CollectionConstant.Collection.STUDIOS_AND_NETWORKS);
        }
        return typeCellLabelContains.format(logoName).isPresent(SHORT_TIMEOUT);
    }

    public boolean validateScrollingInHuluCollection() {
        return validateScrollingInCollections(CollectionConstant.Collection.HULU_ORIGINALS);
    }
}
