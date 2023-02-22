package com.disney.qa.disney.web.appex.footer;

import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusFooterPage extends DisneyPlusBasePage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public DisneyPlusFooterPage(WebDriver driver) {
        super(driver);
    }

    //Strings

    private static final String PRIVACY_POLICY_URL_ASSERT = "legal/privacy-policy";
    private static final String SUBSCRIBER_AGREEMENT_URL_ASSERT = "legal/subscriber-agreement";
    private static final String CALIFORNIA_PRIVACY_URL_ASSERT = "your-california-privacy-rights";
    private static final String DO_NOT_SELL_MY_INFO_URL_ASSERT = "dnsmi";
    private static final String CHILDRENS_ONLINE_PRIVACY_URL_ASSERT = "childrens-online-privacy-policy";
    private static final String HELP_URL_ASSERT = "help.disneyplus.com/csp";
    private static final String GIFT_DISNEY_PLUS_URL_ASSERT = "subscriptioncard.disneyplus.com";

    //Web Elements

    @FindBy(id = "footer")
    private ExtendedWebElement footerId;

    @FindBy(xpath = "//button[text()='Privacy Policy']")
    private ExtendedWebElement privacyPolicyBtn;

    @FindBy(xpath = "//button[text()='Subscriber Agreement']")
    private ExtendedWebElement subscriberAgreementBtn;

    @FindBy(xpath = "//button[text()='Your California Privacy Rights']")
    private ExtendedWebElement californiaPrivacyRightsBtn;

    @FindBy(xpath = "//button[contains(text(),'Sell')]")
    private ExtendedWebElement doNotSellMyInfoBtn;

    @FindBy(xpath = "//button[contains(text(),'Children')]")
    private ExtendedWebElement childrensOnlinePrivacyPolictyBtn;

    @FindBy(xpath = "//button[text()='Help']")
    private ExtendedWebElement helpBtn;

    @FindBy(xpath = "//button[text()='Closed Captioning']")
    private ExtendedWebElement closedCaptioningBtn;

    @FindBy(xpath = "//h2[text()='Closed Captioning Inquiries']")
    private ExtendedWebElement closedCaptioningInquiriesHeader;

    @FindBy(xpath = "//button[text()='Ways to Watch']")
    private ExtendedWebElement waysToWatchBtn;

    @FindBy(xpath = "//h2[text()='What devices and platforms are supported by Disney+?']")
    private ExtendedWebElement waysToWatchHeader;

    @FindBy(xpath = "//button[text()='Gift Disney+']")
    private ExtendedWebElement giftDisneyPlusBtn;

    @FindBy(xpath = "//button[text()='About Us']")
    private ExtendedWebElement aboutUsBtn;

    @FindBy(xpath = "//h2[text()='What is Disney+?']")
    private ExtendedWebElement whatIsDisneyPlusHeader;

    @FindBy(xpath = "//button[text()='Interest-based Ads']")
    private ExtendedWebElement interestBasedAds;

    @FindBy(xpath = "//h1[text()='Your Advertising Choices']")
    private ExtendedWebElement yourAdvertisingChoicesHeader;

    public ExtendedWebElement getFooterId() {
        return footerId;
    }

    public ExtendedWebElement getPrivacyPolicyBtn() {
        return privacyPolicyBtn;
    }

    public ExtendedWebElement getSubscriberAgreementBtn() {
        return subscriberAgreementBtn;
    }

    public ExtendedWebElement getCaliforniaPrivacyRightsBtn() {
        return californiaPrivacyRightsBtn;
    }

    public ExtendedWebElement getDoNotSellMyInfoBtn() {
        return doNotSellMyInfoBtn;
    }

    public ExtendedWebElement getChildrensOnlinePrivacyPolictyBtn() {
        return childrensOnlinePrivacyPolictyBtn;
    }

    public ExtendedWebElement getHelpBtn() {
        return helpBtn;
    }

    public ExtendedWebElement getClosedCaptioningBtn() {
        return closedCaptioningBtn;
    }

    public ExtendedWebElement getWaysToWatchBtn() {
        return waysToWatchBtn;
    }

    public ExtendedWebElement getGiftDisneyPlusBtn() {
        return giftDisneyPlusBtn;
    }

    public ExtendedWebElement getAboutUsBtn() {
        return aboutUsBtn;
    }

    public ExtendedWebElement getInterestBasedAds() {
        return interestBasedAds;
    }

    public void assertFooterElements(SoftAssert sa) {

        String currentUrl = getCurrentUrl();
        String formattedAssertMessage = "%s footer button is not visible on %s";

        util.scrollToBottom();

        sa.assertTrue(getFooterId().isElementPresent(),
                String.format(formattedAssertMessage, "Footer", currentUrl));
        sa.assertTrue(getPrivacyPolicyBtn().isElementPresent(),
                String.format(formattedAssertMessage, "Privacy Policy", currentUrl));
        sa.assertTrue(getSubscriberAgreementBtn().isElementPresent(),
                String.format(formattedAssertMessage, "Subscriber Agreement", currentUrl));
        sa.assertTrue(getCaliforniaPrivacyRightsBtn().isElementPresent(),
                String.format(formattedAssertMessage, "California Subscriber Agreement", currentUrl));
        sa.assertTrue(getDoNotSellMyInfoBtn().isElementPresent(),
                String.format(formattedAssertMessage, "Do Not Sell My Info", currentUrl));
        sa.assertTrue(getChildrensOnlinePrivacyPolictyBtn().isElementPresent(),
                String.format(formattedAssertMessage, "Children's Online Privacy", currentUrl));
        sa.assertTrue(getHelpBtn().isElementPresent(),
                String.format(formattedAssertMessage, "Help", currentUrl));
        sa.assertTrue(getClosedCaptioningBtn().isElementPresent(),
                String.format(formattedAssertMessage, "Closed Captioning", currentUrl));
        sa.assertTrue(getWaysToWatchBtn().isElementPresent(),
                String.format(formattedAssertMessage, "Ways to Watch", currentUrl));
        sa.assertTrue(getGiftDisneyPlusBtn().isElementPresent(),
                String.format(formattedAssertMessage, "Gift Disney+", currentUrl));
        sa.assertTrue(getAboutUsBtn().isElementPresent(),
                String.format(formattedAssertMessage, "About Us", currentUrl));
        sa.assertTrue(getInterestBasedAds().isElementPresent(),
                String.format(formattedAssertMessage, "Interest-based Ads", currentUrl));

    }

    public void clickAllFooterElementsAssert(SoftAssert sa) {

        clickAssertUrlBack(sa, privacyPolicyBtn, PRIVACY_POLICY_URL_ASSERT);
        clickAssertUrlBack(sa, subscriberAgreementBtn, SUBSCRIBER_AGREEMENT_URL_ASSERT);
        clickAssertUrlBack(sa, californiaPrivacyRightsBtn, CALIFORNIA_PRIVACY_URL_ASSERT);
        clickAssertUrlBack(sa, doNotSellMyInfoBtn, DO_NOT_SELL_MY_INFO_URL_ASSERT);
        clickAssertUrlBack(sa, childrensOnlinePrivacyPolictyBtn, CHILDRENS_ONLINE_PRIVACY_URL_ASSERT);
        clickAssertUrlBack(sa, helpBtn, HELP_URL_ASSERT);
        clickAssertUrlBack(sa, giftDisneyPlusBtn, GIFT_DISNEY_PLUS_URL_ASSERT);
        clickAssertElementBack(sa, closedCaptioningBtn, closedCaptioningInquiriesHeader);
        clickAssertElementBack(sa, waysToWatchBtn, waysToWatchHeader);
        clickAssertElementBack(sa, aboutUsBtn, whatIsDisneyPlusHeader);
        clickAssertElementBack(sa, interestBasedAds, yourAdvertisingChoicesHeader);

    }

    //Clicks an element, assert the url with a contains, navigate back.

    public void clickAssertUrlBack(SoftAssert sa, ExtendedWebElement e, String assertion) {

        String assertionUrl;
        e.click();
        waitForPageToFinishLoading();
        List<String> tabs = new ArrayList<>();

        try {
            tabs = new ArrayList<>(driver.getWindowHandles());
        } catch (IndexOutOfBoundsException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        if (tabs.size() > 1) {

            LOGGER.info("Link opened up in new tab, switching tab to grab url");
            driver.switchTo().window(tabs.get(1));
            waitForPageToFinishLoading();
            assertionUrl = getCurrentUrl();
            driver.close();
            driver.switchTo().window(tabs.get(0));

        } else {

            assertionUrl = getCurrentUrl();
        }

        sa.assertTrue(assertionUrl.contains(assertion),
                assertionUrlString(assertionUrl, assertion));

        if (tabs.isEmpty()) {
            driver.navigate().back();
        }

    }

    //Clicks an element, asserts an element from opened page, navigates to the previous page

    private void clickAssertElementBack(SoftAssert sa, ExtendedWebElement e, ExtendedWebElement assertElement) {

        e.click();
        List<String> tabs = new ArrayList<>();

        try {

            tabs = new ArrayList<>(driver.getWindowHandles());

        } catch (IndexOutOfBoundsException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        if (tabs.size() > 1) {

            LOGGER.info("Link opened up in new tab, switching tab to grab element for assertion");
            driver.switchTo().window(tabs.get(1));
            sa.assertTrue(assertElement.isElementPresent(),
                    String.format("Assert Element %s, not visible after clicking %s", assertElement.toString(), e.toString()));
            driver.close();
            driver.switchTo().window(tabs.get(0));

        } else {

            sa.assertTrue(assertElement.isElementPresent(),
                    String.format("Assert Element: %s, not visible after clicking %s", assertElement, e));
        }

        if (tabs.isEmpty()) {
            driver.navigate().back();
        }

    }
}
