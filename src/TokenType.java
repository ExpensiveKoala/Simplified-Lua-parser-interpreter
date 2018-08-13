import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TokenType {
    ID("\\b[a-z]\\b"),
    LITERAL_INTEGER("\\b-?[0-9]+\\b"),
    ASSIGNMENT_OPERATOR("="),
    LE_OPERATOR("<="),
    LT_OPERATOR("<"),
    GE_OPERATOR(">="),
    GT_OPERATOR(">"),
    EQ_OPERATOR("=="),
    NE_OPERATOR("~="),
    ADD_OPERATOR("\\+"),
    SUB_OPERATOR("-"),
    MUL_OPERATOR("\\*"),
    DIV_OPERATOR("\\/"),
    FUNCTION("\\bfunction\\b"),
    END("\\bend\\b"),
    IF("\\bif\\b"),
    THEN("\\bthen\\b"),
    ELSE("\\belse\\b"),
    WHILE("\\bwhile\\b"),
    DO("\\bdo\\b"),
    REPEAT("\\brepeat\\b"),
    UNTIL("\\buntil\\b"),
    PRINT("\\bprint\\b"),
    OPEN_PARENTHESIS("\\("),
    CLOSE_PARENTHESIS("\\)"),
    UNKNOWN("");


    private static final String TOKENREGEX = "(\\b[a-z]\\b)|(\\b-?[0-9]+\\b)|(<=)|(<)|(>=)|(>)|(==)|(~=)|(=)|(\\+)|(-)|(\\*)|(\\/)|(\\bfunction\\b)|(\\bend\\b)|(\\bif\\b)|(\\bthen\\b)|(\\belse\\b)|(\\bwhile\\b)|(\\bdo\\b)|(\\brepeat\\b)|(\\buntil\\b)|(\\bprint\\b)|(\\()|(\\))";

    Pattern pattern;
    String regex;

    TokenType(String regex) {
        this.regex = regex;
        this.pattern = Pattern.compile(regex);
    }

    public boolean matches(String s) {
        return this.pattern.matcher(s).find();
    }

    public static TokenType getTokenFromString(String s) {
        if (ID.pattern.matcher(s).find()) {
            return ID;
        } else if (LITERAL_INTEGER.pattern.matcher(s).find()) {
            return LITERAL_INTEGER;
        } else if (LE_OPERATOR.pattern.matcher(s).find()) {
            return LE_OPERATOR;
        } else if (LT_OPERATOR.pattern.matcher(s).find()) {
            return LT_OPERATOR;
        } else if (GE_OPERATOR.pattern.matcher(s).find()) {
            return GE_OPERATOR;
        } else if (GT_OPERATOR.pattern.matcher(s).find()) {
            return GT_OPERATOR;
        } else if (EQ_OPERATOR.pattern.matcher(s).find()) {
            return EQ_OPERATOR;
        } else if (NE_OPERATOR.pattern.matcher(s).find()) {
            return NE_OPERATOR;
        } else if (ASSIGNMENT_OPERATOR.pattern.matcher(s).find()) {
            return ASSIGNMENT_OPERATOR;
        } else if (ADD_OPERATOR.pattern.matcher(s).find()) {
            return ADD_OPERATOR;
        } else if (SUB_OPERATOR.pattern.matcher(s).find()) {
            return SUB_OPERATOR;
        } else if (MUL_OPERATOR.pattern.matcher(s).find()) {
            return MUL_OPERATOR;
        } else if (DIV_OPERATOR.pattern.matcher(s).find()) {
            return DIV_OPERATOR;
        } else if (FUNCTION.pattern.matcher(s).find()) {
            return FUNCTION;
        } else if (END.pattern.matcher(s).find()) {
            return END;
        } else if (IF.pattern.matcher(s).find()) {
            return IF;
        } else if (THEN.pattern.matcher(s).find()) {
            return THEN;
        } else if (ELSE.pattern.matcher(s).find()) {
            return ELSE;
        } else if (WHILE.pattern.matcher(s).find()) {
            return WHILE;
        } else if (DO.pattern.matcher(s).find()) {
            return DO;
        } else if (REPEAT.pattern.matcher(s).find()) {
            return REPEAT;
        } else if (UNTIL.pattern.matcher(s).find()) {
            return UNTIL;
        } else if (PRINT.pattern.matcher(s).find()) {
            return PRINT;
        } else if (OPEN_PARENTHESIS.pattern.matcher(s).find()) {
            return OPEN_PARENTHESIS;
        } else if (CLOSE_PARENTHESIS.pattern.matcher(s).find()) {
            return CLOSE_PARENTHESIS;
        } else {
            return UNKNOWN;
        }

    }

    public static List<Pair<TokenType, String>> getTokens(String s) {
        List<Pair<TokenType, String>> tokens = new ArrayList<>();

        Matcher m = Pattern.compile(TOKENREGEX).matcher(s);
        while (m.find()) {
            String match = m.group();
            tokens.add(new Pair<>(getTokenFromString(match), match));
        }

        return tokens;
    }
}
