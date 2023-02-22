package com.disney.qa.disney.web.appex.homepage;

import com.disney.qa.disney.web.common.DisneyPlusBaseTilesPage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;


/*
 * Page for brand tiles
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusBrandPage extends DisneyPlusBaseTilesPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @FindBy(xpath = "//*[@id=\"brand-collection\"]/div")
    private List<ExtendedWebElement> brandCollectionList;

    @FindBy(xpath = "//*[@id='unauth-navbar-target']/img")
    private ExtendedWebElement lionsGateImg;


    @FindBy(xpath = "//*[@data-testid='row-collection']/div")
    private List<ExtendedWebElement> brandCollectionsList;

    @FindBy(xpath = "//*[@data-testid='collection-1']//a")
    private List<ExtendedWebElement> secondCollectionTiles;

    @FindBy(xpath = "//*[@data-testid='asset-wrapper-1-%s']")
    private ExtendedWebElement secondCollectionBrandAssetTile;

    public DisneyPlusBrandPage(WebDriver driver) {
        super(driver);
    }

    public List<ExtendedWebElement> getBrandCollectionList() {
        while (brandCollectionList.isEmpty()) LOGGER.debug("Populating brand page..");
        return brandCollectionList;
    }

    public DisneyPlusBrandPage getHomeCollectionListItem(int index) {
        getBrandCollectionList().get(index);
        return this;
    }

    @Override
    public boolean isOpened() {
        if (!getBrandCollectionList().isEmpty()) {
            LOGGER.info("Brand page is opened");
            return true;
        }
        return false;
    }

    public boolean isLionsGateImageVisible() {
        LOGGER.info("Verify if LionsGate image is visible");
        return lionsGateImg.isVisible();
    }

    @Override
    public boolean verifyTileImageBadging(String logo) {
        LOGGER.info("Verify the tile image badging for second collection tiles in LionsGate brand Page");
        waitForSeconds(2);
        for (int i = 0; i < secondCollectionTiles.size(); i++) {
            if (!isTileImageBadgingVisible(getBrandTileImage(i), logo) && isTileImageBadgingVisible(getBrandTileHoverImage(i), logo))
                return false;
        }
        return true;
    }

    public boolean verifyTileImage() {
        LOGGER.info("Verify the tile image badging");
        waitForSeconds(2);
        return getBrandAssetTiles().size() * 2 == getBrandAssetTileImages().size() + getBrandAssetTileHoverImages().size();
    }
}
