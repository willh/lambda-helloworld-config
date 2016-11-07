## AWS Lambda HelloWorld with DynamoDB Config

This project for `helloWorldFunction` contains the lambda request handler for replying with a hello response to a name parameter, a context wrapper class and some unit tests.

The `ContextWrapper` class is used to wrap the lambda execution context class with a few convenience methods for determining the alias and version of the lambda, and from inferring the execution environment from the function name assuming a naming convention like `helloWorldFunction_dev` or `helloWorldFunction_prd`.

Configuration is stored in a DynamoDB to which the lambda must be given IAM access permissions, and assumes a configuration table naming convention like `helloWorldFunctionConfig`.

When executing, the lambda depends on a DynamoDB table entry for the environment keyed on `environmentId` and containing a string property `responseLanguage`.

See accompanying blog post for details <TBC>