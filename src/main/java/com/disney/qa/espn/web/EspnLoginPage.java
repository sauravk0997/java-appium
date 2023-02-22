package com.disney.qa.espn.web;

import com.disney.qa.common.web.SeleniumUtils;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;


public class EspnLoginPage extends EspnBasePage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    //Login Selectors
    @FindBy(css = "a[id='global-user-trigger']")
    private ExtendedWebElement loginIcon;

    @FindBy(css = "a[data-affiliatename='espn']")
    private ExtendedWebElement overlayLoginButton;

    @FindBy(css = "input[type='email']")
    private ExtendedWebElement usernameInput;

    @FindBy(css = "input[type='password']")
    private ExtendedWebElement passwordInput;

    @FindBy(css = "button[type='submit']")
    private ExtendedWebElement loginButton;

    @FindBy(xpath = "//a[@class='AnchorLink AnchorLink--dark Nav__Primary__Branding Nav__Primary__Branding--espn Nav__Primary__Branding--dark']")
    private ExtendedWebElement watchEspnLogo;

    @FindBy(css = "li[class=\"display-user\"]")
    private ExtendedWebElement loggedInIdentifier;

    @FindBy(css = "a.small")
    private ExtendedWebElement logoutButton;

    @FindBy (css = "svg[class='Nav__Primary__Icon__User icon__svg icon__svg--dark']")
    private ExtendedWebElement watchWebRedesignProfileIcon;


    @FindBy(css = "li[class='Nav__Primary__Menu__Item Nav__Primary__Menu__Item--User flex-none relative Nav__Primary__Menu__Item--dark']")
    private ExtendedWebElement watchWebRedesignLoginIcon;

    @FindBy(xpath = "//span[contains(text(),'Log Out')]")
    private ExtendedWebElement watchWebRedesignLogoutIcon;


    //Landing Page Login
    @FindBy(css = "a[id='login-link']")
    private ExtendedWebElement landingPageLogin;

   //Beta Login Info
    @FindBy(css = "input[id='inputUser']")
    private ExtendedWebElement emailInput;

    @FindBy(css = "button[type='submit']")
    private ExtendedWebElement betaCookieSubmit;

    //iFrame
    @FindBy(css = "iframe[id='disneyid-iframe']")
    private ExtendedWebElement iframe;

    @FindBy(css = "div[class='log-in btn center']")
    private ExtendedWebElement loginPaywall;

    @FindBy(css = "div[id='bam-paywall-iframe']>iframe")
    private ExtendedWebElement paywallLoginIframe;

    @FindBy (css = "div[class='btn-close icon-font-before icon-close-solid-before']")
    private ExtendedWebElement paywallXButton;


    private String betaLoginUrl = EspnWebParameters.ESPN_WEB_BETA_LANDING_URL.getValue();
    private String betaUser = EspnWebParameters.ESPN_WEB_BETA_USER.getValue();
    private String betaPass = EspnWebParameters.ESPN_WEB_BETA_PASS.getDecryptedValue();
    private String mlbBetaUrl = EspnWebParameters.ESPN_WEB_BETA_LANDING_MLB_URL.getValue();
    private String baseballBetaUrl = EspnWebParameters.ESPN_WEB_BETA_LANDING_BASEBALL_URL.getValue();
    private String mlbUrl = EspnWebParameters.ESPN_WEB_PROD_LANDING_MLB_URL.getValue();
    private String baseballUrl = EspnWebParameters.ESPN_WEB_PROD_LANDING_BASEBALL_URL.getValue();
    private String betaWebUrl = EspnWebParameters.ESPN_WEB_BETA_URL.getValue();
    private String webUrl = EspnWebParameters.ESPN_WEB_PROD_URL.getValue();
    private String tvBetaMlbUrl = EspnWebParameters.ESPN_WEB_BETA_MLBTV_URL.getValue();
    private String tvProdMlbUrl = EspnWebParameters.ESPN_WEB_PROD_MLBTV_URL.getValue();
    private String mlsBetaUrl = EspnWebParameters.ESPN_WEB_BETA_MLS_URL.getValue();
    private String mlsUrl = EspnWebParameters.ESPN_WEB_MLS_URL.getValue();
    private String ufcBetaUrl = EspnWebParameters.ESPN_WEB_BETA_LANDING_UFC_URL.getValue();
    private String ufcUrl = EspnWebParameters.ESPN_WEB_PROD_LANDING_UFC_URL.getValue();
    private String ufcPpvBetaUrl = EspnWebParameters.ESPN_WEB_BETA_LANDING_UFC_PPV_URL.getValue();
    private String ufcPpvUrl = EspnWebParameters.ESPN_WEB_PROD_LANDING_UFC_PPV_URL.getValue();

    public EspnLoginPage (WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getLoginIcon () {
        return loginIcon;
    }

    public ExtendedWebElement getWatchEspnLogo () {
        return watchEspnLogo;
    }

    public ExtendedWebElement getLoggedInIdentifier () {
        return loggedInIdentifier;
    }

    public static EspnLoginPage open(WebDriver driver, String url) {
        driver.get(url);
        return new EspnLoginPage(driver);
    }

    public EspnBasePage login(String passedUsername, String passedPassword) {

        watchWebRedesignProfileIcon.hover();
        pause(5);
        watchWebRedesignLoginIcon.click();
        getDriver().switchTo().frame(iframe.getElement());
        if (usernameInput.isElementPresent()) {
            usernameInput.click();
            waitFor(usernameInput);
            usernameInput.type(passedUsername);
            waitFor(passwordInput);
            passwordInput.type(passedPassword);
            waitFor(loginButton);
            loginButton.click();
            waitForPageToFinishLoading();
            refresh();
            getDriver().switchTo().defaultContent();
        }  else {
            LOGGER.warn("Unable to login!");
        }
        return new EspnBasePage(driver);
    }

    public EspnBasePage landingPageLogin(String passedUsername, String passedPassword) {
        pause(5);
        getDriver().switchTo().frame(iframe.getElement());
        if (usernameInput.isElementPresent()) {
            usernameInput.click();
            waitFor(usernameInput);
            usernameInput.type(passedUsername);
            waitFor(passwordInput);
            passwordInput.type(passedPassword);
            loginButton.click();
            getDriver().switchTo().defaultContent();
        } else {
            LOGGER.warn("Unable to login!");
        }
        return new EspnBasePage(driver);
    }

    public void checkLoginStatus () {

        watchWebRedesignProfileIcon.click();
        pause(3);
        watchWebRedesignProfileIcon.hover();
        if (watchWebRedesignLoginIcon.isElementNotPresent(5)) {
            PAGEFACTORY_LOGGER.info("Already Logged In");
            logout();
        }
        else {
            PAGEFACTORY_LOGGER.info("Not Logged In");
        }
    }

    public void waitForPageToFinishLoading() {
        SeleniumUtils util = new SeleniumUtils(getDriver());
        util.waitUntilDOMready();
    }

    public void environmentSetUp(String env){
        switch (env){
            case "BETA":
                LOGGER.info("Redirecting to beta-login url for beta access..");
                deleteCookies();
                setBetaCookie();
                break;
            case "PROD":
                deleteCookies();
                break;
            default:
                break;
        }
    }

    public void setBetaCookie(){
        getDriver().get(betaLoginUrl);
        waitForPageToFinishLoading();
        emailInput.type(betaUser);
        waitFor(passwordInput);
        passwordInput.type(betaPass);
        waitFor(betaCookieSubmit);
        betaCookieSubmit.click();
        waitForPageToFinishLoading();
    }

    public void deleteCookies() {
        getDriver().manage().deleteAllCookies();
    }

    public void redirectUrl (String env, String url) {
        switch (env) {
            case "BETA":
                caseBeta(url);
                break;
            case "PROD":
                caseProd(url);
                break;
            default:
                break;
        }
    }

    public void verifyLogin(){
        loginIcon.hover();
    }

    public void logout () {
        waitForPageToFinishLoading();
        watchWebRedesignProfileIcon.hover();
        waitFor(watchWebRedesignLogoutIcon);
        watchWebRedesignLogoutIcon.click();
    }

    public void betaUrlSetUp (String urlType){
        deleteCookies();
        setBetaCookie();
        getDriver().get(urlType);
    }

    public void prodUrlSetUp (String urlType){
        deleteCookies();
        getDriver().get(urlType);
    }

    public void mlbEntUrlSetUp (String urlType){
        getDriver().get(urlType);
    }

    public void caseBeta (String url) {
        switch (url) {
            case "MLB":
                betaUrlSetUp(mlbBetaUrl);
                break;
            case "Baseball":
                betaUrlSetUp(baseballBetaUrl);
                break;
            case "Video":
                betaUrlSetUp(betaWebUrl);
                break;
            case "TV":
                betaUrlSetUp(tvBetaMlbUrl);
                break;
            case "MLS":
                betaUrlSetUp(mlsBetaUrl);
                break;
            case "UFC":
                betaUrlSetUp(ufcBetaUrl);
                break;
            case "PPV":
                betaUrlSetUp(ufcPpvBetaUrl);
                break;
            case "MLBENT":
                mlbEntUrlSetUp(mlbBetaUrl);
                break;
            default:
                break;
        }
    }

    public void caseProd (String url){
        switch (url) {
            case "MLB":
                prodUrlSetUp(mlbUrl);
                break;
            case "Baseball":
                prodUrlSetUp(baseballUrl);
                break;
            case "Video":
                prodUrlSetUp(webUrl);
                break;
            case "TV":
                prodUrlSetUp(tvProdMlbUrl);
                pause(5);
                break;
            case "MLS":
                prodUrlSetUp(mlsUrl);
                break;
            case "UFC":
                prodUrlSetUp(ufcUrl);
                break;
            case "PPV":
                prodUrlSetUp(ufcPpvUrl);
                break;
            case "MLBENT":
                mlbEntUrlSetUp(mlbUrl);
                break;
            default:
                break;
        }
    }
}
