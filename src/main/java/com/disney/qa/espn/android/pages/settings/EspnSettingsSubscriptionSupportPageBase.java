package com.disney.qa.espn.android.pages.settings;

import com.disney.qa.espn.android.pages.common.EspnPageBase;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * ESPN - Settings ESPN+ Subscription Support page
 *
 * @author bzayats
 */
public abstract class EspnSettingsSubscriptionSupportPageBase extends EspnPageBase {

    //Header Toolbar
    @FindBy(id = "com.espn.score_center:id/tv_customer_support_code")
    private ExtendedWebElement title;

    //Page Contents
    @FindBy(id = "com.espn.score_center:id/tv_code")
    private ExtendedWebElement tvCodeId;

    @FindBy(id = "com.espn.score_center:id/tv_contact")
    private ExtendedWebElement tvContantHeaderTitle;

    @FindBy(id = "com.espn.score_center:id/tv_phone_content")
    private ExtendedWebElement phoneInfo;

    @FindBy(id = "com.espn.score_center:id/tv_email_content")
    private ExtendedWebElement emailInfo;

    @FindBy(id = "com.espn.score_center:id/tv_web_content")
    private ExtendedWebElement webContentInfo;

    public EspnSettingsSubscriptionSupportPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return title.isElementPresent();
    }

    /** ESPN > SETTINGS > Subscription Support page get phone content info  **/
    public String getPhoneContentInfo() {

        return phoneInfo.getText();
    }

    /** ESPN > SETTINGS > Subscription Support page get email content info  **/
    public String getEmailContentInfo() {

        return emailInfo.getText();
    }

    /** ESPN > SETTINGS > Subscription Support page get web support content info  **/
    public String getWebSupportContentInfo() {

        return webContentInfo.getText();
    }
}

