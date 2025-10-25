package common;

import engine.TextUI;

public class InputValidator {
    public static final int MAX_NAME_LENGTH = 15;
    public static final int MIN_BOARD_SIZE = 1;
    public static final int MAX_BOARD_SIZE = 10;

    private final TextUI ui;

    public InputValidator(TextUI ui) {
        this.ui = ui;
    }

    public String readValidName(String prompt, String defaultValue) {
        ui.print(prompt);
        String input = ui.nextLine().trim();

        if (input.isEmpty()) {
            input = defaultValue;
        }

        if (input.length() > MAX_NAME_LENGTH) {
            input = input.substring(0, MAX_NAME_LENGTH);
        }

        if (!input.matches("[A-Za-z0-9_]+")) {
            input = input.replaceAll("[^A-Za-z0-9_]", "_");
        }

        return input;
    }

    public int readBoundedInt(String prompt, int defaultValue, int min, int max) {
        ui.print(prompt);
        Integer value = ui.tryParseInt(ui.nextLine());

        if (value == null || value < min || value > max) {
            ui.println(ui.red("Using default: " + defaultValue));
            return defaultValue;
        }

        return value;
    }

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