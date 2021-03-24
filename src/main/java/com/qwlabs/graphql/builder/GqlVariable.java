package com.qwlabs.graphql.builder;

import com.google.common.collect.Sets;

import javax.validation.constraints.NotNull;
import java.util.Set;

public final class GqlVariable {
    private static final String PREFIX_PLACEHOLDER = "==GqlVariable==PREFIX==";
    private static final String SUFFIX_PLACEHOLDER = "==GqlVariable==SUFFIX==";
    private static final Set<Class<?>> CLEAN_TYPES = Sets.newHashSet(
            Integer.class, Long.class, Double.class, Float.class);
    private static final String TEMPLATE = "%s:%s";
    private static final String PRETTIFY_TEMPLATE = "%s: %s";
    private final String name;
    private final Object value;

    private GqlVariable(@NotNull String name, @NotNull Object value) {
        this.name = name;
        this.value = value;
    }

    public static GqlVariable of(@NotNull String name, @NotNull Object value) {
        return new GqlVariable(name, value);
    }

    public String build() {
        return String.format(TEMPLATE, name, buildValue());
    }

    public String buildPrettify(int level) {
        return String.format(PRETTIFY_TEMPLATE, name, buildValue());
    }

    private String buildValue() {
        if (value instanceof GqlVariable) {
            throw new IllegalArgumentException("GqlVariable cannot be used as the value of GqlVariable");
        }
        if (value instanceof GqlVariables) {
            return ((GqlVariables) value).build();
        }
        boolean isCleanType = CLEAN_TYPES.stream().anyMatch(type -> value.getClass().isAssignableFrom(type));
        if (isCleanType) {
            return value.toString();
        }
        return PREFIX_PLACEHOLDER + value.toString() + SUFFIX_PLACEHOLDER;
    }

    protected static String replacePlaceholder(String variables, String prefix, String suffix) {
        variables = variables.replace(PREFIX_PLACEHOLDER, prefix);
        variables = variables.replace(SUFFIX_PLACEHOLDER, suffix);
        return variables;
    }
}
