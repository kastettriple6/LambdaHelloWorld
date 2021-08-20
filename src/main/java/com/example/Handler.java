package com.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.fuxing.airtable.AirtableApi;
import dev.fuxing.airtable.AirtableTable;

public class Handler implements RequestHandler<String, JsonNode> {

    @Override
    public JsonNode handleRequest(String s, Context context) {
        AirtableApi api = new AirtableApi("keylMkRVCV25NxOBl");
        AirtableApi.Application base = api.base("appW1Vv1p4IHxtiWQ");
        AirtableTable table = base.table("tblX1jGmYZoqyHrlE");

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        ObjectMapper objectMapper = new ObjectMapper();

        while (true) {
            int i = 0;
            while (i < table.list().size()) {
                int j = i;
                for (int k = 0; k < 3; k++) {
                    if (j == table.list().size()) {
                        j = 0;
                    }
                    try {
                        String json = objectMapper.writeValueAsString(
                        table.list(querySpec -> {
                            querySpec.sort("ID");
                            querySpec.fields("title");
                        })
                                .get(j).getField("title"));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
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
