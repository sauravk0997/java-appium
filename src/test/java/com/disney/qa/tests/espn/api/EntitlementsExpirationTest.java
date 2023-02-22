package com.disney.qa.tests.espn.api;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.account.DisneySubscriptionApi;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.client.requests.oneId.CreateEspnAccountRequest;
import com.disney.qa.api.client.responses.account.DisneyFullAccount;
import com.disney.qa.api.client.responses.account.espn.items.EspnFullAccount;
import com.disney.qa.api.disney.DisneyContentApiChecker;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyEntitlement;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.pojos.ESPNEntitlement;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.tests.BaseAPITest;
import com.disney.qait.auth.ServiceAccountCredentials;
import com.disney.qait.options.ValueInputOption;
import com.disney.qait.services.GSheets;
import com.evanlennick.retry4j.config.RetryConfigBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.disney.qa.api.utils.DisneySkuParameters.values;

public class EntitlementsExpirationTest extends BaseAPITest {

    private static final Map<String, String> ENTITLEMENTS_MAP = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final ZonedDateTime ZONE_DATE_TIME = new Date().toInstant().atZone(ZoneId.of("America/New_York"));
    private static final String LAST_EXPIRATION_CHECK = ZONE_DATE_TIME.format(DateTimeFormatter.ofPattern("MM/dd/yy"));
    private static final String DISNEY = "disney";
    private static final String ESPN = "espn";
    private static final String SUB_V2_CST = "CST";

    static {
        ENTITLEMENTS_MAP.put("Existing Annual/Monthly E+ User", "8400199910209919911899000");
        ENTITLEMENTS_MAP.put("Verified Disney+/ESPN+/Hulu Bundle User", "1999199999999917051999000_disney");
        ENTITLEMENTS_MAP.put("Hulu purchased Super Bundle with ads (SASH)", "com.hulu.bundle.sashdisneyplusespnplus");
        ENTITLEMENTS_MAP.put("Hulu purchased Super Bundle no ads (NOAH)", "com.hulu.bundle.noahdisneyplusespnplus");
        ENTITLEMENTS_MAP.put("Hulu purchased Super Bundle live tv with ads (LIVE SASH)", "com.hulu.bundle.livesashdisneyplusespnplus");
        ENTITLEMENTS_MAP.put("Hulu purchased Super Bundle live tv no ads (LIVE NOAH)", "com.hulu.bundle.livenoahdisneyplusespnplus");
        ENTITLEMENTS_MAP.put("New ESPN+/UFC Package User", "8400199910209919911899000");
        ENTITLEMENTS_MAP.put("Exec Account", "EXECESPN");
        ENTITLEMENTS_MAP.put("annual_disney", "1999199999999910121999000_disney");
        ENTITLEMENTS_MAP.put("placeholder", "donothing");
    }

    private final DisneyContentApiChecker disneyContentApiChecker = new DisneyContentApiChecker();
    private GSheets gSheets;

    @Test
    public void updateSheetsWithEntitlementExpiry() {
        List<String> spreadSheets = Stream.of(
                "1AtqtvwezAAoZYdxboZrszKOL1swQlTci1YaFSMClx7I",
                "12O8bwatUmWtPUgb57Qoj1qlUiLFL4scZRHlDMppF4tM").collect(Collectors.toList());
        spreadSheets.forEach(spreadSheet -> updatedSheets(gSheets = new GSheets(
                new ServiceAccountCredentials(
                        "/espn/api/google/credentials.json", Stream.of(SheetsScopes.SPREADSHEETS).collect(Collectors.toList())).getCredentials(),
                "GSheets",
                spreadSheet)));
    }

