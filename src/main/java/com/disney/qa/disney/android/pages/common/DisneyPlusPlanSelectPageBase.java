package com.disney.qa.disney.android.pages.common;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusPlanSelectPageBase extends DisneyPlusCommonPageBase {

    @FindBy(id = "header")
    protected ExtendedWebElement header;

    @FindBy(id = "subheader")
    protected ExtendedWebElement subHeader;

    @FindBy(id = "selectButton")
    protected ExtendedWebElement selectBtn;

    @FindBy(id = "standardButtonContainer")
    protected ExtendedWebElement standardButtonContainer;

    @FindBy(id = "sub_selector_standalone_ads_cta")
    protected ExtendedWebElement basicSelectBtn;

    @Override
    public boolean isOpened() {
        return header.isElementPresent();
    }

    public void clickPlanSelect(Integer position) {
        List<WebElement> plansList = getDriver().findElements(By.id(String.valueOf(standardButtonContainer)));
        try {
            plansList.get(position).click();
        } catch (NullPointerException nullPointerException) {
            LOGGER.error("clickPlanSelect():", nullPointerException);
            Assert.fail("Position " + position + " is null and unable to click plan button");
        }
    }

    public DisneyPlusPlanSelectPageBase(WebDriver driver) {
        super(driver);
    }
}
