package com.disney.qa.disney.android.pages.tv;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.common.DisneyPlusLoginPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusWelcomePageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TV, parentClass = DisneyPlusWelcomePageBase.class)
public class DisneyPlusAndroidTVWelcomePage extends DisneyPlusWelcomePageBase {

    @FindBy(id = "welcomeLogoTv")
    private ExtendedWebElement welcomeLogo;

    @FindBy(id = "welcomeButtonLogInTv")
    private ExtendedWebElement loginButtonTV;

    @FindBy(id = "welcomeCtvDeviceImage")
    private ExtendedWebElement mobileIcon;

    @FindBy(id = "ctvActivationDescription")
    private ExtendedWebElement mobileSpecificText;

    public enum WelcomePageItems {
        BTN_LOGIN("btn_login"),
        LOGIN_ONLY("paywall_welcome_subcta_loginonly_copy_sfr"),
        PAYWALL_MOBILE_LINK("paywall_mobile_link"),
        WELCOME_PAGE_SUB_TEXT("welcome_subcta_copy_23a23eedb524254f9d3bc7502010f162"),
        WELCOME_SIGN_UP_BTN("btn_welcome_signup_cta"),
        WELCOME_TAG_TEXT("welcome_tagline_copy_23a23eedb524254f9d3bc7502010f162"),
        WELCOME_LOGIN_ONLY_SUB_TEXT("welcome_subcta_loginonly_copy_03990fb5f9d411537e6a7a408e159689");

        String item;

        public static List<String> getWelcomeScreenItems() {
            return Stream.of(WELCOME_TAG_TEXT.getText(), WELCOME_PAGE_SUB_TEXT.getText(), WELCOME_SIGN_UP_BTN.getText(),
                    BTN_LOGIN.getText()).collect(Collectors.toList());
        }

        public static List<String> getWelcomeScreenItemsNoSignUp() {
            return Stream.of(WELCOME_TAG_TEXT.getText(), BTN_LOGIN.getText(), WELCOME_LOGIN_ONLY_SUB_TEXT.getText()).collect(Collectors.toList());
        }

        WelcomePageItems(String item) {
            this.item = item;
        }

        public String getText() {
            return item;
        }
    }

    public List<String> getWelcomeScreenTexts() {
        List<ExtendedWebElement> textList = findExtendedWebElements(genericTextItemType.getBy());
        UniversalUtils.captureAndUpload(getCastedDriver());
        List<String> sortedList = new ArrayList<>();
        textList.forEach(item -> sortedList.add(item.getText()));
        sortedList.removeIf(item -> item.equalsIgnoreCase("OR"));

        Collections.swap(sortedList, 1, 2);
        return sortedList;
    }

    public boolean isSignUpBtnPresent() {
        boolean isPresent = signUpButton.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public boolean isSignUpBtnFocused(){
        return new AndroidTVUtils(getDriver()).isElementFocused(signUpButton);
    }

    /*
        Partner devices don't have a signup button.
        On partner devices what would normally be the signup button is overridden and used as LOG-IN.
        In this case while there is a login button its ID/xpath is that of the sign-up button.
        So, if the welcome page is visible and the login button is missing you can infer that
        the test is running on a partner device.
    */
    public boolean isPartnerDevice() { return !isLoginButtonPresent(); }

    @Override
    public void proceedToSignUp() {
        if (loginButtonTV.isElementPresent(LONG_TIMEOUT)) {
            signUpButton.click();
        }
    }

    @Override
    public DisneyPlusLoginPageBase continueToLogin() {
        if (loginButtonTV.isElementPresent()) {
            loginButtonTV.click();
        } else {
            signUpButton.click();
        }
        return initPage(DisneyPlusLoginPageBase.class);
    }

    public boolean pressBackToOpenWelcomePage() {
        new AndroidTVUtils(getDriver()).pressBack();
        return isOpened();
    }

    @Override
    public boolean isOpened() {
        boolean isOpened = welcomeText.isElementPresent((long) LONG_TIMEOUT * 2);
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isOpened;
    }

    @Override
    public boolean isLoginButtonPresent(){
        return loginButtonTV.isPresent();
    }

    public DisneyPlusAndroidTVWelcomePage(WebDriver driver) {
        super(driver);
    }

    public void forceFocusSignUp() {
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());
        try {
            fluentWait(getDriver(), DELAY, ONE_SEC_TIMEOUT, "Sign up button was not focused")
                    .until(it -> androidTVUtils.isFocused(signUpButton));
        } catch (Exception e) {
            LOGGER.info("Sign up button was not focused");
            androidTVUtils.pressDown();
        }
    }
}
