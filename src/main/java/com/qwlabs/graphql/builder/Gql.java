package com.qwlabs.graphql.builder;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Optional;

public class Gql {
    private static final Map<Integer, String> PRETTIFY_LEVEL_CACHE = Maps.newConcurrentMap();
    private static final String TEMPLATE = "{\"query\":\"%s %s\", \"variables\": null}";
    private static final String PRETTIFY_TEMPLATE = "{\n \"query\":\"%s %s\",\n \"variables\": null\n}";
    private static final String QUERY = "query";
    private static final String MUTATION = "mutation";
    private final String operation;
    private final String name;

    private GqlVariables variables;
    private GqlFields fields;

    private Gql(@NotNull String operation, @NotNull String name) {
        this.operation = operation;
        this.name = name;
    }

    public static Gql query(String name) {
        return new Gql(QUERY, name);
    }

    public static Gql mutation(String name) {
        return new Gql(MUTATION, name);
    }

    public Gql fields(@NotNull GqlFields fields) {
        this.fields = fields;
        return this;
    }

    public Gql fields(@NotNull GqlField @NotNull ... fields) {
        this.fields = Optional.ofNullable(this.fields).orElseGet(GqlFields::new);
        this.fields.add(fields);
        return this;
    }

    public Gql variables(@NotNull GqlVariable @NotNull ... variables) {
        this.variables = Optional.ofNullable(this.variables).orElseGet(GqlVariables::new);
        this.variables.add(variables);
        return this;
    }

    public String build() {
        return String.format(TEMPLATE, operation, buildSegment());
    }

    public String buildPrettify() {
        return String.format(PRETTIFY_TEMPLATE, operation, buildPrettifySegment(1));
    }

    public String buildSegment() {
        StringBuilder builder = new StringBuilder("{").append(name);
        Optional.ofNullable(variables).ifPresent(vs -> builder.append(vs.build()));
        Optional.ofNullable(fields).ifPresent(fs -> builder.append(fs.build()));
        builder.append("}");
        return builder.toString();
    }

    public String buildPrettifySegment(int level) {
        StringBuilder builder = new StringBuilder("{\n")
                .append(prettifyLevel(level)).append(name);
        Optional.ofNullable(variables).ifPresent(vs -> builder.append(vs.buildPrettify(level)));
        Optional.ofNullable(fields).ifPresent(fs -> builder.append(fs.buildPrettify(level)));
        builder.append("\n}");
        return builder.toString();
    }


    protected static String prettifyLevel(int level) {
        return PRETTIFY_LEVEL_CACHE.computeIfAbsent(level, l -> Strings.repeat("\t", l));
    }
}
