package com.disney.qa.espn.ios.pages.authentication;

import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.espn.EspnParameter;
import com.disney.qa.espn.ios.pages.common.EspnIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.mobile.IMobileUtils;
import com.qaprosoft.carina.core.foundation.webdriver.Screenshot;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.annotations.AccessibilityId;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;


public class EspnPaywallLogInIOSPageBase extends EspnIOSPageBase implements IMobileUtils {


    //Objects

    private static final String DEVICE_NAME_CAP = "capabilities.deviceName";

    private static final String USERNAME_FIELD = "type == 'XCUIElementTypeTextField' AND value == 'Username or Email Address'";

    private static final String PASSWORD_FIELD = "type == 'XCUIElementTypeSecureTextField' AND value == 'Password (case sensitive)'";

    @FindBy(name = "Log In")
    @AccessibilityId
    private ExtendedWebElement logInBtn;

    @FindBy(xpath = "//XCUIElementTypeOther[@name='banner']/following-sibling::XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeTextField")
    private ExtendedWebElement usernameField;

    @FindBy(xpath = "//XCUIElementTypeOther[@name='banner']/following-sibling::XCUIElementTypeOther/following-sibling::XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeSecureTextField")
    private ExtendedWebElement passwordField;



    //Methods

    public EspnPaywallLogInIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public void login(String user) {
        String username = "";
        String password = "";
        if("QA".equalsIgnoreCase(user)) {
            LOGGER.info("Fetching credentials for user: QA");
            username = EspnParameter.ESPN_DEFAULT_USER.getValue();
             password = EspnParameter.ESPN_DEFAULT_PASSWORD.getDecryptedValue();
        } else if ("QE".equalsIgnoreCase(user)) {
            LOGGER.info("Fetching credentials for user: QE");
            username = EspnParameter.ESPN_QE_USER.getValue();
            password = EspnParameter.ESPN_QE_PASS.getDecryptedValue();
        }
        findExtendedWebElement(MobileBy.iOSNsPredicateString(USERNAME_FIELD)).type(username);
        findExtendedWebElement(MobileBy.iOSNsPredicateString(PASSWORD_FIELD)).type(password);
        Screenshot.capture(getDriver(), "Paywall Credentials");
          logInBtn.click();
    }

    public void pressLogInField() {
        IOSUtils iosUtils = new IOSUtils();
        if(R.CONFIG.get(DEVICE_NAME_CAP).contains("Plus")){
            iosUtils.screenPress(2, 8);
        } else iosUtils.screenPress(2, 6);
    }

    public void pressPasswordField() {
        IOSUtils iosUtils = new IOSUtils();
        if(R.CONFIG.get(DEVICE_NAME_CAP).contains("Plus")){
            iosUtils.screenPress(2, 5);
        } else iosUtils.screenPress(2, 4);
    }

    public void pressLogInBtn() {
        if(R.CONFIG.get(DEVICE_NAME_CAP).contains("Plus")){
            LOGGER.info("Device Name contains 'Plus', pressing Log In button with appropriate co-ordinates");
            new IOSUtils().screenPress(2, 4);
        }
            else {
            LOGGER.info("Using default co-ordinates to click on Log In button");
            new IOSUtils().screenPress(2, 3);
        }
    }

    public void pressLogInBtnFromSettings() {
        tap(208, 218, 2);
    }

}
