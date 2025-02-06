import java.util.*;

public class Calc3 {
    public enum TokenTypes {
        INTEGER,
        PLUS,
        MINUS,
        EOF
    }

    /**
     * Token class with types of INTEGER, PLUS, MINUS, or EOF and a value
     */
    static class Token {
        public TokenTypes type;
        public String value;

        public Token(TokenTypes type, String value) {
            this.type = type;
            this.value = value;
        }
    }

    static class Interpreter {
        public String text;
        public int pos;
        public Token currentToken;
        public char currentChar;

        public Interpreter(String text) {
            this.text = text;
            this.pos = 0;
            this.currentToken = null;
            this.currentChar = this.text.charAt(this.pos);
        }

        //##########################################################
        //# Lexer code                                             #
        //##########################################################
        public void advance() {
            this.pos++;
            if (this.pos > this.text.length() - 1) {
                this.currentChar = '\u0000';
            } else {
                this.currentChar = text.charAt(this.pos);
            }
        }

        public String integer() {
            String result = "";
            while (this.currentChar != '\u0000' && Character.isDigit(this.currentChar)) {
                result += this.currentChar;
                advance();
            }
            return result;
        }

        public Token getNextToken() {
            while (this.currentChar != '\u0000') {
                if (Character.isSpaceChar(this.currentChar)) {
                    skipWhitespace();
                }

                if (Character.isDigit(this.currentChar)) {
                    return new Token(TokenTypes.INTEGER, integer());
                }

                if (this.currentChar == '+') {
                    advance();
                    return new Token(TokenTypes.PLUS, "+");
                }

                if (this.currentChar == '-') {
                    advance();
                    return new Token(TokenTypes.MINUS, "-");
                }
            }

            return new Token(TokenTypes.EOF, null);
        }

        public void skipWhitespace() {
            while (this.currentChar != '\u0000' && Character.isSpaceChar(this.currentChar)) { 
                advance();
            }
        }

        public void eat(TokenTypes tokenType) {
            if (this.currentToken.type == tokenType) {
                this.currentToken = getNextToken();
            }
        }

        public int term() {
            Token token = this.currentToken;
            eat(TokenTypes.INTEGER);
            return Integer.parseInt(token.value);
        }

        //##########################################################
        //# Interpreter/Parser code                                #
        //##########################################################
        public int expr() {
            this.currentToken = this.getNextToken();

            int result = term();
            while (this.currentToken.type == TokenTypes.PLUS || this.currentToken.type == TokenTypes.MINUS) {
                if (this.currentToken.type == TokenTypes.PLUS) {
                    eat(TokenTypes.PLUS);
                    result += term();
                } else if (this.currentToken.type == TokenTypes.MINUS) {
                    eat(TokenTypes.MINUS);
                    result -= term();
                }
            }
            return result;
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                try {
                    System.out.print("Enter expression (or type 'exit' to quit): ");
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("exit")) {
                        break;
                    }
                    Interpreter interpreter = new Interpreter(input);
                    int result = interpreter.expr();
                    System.out.println("Result: " + result);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }
}