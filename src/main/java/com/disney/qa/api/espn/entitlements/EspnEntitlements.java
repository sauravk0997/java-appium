package com.disney.qa.api.espn.entitlements;

import com.disney.qa.api.espn.EspnContentProvider;
import com.disney.qa.common.http.resttemplate.RestTemplateBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class EspnEntitlements {

    protected EspnContentProvider contentProvider;
    private RestTemplate restTemplate;
    protected Configuration jsonPathJacksonConfiguration;
    private static final String ESPN_ACCOUNTS_URL =
            "https://s3.amazonaws.com/qe-portal-sandbox/test_data/espn_qe_account_entitlements.json";
    private static final String ACCOUNTS_PATH = "$.Accounts";

    public EspnEntitlements(){
        restTemplate = RestTemplateBuilder.newInstance().withUtf8EncodingMessageConverter()
                .withSpecificJsonMessageConverter().build();

        jsonPathJacksonConfiguration = Configuration.builder()
                .mappingProvider(new JacksonMappingProvider()).jsonProvider(new JacksonJsonNodeJsonProvider()).build();
    }

    public JsonNode getAccountsAndEntitlements() {
        return restTemplate.getForEntity(ESPN_ACCOUNTS_URL, JsonNode.class).getBody();
    }

    public List<String> retrieveNameOfEntitlement(int index) {
        List<String> nameOfEntitlement = new ArrayList<>();
        JsonNode retrieveEntitlementNames = JsonPath.using(jsonPathJacksonConfiguration).parse(getAccountsAndEntitlements()).read(String.format( "%s[%d]..name",ACCOUNTS_PATH,index));
        for (JsonNode node : retrieveEntitlementNames) {
            nameOfEntitlement.add(node.asText());
        }

        return nameOfEntitlement;
    }

    public List<String> retrieveEntitlementDescription(int index) {
        List<String> entitlementDesc = new ArrayList<>();
        JsonNode retrieveEntitlementDescription = JsonPath.using(jsonPathJacksonConfiguration).parse(getAccountsAndEntitlements()).read(String.format("%s[%d]..desc",ACCOUNTS_PATH,index));
        for (JsonNode node : retrieveEntitlementDescription) {
            entitlementDesc.add(node.asText());
        }

        return entitlementDesc;
    }

    public List<String> retrieveEntitlementSku(int index) {
        List<String> sku = new ArrayList<>();
        JsonNode retrieveSkuForEntitlement = JsonPath.using(jsonPathJacksonConfiguration).parse(getAccountsAndEntitlements()).read(String.format( "%s[%d]..sku",ACCOUNTS_PATH,index));
        for (JsonNode node : retrieveSkuForEntitlement) {
            sku.add(node.asText());
        }

        return sku;
    }

}
