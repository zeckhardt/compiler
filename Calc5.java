import java.util.Scanner;

public class Calc5 {
    
    static class Token {
        public String type;
        public String value;

        public Token(String type, String value) {
            this.type = type;
            this.value = value;
        }
    }

    static class Lexer {
        public String text;
        public int pos;
        public char currentChar;

        public Lexer(String text) {
            this.text = text;
            this.pos = 0;
            this.currentChar = this.text.charAt(this.pos);
        }

        public void advance() {
            this.pos++;
            if (this.pos > this.text.length() - 1) {
                this.currentChar = '\u0000';
            } else {
                this.currentChar = this.text.charAt(this.pos);
            }
        }

        public void skipWhitespace() {
            while (this.currentChar != '\u0000' && Character.isSpaceChar(this.currentChar)) {
                advance();
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
                if (Character.isWhitespace(this.currentChar)) {
                    skipWhitespace();
                }

                if (Character.isDigit(this.currentChar)) {
                    return new Token("INTEGER", this.integer());
                }

                if (this.currentChar == '+') {
                    advance();
                    return new Token("PLUS", "+");
                }
                if (this.currentChar == '-') {
                    advance();
                    return new Token("MINUS", "-");
                }
                if (this.currentChar == '*') {
                    advance();
                    return new Token("MUL", "*");
                }
                if (this.currentChar == '/') {
                    advance();
                    return new Token("DIV", "/");
                }
            }

            return new Token("EOF", null);
        }
    }

    static class Interpreter {
        private final Lexer lexer;
        private Token currentToken;

        public Interpreter(Lexer lexer) {
            this.lexer = lexer;
            this.currentToken = this.lexer.getNextToken();
        }

        public void eat(String tokenType) {
            if (this.currentToken.type.equals(tokenType)) {
                this.currentToken = this.lexer.getNextToken();
            }
        }

        public int factor() {
            Token token = this.currentToken;
            eat("INTEGER");
            return Integer.parseInt(token.value);
        }

        public int term() {
            int result = factor();

            while (this.currentToken.type.equals("MUL") || this.currentToken.type.equals("DIV")) {
                Token token = this.currentToken;
                if (token.type.equals("MUL")) {
                    eat("MUL");
                    result *= factor();
                } else if(token.type.equals("DIV")) {
                    eat("DIV");
                    result /= factor();
                }
            }
            return result;
        }

        public int expr() {
            int result = term();

            while (this.currentToken.type.equals("PLUS") || this.currentToken.type.equals("MINUS")) { 
                Token token = this.currentToken;
                if (token.type.equals("PLUS")) {
                    eat("PLUS");
                    result += term();
                } else if (token.type.equals("MINUS")) {
                    eat("MINUS");
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
                    Lexer lexer = new Lexer(input);
                    Interpreter interpreter = new Interpreter(lexer);
                    int result = interpreter.expr();
                    System.out.println("Result: " + result);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }
}
