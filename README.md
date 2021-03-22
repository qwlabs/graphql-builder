# graphql-builder
[ ![Download](https://api.bintray.com/packages/qwlabs/graphql-builder/graphql-builder/images/download.svg) ](https://bintray.com/qwlabs/graphql-builder/graphql-builder/_latestVersion)
GraphQL query builder for Java

# Installation
## Maven
```xml
<dependency>
  <groupId>com.qwlabs</groupId>
  <artifactId>graphql-builder</artifactId>
  <version>0.1</version>
  <type>pom</type>
</dependency>
```
## Gradle
```gradle
implementation 'com.qwlabs:graphql-builder:0.1'
```


# Usage

```java
Gql gql = Gql.mutation("mutation1")
                .variables(GqlVariable.of("input", GqlVariables.of(
                        GqlVariable.of("content", "111")
                )))
                .fields(GqlFields.of("id", "content", "createdAt"));
gql.buildPrettify();
```

```graphql
mutation {
    mutation1{input: {content:"111"}} {
        id
        content
        createdAt
    }
}
```




