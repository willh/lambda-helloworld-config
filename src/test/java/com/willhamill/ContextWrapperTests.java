package com.willhamill;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class ContextWrapperTests {

    private static final Context MOCK_CONTEXT = mock(Context.class);
    private static final String BARE_FUNCTION_NAME = "helloWorldFunction";
    private static final String FUNCTION_NAME_WITH_ENV_SUFFIX = "helloWorldFunction_prd";
    private static final String ARN_WITH_ALIAS = "arn:aws:lambda:eu-west-1:1234567890:function:helloWorldFunction_prd:latestalias";
    private static final String ARN_WITHOUT_ALIAS = "arn:aws:lambda:eu-west-1:937603462484:function:helloWorldFunction_prd";

    @Before
    public void setUp() {
        reset(MOCK_CONTEXT);
    }

    @Test
    public void getAliasFromFunctionNameWithAliasReturnsAlias() {
        when(MOCK_CONTEXT.getFunctionName()).thenReturn(FUNCTION_NAME_WITH_ENV_SUFFIX);
        when(MOCK_CONTEXT.getInvokedFunctionArn()).thenReturn(ARN_WITH_ALIAS);
        ContextWrapper testWrapper = new ContextWrapper(MOCK_CONTEXT);

        assertThat(testWrapper.getAlias().get(), is("latestalias"));
    }

    @Test
    public void getAliasFromFunctionNameWithoutAliasReturnsEmpty() {
        when(MOCK_CONTEXT.getFunctionName()).thenReturn(FUNCTION_NAME_WITH_ENV_SUFFIX);
        when(MOCK_CONTEXT.getInvokedFunctionArn()).thenReturn(ARN_WITHOUT_ALIAS);
        ContextWrapper testWrapper = new ContextWrapper(MOCK_CONTEXT);

        assertThat(testWrapper.getAlias().isPresent(), is(false));
    }

    @Test
    public void getEnvSuffixFromFunctionNameWithEnvironmentSuffixReturnsSuffix() {
        when(MOCK_CONTEXT.getFunctionName()).thenReturn(FUNCTION_NAME_WITH_ENV_SUFFIX);
        ContextWrapper testWrapper = new ContextWrapper(MOCK_CONTEXT);

        assertThat(testWrapper.getFunctionNameEnvironmentSuffix().get(), is("prd"));
    }

    @Test
    public void getEnvSuffixFromFunctionNameWithoutEnvironmentSuffixReturnsEmpty() {
        when(MOCK_CONTEXT.getFunctionName()).thenReturn(BARE_FUNCTION_NAME);
        ContextWrapper testWrapper = new ContextWrapper(MOCK_CONTEXT);

        assertThat(testWrapper.getFunctionNameEnvironmentSuffix().isPresent(), is(false));

    }

    @Test
    public void getDeploymentRegionFromArnReturnsRegion() {
        when(MOCK_CONTEXT.getFunctionName()).thenReturn(FUNCTION_NAME_WITH_ENV_SUFFIX);
        when(MOCK_CONTEXT.getInvokedFunctionArn()).thenReturn(ARN_WITHOUT_ALIAS);
        ContextWrapper testWrapper = new ContextWrapper(MOCK_CONTEXT);

        assertThat(testWrapper.getDeploymentRegion(), is(Regions.EU_WEST_1));
    }

    @Test
    public void getConfigTableNameFromFunctionNameWithEnvSuffix() {
        when(MOCK_CONTEXT.getFunctionName()).thenReturn(FUNCTION_NAME_WITH_ENV_SUFFIX);
        ContextWrapper testWrapper = new ContextWrapper(MOCK_CONTEXT);

        assertThat(testWrapper.getConfigTableName(FUNCTION_NAME_WITH_ENV_SUFFIX), is("helloWorldFunctionConfig"));
    }
}
