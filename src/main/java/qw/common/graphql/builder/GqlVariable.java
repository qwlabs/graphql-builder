package qw.common.graphql.builder;

import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class GqlVariable {
    private static final Set<Class<?>> CLEAN_TYPES = Sets.newHashSet(
            Integer.class, Long.class, Double.class, Float.class);
    private static final String TEMPLATE = "%s:%s";
    private static final String PRETTIFY_TEMPLATE = "%s: %s";
    private final String name;
    private final Object value;

    public GqlVariable(@NotNull String name, @NotNull Object value) {
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
        if (value instanceof GqlVariables) {
            return ((GqlVariables) value).build();
        }
        boolean isCleanType = CLEAN_TYPES.stream().anyMatch(type -> value.getClass().isAssignableFrom(type));
        if (isCleanType) {
            return value.toString();
        }
        return "\\\"" + value.toString() + "\\\"";
    }
}
