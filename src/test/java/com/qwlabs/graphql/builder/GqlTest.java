package com.qwlabs.graphql.builder;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class GqlTest {

    @Test
    public void should_build_query() {
        Gql gql = Gql.query("contents")
                .fields(GqlField.of("nodes").fields("id", "content", "createdAt"),
                        GqlField.of("totalCount"),
                        GqlField.of("pageInfo").fields("limit", "offset"));
        assertThat(gql.buildSegment(), is("query {contents{nodes{id content createdAt} totalCount pageInfo{limit offset}}}"));
        assertThat(gql.buildPrettifySegment(), is("query {\n\tcontents {\n\t\tnodes {\n\t\t\tid\n\t\t\tcontent\n\t\t\tcreatedAt\n\t\t}\n\t\ttotalCount\n\t\tpageInfo {\n\t\t\tlimit\n\t\t\toffset\n\t\t}\n\t}\n}"));
        assertThat(gql.build(), is("{\"query\":\"query {contents{nodes{id content createdAt} totalCount pageInfo{limit offset}}}\", \"variables\": null}"));
        assertThat(gql.buildPrettify(), is("{\n \"query\":\"query {\n\tcontents {\n\t\tnodes {\n\t\t\tid\n\t\t\tcontent\n\t\t\tcreatedAt\n\t\t}\n\t\ttotalCount\n\t\tpageInfo {\n\t\t\tlimit\n\t\t\toffset\n\t\t}\n\t}\n}\",\n \"variables\": null\n}"));
    }

    @Test
    void should_build_mutation() {
        Gql gql = Gql.mutation("createContents")
                .variables(GqlVariable.of("input", GqlVariables.of(
                        GqlVariable.of("content", "111")
                )))
                .fields(GqlFields.of("id", "content", "createdAt"));
        assertThat(gql.buildSegment(), is("mutation {createContents(input:{content:\"111\"}){id content createdAt}}"));
        assertThat(gql.buildPrettifySegment(), is("mutation {\n\tcreateContents(input:{content:\"111\"}) {\n\t\tid\n\t\tcontent\n\t\tcreatedAt\n\t}\n}"));
        assertThat(gql.build(), is("{\"query\":\"mutation {createContents(input:{content:\\\"111\\\"}){id content createdAt}}\", \"variables\": null}"));
        assertThat(gql.buildPrettify(), is("{\n \"query\":\"mutation {\n\tcreateContents(input:{content:\\\"111\\\"}) {\n\t\tid\n\t\tcontent\n\t\tcreatedAt\n\t}\n}\",\n \"variables\": null\n}"));
    }
}
