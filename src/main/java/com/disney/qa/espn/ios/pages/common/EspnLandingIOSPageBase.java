package com.disney.qa.espn.ios.pages.common;


import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.annotations.AccessibilityId;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;


public class EspnLandingIOSPageBase extends EspnIOSPageBase {


    //Objects

    @FindBy(id = "espn-user-session-centered-image-handset")
    private ExtendedWebElement espnLogo;

    @FindBy(name = "login.btn")
    @AccessibilityId
    private ExtendedWebElement loginBtn;

    @FindBy(name = "bypass.btn")
    @AccessibilityId
    private ExtendedWebElement signUpLtrBtn;



    //Methods

    public EspnLandingIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public EspnEditionIOSPageBase getEditionPage() {
        signUpLtrBtn.click();
        return initPage(EspnEditionIOSPageBase.class);
    }

    public void getLogInPage() {
        loginBtn.click();
    }



}
