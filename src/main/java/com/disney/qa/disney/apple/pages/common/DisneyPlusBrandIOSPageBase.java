package com.disney.qa.disney.apple.pages.common;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

public class DisneyPlusBrandIOSPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"brandLandingView\"`]")
    private ExtendedWebElement brandLandingView;

    public DisneyPlusBrandIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return brandLandingView.isElementPresent();
    }

    public void clickFirstCarouselPoster() {
        clickContent(3, 1);
        pause(5);
    }

}
