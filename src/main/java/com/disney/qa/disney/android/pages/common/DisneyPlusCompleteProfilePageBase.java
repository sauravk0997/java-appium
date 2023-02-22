package com.disney.qa.disney.android.pages.common;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusCompleteProfilePageBase extends DisneyPlusCommonPageBase {

    @FindBy(id = "inputConstraintLayout")
    protected ExtendedWebElement genderSelector;

    @FindBy(id = "gender")
    protected ExtendedWebElement gender;

    @FindBy(id = "profileStandardButton")
    protected ExtendedWebElement saveBtn;

    public void clickGenderSelector() { genderSelector.click(); }

    public void selectFirstGender() { gender.click(); }

    public void clickSave() { saveBtn.click(); }

    @FindBy(id = "chooseProfileHeaderTitleText")
    private ExtendedWebElement chooseProfileHeaderTitleText;

    public boolean isChooseProfileHeaderTitleTextDisplayed() {
        return chooseProfileHeaderTitleText.isElementPresent();
    }

    public DisneyPlusCompleteProfilePageBase(WebDriver driver) {
        super(driver);
    }
}
