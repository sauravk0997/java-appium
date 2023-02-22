package com.disney.qa.disney.web.common;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusBaseModal extends DisneyPlusBasePage{

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected String modalName;

    @FindBy(xpath = "//h4")
    protected ExtendedWebElement modalTitle;

    @FindBy(xpath = "//p")
    protected ExtendedWebElement modalBody;

    @FindBy(css = "button[aria-label='Close']")
    protected ExtendedWebElement modalCloseButton;

    @FindBy(xpath = "//*[@data-testid='modal-primary-button']")
    protected ExtendedWebElement modalPrimaryButton;

    @FindBy(xpath = "//*[@data-testid='modal-secondary-button']")
    protected ExtendedWebElement modalSecondaryButton;

    public DisneyPlusBaseModal(WebDriver driver) {
        super(driver);
    }

    public boolean verifyModal() {
        LOGGER.info("Verify {} loaded", modalName);
        return modalTitle.isVisible();
    }

    public boolean verifyModal(String modalName) {
        LOGGER.info("Verify {} loaded", modalName);
        return modalTitle.isVisible();
    }

    public boolean isModalBodyVisible() {
        LOGGER.info("Is modal body visible");
        return modalBody.isVisible();
    }

    public DisneyPlusBaseModal clickModalCloseButton() {
        LOGGER.info("Click modal close button");
        modalCloseButton.click();
        return this;
    }

    public DisneyPlusBaseModal clickModalPrimaryButton() {
        LOGGER.info("Click modal primary button");
        modalPrimaryButton.clickByJs();
        return this;
    }

    public DisneyPlusBaseModal clickModalSecondaryButton() {
        LOGGER.info("Click modal secondary button");
        modalSecondaryButton.click();
        return this;
    }
}
