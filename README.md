# Graphql Builder

[![Maven Central](https://img.shields.io/maven-central/v/com.qwlabs/graphql-builder.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.qwlabs%22%20AND%20a:%22graphql-builder%22)
[![Commit](https://github.com/qwlabs/graphql-builder/actions/workflows/commit.yml/badge.svg?branch=master)](https://github.com/qwlabs/graphql-builder/actions/workflows/commit.yml)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

GraphQL query builder for Java

# Installation

## Maven

```xml

<dependency>
    <groupId>com.qwlabs</groupId>
    <artifactId>graphql-builder</artifactId>
    <version>0.2.15</version>
    <type>pom</type>
</dependency>
```

## Gradle

```gradle
implementation 'com.qwlabs:graphql-builder:0.2.15'
```

# Usage

## query

```java
Gql gql=Gql.query("contents")
        .fields(GqlField.of("nodes").fields("id","content","createdAt"),
        GqlField.of("totalCount"),
        GqlField.of("pageInfo").fields("limit","offset"));
```

- gql.buildSegment()

```graphql
query {contents{nodes{id content createdAt} totalCount pageInfo{limit offset}}}
```

- gql.buildPrettifySegment()

```graphql
query {
    contents {
        nodes {
            id
            content
            createdAt
        }
        totalCount
        pageInfo {
            limit
            offset
        }
    }
}
```

- gql.build()

```graphql
{"query":"query {contents{nodes{id content createdAt} totalCount pageInfo{limit offset}}}", "variables": null}
```

- gql.buildPrettify()

```graphql
{
"query":"query {
    contents {
        nodes {
            id
            content
            createdAt
       }
       totalCount
       pageInfo {
            limit
            offset
        }
       }
    }",
 "variables": null
}

```

## mutation


```java
Gql gql = Gql.mutation("createContents")
        .variables(GqlVariable.of("input", GqlVariables.of(
        GqlVariable.of("content", "111")
        )))
        .fields(GqlFields.of("id", "content", "createdAt"));
```

- gql.buildSegment()

```graphql
mutation {createContents(input:{content:"111"}){id content createdAt}}
```

- gql.buildPrettifySegment()

```graphql
mutation {
    createContents(input:{content:"111"}) {
        id
        content
        createdAt
    }
}
```

- gql.build()

```graphql
{"query":"mutation {createContents(input:{content:\"111\"}){id content createdAt}}", "variables": null}
```

- gql.buildPrettify()

```graphql
{
"query":"mutation {
	createContents(input:{content:\"111\"}) {
        id
        content
        createdAt
    }
}",
 "variables": null
}
```



