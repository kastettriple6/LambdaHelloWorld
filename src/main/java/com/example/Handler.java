package com.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.fuxing.airtable.AirtableApi;
import dev.fuxing.airtable.AirtableRecord;
import dev.fuxing.airtable.AirtableTable;

import java.util.Map;
import java.util.Properties;

public class Handler implements RequestHandler<Map<String, Object>, AirtableRecord> {
    private final Properties props = new Properties();
    private final String KEY = props.getProperty("airtable.api.key");
    private final String APP = props.getProperty("airtable.api.app");
    private final String FIRST_RECORD = props.getProperty("airtable.record.index.0");
    private final AirtableApi API = new AirtableApi(KEY);
    private final AirtableTable TABLE = API.base(APP).table("MainTable");

    @Override
    public AirtableRecord handleRequest(Map<String, Object> stringObjectMap, Context context) {
        return TABLE.get(FIRST_RECORD);
    }
}
