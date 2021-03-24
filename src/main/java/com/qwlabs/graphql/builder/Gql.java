package com.qwlabs.graphql.builder;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

public final class Gql {
    private static final Map<Integer, String> PRETTIFY_LEVEL_CACHE = Maps.newConcurrentMap();
    private static final String TEMPLATE = "{\"query\":\"%s %s\", \"variables\": null}";
    private static final String SEGMENT_TEMPLATE = "%s %s";
    private static final String PRETTIFY_TEMPLATE = "{\n \"query\":\"%s %s\",\n \"variables\": null\n}";
    private static final String PRETTIFY_SEGMENT_TEMPLATE = "%s %s";
    private static final String QUERY = "query";
    private static final String MUTATION = "mutation";
    private final String operation;
    private final String name;

    private List<GqlVariable> variables;
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

    public Gql fields(@NotNull GqlField... fields) {
        this.fields = Optional.ofNullable(this.fields).orElseGet(GqlFields::new);
        this.fields.add(fields);
        return this;
    }

    public Gql variables(@NotNull GqlVariable... variables) {
        this.variables = Optional.ofNullable(this.variables).orElseGet(ArrayList::new);
        Arrays.stream(variables)
                .filter(Objects::nonNull)
                .forEach(this.variables::add);
        return this;
    }

    public String build() {
        return String.format(TEMPLATE, operation, doBuildSegment("\\\"", "\\\""));
    }

    public String buildPrettify() {
        return String.format(PRETTIFY_TEMPLATE, operation, doBuildPrettifySegment(1, "\\\"", "\\\""));
    }

    public String buildSegment() {
        return String.format(SEGMENT_TEMPLATE, operation, doBuildSegment("\"", "\""));
    }

    public String buildPrettifySegment() {
        return String.format(PRETTIFY_SEGMENT_TEMPLATE, operation, doBuildPrettifySegment(1, "\"", "\""));
    }

    private String doBuildSegment(String variablePrefix, String variableSuffix) {
        StringBuilder builder = new StringBuilder("{").append(name);
        builder.append(buildVariables(variablePrefix, variableSuffix));
        Optional.ofNullable(fields).ifPresent(fs -> builder.append(fs.build()));
        builder.append("}");
        return builder.toString();
    }

    private String doBuildPrettifySegment(int level, String variablePrefix, String variableSuffix) {
        StringBuilder builder = new StringBuilder("{\n")
                .append(prettifyLevel(level)).append(name);
        builder.append(buildVariables(variablePrefix, variableSuffix));
        Optional.ofNullable(fields).ifPresent(fs -> builder.append(fs.buildPrettify(level)));
        builder.append("\n}");
        return builder.toString();
    }

    private String buildVariables(String variablePrefix, String variableSuffix) {
        return Optional.ofNullable(variables)
                .map(vs -> {
                    StringJoiner joiner = new StringJoiner(",", "(", ")");
                    vs.stream()
                            .filter(Objects::nonNull)
                            .forEach(v -> joiner.add(v.build()));
                    return joiner.toString();
                })
                .map(vs -> GqlVariable.replacePlaceholder(vs, variablePrefix, variableSuffix))
                .orElse("");
    }


    protected static String prettifyLevel(int level) {
        return PRETTIFY_LEVEL_CACHE.computeIfAbsent(level, l -> Strings.repeat("\t", l));
    }
}
