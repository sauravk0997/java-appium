package com.disney.util.disney;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyEntitlement;
import com.disney.qa.api.pojos.DisneyOrder;
import com.disney.qa.disney.web.entities.WebConstant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("squid:S2245")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccountUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static ThreadLocal<DisneyAccount> createHuluPartnerAccount(ThreadLocal<DisneyAccount> disneyAccount) {
        LOGGER.info("Creating a new Hulu partner account");
        DisneyAccountApi accountApi = new DisneyAccountApi(WebConstant.WEB, WebConstant.QA, WebConstant.HULU);
        DisneyAccount account = accountApi.createAccount(WebConstant.US, WebConstant.EN);
        disneyAccount.set(account);
        return disneyAccount;
    }

    public static Boolean createAccountViaApi(String locale, ThreadLocal<DisneyAccount> disneyAccount, DisneyAccountApi accountApi) throws MalformedURLException, URISyntaxException  {
        CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder()
                .country(locale).isStarOnboarded(false).build();
        disneyAccount.set(accountApi.createAccount(accountRequest));
        return accountApi.overrideLocations(disneyAccount.get(), locale);
    }

    public static CreateDisneyAccountRequest createAccountRequest(String country, String language, String subscriptionVersion, String gender, String dob) {
        CreateDisneyAccountRequest request = new CreateDisneyAccountRequest();
        DisneyEntitlement disneyEntitlement = new DisneyEntitlement();
        disneyEntitlement.setSubVersion(subscriptionVersion);
        request.addEntitlement(disneyEntitlement);
        request.setCountry(country);
        request.setLanguage(language);
        request.setGender(gender);
        request.setDateOfBirth(dob);
        return request;
    }

    public static CreateDisneyAccountRequest createExpiredAccountWithoutDob(String country, String language, String subscriptionVersion) {
        CreateDisneyAccountRequest request = createAccountRequest(country, language, subscriptionVersion, null, null);
        List<DisneyOrder> orderList = new LinkedList<>();
        orderList.add(DisneyOrder.SET_EXPIRED);
        request.setOrderSettings(orderList);
        return request;
    }
}
