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
    private final String TABLE = props.getProperty("airtable.api.table");

    @Override
    public AirtableRecord handleRequest(Map<String, Object> stringObjectMap, Context context) {
        AirtableApi api = new AirtableApi(KEY);
        AirtableApi.Application base = api.base(APP);
        AirtableTable table = base.table(TABLE);

        while (true) {
            int i = 0;
            while (i < table.list().size()) {
                int j = i;

                for (int k = 0; k < 3; k++) {
                    if (j == table.list().size()) {
                        j = 0;
                    }
                    System.out.println(table.list(querySpec -> {
                        querySpec.sort("ID");
                        querySpec.fields("title");
                    })
                            .get(j).getField("title"));
                    j++;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                i++;
            }
        }
    }
}
