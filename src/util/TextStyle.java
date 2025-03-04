/**An enum is a special "class" that represents a group of constants (unchangeable variables).
 *Enum constants are public, static and final*/

package util;

public enum TextStyle {

    PURPLE("\u001b[38;5;171m"),
    GREEN ("\u001B[32m"),
    YELLOW("\u001B[38;5;227m"),
    RED("\u001B[38;5;197m"),
    BLUE("\u001B[34m"),
    BOLD("\u001B[1m"),
    RESET("\u001B[0m"),
    ATTENTION("🚨"),
    ARROW("➜"),
    EMAIL("✏\uFE0F"),
    PASSWORD("\uD83D\uDD13"),
    GOODBYE("👋"),
    PLEASE("🙏"),
    HEART("💙"),
    CONGRATULATIONS("🥳"),
    CONFETTI("🎉"),
    NO("🛑✋🚷⛔"),
    NOTFOUND("\uD83E\uDD37\u200D♂\uFE0F"),

    BYE(    "Exit the program...\n" +
            BOLD.getStyle() +
            YELLOW.getStyle() +
            ARROW.getStyle() + " Good bye!" +
            GOODBYE.getStyle() + "\n" +
            RESET.getStyle()),

    OPTION (PURPLE.getStyle() +
            ARROW.getStyle() + " Choose an option: "  +
            PLEASE.getStyle() +
            RESET.getStyle() + "\n"),

    WRONG_OPTION   (RED.getStyle() +
                    ARROW.getStyle() + " Wrong option " +
                    NOTFOUND.getStyle() +
                    BOLD.getStyle() +
                    BLUE.getStyle() + " Try again " +
                    HEART.getStyle() +
                    RESET.getStyle() + "\n"),;

    private final String style;

    //constructor
    TextStyle(String style) {
        this.style = style;
    }

    //getter
    public String getStyle() {
        return style;
    }
}
