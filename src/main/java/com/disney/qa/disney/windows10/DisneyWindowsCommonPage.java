package com.disney.qa.disney.windows10;

import com.disney.qa.common.DisneyAbstractPage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import java.util.stream.IntStream;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyWindowsCommonPage extends DisneyAbstractPage {
    @ExtendedFindBy(accessibilityId = "Back")
    private ExtendedWebElement backButton;

    public DisneyWindowsCommonPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return false;
    }

    public void pressBack(int times, long timeout) {
        IntStream.range(0, times).forEach(i -> {
            backButton.click();
            pause(timeout);
        });
    }
}
