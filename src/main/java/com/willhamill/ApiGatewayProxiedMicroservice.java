package com.willhamill;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.willhamill.lambda.apigateway.ApiGatewayProxyResponse;
import com.willhamill.lambda.apigateway.ApiGatewayRequest;

public class ApiGatewayProxiedMicroservice implements RequestHandler<ApiGatewayRequest, ApiGatewayProxyResponse> {

    public ApiGatewayProxyResponse handleRequest(ApiGatewayRequest request, Context context) {
        String name = request.getPathParameters().getOrDefault("name", "Steve");
        String reply = "Hello " + name;
        ApiGatewayProxyResponse response = new ApiGatewayProxyResponse(200, null, reply);
        return response;
    }
}
