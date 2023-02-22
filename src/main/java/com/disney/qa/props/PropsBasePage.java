package com.disney.qa.props;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

public class PropsBasePage extends AbstractPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // Sign In

    @FindBy(css = "button[class='w-btn w-btn--default sign-on-btn']")
    private ExtendedWebElement signInSSO;

    @FindBy(css = "input[id='emailInput']")
    private ExtendedWebElement usernameInput;

    @FindBy(css = "input[id='passwordInput']")
    private ExtendedWebElement passwordInput;

    @FindBy(css = "input[class='submit']")
    private ExtendedWebElement submitOne;

    @FindBy(css = "span[class='submit']")
    private ExtendedWebElement submitTwo;

    // Dashboard general

    @FindBy(css = "div[class='w-app-selector__selected']")
    private ExtendedWebElement propsLogo;

    @FindBy(css = "div[class='w-partner-selector__selected']")
    private ExtendedWebElement partnerSelector;

    @FindBy(css = "div[class='w-partner-selector__menu']>div>div>ul>li")
    private List<ExtendedWebElement> partnerMenu;

    @FindBy(css = "div[class='w-section-selector w-section-selector--empty']>div")
    private ExtendedWebElement sectionSelector;

    @FindBy(css = "div[class='w-menu-list__item__link__label']>div>div>ul>li:nth-child(2)")
    private ExtendedWebElement sectionMenu;

    @FindBy(css = "button[class='w-btn w-btn--default']")
    private ExtendedWebElement plusButton;

    // USE indices 0, 1, OR 2 to indicate selection of "Direct billing product", "Third party product", or "EI product" when ready to use
    @FindBy(css = "div[class='w-overlay w-menu w-menu--contextual props-menu']>div>ul>li")
    private List<ExtendedWebElement> plusMenu;

    // PRODUCTS Dashboard specific

    @FindBy(css = "tr[class='products-table__row__primary live']")
    private ExtendedWebElement liveProduct;

    @FindBy(css = "div[class='appbar_notification warn']")
    private ExtendedWebElement editWarningModal;

    @FindBy(css = "div[class='w-modal__content w-modal__content--small']")
    private ExtendedWebElement deactivateWarningModal;

    @FindBy(css = "span[class='w-avatar']")
    private ExtendedWebElement heroIcon;

    @FindBy(css = "div[class='w-overlay w-menu w-menu--contextual avatar_menu']>div>ul>li")
    private List<ExtendedWebElement> heroIconMenu;

    // USE text "All groups" OR "All entitlements" OR "All platforms" OR "All products" OR "All territories" when ready to use
    @FindBy(css = "div[class='w-multiselect__values']")
    private List<ExtendedWebElement> filterDropdown;

    @FindBy(css = "span[class='product-select-unselect-text']")
    private ExtendedWebElement selectAllToggle;

    @FindBy(css = "span[class='product-report-btn']")
    private ExtendedWebElement productReport;

    @FindBy(css = "div[class='w-overlay w-menu w-menu--contextual props-menu']")
    private ExtendedWebElement generateCsv;

    @FindBy(css = "span[class='MuiButtonBase-root MuiIconButton-root jss3 MuiCheckbox-root MuiCheckbox-colorPrimary entitlement-header-checkbox_component MuiIconButton-colorPrimary']")
    private ExtendedWebElement checkbox;

    @FindBy(css = "span[class='w-search list_header_search_box']")
    private ExtendedWebElement searchBar;
    
    @FindBy(css = "div[class='products-table_primary_div']")
    private ExtendedWebElement productRow;

    // Entitlement Dashboard

    @FindBy(css = "div[class='entitlement-row']")
    private ExtendedWebElement entitlementRow;

    /* USE text "Description" OR "DSS Entitlement ID" when ready to use
    Otherwise, leave no text to indicate "Name" column */
    @FindBy(css = "span[class='entitlement-cell']")
    private ExtendedWebElement entitlementCell;

    @FindBy(css = "div[class='entitlement-row view']")
    private List<ExtendedWebElement> entitlementBody;

    @FindBy(css = "span[class='entitlement-cell id']")
    private List<ExtendedWebElement> entitlementID;

    @FindBy(css = "span[class='w-icon history-btn']")
    private ExtendedWebElement entitlementHistory;

    // IF user type is Super Admin AND user has selected 'User Administration' from profileIcon

    @FindBy(css = "div[class='props-dropdown w-multiselect not-multi']")
    private ExtendedWebElement userDropdown;

    @FindBy(css = "span[class='w-input w-input--undefined w-input--email w-input--medium w-input--fullwidth']>input")
    private ExtendedWebElement userEmail;

    @FindBy(css = "div[class='props-dropdown w-multiselect medium not-multi']")
    private ExtendedWebElement userRoleDropdown;

    @FindBy(css = "div[class='props-dropdown w-multiselect medium']")
    private ExtendedWebElement userPartnerDropdown;

    // IF user type is approver AND user has selected a product from dashboard for financial approval

    @FindBy(css = "div[class='content-form']")
    private ExtendedWebElement financeMetadata;

    @FindBy(css = "span[class='status-badge_name']")
    private List<ExtendedWebElement> statusBadge;

    @FindBy(css = "input[type='text']")
    private ExtendedWebElement issueID;

    @FindBy(css = "div[class='w-field w-field--medium w-field--default accounting-recurring-type']>div>div>span>select")
    private ExtendedWebElement accountingRecurringType;

    @FindBy(css = "div[class='w-field w-field--medium w-field--default tax-category']>div>div>span>select")
    private ExtendedWebElement taxCategory;

    @FindBy(css = "div[class='w-field w-field--medium w-field--default is-taxable switch']>div>div>label>span")
    private ExtendedWebElement isTaxableSwitch;

    // IF user is on External Integration product form

    @FindBy(xpath = "//textarea[@placeholder='Untitled product']")
    private ExtendedWebElement productName;

    @FindBy(xpath = "//span[@class='w-input w-input--undefined w-input--text w-input--medium w-input--fullwidth']//input")
    private ExtendedWebElement productKey;

    @FindBy(css = "div[class='w-field w-field--medium w-field--default jira-details']>div>div>div")
    private ExtendedWebElement productJiraURL;

    @FindBy(css = "div[class='w-field w-field--medium w-field--default platform']>div>div>div>div")
    private ExtendedWebElement productPlatform;

    // USE text such as "Adobe Pass" for platform menu and text such as "GUNSNROSES________" for entitlements menu
    @FindBy(css = "span[class='w-menu-list__item__link__label']")
    private ExtendedWebElement menuOption;

    @FindBy(css = "span[class='w-menu-list__item__link__label']")
    private List<ExtendedWebElement> menuOptionList;

    @FindBy(css = "div[class='w-field w-field--medium w-field--default entitlements']>div>div>div>div")
    private ExtendedWebElement productEntitlement;

    @FindBy(css = "div[class='w-field w-field--medium w-field--default recurs switch']>div")
    private ExtendedWebElement subscriptionRecurSwitch;

    // USE text "Yearly" OR "Monthly" OR "Daily" when ready to use
    @FindBy(css = "div[class='w-field w-field--medium w-field--default subscription-time-unit']>div>div>span>label>span")
    private ExtendedWebElement subscriptionPeriod;

    @FindBy(css = "div[class='props-dropdown w-multiselect medium not-multi']")
    private ExtendedWebElement productCurrency;

    @FindBy(css = "span[class='w-input w-input--undefined w-input--number w-input--medium w-input--fullwidth']")
    private ExtendedWebElement productPrice;

    @FindBy(css = "div[class='w-field w-field--medium w-field--default countries']>div>div>div>div")
    private ExtendedWebElement productTerritory;

    @FindBy(css = "div[class='w-field w-field--medium w-field--default has-trial switch']>div")
    private ExtendedWebElement freeTrialSwitch;

    @FindBy(css = "div[class='w-field w-field--medium w-field--default function']>div")
    private ExtendedWebElement productFunction;

    // CONTAINS clone button
    @FindBy(css = "div[class='product-options-menu']")
    private ExtendedWebElement productOptions;

    // FOUND IN product options
    @FindBy(css = "div[class='product-options-menu']")
    private ExtendedWebElement cloneMenu;

    @FindBy(css = "div[class='w-overlay w-menu w-menu--default clone_menu']>div>ul>li>span")
    private ExtendedWebElement cloneButton;

    @FindBy(css = "div[class='product-comment-icon']")
    private ExtendedWebElement commentIcon;

    // IF user has clicked on comment icon and wants to leave a comment on product
    @FindBy(css = "div[class='comment-entry-input']>span")
    private ExtendedWebElement leaveComment;

    @FindBy(css = "div[class='product-history-icon']")
    private ExtendedWebElement productHistory;

    // IF user is viewing live product
    @FindBy(css = "button[class='w-btn w-btn--default deactivate-btn']")
    private ExtendedWebElement deactivateButton;

    // IF user is viewing not-live product
    @FindBy(css = "button[class='w-btn w-btn--danger']")
    private ExtendedWebElement approveLiveButton;

    @FindBy(css = "span[class='groups_link']")
    private ExtendedWebElement addToGroups;

    // IF user is able to create entitlements AND is on the Entitlements dashboard AND has clicked on 'Create a new entitlement'

    @FindBy(css = "input[placeholder='Entitlement name']")
    private ExtendedWebElement entitlementName;

    @FindBy(css = "input[placeholder='Entitlement description']")
    private ExtendedWebElement entitlementDescription;

    @FindBy(css = "span[class='w-icon close-icon']")
    private ExtendedWebElement closeEntitlement;

    // IF user is viewing Paywalls dashboard

    @FindBy(css = "div[class='w-field w-field--medium w-field--default platform-filter selected']")
    private ExtendedWebElement paywallPlatformFilter;

    @FindBy(css = "div[class='w-field w-field--medium w-field--default country-filter selected']")
    private ExtendedWebElement paywallTerritoryFilter;

    // IF user is able to create paywalls AND has clicked on 'Create a third party paywall' OR 'Create a direct billing paywall'

    @FindBy(css = "textarea[placeholder='Untitled paywall']")
    private ExtendedWebElement paywallName;

    @FindBy(css = "div[class='w-field w-field--medium w-field--default jira-details']>div")
    private ExtendedWebElement paywallJira;

    @FindBy(css = "div[class='w-field w-field--medium w-field--default paywall-platform']>div")
    private ExtendedWebElement paywallPlatform;

    @FindBy(css = "div[class='w-field w-field--medium w-field--default paywall-countries']>div")
    private ExtendedWebElement paywallTerritory;

    @FindBy(css = "span[class='w-search dropdown_search_box']>span>input")
    private ExtendedWebElement paywallTerritorySearch;

    @FindBy(css = "div[class='w-field w-field--medium w-field--default']>div")
    private ExtendedWebElement paywallAssociatedTerritory;

    @FindBy(css = "div[class='w-field w-field--medium w-field--default subscription-confirmation-field']>div")
    private ExtendedWebElement paywallSubscriptionToggle;

    @FindBy(css = "div[class='w-field w-field--medium w-field--default context-dropdown']>div")
    private ExtendedWebElement userContext;

    @FindBy(css = "div[class='w-field w-field--medium w-field--default context-dropdown']>div>div>div>div>div>ul>li")
    private ExtendedWebElement userSelection;

    @FindBy(css = "div[class='w-field w-field--medium w-field--has-help-text w-field--default rules_container']>div")
    private ExtendedWebElement consumerPurchased;

    @FindBy(css = "div[class='w-field w-field--medium w-field--has-help-text w-field--default rules_container']>div>div>div>div>div>ul>li")
    private ExtendedWebElement consumerProductSelection;

    @FindBy(css = "div[class='w-field w-field--medium w-field--default mapping-product']>div")
    private ExtendedWebElement paywallProduct;

    @FindBy(css = "div[class='w-field w-field--medium w-field--default mapping-product']>div>div>div>div>div>ul>li")
    private ExtendedWebElement paywallProductSelection;

    @FindBy(css = "div[class='paywall-mapping-plus']>svg")
    private ExtendedWebElement addPaywallProduct;

    @FindBy(css = "div[class='paywall-mapping-button']>button")
    private ExtendedWebElement addPaywallMapping;

    @FindBy(css = "span[class='w-icon paywall-mapping-delete-btn']")
    private ExtendedWebElement deletePaywallMapping;

    // IF user is able to create paywalls AND has clicked on 'Create a direct billing paywall'

    @FindBy(css = "div[class='w-field w-field--medium w-field--has-help-text w-field--default campaign-code']>div")
    private ExtendedWebElement campaignCode;

    @FindBy(css = "div[class='w-field w-field--medium w-field--has-help-text w-field--default campaign-code']>div>div>div>div>div>ul>li")
    private ExtendedWebElement campaignSelection;

    @FindBy(css = "div[class='w-field w-field--medium w-field--has-help-text w-field--default voucher-code']>div")
    private ExtendedWebElement voucherCode;

    @FindBy(css = "div[class='w-field w-field--medium w-field--has-help-text w-field--default voucher-code']>div>div>div>div>div>ul>li")
    private ExtendedWebElement voucherSelection;

    // IF user is viewing an existing paywall form

    @FindBy(css = "button[class='w-btn w-btn--primary go-live-btn']")
    private ExtendedWebElement paywallGoLive;

    // IF user is creating paywall and has filled all necessary elements in the form

    @FindBy(css = "button[class='w-btn w-btn--default go-preview-btn']")
    private ExtendedWebElement paywallSave;


    // SAVE buttons for APPROVER approving financial review OR ADMIN creating EI product

    @FindBy(css = "button[class='w-btn w-btn--default']")
    private ExtendedWebElement saveButton;

    /* IF user is Super Admin in 'User Administration', THEN this is 'Submit'
    IF user is approving financial review, THEN this is 'Save and Approve'
    IF user is creating EI product, THEN this is 'Save and send to testing'*/
    @FindBy(css = "button[class='w-btn w-btn--primary']")
    private ExtendedWebElement saveAndButton;

    // IF user is editing an existing Entitlement OR creating a new Entitlement
    @FindBy(css = "button[class='w-btn w-btn--small w-btn--primary']")
    private ExtendedWebElement saveEntitlement;

    // IF user is viewing a product form OR user is viewing a paywall

    @FindBy(css = "button[class='w-btn w-btn--default edit-btn']")
    private ExtendedWebElement editButton;

    @FindBy(css = "span[class='material-icons edit_close_x']")
    private ExtendedWebElement editClose;

    // IF user is on Entitlement dashboard AND hovers over existing entitlement

    @FindBy(css = "button[class='w-btn w-btn--small w-btn--default edit-btn']")
    private List<ExtendedWebElement> editEntitlement;

    // STRINGS

    private String adminEmail = PropsParameter.PROPS_ADMIN_EMAIL.getValue();
    private String productAEmail = PropsParameter.PROPS_PRODUCT_EMAIL_A.getValue();
    private String productBEmail = PropsParameter.PROPS_PRODUCT_EMAIL_B.getValue();
    private String iapEmail = PropsParameter.PROPS_IAP_EMAIL.getValue();
    private String approverEmail = PropsParameter.PROPS_APPROVER_EMAIL.getValue();
    private String basicEmail = PropsParameter.PROPS_BASIC_EMAIL.getValue();
    private String password = PropsParameter.PROPS_PASSWORD.getDecryptedValue();

    // GETTERS

    public ExtendedWebElement getSignInSSO() {
        return signInSSO;
    }

    public ExtendedWebElement getUsernameInput() {
        return usernameInput;
    }

    public ExtendedWebElement getPasswordInput() {
        return passwordInput;
    }

    public ExtendedWebElement getSubmitOne() {
        return submitOne;
    }

    public ExtendedWebElement getSubmitTwo() {
        return submitTwo;
    }

    public ExtendedWebElement getPropsLogo() {
        return propsLogo;
    }

    public ExtendedWebElement getPartnerSelector() {
        return partnerSelector;
    }

    public ExtendedWebElement getPartnerMenu(int index) {
        return partnerMenu.get(index);
    }

    public ExtendedWebElement getSectionSelector() {
        return sectionSelector;
    }

    public ExtendedWebElement getSectionMenu() {
        return sectionMenu;
    }

    public ExtendedWebElement getPlusButton() {
        return plusButton;
    }

    public ExtendedWebElement getPlusMenu(int index) {
        return plusMenu.get(index);
    }

    public ExtendedWebElement getLiveProduct() {
        return liveProduct;
    }

    public ExtendedWebElement getEditWarningModal() {
        return editWarningModal;
    }

    public ExtendedWebElement getDeactivateWarningModal() {
        return deactivateWarningModal;
    }

    public ExtendedWebElement getFinanceMetadata() {
        return financeMetadata;
    }

    public List<ExtendedWebElement> getStatusBadge() {
        return statusBadge;
    }

    public ExtendedWebElement getIssueID() {
        return issueID;
    }

    public ExtendedWebElement getAccountingRecurringType() {
        return accountingRecurringType;
    }

    public ExtendedWebElement getTaxCategory() {
        return taxCategory;
    }

    public ExtendedWebElement getIsTaxableSwitch() {
        return isTaxableSwitch;
    }

    public ExtendedWebElement getHeroIcon() {
        return heroIcon;
    }

    public ExtendedWebElement getHeroIconMenu(int index) {
        return heroIconMenu.get(index);
    }

    public List<ExtendedWebElement> getFilterDropdown() {
        return filterDropdown;
    }

    public ExtendedWebElement getSelectAllToggle() {
        return selectAllToggle;
    }

    public ExtendedWebElement getProductReport() {
        return productReport;
    }

    public ExtendedWebElement getGenerateCsv() {
        return generateCsv;
    }

    public ExtendedWebElement getCheckbox() {
        return checkbox;
    }

    public ExtendedWebElement getSearchBar() {
        return searchBar;
    }

    public ExtendedWebElement getProductRow() {
        return productRow;
    }

    public ExtendedWebElement getEntitlementRow() {
        return entitlementRow;
    }

    public ExtendedWebElement getEntitlementCell() {
        return entitlementCell;
    }

    public ExtendedWebElement getEntitlementBody(int index) {
        return entitlementBody.get(index);
    }

    public ExtendedWebElement getEntitlementID(int index) {
        return entitlementID.get(index);
    }

    public ExtendedWebElement getEntitlementHistory() {
        return entitlementHistory;
    }

    public ExtendedWebElement getUserDropdown() {
        return userDropdown;
    }

    public ExtendedWebElement getUserEmail() {
        return userEmail;
    }

    public ExtendedWebElement getUserRoleDropdown() {
        return userRoleDropdown;
    }

    public ExtendedWebElement getUserPartnerDropdown() {
        return userPartnerDropdown;
    }

    public ExtendedWebElement getProductName() {
        return productName;
    }

    public ExtendedWebElement getProductKey() {
        return productKey;
    }

    public ExtendedWebElement getProductJiraURL() {
        return productJiraURL;
    }

    public ExtendedWebElement getProductPlatform() {
        return productPlatform;
    }

    public ExtendedWebElement getMenuOption() {
        return menuOption;
    }

    public List<ExtendedWebElement> getMenuOptionList() {
        return menuOptionList;
    }

    public ExtendedWebElement getProductEntitlement() {
        return productEntitlement;
    }

    public ExtendedWebElement getSubscriptionRecurSwitch() {
        return subscriptionRecurSwitch;
    }

    public ExtendedWebElement getSubscriptionPeriod() {
        return subscriptionPeriod;
    }

    public ExtendedWebElement getProductCurrency() {
        return productCurrency;
    }

    public ExtendedWebElement getProductPrice() {
        return productPrice;
    }

    public ExtendedWebElement getProductTerritory() {
        return productTerritory;
    }

    public ExtendedWebElement getFreeTrialSwitch() {
        return freeTrialSwitch;
    }

    public ExtendedWebElement getProductFunction() {
        return productFunction;
    }

    public ExtendedWebElement getProductOptions() {
        return productOptions;
    }

    public ExtendedWebElement getCloneMenu() {
        return cloneMenu;
    }

    public ExtendedWebElement getCloneButton() {
        return cloneButton;
    }

    public ExtendedWebElement getCommentIcon() {
        return commentIcon;
    }

    public ExtendedWebElement getLeaveComment() {
        return leaveComment;
    }

    public ExtendedWebElement getProductHistory() {
        return productHistory;
    }

    public ExtendedWebElement getDeactivateButton() {
        return deactivateButton;
    }

    public ExtendedWebElement getApproveLiveButton() {
        return approveLiveButton;
    }

    public ExtendedWebElement getAddToGroups() {
        return addToGroups;
    }

    public ExtendedWebElement getEntitlementName() {
        return entitlementName;
    }

    public ExtendedWebElement getEntitlementDescription() {
        return entitlementDescription;
    }

    public ExtendedWebElement getSaveEntitlement() {
        return saveEntitlement;
    }

    public ExtendedWebElement getCloseEntitlement() {
        return closeEntitlement;
    }

    public ExtendedWebElement getPaywallPlatformFilter() {
        return paywallPlatformFilter;
    }

    public ExtendedWebElement getPaywallTerritoryFilter() {
        return paywallTerritoryFilter;
    }

    public ExtendedWebElement getPaywallName() {
        return paywallName;
    }

    public ExtendedWebElement getPaywallJira() {
        return paywallJira;
    }

    public ExtendedWebElement getPaywallPlatform() {
        return paywallPlatform;
    }

    public ExtendedWebElement getPaywallTerritory() {
        return paywallTerritory;
    }

    public ExtendedWebElement getPaywallTerritorySearch() {
        return paywallTerritorySearch;
    }

    public ExtendedWebElement getPaywallAssociatedTerritory() {
        return paywallAssociatedTerritory;
    }

    public ExtendedWebElement getPaywallSubscriptionToggle() {
        return paywallSubscriptionToggle;
    }

    public ExtendedWebElement getUserContext() {
        return userContext;
    }

    public ExtendedWebElement getUserSelection() {
        return userSelection;
    }

    public ExtendedWebElement getConsumerPurchased() {
        return consumerPurchased;
    }

    public ExtendedWebElement getConsumerProductSelection() {
        return consumerProductSelection;
    }

    public ExtendedWebElement getPaywallProduct() {
        return paywallProduct;
    }

    public ExtendedWebElement getPaywallProductSelection() {
        return paywallProductSelection;
    }

    public ExtendedWebElement getAddPaywallProduct() {
        return addPaywallProduct;
    }

    public ExtendedWebElement getAddPaywallMapping() {
        return addPaywallMapping;
    }

    public ExtendedWebElement getDeletePaywallMapping() {
        return deletePaywallMapping;
    }

    public ExtendedWebElement getCampaignCode() {
        return campaignCode;
    }

    public ExtendedWebElement getCampaignSelection() {
        return campaignSelection;
    }

    public ExtendedWebElement getVoucherCode() {
        return voucherCode;
    }

    public ExtendedWebElement getVoucherSelection() {
        return voucherSelection;
    }

    public ExtendedWebElement getPaywallGoLive() {
        return paywallGoLive;
    }

    public ExtendedWebElement getPaywallSave() {
        return paywallSave;
    }

    public ExtendedWebElement getEditButton() {
        return editButton;
    }

    public ExtendedWebElement getEditClose() {
        return editClose;
    }

    public ExtendedWebElement getEditEntitlement(int index) {
        return editEntitlement.get(index);
    }

    public ExtendedWebElement getSaveButton() {
        return saveButton;
    }

    public ExtendedWebElement getSaveAndButton() {
        return saveAndButton;
    }

    public PropsBasePage(WebDriver driver) {
        super(driver);
    }

    public static PropsBasePage open (WebDriver driver, String url) {

        PAGEFACTORY_LOGGER.info("Url: ".concat(url));
        driver.get(url);
        return new PropsBasePage(driver);

    }

    public void deleteCookies() {
        getDriver().manage().deleteAllCookies();
    }

    //Strings
    private String deleteCookiesMessage = "Delete Cookies";

    //Methods
    //Delete Cookies, Open & Go to URL
    public void pageSetUp(String link) {
        LOGGER.info(deleteCookiesMessage);
        deleteCookies();
        setPageAbsoluteURL(link);
        open();
    }

    public void waitFor(ExtendedWebElement ele){
        WebDriverWait wait = new WebDriverWait(driver,  2, 1000);
        wait.until(ExpectedConditions.elementToBeClickable(ele.getBy()));
    }

    // LOGIN method accepts int parameter to indicate type of user logging in
    /* KEY:
    * 1 = Admin
    * 2 = Product User A
    * 3 = Product User B
    * 4 = IAP User
    * 5 = Approver
    * 6 = Basic User*/

    public void login(int user) {

        getSignInSSO().click();
        waitFor(getUsernameInput());

        if (user == 1) {
            getUsernameInput().type(adminEmail);
        }
        else if (user == 2) {
            getUsernameInput().type(productAEmail);
        }
        else if (user == 3) {
            getUsernameInput().type(productBEmail);
        }
        else if (user == 4) {
            getUsernameInput().type(iapEmail);
        }
        else if (user == 5) {
            getUsernameInput().type(approverEmail);
        }
        else {
            getUsernameInput().type(basicEmail);
        }

        getSubmitOne().click();
        waitFor(getPasswordInput());
        getPasswordInput().type(password);
        getSubmitTwo().click();
        pause(3);
    }

    public void scrollDown() {
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollBy(0,250)", "");
    }

}