package com.disney.qa.disney.android.pages.common;

import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusEditorialPageBase extends DisneyPlusCommonPageBase{

    @FindBy(id = "editorialLogoImageView")
    private ExtendedWebElement editorialTitleImage;

    @FindBy(id = "editorialBackgroundImageView")
    private ExtendedWebElement editorialBackgroundImage;

    @FindBy(id = "poster")
    private ExtendedWebElement mediaPoster;

    @FindBy(id = "poster")
    private List<ExtendedWebElement> mediaPosters;

    @FindBy(xpath = "//*[contains(@content-desc, '%s')]")
    private ExtendedWebElement describedPoster;

    public DisneyPlusEditorialPageBase(WebDriver driver){
        super(driver);
    }

    @Override
    public boolean isOpened(){
        return editorialTitleImage.isElementPresent();
    }

    public boolean isBackgroundPresent(){
        return editorialBackgroundImage.isElementPresent();
    }

    public boolean isMediaItemDisplayed(String contentDesc){
        List<Boolean> foundSegment = new ArrayList<>();
        for(String segment : contentDesc.split("'")){
            foundSegment.add(new AndroidUtilsExtended().swipe(describedPoster.format(segment)));
        }
        return !foundSegment.contains(false);
    }
}
