package com.disney.qa.disney.android.pages.common;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusBrandPageBase extends DisneyPlusCommonPageBase {

    @FindBy(id = "brandBackgroundImageView")
    protected ExtendedWebElement brandBackground;

    @FindBy(id = "poster")
    private ExtendedWebElement mediaPoster;

    @FindBy(id = "brandLogoImageView")
    private ExtendedWebElement brandLogoImage;

    @FindBy(id = "poster")
    private List<ExtendedWebElement> brandCollectionList;

    public DisneyPlusBrandPageBase(WebDriver driver){
        super (driver);
    }

    @Override
    public boolean isOpened(){
        if(isBrandCollectionListPopulated()) {
            LOGGER.info("Brand page is opened");
            return true;
        }
        return false;
    }

    public void selectFirstMediaItemFound(){
        mediaPoster.click();
    }

    public ExtendedWebElement getBrandLogoImage() {
        pause(SHORT_TIMEOUT);
        return brandLogoImage;
    }

    public Boolean isBrandCollectionListPopulated() {
        AtomicBoolean isListPopulated = new AtomicBoolean(false);

        try {
            isListPopulated.set(fluentWait(getDriver(), LONG_TIMEOUT, SHORT_TIMEOUT, "Brand page was empty")
                    .until(it -> !brandCollectionList.isEmpty()));
        } catch (TimeoutException exception) {
            LOGGER.error("Brand collection list never populated");
            isListPopulated.set(false);
        }

        return isListPopulated.get();
    }

    public String getBrandLogoDesc() {
        return brandLogoImage.getAttribute("content-desc");
    }
}
