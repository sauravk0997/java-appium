package com.disney.qa.disney.android.pages.common;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusSubscriberAgreementPageBase extends DisneyPlusCommonPageBase {

    public DisneyPlusSubscriberAgreementPageBase(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "disclosureReviewLayout")
    protected ExtendedWebElement disclosureReviewLayout;

    @Override
    public boolean isOpened() {
        return disclosureReviewLayout.isVisible();
    }
}
