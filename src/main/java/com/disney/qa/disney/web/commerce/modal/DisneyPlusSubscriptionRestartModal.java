package com.disney.qa.disney.web.commerce.modal;

import com.disney.qa.disney.web.common.DisneyPlusBaseModal;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusSubscriptionRestartModal extends DisneyPlusBaseModal {

    @FindBy(xpath = "//*[@data-testid='consent-restart-subscription-modal']")
    protected ExtendedWebElement modalContainer;

    @FindBy(linkText = "disneyplus.com/priceinfo")
    private ExtendedWebElement btnSignOut;

    public DisneyPlusSubscriptionRestartModal(WebDriver driver) {
        super(driver);
        modalName = "Confirm Restart Modal";
        modalTitle.setBy(By.xpath("//span[contains(text(),'Confirm Restart')]"));
        modalPrimaryButton = new ExtendedWebElement(By.xpath("//*[@data-testid='cta-button']"),
                "title", driver, driver);
    }
}
