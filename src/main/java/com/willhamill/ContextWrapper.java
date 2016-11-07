package com.willhamill;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;

import java.util.Optional;

/*
 * Context-wrapping class providing convenience methods for
 * determining lambda region, function name suffix, alias & version,
 * and returning a config object from DynamoDB defined by function name
 */
public class ContextWrapper {

    private static final String CONFIG_TABLE_SUFFIX = "Config";
    private static final String DEFAULT_ENVIRONMENT = "test";
    private static final String CONFIG_TABLE_KEY_NAME = "environmentId";

    private final Context context;

    public ContextWrapper(Context context) {
        this.context = context;
    }

    public Optional<String> getAlias() {
        String functionName = context.getFunctionName();
        String amazonResourceName = context.getInvokedFunctionArn();

        String functionNameAndAliasIfPresent = amazonResourceName.substring(amazonResourceName.lastIndexOf(functionName));

        boolean containsAlias = functionNameAndAliasIfPresent.contains(":");

        if (containsAlias) {
            return Optional.of(functionNameAndAliasIfPresent.substring(functionNameAndAliasIfPresent.lastIndexOf(':') + 1));
        } else {
            return Optional.empty();
        }
    }

    public Optional<String> getFunctionNameEnvironmentSuffix() {
        // assuming naming convention like helloWorldFunction_dev, helloWorldFunction_prd etc
        String functionName = context.getFunctionName();

        boolean containsSuffix = functionName.contains("_");

        if (containsSuffix) {
            return Optional.of(functionName.substring(functionName.lastIndexOf('_') + 1));
        } else {
            return Optional.empty();
        }
    }

    public Regions getDeploymentRegion() {
        String[] arnColonSeparatedTokens = context.getInvokedFunctionArn().split(":");
        String regionName = arnColonSeparatedTokens[3];
        return Regions.fromName(regionName);
    }

    public Item getConfigItem() {
        Regions currentRegion = getDeploymentRegion();
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient().withRegion(currentRegion);

        String configTableName = getConfigTableName(context.getFunctionName());
        Table configTable = new DynamoDB(dynamoDBClient).getTable(configTableName);
        String environment = getFunctionNameEnvironmentSuffix().orElse(DEFAULT_ENVIRONMENT);

        System.out.println("Looking up config table : " + configTableName + " for config by key: "+environment);
        return configTable.getItem(CONFIG_TABLE_KEY_NAME, environment);
    }

    public String getConfigTableName(String functionName) {
        String configTableName = functionName + CONFIG_TABLE_SUFFIX;
        Optional<String> envSuffix = getFunctionNameEnvironmentSuffix();

        if (envSuffix.isPresent()) {
            // strip environment suffix from config table name if present
            configTableName = configTableName.replace("_"+envSuffix.get(), "");
        }

        return configTableName;
    }

    public Context getContext() {
        return context;
    }
}
