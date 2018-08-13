public class Token {
    private TokenType token;
    private String text;
    private int lineNumber;
    private int colNumber;

    //Immutable token class
    public Token(TokenType token, String text, int lineNumber, int colNumber) {
        this.token = token;
        this.text = text;
        this.lineNumber = lineNumber;
        this.colNumber = colNumber;
    }

    public TokenType getToken() {
        return token;
    }

    public String getText() {
        return text;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColNumber() {
        return colNumber;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getToken());
        stringBuilder.append(" : ");
        stringBuilder.append(getText());
        return PrintColorFormatter.format(PrintColorFormatter.Color.CYAN, stringBuilder.toString());
    }
}
