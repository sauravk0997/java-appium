package com.disney.qa.disney.android.pages.common;

import com.disney.qa.api.disney.DisneyContentApiChecker;
import com.fasterxml.jackson.databind.JsonNode;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public abstract class DisneyPlusWelcomePageBase extends DisneyPlusCommonPageBase {

    @FindBy(id = "welcomeLogo")
    protected ExtendedWebElement disneyPlusLogo;

    @FindBy(id = "welcomeBrandLogos")
    protected ExtendedWebElement welcomeBrandLogos;

    @FindBy(id = "welcomeCtvDeviceImage")
    protected ExtendedWebElement welcomeDeviceImage;

    @FindBy(id = "welcomeDescriptionMain")
    protected ExtendedWebElement welcomeText;

    @FindBy(id = "welcomeButtonSignUp")
    protected ExtendedWebElement signUpButton;

    @FindBy(id = "welcomeButtonLogIn")
    protected ExtendedWebElement logInButton;

    @FindBy(id = "welcomeDescriptionSub1")
    protected ExtendedWebElement welcomePageSubText;

    @FindBy(id = "welcomeBackgroundImageView")
    private ExtendedWebElement welcomeBackgroundImage;

    @FindBy(id = "serviceUnavailableAvailabilityButton")
    private ExtendedWebElement invalidRegionLink;

    @FindBy(id = "serviceUnavailableLoginButton")
    private ExtendedWebElement invalidRegionLoginButton;

    @FindBy(id = "serviceUnavailableTitle")
    private ExtendedWebElement serviceUnavailableTitle;

    @FindBy(id = "serviceUnavailableBody")
    private ExtendedWebElement serviceUnavailableBody;

    public DisneyPlusWelcomePageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened(){
        return welcomeText.isElementPresent();
    }

    public boolean isSignUpButtonPresent(){
        return signUpButton.isElementPresent();
    }

    public void proceedToSignUp(){
        signUpButton.click();
    }

    public boolean isLoginButtonPresent(){
        return logInButton.isElementPresent();
    }

    public void clickLoginButton() { logInButton.click(); }

    public boolean isDeviceImagePresent(){
        return welcomeDeviceImage.isElementPresent();
    }

    public boolean isBackgroundImageVisible() { return welcomeBackgroundImage.isElementPresent();}

    public boolean isDisneyPlusLogoVisible() {
        return disneyPlusLogo.isElementPresent();
    }

    public boolean isWelcomeTextVisible() {
        return welcomeText.isElementPresent();
    }

    public boolean isWelcomeBrandLogosVisible() {
        return welcomeBrandLogos.isElementPresent();
    }

    public boolean isInvalidRegionLinkVisible() { return invalidRegionLink.isElementPresent(); }

    public boolean isServiceUnavailableTitleVisible() { return serviceUnavailableTitle.isElementPresent(); }

    public boolean isServiceUnavailableBodyVisible() { return serviceUnavailableBody.isElementPresent(); }

    public boolean isInvalidRegionLoginButtonVisible() { return invalidRegionLoginButton.isElementPresent(); }

    //Uses the possible Strings that display if no purchase is available.
    public boolean isPurchaseAvailable(JsonNode appDictionary){
        DisneyContentApiChecker apiChecker = new DisneyContentApiChecker();
        List<String> noPurchaseTextValues = apiChecker.getNoPurchaseSubtextValues(appDictionary);
        if(welcomePageSubText.isElementPresent()){
            String subText = welcomePageSubText.getText();

            for(String noPurchaseSubText : noPurchaseTextValues){
                if(subText.equals(noPurchaseSubText)){
                    return false;
                }
            }
        }
        return true;
    }

    //Uses the possible Strings that display if no purchase is available.
    public boolean isPurchaseAvailable(List<String> iapSubTexts) {
        if(welcomePageSubText.isElementPresent()){
            String subText = welcomePageSubText.getText();
            for(String noPurchaseSubText : iapSubTexts){
                if(subText.equals(noPurchaseSubText)){
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    //Handles the Invalid Region button if we launch the driver in an unsupported locale intentionally
    public DisneyPlusLoginPageBase continueToLogin() {
        if (invalidRegionLoginButton.isElementPresent(SHORT_TIMEOUT)) {
            invalidRegionLoginButton.click();
        } else {
            if(logInButton.isElementPresent(DELAY)) {
                logInButton.click();
            } else {
                signUpButton.click();
            }
        }
        return initPage(DisneyPlusLoginPageBase.class);
    }

    public ExtendedWebElement getWelcomeTextElement(){
        return welcomeText;
    }

    public void clickInvalidLoginButton() { invalidRegionLoginButton.click(); }
}
