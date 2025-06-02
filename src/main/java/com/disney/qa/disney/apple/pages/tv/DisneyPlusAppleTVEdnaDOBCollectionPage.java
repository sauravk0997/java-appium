package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.apple.pages.common.DisneyPlusEdnaDOBCollectionPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusEdnaDOBCollectionPageBase.class)
public class DisneyPlusAppleTVEdnaDOBCollectionPage extends DisneyPlusEdnaDOBCollectionPageBase {

    public DisneyPlusAppleTVEdnaDOBCollectionPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void enterDOB(DateHelper.Month month, String day, String year) {
        String monthNumber = month.getNum();
        String fullDate = String.join("", monthNumber, day, year);

        if (fullDate.length() != 8) {
            throw new InvalidArgumentException("Given Birthdate was invalid");
        }
        for (char number : fullDate.toCharArray()) {
            dynamicBtnFindByLabel.format(number).click();
        }
    }
}
