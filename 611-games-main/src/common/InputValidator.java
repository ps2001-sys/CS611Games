package common;

import engine.TextUI;

/**
 * Utility class for handling and validating user input.
 * Centralizes input validation logic to avoid code duplication.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class InputValidator {
    // Configuration constants (no hardcoded values in methods)
    public static final int MAX_NAME_LENGTH = 15;
    public static final int MIN_BOARD_SIZE = 1;
    public static final int MAX_BOARD_SIZE = 10;

    private final TextUI ui;

    public InputValidator(TextUI ui) {
        this.ui = ui;
    }

    /**
     * Read a non-empty string with validation.
     * Enforces name length and character restrictions.
     */
    public String readValidName(String prompt, String defaultValue) {
        ui.print(prompt);
        String input = ui.nextLine().trim();

        if (input.isEmpty()) {
            input = defaultValue;
        }

        // Enforce max length
        if (input.length() > MAX_NAME_LENGTH) {
            input = input.substring(0, MAX_NAME_LENGTH);
        }

        // Sanitize: only allow alphanumeric and underscore
        if (!input.matches("[A-Za-z0-9_]+")) {
            input = input.replaceAll("[^A-Za-z0-9_]", "_");
        }

        return input;
    }

    /**
     * Ask for an integer within specified bounds.
     * Returns default if input is invalid or out of range.
     */
    public int readBoundedInt(String prompt, int defaultValue, int min, int max) {
        ui.print(prompt);
        Integer value = ui.tryParseInt(ui.nextLine());

        if (value == null || value < min || value > max) {
            ui.println(ui.red("Using default: " + defaultValue));
            return defaultValue;
        }

        return value;
    }

    /**
     * Keep prompting until user input matches the allowed pattern.
     */
    public String readChoice(String prompt, String validPattern) {
        while (true) {
            ui.println(prompt);
            ui.print("> ");
            String input = ui.nextLine().trim();

            if (input.matches(validPattern)) {
                return input;
            }

            ui.println(ui.red("Please enter one of: " + validPattern));
        }
    }
}