    public void updatedSheets(GSheets gSheets) {
        List<String> validSheets = new ArrayList<>();

        gSheets.getAllSheets().forEach(item -> {
            String title = item.getProperties().getTitle();
            if (title.toLowerCase().contains("qa") || title.toLowerCase().contains("prod"))
                validSheets.add(title);
        });

        List<ValueRange> emails = new ArrayList<>();
        List<ValueRange> entitlementList = new ArrayList<>();
        List<ValueRange> passwords = new ArrayList<>();
        validSheets.forEach(item -> emails.add(gSheets.getValues(item + "!C2:C")));
        validSheets.forEach(item -> entitlementList.add(gSheets.getValues(item + "!E2:E")));
        validSheets.forEach(item -> passwords.add(gSheets.getValues(item + "!D2:D")));

        //Iterate over all valid sheets
        for (int i = 0; i < emails.size(); i++) {
            List<List<Object>> emailValues = emails.get(i).getValues();
            List<List<Object>> passwordValues = passwords.get(i).getValues();
            String environment = emails.get(i).getRange().toLowerCase().contains("prod") ? "PROD" : "QA";
            DisneyAccountApi disneyAccountApi = new DisneyAccountApi(
                    "browser",
                    environment,
                    ESPN);
            //counter for cell to update
            int k = 2;
            List<List<List<Object>>> batchExpirationValues = new ArrayList<>();
            List<String> batchExpirationCells = new ArrayList<>();
            List<List<List<Object>>> batchLastExpirationCheckValues = new ArrayList<>();
            List<String> batchLastExpirationCheckCells = new ArrayList<>();
            //Iterate over all emails in the sheet
            for (int j = 0; j < emailValues.size(); j++) {
                try {
                    List<Object> emailObject = emailValues.get(j);
                    List<Object> passwordObject = passwordValues.get(j);
                    //Check if cell is empty
                    if (!emailObject.isEmpty()) {
                        String email = emailObject.get(0).toString();
                        String entitlement = entitlementList
                                .get(i)
                                .getValues()
                                .get(j)
                                .get(0).toString();
                        //Pre-entitlement verification check to see if accounts need to be created
                        createAccount(
                                disneyAccountApi.getAccountIdsByEmail(email),
                                entitlement,
                                email,
                                passwordObject.get(0).toString(),
                                environment);
                        String expiration = getExpiration(disneyAccountApi, email, passwordObject.get(0).toString(), entitlement);
                        expiration = LocalDate.parse(expiration.split("T")[0]).format(DateTimeFormatter.ofPattern("MM/dd/yy"));
                        batchExpirationCells.add(validSheets.get(i) + "!F" + k);
                        batchLastExpirationCheckCells.add(validSheets.get(i) + "!G" + k);
                        batchExpirationValues.add(Collections.singletonList(Collections.singletonList(expiration)));
                        batchLastExpirationCheckValues.add(Collections.singletonList(Collections.singletonList(LAST_EXPIRATION_CHECK)));
                    }
                } catch (Exception e) {
                    LOGGER.info("Failed because of {}: ", e.getMessage());
                    e.printStackTrace();
                }
                //Even if cell is empty k needs to be incremented
                k++;
            }
            gSheets.batchWrite(batchExpirationValues, batchExpirationCells, ValueInputOption.RAW);
            gSheets.batchWrite(batchLastExpirationCheckValues, batchLastExpirationCheckCells, ValueInputOption.RAW);
        }
    }

    public void createAccount(JsonNode getAccountIdResponse, String entitlement, String email, String password, String environment) {
        String sku = ENTITLEMENTS_MAP.get(entitlement);
        boolean createBoth = sku.contains(DISNEY) || isDSSpreadSheet();
        List<String> partners = getPartners(getAccountIdResponse);
        if (partners.size() == 0) {
            if (createBoth) {
                createBothAccounts(email, password, environment, entitlement);
            } else {
                createEspnAccount(email, password, environment, entitlement);
            }
        } else if (partners.size() == 1) {
            switch (partners.get(0).toLowerCase()) {
                case DISNEY:
                    createEspnAccount(email, password, environment, entitlement);
                    break;
                case ESPN:
                    if (createBoth) {
                        createDisneyAccount(email, password, environment, entitlement);
                    }
                    break;
                default:
                    LOGGER.info("Unknown partner for email : {}", email);
                    break;
            }
        }
    }

    public void createBothAccounts(String email, String password, String environment, String entitlement) {
        createEspnAccount(email, password, environment, entitlement);
        createDisneyAccount(email, password, environment, entitlement);
    }

    public void createEspnAccount(String email, String password, String environment, String entitlement) {
        DisneyAccountApi disneyAccountApi = new DisneyAccountApi(
                "browser",
                environment,
                ESPN);
        List<ESPNEntitlement> espnEntitlements = getEspnEntitlements(entitlement);
        CreateEspnAccountRequest createEspnAccountRequest = CreateEspnAccountRequest.builder()
                .email(email)
                .password(password)
                .isTest(false)
                .entitlementExpiry(52)
                .build();
        createEspnAccountRequest.setEntitlements(espnEntitlements);
        try {
            disneyAccountApi.createESPNAccount(createEspnAccountRequest);
        } catch (Exception e) {
            LOGGER.info("Account possibly already registered attempting to log in");
            EspnFullAccount espnFullAccount = disneyAccountApi.loginOneId(email, password);
            DisneyAccount disneyAccount = espnFullAccount.getDisneyAccount();
            disneyAccount.setOrderSettings(new LinkedList<>());
            for (ESPNEntitlement espnEntitlement : espnEntitlements) {
                try {
                    disneyAccountApi.entitleAccount(disneyAccount, espnEntitlement.getSku(), SUB_V2_CST);
                } catch (Exception ex) {
                    LOGGER.info("Failed to entitle account: {}", ex.getMessage());
                }
            }
        }
    }

