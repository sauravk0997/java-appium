package com.disney.qa.disney.apple.pages.common;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.NoSuchElementException;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class JarvisIOSPageBase extends DisneyPlusApplePageBase {
    //LOCATORS
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[$label == 'Offline Expired License Override'$]" +
            "/**/XCUIElementTypeOther[`name == 'toggleView'`]")
    private ExtendedWebElement offlineExpiredLicenseOverrideToggle;

    public JarvisIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getOfflineExpiredLicenseOverrideToggle() {
        return offlineExpiredLicenseOverrideToggle;
    }

    public boolean isToggleEnabled(ExtendedWebElement toggle) {
        if (!toggle.isPresent()) {
            throw new NoSuchElementException("Given Jarvis toggle was not present");
        }
        List<ExtendedWebElement> toggleSubElements = toggle.findExtendedWebElements(typeOtherElements.getBy());
        if (toggleSubElements.isEmpty() || toggleSubElements.size() < 2) {
            throw new IndexOutOfBoundsException("Unable to find given Jarvis toggle track and slider");
        }
        ExtendedWebElement toggleTrack = toggleSubElements.get(0);
        ExtendedWebElement toggleSlider = toggleSubElements.get(1);
        return toggleSlider.getLocation().getX() > toggleTrack.getLocation().getX();
    }
}
