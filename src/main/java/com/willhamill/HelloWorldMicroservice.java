package com.willhamill;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class HelloWorldMicroservice implements RequestHandler<String, HelloResponse>{
    String languageDefault = "en-GB";

    public HelloResponse handleRequest(String name, Context context) {

        String response = "Hello " + name;
        return new HelloResponse(languageDefault, response);
    }

}
