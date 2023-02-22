package com.disney.qa.tests.espn.android;

import com.disney.qa.espn.EspnParameter;
import com.disney.qa.espn.android.pages.authentication.EspnAddFavoritesPageBase;
import com.disney.qa.espn.android.pages.authentication.EspnAddMoreFavoritesPageBase;
import com.disney.qa.espn.android.pages.authentication.EspnFirstTimeLaunchPageBase;
import com.disney.qa.espn.android.pages.authentication.EspnLoginPageBase;
import com.disney.qa.espn.android.pages.common.EspnCommonPageBase;
import com.disney.qa.espn.android.pages.home.EspnHomePageBase;
import com.disney.qa.espn.android.pages.paywall.EspnEpaywallPageBase;
import com.disney.qa.espn.android.pages.settings.EspnSettingsPageBase;
import com.disney.qa.espn.android.pages.watch.EspnWatchPageBase;
import com.disney.qa.tests.BaseMobileTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * Generic Class for Methods that can be utilized by the different RIOT Tests
 *
 * @author bzayats
 */
public class BaseEspnTest extends BaseMobileTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String LOGIN_USER_WITH_FULL_ENTITLEMENTS = EspnParameter.ESPN_DEFAULT_USER.getValue();
    private static final String PASS_USER_WITH_FULL_ENTITLEMENTS = EspnParameter.ESPN_DEFAULT_PASSWORD.getDecryptedValue();
    private static final String LOGIN_USER_NO_ENTITLEMENTS = EspnParameter.ESPN_NO_ENTITLEMENTS_USER.getValue();
    private static final String LOGIN_USER_BASE_ENTITLEMENTS = EspnParameter.ESPN_BASE_E_USER.getValue();
    private static final String LOGIN_USER_QE = EspnParameter.ESPN_QE_USER.getValue();
    private static final String LOGIN_PASS_QE = EspnParameter.ESPN_QE_PASS.getDecryptedValue();
    private static final String APP_PACKAGE = "com.espn.score_center";
    private static final String APP_LAUNCH_ACTIVITY = "com.espn.sportscenter.ui.LaunchActivity";

    /** ESPN > watch tabs Enum **/
    public enum AcctTypes{
        BASE_E("baseE"),
        BASE_E_ENTITLEMENTS("baseE_entitlements"),
        NO_BASE_NO_ENTITLEMENTS("no_base_no_entitlements"),
        QE("qe");

        private String acct;

        AcctTypes(String tab){
            this.acct = tab;
        }

        public String getText(){

            return acct;
        }
    }

    /** ESPN > login flow with existing user acct. **/
    protected void espnLogin(String acctType) {
        EspnEpaywallPageBase paywallPageBase = initPage(EspnEpaywallPageBase.class);
        EspnWatchPageBase watchPageBase = initPage(EspnWatchPageBase.class);
        EspnSettingsPageBase settingsPageBase = initPage(EspnSettingsPageBase.class);

        if (paywallPageBase.isOpened()){
            paywallPageBase.openLoginScreen();
            login(acctType);

            return;
        }

        if (watchPageBase.isOpened()){
            watchPageBase.openSettingsPage();

            if (settingsPageBase.isOpened()){
                settingsPageBase.logInToESPNacct();
                login(acctType);

                initPage(EspnCommonPageBase.class).clickNavUpBtn();
            }
        }
    }

    /** login helper method **/
    public void login(String acctType){

        switch(acctType){
            case "baseE":
                initPage(EspnLoginPageBase.class).login(LOGIN_USER_BASE_ENTITLEMENTS, PASS_USER_WITH_FULL_ENTITLEMENTS);
                break;
            case "baseE_entitlements":
                initPage(EspnLoginPageBase.class).login(LOGIN_USER_WITH_FULL_ENTITLEMENTS, PASS_USER_WITH_FULL_ENTITLEMENTS);
                break;
            case "no_base_no_entitlements":
                initPage(EspnLoginPageBase.class).login(LOGIN_USER_NO_ENTITLEMENTS, PASS_USER_WITH_FULL_ENTITLEMENTS);
                break;

            case "qe":
                initPage(EspnLoginPageBase.class).login(LOGIN_USER_QE, LOGIN_PASS_QE);
                break;

                default:
                    initPage(EspnLoginPageBase.class).login(LOGIN_USER_WITH_FULL_ENTITLEMENTS, PASS_USER_WITH_FULL_ENTITLEMENTS);
                    break;
        }
    }

    /** ESPN > first time launch login flow with existing user acct. **/
    protected void espnInitialLogin() {
        espnInitialLogin(AcctTypes.BASE_E.getText());
    }

    /** ESPN > first time launch login flow with existing user acct. **/
    protected void espnInitialLogin(String acctType) {
        LOGGER.info("'Welcome Page' is displayed with corresponding elements");
        EspnFirstTimeLaunchPageBase firstTimeLaunchPageBase = initPage(EspnFirstTimeLaunchPageBase.class);

        firstTimeLaunchPageBase.openLoginScreen();
        login(acctType);

        initPage(EspnAddFavoritesPageBase.class).openAnyMoreFavoritesScreen();

        initPage(EspnAddMoreFavoritesPageBase.class).openHomeScreen();
    }

    /** ESPN > login flow with existing user acct. **/
    protected void espnLogOut() {
        LOGGER.info("Logging out.. ");
        EspnWatchPageBase watchPageBase = initPage(EspnWatchPageBase.class);
        EspnSettingsPageBase settingsPageBase = initPage(EspnSettingsPageBase.class);

        watchPageBase.openSettingsPage();

        if (settingsPageBase.isOpened()){
            settingsPageBase.logOutFromESPNacct();
            initPage(EspnCommonPageBase.class).clickNavUpBtn();
        }
    }

    /** Internal Setup for test cases acting as a BeforeMethod() with footer tab option **/
    protected void internalSetup(String appFooterTab){
        internalSetup(AcctTypes.BASE_E.getText(), appFooterTab);
    }

    /** Internal Setup for test cases acting as a BeforeMethod() **/
    protected void internalSetup(){
        internalSetup(AcctTypes.BASE_E.getText(), EspnCommonPageBase.FooterTabs.ESPN_TAB.getText());
    }

    /** Internal Setup for test cases acting as a BeforeMethod() **/
    protected void internalSetup(String acctType, String appFooterTab){
        //DO NOT remove this commented out line
//        relaunchApp();

        EspnHomePageBase homePageBase = initPage(EspnHomePageBase.class);
        EspnFirstTimeLaunchPageBase firstTimeLaunchPageBase = initPage(EspnFirstTimeLaunchPageBase.class);
        EspnCommonPageBase commonPageBase = initPage(EspnCommonPageBase.class);

        if (homePageBase.isOpened()){
            LOGGER.info("homePageBase is open");
            commonPageBase.navigateToPage(appFooterTab);
        } else {
            initPage(    EspnCommonPageBase.class).handleAccessToMediaAlert(true);
            initPage(    EspnCommonPageBase.class).handleLocationAlert(true);

            if (firstTimeLaunchPageBase.isOpened()) {
                espnInitialLogin(acctType);
                commonPageBase.navigateToPage(appFooterTab);
            }
        }
    }

}