    public void createDisneyAccount(String email, String password, String environment, String entitlement) {
        DisneyAccountApi disneyAccountApi = new DisneyAccountApi(
                "browser",
                environment,
                DISNEY);

        CreateDisneyAccountRequest createDisneyAccountRequest = CreateDisneyAccountRequest.builder()
                .email(email)
                .password(password)
                .isTestUser(false)
                .entitlementExpiry(52)
                .addDefaultEntitlement(false)
                .build();
        disneyAccountApi.createAccount(createDisneyAccountRequest);
        try {
            disneyAccountApi.graphQLLogin(email, password);
            DisneyAccount disneyAccount = new DisneyAccount();
            disneyAccount.setEmail(email);
            disneyAccount.setUserPass(password);
            disneyAccount.setEntitlementExpiry(52);
            disneyAccount.setAccountId(getPartnerAccountId(disneyAccountApi, email, DISNEY));
            List<DisneyEntitlement> entitlements = getDisneyEntitlements(entitlement);
            for (DisneyEntitlement disneyEntitlement : entitlements) {
                disneyAccountApi.entitleAccount(disneyAccount, disneyEntitlement.getOffer(), disneyEntitlement.getSubVersion());
            }
        } catch (Exception ex) {
            LOGGER.info("Disney Account creation failed: {}", ex.getMessage());
        }
    }

