package com.disney.qa.api.disney.pojo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;

public class DisneyOffers {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private List<DisneyOffer> offer = new LinkedList<>();

    public DisneyOffers(String offerInformation) {
        var mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(offerInformation);
            var iterator = node.iterator();
            while (iterator.hasNext()) {
                JsonNode current = iterator.next();
                var currentOffer = new DisneyOffer(current);
                this.getOfferList().add(currentOffer);
            }
        } catch (Exception ex) {
            LOGGER.error("Error Loading Offers {}", ex.getMessage(), ex);
        }
    }

    public List<DisneyOffer> getOfferList() {
        return offer;
    }

    public void setOfferList(List<DisneyOffer> offer) {
        this.offer = offer;
    }
}
