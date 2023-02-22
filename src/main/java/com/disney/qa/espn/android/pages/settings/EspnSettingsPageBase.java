package com.disney.qa.espn.android.pages.settings;

import com.disney.qa.espn.android.pages.authentication.EspnLoginPageBase;
import com.disney.qa.espn.android.pages.common.EspnCommonPageBase;
import com.disney.qa.espn.android.pages.common.EspnPageBase;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * ESPN - Settings page
 *
 * @author bzayats
 */
public abstract class EspnSettingsPageBase extends EspnPageBase {

    //Settings list containers
    @FindBy(id = "com.espn.score_center:id/listview")
    private ExtendedWebElement settingsScrollableContainer;

    @FindBy(id = "com.espn.score_center:id/xLabelTextView")
    private ExtendedWebElement settingsContainerItem;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/xLabelTextView' and @text='%s']")
    private ExtendedWebElement settingsContainerDynamicItem;

    public EspnSettingsPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return initPage(EspnCommonPageBase.class).isOpened();
    }

    /** ESPN > watch tabs Enum **/
    public enum Settings{
        ESPN_SUBSCRIPTION_SUPPORT("ESPN+ Subscription Support"),
        ESPN_LOG_OUT("Log Out of ESPN Account"),
        ESPN_LOG_IN("Log In to ESPN Account"),
        DEFAULT("ESPN+ Subscription Support");

        private String tab;

        Settings(String tab){
            this.tab = tab;
        }

        public String getText(){

            return tab;
        }
    }

    /** ESPN > SETTINGS > open ESPN+ Subscription Support page **/
    public EspnSettingsSubscriptionSupportPageBase openSettingsESPNSubscriptionSupposrtPage() {
        settingsContainerDynamicItem.format(Settings.ESPN_SUBSCRIPTION_SUPPORT.getText()).clickIfPresent();

        return initPage(EspnSettingsSubscriptionSupportPageBase.class);
    }

    /** ESPN > SETTINGS > log out from ESPN acct **/
    public EspnSettingsPageBase logOutFromESPNacct(){
        if (settingsContainerDynamicItem.format(Settings.ESPN_LOG_OUT.getText()).isElementPresent()) {
            settingsContainerDynamicItem.format(Settings.ESPN_LOG_OUT.getText()).clickIfPresent();
        }

        return initPage(EspnSettingsPageBase.class);
    }

    /** ESPN > SETTINGS > log in to ESPN acct **/
    public EspnLoginPageBase logInToESPNacct(){
        if (settingsContainerDynamicItem.format(Settings.ESPN_LOG_IN.getText()).isElementPresent()) {
            settingsContainerDynamicItem.format(Settings.ESPN_LOG_IN.getText()).clickIfPresent();
        }

        return initPage(EspnLoginPageBase.class);
    }
}

