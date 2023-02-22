package com.disney.qa.disney.android.pages.tv.pages;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVCommonPage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusAndroidTVLegalPageBase extends DisneyPlusAndroidTVCommonPage {

    @FindBy(id = "legalContentTextView")
    private ExtendedWebElement legalTextView;

    @FindBy(xpath = "//android.widget.TextView[@resource-id = 'legal_title' and @text = '%s']")
    private ExtendedWebElement legalButtons;

    @FindBy(id = "legal_title")
    private ExtendedWebElement legalBtnsId;

    @FindBy(id = "legalContentTitle")
    private ExtendedWebElement legalContentTitle;

    @FindBy(xpath = "//*[@resource-id= 'com.disney.disneyplus:id/legalTitlesRoot']/*")
    private ExtendedWebElement legalScreenRootView;

    public DisneyPlusAndroidTVLegalPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return legalBtnsId.isElementPresent();
    }

    public enum LegalItems {
        ALL_LEGAL_DOCUMENTS("links[*]"),
        GET_ALL_LABELS("$..label"),
        GET_ALL_DOCUMENT_CODES("$..documentCode"),
        LEGAL_TITLE("legalcenter_title");
        private String dictionaryKey;

        LegalItems(String dictionaryKey) {
            this.dictionaryKey = dictionaryKey;
        }

        public String getText() {
            return this.dictionaryKey;
        }
    }

    public List<String> getAllLegalButtonsText() {
        return findExtendedWebElements(legalBtnsId.getBy())
                .stream()
                .map(ExtendedWebElement::getText)
                .collect(Collectors.toList());
    }

    public String getDocumentTitle() {
        return legalContentTitle.getText();
    }

    public boolean isDocumentTitlePresent() {
        return legalContentTitle.isElementPresent();
    }

    public String getDocumentText() {
        String text = legalTextView.getText();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return text;
    }

    public boolean isLegalButtonFocused(int index) {
        List<ExtendedWebElement> legalDocButtons = findExtendedWebElements(legalScreenRootView.getBy());
        legalDocButtons.remove(0);
        return new AndroidTVUtils(getDriver()).isElementFocused(legalDocButtons.get(index));
    }

    public String getLegalScreenTitle() {
        String text = title.getText();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return text;
    }
}
