package dev.itea.echo.entity;

import java.util.function.Function;

/**
 * Custom sensitive strategy
 *
 * @author: isixe
 * @create: 2024-02-06 19:56
 **/
public enum SensitiveStrategy {

    /**
     * password
     */
    PASSWORD(s -> s.replaceAll("(?<=\\S{3})\\S{54}(?=\\S{3})", "*".repeat(10)));

    private final Function<String, String> desensitizer;

    SensitiveStrategy(Function<String, String> desensitizer) {
        this.desensitizer = desensitizer;
    }

    public Function<String, String> desensitizer() {
        return desensitizer;
    }
}
