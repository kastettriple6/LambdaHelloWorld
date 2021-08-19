package com.example.service;

import dev.fuxing.airtable.AirtableApi;
import dev.fuxing.airtable.AirtableRecord;
import dev.fuxing.airtable.AirtableTable;

import java.util.Properties;

public class AirTableService {
    private final Properties props = new Properties();
    private final String KEY = props.getProperty("airtable.api.key");
    private final String APP = props.getProperty("airtable.api.app");
    private final String FIRST_RECORD = props.getProperty("airtable.record.index.0");
    private final AirtableApi API = new AirtableApi(KEY);
    private final AirtableTable TABLE = API.base(APP).table("MainTable.csv");

    public AirtableRecord getSingleRecord() {
        return TABLE.get(FIRST_RECORD);
    }

    public AirtableTable.PaginationList getAllRecords() {
        return TABLE.list(querySpec -> {
            querySpec.fields("title");
        });
    }
}
