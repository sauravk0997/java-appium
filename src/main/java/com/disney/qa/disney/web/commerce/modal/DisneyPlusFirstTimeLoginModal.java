package com.disney.qa.disney.web.commerce.modal;

import com.disney.qa.disney.web.common.DisneyPlusBaseModal;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusFirstTimeLoginModal extends DisneyPlusBaseModal {

    public DisneyPlusFirstTimeLoginModal(WebDriver driver) {
        super(driver);
        modalName = "First Time Login Modal";
        modalTitle.setBy(By.xpath("//h4[contains(text(),'First time logging in')]"));
    }
}
