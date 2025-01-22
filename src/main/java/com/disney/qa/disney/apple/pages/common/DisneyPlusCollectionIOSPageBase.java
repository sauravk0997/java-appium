package com.disney.qa.disney.apple.pages.common;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusCollectionIOSPageBase extends DisneyPlusApplePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[`name == 'On the %s screen.'`]")
    private ExtendedWebElement collectionScreen;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeImage[`name == 'On the %s screen.'`]")
    private ExtendedWebElement collectionLogo;

    public DisneyPlusCollectionIOSPageBase(WebDriver driver) { super(driver); }

    public boolean isOpened(String collectionName) {
        return collectionScreen.format(collectionName).isPresent();
    }

    public void waitForCollectionPageToOpen(String collectionName) {
        LOGGER.info("Waiting for {} collection page to load", collectionName);
        fluentWait(getDriver(), SIXTY_SEC_TIMEOUT, THREE_SEC_TIMEOUT, "Collection page is not opened")
                .until(it -> isOpened(collectionName));
    }

    public ExtendedWebElement getCollectionLogo(String collectionName) {
        return collectionLogo.format(collectionName);
    }
}
