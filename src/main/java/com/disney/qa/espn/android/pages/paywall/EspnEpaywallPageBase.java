package com.disney.qa.espn.android.pages.paywall;

import com.disney.qa.espn.android.pages.authentication.EspnLoginPageBase;
import com.disney.qa.espn.android.pages.common.EspnPageBase;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * ESPN - E+ paywall screen
 *
 * @author bzayats
 */
public abstract class EspnEpaywallPageBase extends EspnPageBase {
    @FindBy(xpath = "//*[contains(@text, 'charged monthly until you cancel')]")
    private ExtendedWebElement paywallConditionsText;

    @FindBy(xpath = "//*[@class='android.widget.Button' and contains(@text, 'iap.Monthly')]")
    private ExtendedWebElement paywallMonthlySubscriptionBtn;

    @FindBy(xpath = "//*[@class='android.widget.Button' and @text='LOG IN']")
    private ExtendedWebElement paywallLogInBtn;

    @FindBy(xpath = "//*[@class='android.widget.Button' and @text='RESTORE']")
    private ExtendedWebElement paywallRestoreBtn;

    //alerts
    @FindBy(xpath = "//*[@resource-id='android:id/message' and @text='Unable to restore purchases']")
    private ExtendedWebElement paywallRestorePurchaseDialog;

    @FindBy(xpath = "//*[@resource-id='android:id/button2' and @text='DISMISS' or @text='Dismiss']")
    private ExtendedWebElement paywallRestorePurchaseDialogDissmissBtn;

    public EspnEpaywallPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened(){
        clearPaywallUnableToRestorePurchaseDialog();
        return paywallConditionsText.isElementPresent();
    }

    /** ESPN - open Log In screen **/
    public EspnLoginPageBase openLoginScreen(){
        paywallLogInBtn.clickIfPresent();

        return initPage(EspnLoginPageBase.class);
    }

    /** ESPN > E+ Paywall > check for presence of paywallConditionsText  **/
    public boolean isPaywallFreeTrialTextPresent() {

        return paywallConditionsText.isElementPresent();
    }

    /** ESPN > E+ Paywall > check for presence of paywallMonthlySubscriptionBtn  **/
    public boolean isPaywallMonthlySubscriptionBtnPresent() {

        return paywallMonthlySubscriptionBtn.isElementPresent();
    }

    /** ESPN > E+ Paywall > check for presence of paywallLogInBtn  **/
    public boolean isPaywallLogInBtnPresent() {

        return paywallLogInBtn.isElementPresent();
    }

    /** ESPN > E+ Paywall > check for presence of paywallRestoreBtn  **/
    public boolean isPaywallRestoreBtn() {

        return paywallRestoreBtn.isElementPresent();
    }

    /** ESPN > E+ Paywall > clear Unable to restore purchase dialog (currently appears after logging out)  **/
    public void clearPaywallUnableToRestorePurchaseDialog() {

        if (paywallRestorePurchaseDialog.isElementPresent(DELAY)){
            paywallRestorePurchaseDialogDissmissBtn.click();
        }
    }

}

