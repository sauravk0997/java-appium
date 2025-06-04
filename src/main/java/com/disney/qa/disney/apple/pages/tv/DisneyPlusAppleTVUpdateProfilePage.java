package com.disney.qa.disney.apple.pages.tv;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.disney.qa.disney.apple.pages.common.DisneyPlusUpdateProfileIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;

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

    public ExtendedWebElement getSaveProfileBtn() {
        return saveProfileBtn;
    }

}
