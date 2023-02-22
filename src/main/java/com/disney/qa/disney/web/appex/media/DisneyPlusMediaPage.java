package com.disney.qa.disney.web.appex.media;

import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusMediaPage extends DisneyPlusBasePage {

    @FindBy(css = "button.skipToContentTarget")
    private ExtendedWebElement startPlaybackButton;

    public DisneyPlusMediaPage(WebDriver driver) {
        super(driver);
    }

    public void startPlayback() {
        startPlaybackButton.click();
    }
}
