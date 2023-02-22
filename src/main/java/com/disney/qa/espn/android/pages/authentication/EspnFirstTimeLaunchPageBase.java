package com.disney.qa.espn.android.pages.authentication;

import com.disney.qa.espn.android.pages.common.EspnPageBase;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

/**
 * ESPN - First Launch screen
 *
 * @author bzayats
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public abstract class EspnFirstTimeLaunchPageBase extends EspnPageBase {
    @FindBy(id = "com.espn.score_center:id/logo")
    private ExtendedWebElement espnLogoImage;

    @FindBy(id = "com.espn.score_center:id/btn_signup")
    private ExtendedWebElement signUpBtn;

    @FindBy(id = "btn_login")
    private ExtendedWebElement logInBtn;

    @FindBy(id = "com.espn.score_center:id/btn_sign_up_later")
    private ExtendedWebElement signUpLaterBtn;

    public EspnFirstTimeLaunchPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened(){
        return logInBtn.isElementPresent(DELAY);
    }

    /** ESPN - open Log In screen **/
    public EspnLoginPageBase openLoginScreen(){
        logInBtn.clickIfPresent();

        return initPage(EspnLoginPageBase.class);
    }

    /** ESPN > First Time Launch > verify all elements are present **/
    public boolean checkForWelcomePageElements(){
        List<Boolean> result = new ArrayList<>();

        result.add(espnLogoImage.isElementPresent());
        result.add(logInBtn.isElementPresent());
        result.add(signUpBtn.isElementPresent());
        result.add(signUpLaterBtn.isElementPresent());

        return result.contains(false);
    }

    /** ESPN - skip login via Sign In Later btn **/
    public EspnAddFavoritesPageBase clickSignUpLater(){
        signUpLaterBtn.clickIfPresent();

        return initPage(EspnAddFavoritesPageBase.class);
    }

}

