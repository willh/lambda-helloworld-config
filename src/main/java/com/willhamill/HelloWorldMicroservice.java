package com.willhamill;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Optional;

/*
    This class acts as the main function handler for the lambda
 */
public class HelloWorldMicroservice implements RequestHandler<String, String> {

    private static final String DEFAULT_LANGUAGE = "en-GB";

    /*
        This method is the main handler for the hello world function,
        taking a string param and returning a HTML templated response
        containing a language read in from environment variable config
     */
    public String handleRequest(String name, Context context) {
        String responseLanguage = Optional.ofNullable(System.getenv("language")).orElse(DEFAULT_LANGUAGE);

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
