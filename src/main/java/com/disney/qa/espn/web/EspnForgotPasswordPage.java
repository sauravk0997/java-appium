package com.disney.qa.espn.web;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;



public class EspnForgotPasswordPage extends AbstractPage{
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    public EspnForgotPasswordPage(WebDriver driver){
        super(driver);
    }

    public static EspnForgotPasswordPage open(WebDriver driver, String url) {
        LOGGER.info("Url: http://www.espn.com/".concat(url));
        driver.get(url);
        return new EspnForgotPasswordPage(driver);
    }

    @FindBy(id="verify_email")
    private ExtendedWebElement email;

    @FindBy(id="content_cob_check")
    private ExtendedWebElement checkBtn;


    @FindBy(css = "a[id = 'global-user-trigger']")
    private ExtendedWebElement loginIcon;

    @FindBy(css = "a[data-affiliatename = 'espn']")
    private ExtendedWebElement loginButton;

    @FindBy(css = "iframe[id='disneyid-iframe']" )
    private ExtendedWebElement iFrame;

    @FindBy(css = "a[ng-click='vm.needHelpSigningIn()']" )
    private ExtendedWebElement helpSignIn;

    @FindBy(css = "input[ng-model = 'vm.username']")
    private ExtendedWebElement userEmail;

    @FindBy (css = "button[type = 'submit']")
    private ExtendedWebElement continueButton;

    @FindBy(css = "input[name = 'code']")
    private ExtendedWebElement code;


    @FindBy (css = "input[ng-model='vm.loginValue']")
    private ExtendedWebElement userID;

    public ExtendedWebElement getLoginIcon() {
        return loginIcon;
    }

    public ExtendedWebElement getLoginButton() {
        return loginButton;
    }

    public ExtendedWebElement getiFrame(){
        return iFrame;
    }

    public ExtendedWebElement gethelpSignIn() {
        return helpSignIn;
    }

    public ExtendedWebElement getUserEmail() {
        return userEmail;
    }

    public ExtendedWebElement getContinue() {
       return continueButton;
    }

    public ExtendedWebElement getCode() {
        return code;
    }

    public ExtendedWebElement getUserID(){
        return userID;
    }

    public void deleteCookies() {
        getDriver().manage().deleteAllCookies();
    }

}