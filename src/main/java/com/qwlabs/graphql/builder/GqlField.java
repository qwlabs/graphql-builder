package com.qwlabs.graphql.builder;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public final class GqlField {
    private final String name;
    private GqlVariables variables;
    private GqlFields fields;

    private GqlField(@NotNull String name) {
        this.name = name;
    }

    public static GqlField of(@NotNull String name) {
        return new GqlField(name);
    }

    public GqlField variables(@NotNull GqlVariable... variables) {
        this.variables = Optional.ofNullable(this.variables).orElseGet(GqlVariables::of);
        this.variables.add(variables);
        return this;
    }

    public GqlField fields(@NotNull String... names) {
        return fields(Arrays.stream(names).map(GqlField::of).collect(Collectors.toList()));
    }

    public GqlField fields(@NotNull GqlField... fields) {
        return fields(Arrays.asList(fields));
    }

    public GqlField fields(@NotNull Collection<GqlField> fields) {
        this.fields = Optional.ofNullable(this.fields).orElseGet(GqlFields::new);
        this.fields.add(fields);
        return this;
    }

    public String build() {
        StringBuilder builder = new StringBuilder(name);
        Optional.ofNullable(variables).ifPresent(vs -> builder.append(vs.build()));
        Optional.ofNullable(fields).ifPresent(fs -> builder.append(fs.build()));
        return builder.toString();
    }

    public String buildPrettify(int level) {
        StringBuilder builder = new StringBuilder(Gql.prettifyLevel(level)).append(name);
        Optional.ofNullable(variables).ifPresent(vs -> builder.append(vs.buildPrettify(level)));
        Optional.ofNullable(fields).ifPresent(fs -> builder.append(fs.buildPrettify(level)));
        return builder.toString();
    }
}
