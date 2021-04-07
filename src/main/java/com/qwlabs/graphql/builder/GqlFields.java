package com.qwlabs.graphql.builder;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public final class GqlFields {
    private List<GqlField> fields;

    protected GqlFields() {
    }

    public static GqlFields of(@NotNull GqlField... fields) {
        return new GqlFields().add(fields);
    }

    public static GqlFields of(@NotNull String... names) {
        return new GqlFields().add(names);
    }

    public GqlFields add(@NotNull GqlField... fields) {
        return add(Arrays.stream(fields)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
    }

    public GqlFields add(@NotNull String... names) {
        return add(Arrays.stream(names)
                .filter(Objects::nonNull)
                .map(GqlField::of)
                .collect(Collectors.toList()));
    }

    public GqlFields add(@NotNull Collection<GqlField> fields) {
        this.fields = Optional.ofNullable(this.fields).orElseGet(ArrayList::new);
        this.fields.addAll(fields);
        return this;
    }


    public String build() {
        StringJoiner joiner = new StringJoiner(" ", "{", "}");
        fields.stream().map(GqlField::build).forEach(joiner::add);
        return joiner.toString();
    }

    public String buildPrettify(int level) {
        StringJoiner joiner = new StringJoiner("\n",
                " {\n",
                "\n" + Gql.prettifyLevel(level) + "}");
        fields.stream().map(field -> field.buildPrettify(level + 1)).forEach(joiner::add);
        return joiner.toString();
    }
}
