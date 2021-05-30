package com.test.ingestion.model.event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DistributionEvent implements Serializable {

    private Map<String, String> headers = new HashMap<String, String>();
    private Map<String, String> payload = new HashMap<String, String>();

    public DistributionEvent(Map<String, String> headers, Map<String, String> payload) {
        this.headers = headers;
        this.payload = payload;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getPayload() {
        return payload;
    }
}
