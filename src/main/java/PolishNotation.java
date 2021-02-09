import java.util.Stack;

public class PolishNotation {

    /**
     * Helps to set priority of operators
     *
     * @param c operator
     * @return priority
     */
    static int precedence(char c) {
        switch (c) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
        }
        return -1;
    }

    /**
     * Creates expression written in normal polish notation (NPN) based on infix notation
     *
     * @param expression in infix notation
     * @return expression in NPN
     */
    public static StringBuilder createNPN(String expression) {

        StringBuilder result = new StringBuilder();
        if (!isValidExpression(expression)) {
            throw new IllegalArgumentException();
        } else {
            StringBuilder input = new StringBuilder(expression);
            input.reverse();
            Stack<Character> stack = new Stack<>();

            char[] charsExp = new String(input).toCharArray();
            for (int i = 0; i < charsExp.length; i++) {

                if (charsExp[i] == '(') {
                    charsExp[i] = ')';
                    i++;
                } else if (charsExp[i] == ')') {
                    charsExp[i] = '(';
                    i++;
                }
            }
            for (char c : charsExp) {
                //check if char is operator or operand
                if (precedence(c) > 0) {
                    while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(c)) {
                        result.append(stack.pop());
                    }
                    stack.push(c);
                } else if (c == ')') {
                    char x = stack.pop();
                    while (x != '(') {
                        result.append(x);
                        x = stack.pop();
                    }
                } else if (c == '(') {
                    stack.push(c);
                } else {
                    //character is neither operator nor "("
                    result.append(c);
                }
            }

            for (int i = 0; i <= stack.size(); i++) {
                result.append(stack.pop());
            }
        }
        return result.reverse();
    }

    /**
     * Evaluates expression written in normal polish notation (NPN)
     *
     * @param expression expression to evaluate
     * @return the result
     */
    public static Double evaluateNPN(String expression) {

        StringBuilder input = new StringBuilder(expression);
        input.reverse();

        Stack<Double> stack = new Stack<>();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            //check if it is a space (separator)
            if (c == ' ')
                continue;

            if (c == '*' || c == '/' || c == '^' || c == '+' || c == '-') {
                double s1 = stack.pop();
                double s2 = stack.pop();
                double temp = evaluate(s2, s1, c);
                stack.push(temp);
            } else {
                //if here means, its a digit

                //extract the characters and store it in num
                StringBuilder temp = new StringBuilder();
                while (Character.isDigit(c)) {
                    temp.append(c);
                    i++;
                    c = input.charAt(i);
                }
                i--;
                //push the number in stack
                double num = Double.parseDouble(temp.reverse().toString());
                stack.push(num);
            }
        }
        return stack.pop();
    }

    /**
     * Performs operations +,-,*,/
     *
     * @param a        first operand
     * @param b        second operand
     * @param operator one of the operators
     * @return the result of the operation between two operands
     */
    static Double evaluate(double a, double b, char operator) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return b - a;
            case '*':
                return a * b;
            case '/':
                if (a == 0)
                    throw new
                            UnsupportedOperationException("Cannot divide by zero");
                return b / a;
        }
        return 0.0;
    }

    /**
     * Utility function to check if a given character is an arithmetic operator
     *
     * @param c character to analyze
     * @return true if operator, false if not
     */
    public static boolean isAnOperator(char c) {
        return (c == '*' || c == '/' || c == '+' || c == '-');
    }

    /**
     * Checks position and placement of (, ), and operators in a string
     * to make sure it is a valid arithmetic expression
     *
     * @param expression expression to validate
     * @return true if the string is a valid arithmetic expression, false if not
     */
    private static boolean isValidExpression(String expression) {
        //remove unnecessary whitespaces
        expression = expression.replaceAll("\\s+", "");
        //TEST 1: False if expression starts or ends with an operator
        if (isAnOperator(expression.charAt(0)) || isAnOperator(expression.charAt(expression.length() - 1)))
            return false;

        //TEST 2: False if test has mismatching number of opening and closing parantheses

        int unclosedParenthesis = 0;

        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                unclosedParenthesis++;

                //SUBTEST: False if expression ends with '('
                if (i == expression.length() - 1) return false;
            }
            if (expression.charAt(i) == ')') {
                unclosedParenthesis--;
                //SUBTEST: False if expression starts with ')'
                if (i == 0) return false;

            }
            if (isAnOperator(expression.charAt(i))) {

                //TEST 3: False if operator is preceded by an operator or opening parentheses
                //or followed by closing parentheses
                if (expression.charAt(i - 1) == '(' || expression.charAt(i + 1) == ')' || isAnOperator(expression.charAt(i + 1))) {
                    return false;
                }

            }

        }
        return (unclosedParenthesis == 0);
    }
}
