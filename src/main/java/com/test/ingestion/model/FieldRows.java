package com.test.ingestion.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FieldRows {

    private Map<String, String> payload = new HashMap<String, String>();

    public FieldRows(Map<String, String> row) {
        this.payload = row;
    }

    public Map<String, String> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, String> payload) {
        this.payload = payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldRows fieldRows = (FieldRows) o;
        return Objects.equals(payload, fieldRows.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payload);
    }
}
