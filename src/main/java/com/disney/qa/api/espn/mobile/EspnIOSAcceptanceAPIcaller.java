package com.disney.qa.api.espn.mobile;

import com.disney.qa.api.espn.mobile.watch.EspnIOSWatchBucketsPage;
import com.disney.qa.api.espn.mobile.watch.EspnIOSWatchContentsPage;
import com.disney.qa.common.http.resttemplate.RestTemplateBuilder;
import com.disney.qa.espn.EspnParameter;
import com.disney.qa.espn.ios.pages.common.EspnIOSPageBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;

public class EspnIOSAcceptanceAPIcaller {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String BUCKET_PATH = "$.page.buckets[*]";
    private RestTemplate restTemplate;
    protected Configuration jsonPathJacksonConfiguration;


    /** Requests **/

    private static final String DYNAMIC_SUBNAV_REQUEST = "/api/product/v3/mobile/espn/%s?profile=sportscenter_v1&" +
            "platform=ios&device=handset&countryCode=US&deviceType=handset&lang=en&tz=America/New_York";



    public EspnIOSAcceptanceAPIcaller() {
        restTemplate = RestTemplateBuilder.newInstance().
                withSpecificJsonMessageConverter().withUtf8EncodingMessageConverter().build();

        jsonPathJacksonConfiguration = Configuration.builder()
                .mappingProvider(new JacksonMappingProvider()).jsonProvider(new JacksonJsonNodeJsonProvider()).build();
    }

    /**
     *
     * Gets response based on Subnav option
     *
     * @param subnavOption (Featured, Espnplus, Originals)
     *
     * @return response in JsonNode
     */
    public JsonNode getResponseBasedOnSubnav(EspnIOSPageBase.WatchSubnavOptions subnavOption) {
        String requestURL= String.format(DYNAMIC_SUBNAV_REQUEST, subnavOption);
        LOGGER.info("URL: " + EspnParameter.getEspnApiHost() + requestURL);

        return restTemplate.getForEntity(EspnParameter.getEspnApiHost() +
                requestURL, JsonNode.class).getBody();
    }

    /**
     * Can be used to look for LIVE content
     **/
    public List<String> getBucketNames(EspnIOSPageBase.WatchSubnavOptions subnavOption) {

        LinkedList<String> sectionHeaders = new LinkedList<>();

        JsonNode bucketNames = JsonPath.using(jsonPathJacksonConfiguration)
                .parse(getResponseBasedOnSubnav(subnavOption)).read("$..buckets[*].name");

        if (bucketNames.size() > 1) {
            for (JsonNode node : bucketNames) {
                sectionHeaders.add(node.asText());
            }
        }
        return sectionHeaders;
    }

    public List<String> getContentTypes(EspnIOSPageBase.WatchSubnavOptions subnavOption) {

        LinkedList<String> sectionHeaders = new LinkedList<>();

        JsonNode bucketNames = JsonPath.using(jsonPathJacksonConfiguration)
                .parse(getResponseBasedOnSubnav(subnavOption)).read("$..type");

        if (bucketNames.size() > 1) {
            for (JsonNode node : bucketNames) {
                sectionHeaders.add(node.asText());
            }
        }
        return sectionHeaders;
    }

    public List<String> getLiveCarouselTitles(EspnIOSPageBase.WatchSubnavOptions subnavOption) {

        LinkedList contentMetadata = new LinkedList<>();

        JsonNode metadataInfo = JsonPath.using(jsonPathJacksonConfiguration)
                .parse(getResponseBasedOnSubnav(subnavOption)).read("$..[?(@.name == 'Live')].contents[*].name");

        if(metadataInfo.size() > 1) {
            for(JsonNode node : metadataInfo) {
                contentMetadata.add(node.asText());
            }
        }
        return contentMetadata;
    }

    public List<String> getLiveContentNames(EspnIOSPageBase.WatchSubnavOptions subnavOption) {

        LinkedList contentMetadata = new LinkedList<>();

        JsonNode metadataInfo = JsonPath.using(jsonPathJacksonConfiguration)
                .parse(getResponseBasedOnSubnav(subnavOption)).read("$..contents[*].[?(@.status == 'live')].name");

        if(!metadataInfo.isNull()) {
            for(JsonNode node : metadataInfo) {
                contentMetadata.add(node.asText());
            }
        }
        return contentMetadata;
    }

    public List<String> getLiveNetworkAndLeague(EspnIOSPageBase.WatchSubnavOptions subnavOption) {

        LinkedList contentMetadata = new LinkedList<>();

        JsonNode metadataInfo = JsonPath.using(jsonPathJacksonConfiguration)
                .parse(getResponseBasedOnSubnav(subnavOption)).read("$..[?(@.name == 'Live')].contents[*].subtitle");

        if(metadataInfo.size() > 1) {
            for(JsonNode node : metadataInfo) {
                contentMetadata.add(node.asText());
            }
        }
        return contentMetadata;
    }

