package com.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.airtable.AirtableApi;
import dev.fuxing.airtable.AirtableTable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Handler implements RequestHandler<Map<String, Object>, List<String>> {
    private InputStream is;
    private Properties prop = new Properties();
    private Offset offset = new Offset();

    @Override
    public List<String> handleRequest(Map<String, Object> s, Context context) {

        int offs = offset.get();
        return Arrays.asList(getRecord(offs), getRecord(offs + 1), getRecord(offs + 2));
    }


    public String getRecord(int index) {
        String key = prop.getProperty("airtable.api.key");
        String app = prop.getProperty("airtable.api.app");
        String tableId = prop.getProperty("airtable.api.table");

        is = getClass().getClassLoader().getResourceAsStream("config.properties");
        try {
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AirtableApi api = new AirtableApi(key);
        AirtableApi.Application base = api.base(app);
        AirtableApi.Table table = base.table(tableId);

        index %= table.list().size();
        JsonNode response = table.list(querySpec -> {
            querySpec.sort("ID");
            querySpec.fields("title");
        })
                .get(index).getField("title");
        return response.toPrettyString();
    }
}

class Offset {

    private AtomicInteger offset = new AtomicInteger();

    {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(() -> offset.incrementAndGet(), 0, 1, TimeUnit.SECONDS);
    }

    public int get() {
        return offset.get();
    }
}
