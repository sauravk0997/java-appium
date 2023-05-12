package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusUpdateProfileIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusUpdateProfileIOSPageBase.class)
public class DisneyPlusAppleTVUpdateProfilePage extends DisneyPlusUpdateProfileIOSPageBase {

    @ExtendedFindBy(accessibilityId = "saveProfileButton")
    private ExtendedWebElement saveProfileBtn;

    @FindBy(xpath = "//*[@name='changeAvatarSelectorCell']/following-sibling::*[2]")
    ExtendedWebElement genderBtn;

    public DisneyPlusAppleTVUpdateProfilePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void clickGenderDropDown() {
        genderBtn.click();
    }

}