    public List<String> getVODnames(EspnIOSPageBase.WatchSubnavOptions subnavOption) {

        LinkedList vodNamesList = new LinkedList<>();

        JsonNode vodContentNames = JsonPath.using(jsonPathJacksonConfiguration)
                .parse(getResponseBasedOnSubnav(subnavOption)).read("$..[?(@.type=='vod')].name");

        if(vodContentNames.size() > 1) {
            for(JsonNode node : vodContentNames) {
                vodNamesList.add(node.asText());
            }
        }
        return vodNamesList;
    }

    public List<String> getVODnames(EspnIOSPageBase.WatchSubnavOptions subnavOption, int minimumCarouselCount) {

        List<String> totalCount = new LinkedList();

        ArrayNode bucket = JsonPath.using(jsonPathJacksonConfiguration)
                .parse(getResponseBasedOnSubnav(subnavOption)).read(BUCKET_PATH);

        List<EspnIOSWatchBucketsPage> items = new LinkedList<>();

        List contentNames = new LinkedList<>();

        for (JsonNode bucketItem : bucket) {
            items.add(fillWatchBucketItems(bucketItem));
        }

        if (items.size() > 1) {
            LOGGER.info("Selecting VOD Titles with more than " + minimumCarouselCount + " carousel items");
            for (EspnIOSWatchBucketsPage pageItem : items) {
                if (pageItem.getTotalCount() > minimumCarouselCount  && containsContentType("vod", pageItem.getContents())) {
                    totalCount.add(pageItem.getName());
                    LOGGER.info("Titles Selected: " + totalCount);
                }
                if(!totalCount.isEmpty()) {
                    for(String nameValue : totalCount) {
                        ArrayNode vodContentNames = JsonPath.using(jsonPathJacksonConfiguration)
                                .parse(getResponseBasedOnSubnav(subnavOption))
                                .read("$..[?(@.name=='" + nameValue + "')].contents[*].name");

                        for (JsonNode node : vodContentNames) {
                            contentNames.add(node.asText());
                        }
                    }
                }
            }
            LOGGER.info("Content Names: " + contentNames);
        }
        return contentNames;
    }

    private boolean containsContentType(String contentType, List<EspnIOSWatchContentsPage> contentList) {
        for(EspnIOSWatchContentsPage content : contentList){
            if(contentType.equalsIgnoreCase(content.getType())) {
                LOGGER.debug("Content Type: " + contentType);
                return true;
            }
        }
        return false;
    }


    public List<String> getVODMetada(EspnIOSPageBase.WatchSubnavOptions subnavOption) {

        LinkedList vodMetadataList = new LinkedList<>();

        JsonNode vodInfo = JsonPath.using(jsonPathJacksonConfiguration)
                .parse(getResponseBasedOnSubnav(subnavOption)).read("$..[?(@.type=='vod')].subtitle");

        if(vodInfo.size() > 1) {
            for(JsonNode node : vodInfo) {
                vodMetadataList.add(node.asText());
            }
        }
        return vodMetadataList;
    }

    public List<String> getDynamicContentNames(EspnIOSPageBase.WatchSubnavOptions subnavOption, String bucketName) {

        LinkedList metadataList = new LinkedList<>();

        JsonNode subtitleInfo = JsonPath.using(jsonPathJacksonConfiguration)
                .parse(getResponseBasedOnSubnav(subnavOption))
                .read("$..[?(@.name == '" + bucketName + "')].contents..name");

        if(subtitleInfo.size() > 1) {
            for(JsonNode node : subtitleInfo) {
                metadataList.add(node.asText());
            }
        }
        return metadataList;
    }

    public List<String> getDynamicContentSubtitles(EspnIOSPageBase.WatchSubnavOptions subnavOption, String bucketName) {
        LinkedList metadataList = new LinkedList<>();

        JsonNode subtitleInfo = JsonPath.using(jsonPathJacksonConfiguration)
                .parse(getResponseBasedOnSubnav(subnavOption))
                .read("$..[?(@.name == '" + bucketName + "')]..subtitle");

        if(subtitleInfo.size() > 1) {
            for(JsonNode node : subtitleInfo) {
                metadataList.add(node.asText());
            }
        }
        return metadataList;
    }

    public List<String> getNonPlayableAssets(EspnIOSPageBase.WatchSubnavOptions subnavOption) {

        LinkedList vodNamesList = new LinkedList<>();

        JsonNode vodInfo = JsonPath.using(jsonPathJacksonConfiguration)
                .parse(getResponseBasedOnSubnav(subnavOption)).read("$..[?(@.imageFormat=='5x2')].name");

        if(vodInfo.size() > 1) {
            for(JsonNode node : vodInfo) {
                vodNamesList.add(node.asText());
            }
        }
        return vodNamesList;
    }

