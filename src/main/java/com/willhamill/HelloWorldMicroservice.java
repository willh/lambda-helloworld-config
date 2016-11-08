package com.willhamill;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Optional;

public class HelloWorldMicroservice implements RequestHandler<String, String> {

    private Item dynamoDbConfigItem;

    public String handleRequest(String name, Context context) {
        ContextWrapper contextWrapper = new ContextWrapper(context);
        if (dynamoDbConfigItem == null) {
            dynamoDbConfigItem = contextWrapper.getConfigItem();
        }
        String responseLanguage = dynamoDbConfigItem.getString("responseLanguage");

        Mustache mustache = new DefaultMustacheFactory().compile("hellopage.mustache");

        String envVersion = Optional.ofNullable(context.getFunctionVersion()).orElse("no-alias");
        String functionName = Optional.ofNullable(context.getFunctionName()).orElse("default-name");
        String arnName = Optional.ofNullable(context.getInvokedFunctionArn()).orElse("default-arn");

        String response = "Hello " + name + " from version: " + envVersion + " of " + functionName + " with arn: " + arnName;

        HelloResponse responseData = new HelloResponse(responseLanguage, response);
        StringWriter stringWriter = new StringWriter();

        try {
            mustache.execute(stringWriter, responseData).flush();
        } catch (IOException e) {
            stringWriter.append("Failed to execute mustache template and flush output");
        }

        return stringWriter.toString();
    }

}
