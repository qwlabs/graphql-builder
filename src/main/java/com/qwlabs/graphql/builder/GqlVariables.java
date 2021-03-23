package com.qwlabs.graphql.builder;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class GqlVariables {
    private List<GqlVariable> variables;

    public static GqlVariables of(@NotNull GqlVariable @NotNull ... variables) {
        return new GqlVariables().add(variables);
    }

    public GqlVariables add(@NotNull GqlVariable @NotNull ... variables) {
        this.variables = Optional.ofNullable(this.variables).orElseGet(ArrayList::new);
        this.variables.addAll(Arrays.stream(variables)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        return this;
    }

    public String build() {
        StringJoiner joiner = new StringJoiner(",", "{", "}");
        variables.stream().map(GqlVariable::build).forEach(joiner::add);
        return joiner.toString();
    }

    public String buildPrettify(int level) {
        StringJoiner joiner = new StringJoiner(", ", "{", "}");
        variables.stream().map(v -> v.buildPrettify(level)).forEach(joiner::add);
        return joiner.toString();
    }
}
