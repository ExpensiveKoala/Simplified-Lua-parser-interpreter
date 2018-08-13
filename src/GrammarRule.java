public enum GrammarRule {
    PROGRAM,
    BLOCK, //Repeat
    STATEMENT, //OR
    IF_STATEMENT,
    WHILE_STATEMENT,
    ASSIGNMENT_STATEMENT,
    REPEAT_STATEMENT,
    PRINT_STATEMENT,
    BOOLEAN_EXPRESSION,
    RELATIVE_OP, //OR
    ARITHMETIC_EXPRESSION, //OR
    ARITHMETIC_OP; //OR

    static {
        PROGRAM.rules = new Object[][]{
          {TokenType.FUNCTION, TokenType.ID, TokenType.OPEN_PARENTHESIS, TokenType.CLOSE_PARENTHESIS, BLOCK, TokenType.END}
        };
        BLOCK.rules = new Object[][]{
          {STATEMENT} //Special casing in code for BLOCK
        };
        STATEMENT.rules = new Object[][]{
          {IF_STATEMENT}, {ASSIGNMENT_STATEMENT}, {WHILE_STATEMENT}, {PRINT_STATEMENT}, {REPEAT_STATEMENT}
        };
        IF_STATEMENT.rules = new Object[][]{
          {TokenType.IF, BOOLEAN_EXPRESSION, TokenType.THEN, BLOCK, TokenType.ELSE, BLOCK, TokenType.END}
        };
        WHILE_STATEMENT.rules = new Object[][]{
          {TokenType.WHILE, BOOLEAN_EXPRESSION, TokenType.DO, BLOCK, TokenType.END}
        };
        ASSIGNMENT_STATEMENT.rules = new Object[][]{
          {TokenType.ID, TokenType.ASSIGNMENT_OPERATOR, ARITHMETIC_EXPRESSION}
        };
        REPEAT_STATEMENT.rules = new Object[][]{
          {TokenType.REPEAT, BLOCK, TokenType.UNTIL, BOOLEAN_EXPRESSION}
        };
        PRINT_STATEMENT.rules = new Object[][]{
          {TokenType.PRINT, TokenType.OPEN_PARENTHESIS, ARITHMETIC_EXPRESSION, TokenType.CLOSE_PARENTHESIS}
        };
        BOOLEAN_EXPRESSION.rules = new Object[][]{
          {RELATIVE_OP, ARITHMETIC_EXPRESSION, ARITHMETIC_EXPRESSION}
        };
        RELATIVE_OP.rules = new Object[][]{
          {TokenType.LE_OPERATOR}, {TokenType.LT_OPERATOR}, {TokenType.GE_OPERATOR}, {TokenType.GT_OPERATOR}, {TokenType.EQ_OPERATOR}, {TokenType.NE_OPERATOR}
        };
        ARITHMETIC_EXPRESSION.rules = new Object[][]{
          {TokenType.ID}, {TokenType.LITERAL_INTEGER}, {ARITHMETIC_OP, ARITHMETIC_EXPRESSION, ARITHMETIC_EXPRESSION}
        };
        ARITHMETIC_OP.rules = new Object[][]{
          {TokenType.ADD_OPERATOR}, {TokenType.SUB_OPERATOR}, {TokenType.MUL_OPERATOR}, {TokenType.DIV_OPERATOR}
        };
    }

    private Object[][] rules;

    public boolean isSpecialRule() {
        return this == BLOCK || rules.length > 1;
    }

    public Object[][] getRules() {
        return rules;
    }


    @Override
    public String toString() {
        return PrintColorFormatter.format(PrintColorFormatter.Color.YELLOW, "<" + super.toString() + ">");
    }
}
