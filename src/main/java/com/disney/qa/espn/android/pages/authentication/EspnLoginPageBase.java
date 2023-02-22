package com.disney.qa.espn.android.pages.authentication;

import com.disney.qa.espn.android.pages.common.EspnPageBase;
import com.qaprosoft.carina.core.foundation.utils.mobile.IMobileUtils;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

/**
 * ESPN - Log In screen
 *
 * @author bzayats
 */
public abstract class EspnLoginPageBase extends EspnPageBase implements IMobileUtils {
    @FindBy(xpath = "//*[@class='android.widget.EditText' and @password='false']")
    private ExtendedWebElement userNameEditField;

    @FindBy(xpath = "//*[@class='android.widget.EditText' and @password='true']")
    private ExtendedWebElement passwordEditField;

    @FindBy(xpath = "//*[@class='android.widget.Button' and @text='Log In' or @content-desc='Log In']")
    private ExtendedWebElement loginBtn;

    @FindBy(xpath = "//*[@class='android.widget.Button' and @text='Log In with Facebook']")
    private ExtendedWebElement loginWithFBbtn;

    @FindBy(xpath = "//*[@text='Sign Up']")
    private ExtendedWebElement signUpBtn;

    @FindBy(xpath = "//*[contains(@text,'Sorry')]")
    private ExtendedWebElement signInErrorMsg;

    public EspnLoginPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened(){
        return userNameEditField.isElementPresent();
    }

    /** ESPN - login > opens EspnAddFavorites Screen **/
    public EspnAddFavoritesPageBase login(String email, String password){
        //TODO: check on login error msg and modify existing if necessary
        if (userNameEditField.isElementPresent()) {
            userNameEditField.type(email);
            if (!userNameEditField.getText().equals(email)) {
                LOGGER.error("Error with typing in email. Try again.");
                userNameEditField.type(email);
            }
            hideKeyboard();
            passwordEditField.type(password);

            hideKeyboard();
            loginBtn.click();

            if (signInErrorMsg.isElementPresent(SHORT_TIMEOUT)) {
                LOGGER.error("We got message 'Invalid email address or password, please try again.'. Probably there are problems with user '" + email + "'.");
                Assert.fail("We got message 'Invalid email address or password, please try again.'. Probably there are problems with user '" + email + "'.");
            }
        }

        return initPage(EspnAddFavoritesPageBase.class);
    }

    /** ESPN - login **/



}

