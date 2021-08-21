package com.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.airtable.AirtableApi;
import dev.fuxing.airtable.AirtableTable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class Handler implements RequestHandler<Map<String, Object>, String> {

    @Override
    public String handleRequest(Map<String, Object> s, Context context) {
        InputStream is;
        Properties prop = new Properties();
        is = getClass().getClassLoader().getResourceAsStream("config.properties");
        try {
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String key = prop.getProperty("airtable.api.key");
        String app = prop.getProperty("airtable.api.app");
        String tableId = prop.getProperty("airtable.api.table");

        AirtableApi api = new AirtableApi(key);
        AirtableApi.Application base = api.base(app);
        AirtableTable table = base.table(tableId);

        String printing = "";
        try {
            while (true) {
                int i = 0;
                while (i < table.list().size()) {
                    int j = i;
                    for (int k = 0; k < 3; k++) {
                        if (j == table.list().size()) {
                            j = 0;
                        }
                        JsonNode response = table.list(querySpec -> {
                            querySpec.sort("ID");
                            querySpec.fields("title");
                        })
                                .get(j).getField("title");
                        printing = response.toPrettyString();
                        System.out.println(printing);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return printing;
    }
}
