package com.qwlabs.graphql.builder;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public final class GqlVariables {
    private final boolean root;
    private List<GqlVariable> variables;

    protected GqlVariables() {
        this(false);
    }

    protected GqlVariables(boolean root) {
        this.root = root;
    }

    public static GqlVariables of(@NotNull GqlVariable... variables) {
        return new GqlVariables(false).add(variables);
    }

    public static GqlVariables root() {
        return new GqlVariables(true);
    }

    public GqlVariables add(@NotNull GqlVariable... variables) {
        this.variables = Optional.ofNullable(this.variables).orElseGet(ArrayList::new);
        this.variables.addAll(Arrays.stream(variables)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        return this;
    }

    public String build() {
        StringJoiner joiner = new StringJoiner(",", getPrefix(), getSuffix());
        variables.stream().map(GqlVariable::build).forEach(joiner::add);
        return joiner.toString();
    }

    public String buildPrettify(int level) {
        StringJoiner joiner = new StringJoiner(", ", getPrefix(), getSuffix());
        variables.stream().map(v -> v.buildPrettify(level)).forEach(joiner::add);
        return joiner.toString();
    }

    private String getPrefix() {
        return this.root ? "(" : "{";
    }

    private String getSuffix() {
        return this.root ? ")" : "}";
    }
}