    public void entitleAccount(DisneyAccountApi disneyAccountApi, String email, String password, String entitlement, List<String> partners) {
        try {
            partners.forEach(partner -> {
                if (partner.equalsIgnoreCase(DISNEY)) {
                    disneyAccountApi.partner = DISNEY;
                    List<DisneyEntitlement> disneyEntitlements = getDisneyEntitlements(entitlement);
                    DisneyAccount disneyAccount = getDisneyAccount(email, password);
                    disneyAccount.setAccountId(getPartnerAccountId(disneyAccountApi, email, DISNEY));
                    DisneyFullAccount disneyFullAccount = disneyAccountApi.getAccountBody(disneyAccount);
                    disneyFullAccount.getIdentities().forEach(item -> {
                        if (item.getProviderId().toLowerCase().contains("idp:disney")) {
                            disneyAccount.setIdentityPointId(item.getIdentityId());
                        }
                    });
                    disneyAccount.setCountryCode("US");
                    try {
                        for (DisneyEntitlement disneyEntitlement : disneyEntitlements) {
                            disneyAccountApi.entitleAccount(disneyAccount, disneyEntitlement.getOffer(), disneyEntitlement.getSubVersion());
                        }
                    } catch (Exception e) {
                        LOGGER.info("Unable to entitle D+ account: {}", e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    try {
                        disneyAccountApi.partner = ESPN;
                        List<ESPNEntitlement> espnEntitlements = getEspnEntitlements(entitlement);
                        DisneyAccount disneyAccount = getDisneyAccount(email, password);
                        disneyAccount.setOrderSettings(new LinkedList<>());
                        disneyAccount.setAccountId(getPartnerAccountId(disneyAccountApi, email, ESPN));
                        for (ESPNEntitlement espnEntitlement : espnEntitlements) {
                            disneyAccountApi.entitleAccount(disneyAccount, espnEntitlement.getSku(), espnEntitlement.getSubVersion());
                        }
                    } catch (Exception e) {
                        LOGGER.info("Unable to entitle ESPN+ account: {}", e.getMessage());
                    }
                }

            });
        } catch (Exception e) {
            LOGGER.info("Failed to entitle account: {}", e.getMessage());
        }
    }

    public String getExpiration(DisneyAccountApi disneyAccountApi, String email, String password, String entitlement) {
        JsonNode getAccountIdResponse = disneyAccountApi.getAccountIdsByEmail(email);
        List<String> partners = getPartners(getAccountIdResponse);
        if (partners.size() == 1) {
            String expiration = "";
            disneyAccountApi.partner = ESPN;
            expiration = getExpiration(disneyAccountApi, email, entitlement, ESPN, getPartnerAccountId(disneyAccountApi, email, ESPN));
            if (StringUtils.isNotEmpty(expiration))
                return expiration;
            entitleAccount(disneyAccountApi, email, password, entitlement, Stream.of(ESPN).collect(Collectors.toList()));
            return getExpiration(disneyAccountApi, email, entitlement, ESPN, getPartnerAccountId(disneyAccountApi, email, ESPN));
        }
        if (partners.size() >= 2) {
            String expiration = "";
            disneyAccountApi.partner = ESPN;
            String espnAccountId = getPartnerAccountId(disneyAccountApi, email, ESPN);
            expiration = getExpiration(disneyAccountApi, email, entitlement, ESPN, espnAccountId);
            if (StringUtils.isEmpty(expiration)) {
                entitleAccount(disneyAccountApi, email, password, entitlement, Stream.of(ESPN).collect(Collectors.toList()));
                expiration = getExpiration(disneyAccountApi, email, entitlement, ESPN, espnAccountId);
            }
            String sku = ENTITLEMENTS_MAP.get(entitlement);
            boolean isNotBase = sku.toLowerCase().contains(DISNEY);
            String disneyExpiration = getExpiration(
                    disneyAccountApi,
                    email,
                    isNotBase ? entitlement : "annual_disney",
                    DISNEY,
                    getPartnerAccountId(disneyAccountApi, email, DISNEY));
            if (StringUtils.isEmpty(disneyExpiration)) {
                disneyAccountApi.partner = DISNEY;
                entitleAccount(disneyAccountApi, email, password, isNotBase ? entitlement : "placeholder", Stream.of(DISNEY).collect(Collectors.toList()));
            }
            return expiration;
        }
        throw new RuntimeException(String.format("Entitlement expiration not found for:%s Entitlement:%s", email, entitlement));
    }

    public String getExpiration(DisneyAccountApi disneyAccountApi, String email, String entitlement, String partner, String accountId) {
        disneyAccountApi.partner = partner;
        DisneyAccount disneyAccount = new DisneyAccount();
        disneyAccount.setAccountId(accountId);
        LOGGER.info("Email: {}", email);
        JsonNode entitlementResponse = null;
        try {
            DisneySubscriptionApi disneySubscriptionApi = new DisneySubscriptionApi(disneyAccountApi.platform, disneyAccountApi.environment, disneyAccountApi.partner);
            disneySubscriptionApi.setDefaultRetryConfig(
                    new RetryConfigBuilder()
                            .retryOnAnyException()
                            .withMaxNumberOfTries(3)
                            .withDelayBetweenTries(1, ChronoUnit.SECONDS)
                            .withExponentialBackoff()
                            .build()
            );
            entitlementResponse = disneySubscriptionApi.getSubscriptions(accountId);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            e.printStackTrace();
        }
        String expiration = "";
        try {
            expiration = disneyContentApiChecker
                    .queryResponse(
                            entitlementResponse,
                            String.format("$..[?(@.id contains '%s')]..expirationDate", ENTITLEMENTS_MAP.get(entitlement)))
                    .get(0);
        } catch (Exception e) {
            LOGGER.info("Unable to retrieve expiration date: {}", e.getMessage());
        }
        return expiration;
    }

    public List<ESPNEntitlement> getEspnEntitlements(String entitlement) {
        Optional<DisneySkuParameters> sku = Arrays.stream(values()).filter(item -> item.getValue().equalsIgnoreCase(ENTITLEMENTS_MAP.get(entitlement))).findFirst();
        if (sku.isEmpty()) {
            throw new RuntimeException("sku not found");
        }
        return Stream.of(ESPNEntitlement.builder().sku(sku.get()).subVersion(SUB_V2_CST).build()).collect(Collectors.toList());
    }

    public List<DisneyEntitlement> getDisneyEntitlements(String entitlement) {
        String sku = ENTITLEMENTS_MAP.get(entitlement);
        if (!sku.toLowerCase().contains(DISNEY)) {
            return Stream.of(new DisneyEntitlement().setSubVersion(SUB_V2_CST)).collect(Collectors.toList());
        }
        DisneyEntitlement disneyEntitlement = new DisneyEntitlement();
        DisneyOffer disneyOffer = new DisneyOffer().setSku(sku);
        disneyEntitlement.setOffer(disneyOffer);
        disneyEntitlement.setSubVersion(SUB_V2_CST);
        return Stream.of(disneyEntitlement).collect(Collectors.toList());
    }

    public DisneyAccount getDisneyAccount(String email, String password) {
        DisneyAccount disneyAccount = new DisneyAccount();
        disneyAccount.setEmail(email);
        disneyAccount.setUserPass(password);
        disneyAccount.setEntitlementExpiry(52);
        return disneyAccount;
    }

    public boolean isDSSpreadSheet() {
        return gSheets.getSpreadSheet().equals("12O8bwatUmWtPUgb57Qoj1qlUiLFL4scZRHlDMppF4tM");
    }

    public List<String> getPartners(JsonNode getAccountIdResponse) {
        return disneyContentApiChecker.queryResponse(getAccountIdResponse, "$..partner");
    }

    public String getPartnerAccountId(DisneyAccountApi disneyAccountApi, String email, String partner) {
        return disneyContentApiChecker.queryResponse(disneyAccountApi.getAccountIdsByEmail(email),
                String.format("$..[?(@.partner == '%s')].accountId", partner)).get(0);
    }
}