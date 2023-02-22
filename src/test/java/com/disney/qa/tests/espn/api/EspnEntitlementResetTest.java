package com.disney.qa.tests.espn.api;

import com.disney.qa.api.espn.EspnApiCommon;
import com.disney.qa.api.espn.EspnContentProvider;
import com.disney.qa.api.espn.EspnContentServiceBuilder;
import com.disney.qa.api.espn.entitlements.EspnEntitlements;
import com.disney.qa.tests.BaseAPITest;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EspnEntitlementResetTest extends BaseAPITest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected EspnContentProvider contentProvider;
    protected Configuration jsonPathJacksonConfiguration;
    private EspnApiCommon espnApiCommon;


    @BeforeTest
    public void beforeTest() {

        jsonPathJacksonConfiguration = Configuration.builder()
                .mappingProvider(new JacksonMappingProvider()).jsonProvider(new JacksonJsonNodeJsonProvider()).build();

        contentProvider = EspnContentServiceBuilder.newInstance().entitlementHostAndTemplate();
        espnApiCommon = new EspnApiCommon();
    }

    @Test(description = "Resetting Entitlements for QE Accounts")
    public void resetEntitlements() {
        try {
            resetAccountEntitlements();
        } catch (Exception exception) {
            LOGGER.error("ERROR: " + exception.getMessage(), exception);
        }
    }

    private void resetAccountEntitlements() {
        Map<String, String> headers = ImmutableMap.of("Accept", MediaType.APPLICATION_JSON, "X-BAMTech-partner", "espn");
        EspnEntitlements espnEntitlements = new EspnEntitlements();

        Map<String, String> postHeader = ImmutableMap.of("Accept", MediaType.APPLICATION_JSON, "Content-Type",
                MediaType.APPLICATION_JSON, "X-BAMTech-partner", "espn");
        List<String> nameOfEntitlement;
        List<String> expectedSku;
        List<String> entitlementDesc;
        List<String> actualSku = new ArrayList<>();
        JsonNode accounts = JsonPath.using(jsonPathJacksonConfiguration).parse(espnEntitlements
                .getAccountsAndEntitlements()).read("$.Accounts..account");
        LOGGER.info("Account IDs Retrieved:" + accounts);
        for (int i = 0; i < accounts.size(); i++) {
            nameOfEntitlement =  espnEntitlements.retrieveNameOfEntitlement(i);
            expectedSku =  espnEntitlements.retrieveEntitlementSku(i);
            entitlementDesc = espnEntitlements.retrieveEntitlementDescription(i);

            JsonNode retrieveActualIds = contentProvider.getRetrieveEntitlements(headers, ImmutableMap.of(
                    "active", "true", "accountId", accounts.get(i).asText()), JsonNode.class);
            retrieveActualIds = JsonPath.using(jsonPathJacksonConfiguration).parse(retrieveActualIds).read("$..products..sku");
            LOGGER.info(String.format("Retrieving actual skus for account ID %s:\n " + retrieveActualIds, accounts.get(i).asText()));
            for (JsonNode node : retrieveActualIds) {
                actualSku.add(node.asText());
            }

            for (int j = 0; j < expectedSku.size(); j++) {
                if (!(actualSku.contains(expectedSku.get(j)))) {

                    String startDate = espnApiCommon.getDateForGames(0, "yyyy-MM-dd");
                    String expiryDate = espnApiCommon.getDateForGames(30, "yyyy-MM-dd");
                    String startDateUtc = startDate + "T00:01:59Z";
                    String expiryDateUtc = expiryDate + "T00:01:00Z";
                    //TODO Convert this into a template
                    String data = "{ \"sku\": [ \"" + expectedSku.get(j) + "\" ], \"accountId\": \"" + accounts.get(i).asText() +
                            "\", \"type\": \"BAM_IDENTITY\", \"source\": { \"type\": \"CST\", \"provider\": \"BAMTECH\", " +
                            "\"subType\": \"COMP\", \"ref\": \"" + accounts.get(i).asText() +
                            "_" + expectedSku.get(j) + "\" }, \"status\": { \"type\": \"SUBSCRIBED\" }, \"offer\": " +
                            "{ \"type\": \"COMPLIMENTARY\" }, \"startDate\": \"" +
                            startDateUtc + "\", \"expirationDate\": \"" + expiryDateUtc + "\"}";
                    contentProvider.postAddSubscription(postHeader, data, null);
                    LOGGER.info(String.format("%s is not present in account: %s, Entitlement name is %s with entitlement sku: %s. ADDING ENTITLEMENT",
                            entitlementDesc.get(j), accounts.get(i), nameOfEntitlement.get(j), expectedSku.get(j)));
                }
            }

            actualSku.removeAll(expectedSku);
            if (!actualSku.isEmpty()) {
                for (String item : actualSku) {
                    LOGGER.info(String.format("%s Entitlement sku will be removed for account: %s",
                            item, accounts.get(i)));
                    String expirationDate = espnApiCommon.getDateForGames(0, "yyyy-MM-dd");
                    String expiryDateAndTime = expirationDate + "T00:01:59Z";
                    String dataToSend = "{ \"expirationDate\": \"" + expiryDateAndTime + "\", \"status\": { \"subType\": \"VOLUNTARY_CANCEL\" }}";
                    contentProvider.postCancelSubscription(postHeader, dataToSend, null,
                            String.format("CST/BAMTECH/" + accounts.get(i).asText() + "_" + item + "/revoke"));

                }
            }
            nameOfEntitlement.clear();
            expectedSku.clear();
            entitlementDesc.clear();
            actualSku.clear();
        }

    }
}
