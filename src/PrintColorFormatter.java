public class PrintColorFormatter {

    public static String format(Color color, String text) {
        return color.value + text + Color.RESET.value;
    }

    public enum Color {
        RESET((char) 27 + "[0m"),
        BLACK((char) 27 + "[30m"),
        RED((char) 27 + "[31m"),
        GREEN((char) 27 + "[32m"),
        YELLOW((char) 27 + "[33m"),
        BLUE((char) 27 + "[34m"),
        PURPLE((char) 27 + "[35m"),
        CYAN((char) 27 + "[36m"),
        WHITE((char) 27 + "[37m");

        String value;

        Color(String value) {
            this.value = value;
        }
    }
}
