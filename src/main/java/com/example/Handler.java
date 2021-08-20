package com.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.fuxing.airtable.AirtableApi;
import dev.fuxing.airtable.AirtableTable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Handler implements RequestHandler<String, String> {

    @Override
    public String handleRequest(String s, Context context) {
        Properties props = new Properties();
        InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties");
        try {
            props.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String key = props.getProperty("airtable.api.key");
        String app = props.getProperty("airtable.api.app");
        String tableId = props.getProperty("airtable.api.table");

        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        AirtableApi api = new AirtableApi(key);
        AirtableApi.Application base = api.base(app);
        AirtableTable table = base.table(tableId);

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
