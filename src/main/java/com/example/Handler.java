package com.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

import java.util.Map;

public class Handler implements RequestHandler<Map<String,Object>, String> {
    @Override
    public String handleRequest(Map<String, Object> stringObjectMap, Context context) {
        Gson gson = new Gson();
        String message = "Hello World";
        return gson.toJson(message);
    }
}
