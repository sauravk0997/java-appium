package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.common.constant.CollectionConstant;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusHuluIOSPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"highEmphasisView\"`]/" +
            "XCUIElementTypeOther[3]/XCUIElementTypeOther/XCUIElementTypeImage")
    protected ExtendedWebElement huluBrandImageExpanded;

    @FindBy(xpath = "//XCUIElementTypeButton[@name=\"buttonBack\"]/parent::XCUIElementTypeOther/following-sibling::XCUIElementTypeOther//XCUIElementTypeImage")
    protected ExtendedWebElement huluBrandImageCollapsed;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"iconNavBack24LightActive\"`]")
    protected ExtendedWebElement networkBackButton;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[`name == \"%s\"`]/**/XCUIElementTypeCell[`label CONTAINS \"%s,\"`]")
    protected ExtendedWebElement networkLogo;

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

    public ExtendedWebElement getNetworkLogo(String network){
        return networkLogo.format(CollectionConstant.getCollectionName(CollectionConstant.Collection.STUDIOS_AND_NETWORKS), network);
    }

   public boolean isStudiosAndNetworkPresent() {
        ExtendedWebElement studiosLabel = getStudiosAndNetwork();
        swipePageTillElementPresent(studiosLabel, 8, brandLandingView, Direction.UP, 1000);
        if (!getCollection(CollectionConstant.Collection.STUDIOS_AND_NETWORKS).isPresent()){
            swipeInContainer(brandLandingView, Direction.UP, 1000);
        }
        return studiosLabel.isPresent();
    }

    public boolean isHuluBrandImageExpanded() {
        return huluBrandImageExpanded.isPresent() && !huluBrandImageCollapsed.isPresent(THREE_SEC_TIMEOUT);
    }

    public boolean isHuluBrandImageCollapsed() {
        return huluBrandImageCollapsed.isPresent(FIVE_SEC_TIMEOUT) &&
                !huluBrandImageExpanded.isPresent(THREE_SEC_TIMEOUT);
    }

    public boolean isNetworkLogoPresent(String logoName) {
        int count = 10;
        while (!typeCellLabelContains.format(logoName).isPresent(THREE_SEC_TIMEOUT) && count >= 0) {
            // studiosAndNetworkCollection element has visible attribute in false. This is a workaround
            swipeLeftInCollection(CollectionConstant.Collection.STUDIOS_AND_NETWORKS);
            count--;
        }
        return typeCellLabelContains.format(logoName).isPresent(THREE_SEC_TIMEOUT);
    }

    public boolean validateScrollingInHuluCollection(CollectionConstant.Collection collection) {
        return validateScrollingInCollections(collection);
    }

    public void clickOnNetworkLogo(String network){
        getNetworkLogo(network).click();
    }

    public void clickOnNetworkBackButton(){
        networkBackButton.click();
    }

    public boolean isNetworkBackButtonPresent() {
        return networkBackButton.isPresent();
    }
}
