package com.disney.qa.espn.android.pages.common;

import com.disney.qa.common.DisneyAbstractPage;
import org.openqa.selenium.WebDriver;

/**
 * Base Android Page for 'ESPN' Application
 *
 * @author bzayats
 */

public abstract class EspnPageBase extends DisneyAbstractPage {

    protected static final int MINIMAL_TIMEOUT = 1;

    protected EspnCommonPageBase commonPage;

    public EspnPageBase(WebDriver driver) {
        super(driver);
        commonPage = initPage(EspnCommonPageBase.class);
    }

    @Override
    public boolean isOpened() {
        return commonPage.isOpened();
    }

    public boolean isMenuExists(String tab) {
        return commonPage.isFooterTabEnabled(tab);
    }

    public String getPageHeaderText(){
        return commonPage.getPageHeaderText();
    }


    /**
     * clickNavUp
     */
    public void clickNavUpBtn() {
        commonPage.clickNavUpBtn();
    }
}