    public String getFirstNonPlayableAssetTitle(EspnIOSPageBase.WatchSubnavOptions subnavOption) {

        String firstNonPlayableAssetTitle = "";

        ArrayNode bucket = JsonPath.using(jsonPathJacksonConfiguration)
                .parse(getResponseBasedOnSubnav(subnavOption)).read(BUCKET_PATH);

        List<EspnIOSWatchBucketsPage> items = new LinkedList<>();

        for (JsonNode bucketItem : bucket) {
            items.add(fillWatchBucketItems(bucketItem));
        }
        if (items.size() > 1) {
            for (EspnIOSWatchBucketsPage pageItem : items) {
                if ("5x2".equalsIgnoreCase(pageItem.getRatio())) {
                    firstNonPlayableAssetTitle = pageItem.getName();
                    break;
                }
            }
        }
        return firstNonPlayableAssetTitle;
    }

    public List<String> getNoSelfTitles(EspnIOSPageBase.WatchSubnavOptions subnavOption) {

        LinkedList nonSelfLinks = new LinkedList();

        ArrayNode bucket = JsonPath.using(jsonPathJacksonConfiguration)
                .parse(getResponseBasedOnSubnav(subnavOption)).read(BUCKET_PATH);

        List<EspnIOSWatchBucketsPage> items = new LinkedList<>();

        for (JsonNode bucketItem : bucket) {
            items.add(fillWatchBucketItems(bucketItem));
        }

        if (items.size() > 1) {
            for (EspnIOSWatchBucketsPage pageItem : items) {
                if (pageItem.getSelf().isEmpty()) {
                    nonSelfLinks.add(pageItem.getName());
                }
            }
            LOGGER.info("Content Names without Self Link: " + nonSelfLinks);
        }
        return nonSelfLinks;
    }

    public List<String> getSelfTitles(EspnIOSPageBase.WatchSubnavOptions subnavOption) {

        LinkedList selfLinks = new LinkedList();

        ArrayNode bucket = JsonPath.using(jsonPathJacksonConfiguration)
                .parse(getResponseBasedOnSubnav(subnavOption)).read(BUCKET_PATH);

        List<EspnIOSWatchBucketsPage> items = new LinkedList<>();

        for (JsonNode bucketItem : bucket) {
            items.add(fillWatchBucketItems(bucketItem));
        }
        if (items.size() > 1) {
            for (EspnIOSWatchBucketsPage pageItem : items) {
                if (!pageItem.getSelf().isEmpty()) {
                    selfLinks.add(pageItem.getName());
                }
            }
            LOGGER.info("Content Names with Self Link: " + selfLinks);
        }
        return selfLinks;
    }

    public List<String> getReplayTitles(EspnIOSPageBase.WatchSubnavOptions subnavOption) {
        LinkedList replayTitlesList = new LinkedList<>();

        JsonNode replayNames = JsonPath.using(jsonPathJacksonConfiguration)
                .parse(getResponseBasedOnSubnav(subnavOption)).read("$..[?(@.status=='replay')].name");

        if(replayNames.size() > 1) {
            for(JsonNode node : replayNames) {
                replayTitlesList.add(node.asText());
            }
        }
        return replayTitlesList;
    }

    public EspnIOSWatchBucketsPage fillWatchBucketItems(JsonNode json) {
        EspnIOSWatchBucketsPage item = new EspnIOSWatchBucketsPage();
        item.setId(json.findPath("id").asText());
        item.setName(json.findPath("name").asText());
        item.setRatio(json.findPath("ratio").asText());
        item.setTotalCount(json.findPath("metadata").findPath("totalCount").asInt());
        item.setContents(getContentItems(json));
        if(!json.findPath("links").isNull()) {
            item.setSelf(json.findPath("links").findPath("self").asText());
        }
        return item;
    }

    public EspnIOSWatchContentsPage fillWatchContentItems(JsonNode json) {
        EspnIOSWatchContentsPage item = new EspnIOSWatchContentsPage();
        item.setStatus(json.findPath("status").asText());
        item.setName(json.findPath("name").asText());
        item.setType(json.findPath("type").asText());
        item.setId(json.findPath("id").asText());
        return item;
    }

    public List<EspnIOSWatchContentsPage> getContentItems(JsonNode json) {
        List<EspnIOSWatchContentsPage> contentList = new LinkedList<>();
        ArrayNode contents = JsonPath.using(jsonPathJacksonConfiguration)
                .parse(json).read("$..contents[*]");
        for(JsonNode content : contents) {
            contentList.add(fillWatchContentItems(content));
        }
        return contentList;
    }

}
