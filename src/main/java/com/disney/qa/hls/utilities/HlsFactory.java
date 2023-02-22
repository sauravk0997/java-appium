package com.disney.qa.hls.utilities;

import com.disney.qa.common.web.SeleniumUtils;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class HlsFactory extends AbstractPage {

    SeleniumUtils su;

    /** Page Object Elements **/

    /** Default BAMHLS Test Harnness Elements **/

    @FindBy(xpath = "//h2[text()='Web Media Test Harness']")
    protected ExtendedWebElement titlePage;

    @FindBy (id = "showPayload")
    protected ExtendedWebElement showEventPayloadChkBx;

    @FindBy (xpath = "//div[@class='video-overlays-container']")
    protected ExtendedWebElement videoPlayerOverlay;

    @FindBy (xpath = "//pre")
    protected ExtendedWebElement iEGrab;

    @FindBy (xpath = "//select[@id='bam-hls-version']//option[contains (text(), 'development')]")
    protected ExtendedWebElement bamHlsVersion;

    @FindBy (css = "Body")
    protected ExtendedWebElement body;

    @FindBy (xpath = "//input[@id='playerWidthSlider']")
    protected ExtendedWebElement playerWidthSlider;

    @FindBy (id = "loadMedia")
    protected ExtendedWebElement loadMediaBtn;

    @FindBy(xpath = "//div[@class='alert-message-text']//li[1]")
    protected ExtendedWebElement alertMessageTxt;


    /** End of Page Object Elements **/

    protected HlsFactory (WebDriver driver) {
        super(driver);
        su = new SeleniumUtils(driver);
    }

    /** Element is present Methods **/

    public Boolean isMediaPlayerPresent() {
        return videoPlayerOverlay.isElementPresent();
    }

    /** End Element is Present Methods **/

    public Object getBamHlsVersion() {
        bamHlsVersion.isElementPresent(90);
        return bamHlsVersion.getText();
    }
}