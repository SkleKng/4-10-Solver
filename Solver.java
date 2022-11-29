import java.util.Scanner;

public class Solver {

    public static void main(String[] args) {
        System.out.println("Welcome to the amazing 4=10 solver! (Made in 30 minutes)");
        Scanner sc = new Scanner(System.in);
        int a = -1, b = -1, c = -1, d = -1;
        while(true)
        {
            System.out.println("Enter the four numbers (-1 to exit): ");
            a = sc.nextInt();
            if(a == -1) {
                System.out.println("Exiting...");
                System.exit(0);
            }
            b = sc.nextInt();
            if(b == -1){
                System.out.println("Exiting...");
                System.exit(0);
            }
            c = sc.nextInt();
            if(c == -1){
                System.out.println("Exiting...");
                System.exit(0);
            }
            d = sc.nextInt();
            if(d == -1){
                System.out.println("Exiting...");
                System.exit(0);
            }
            findCombinationsThatEqual10(a, b, c, d);
        }
    }

    public static void findCombinationsThatEqual10(int a, int b, int c, int d) {
        System.out.println("Input: " + a + ", " + b + ", " + c + ", " + d);
        System.out.println("Possible Combinations: ");
        System.out.println("______________________");
        findCombinationsThatEqual10(a, b, c, d, false, false, false, false, "");
    }

    public static void findCombinationsThatEqual10(int a, int b, int c, int d, boolean aUsed, boolean bUsed, boolean cUsed, boolean dUsed, String current)
    {
        //recursively find combinations of a, b, c, and d that equal 10 with the standard operators (+, -, *, /)
        //base case
        if((current.length() == 7) && eval(current) == 10.0) // solution found
        {
            System.out.println(current);
        }
        else if(current.length() == 7) //the string is complete
        {
            return;
        }
        else
        {
            //recursive case
            if(current.length() == 0){ // the first call
                findCombinationsThatEqual10(a, b, c, d, true, false, false, false, a + "");
                findCombinationsThatEqual10(a, b, c, d, false, true, false, false, b + "");
                findCombinationsThatEqual10(a, b, c, d, false, false, true, false, c + "");
                findCombinationsThatEqual10(a, b, c, d, false, false, false, true, d + "");
            }
            else{
                if(!aUsed){
                    findCombinationsThatEqual10(a, b, c, d, true, bUsed, cUsed, dUsed, current + "+" + a);
                    findCombinationsThatEqual10(a, b, c, d, true, bUsed, cUsed, dUsed, current + "-" + a);
                    findCombinationsThatEqual10(a, b, c, d, true, bUsed, cUsed, dUsed, current + "*" + a);
                    findCombinationsThatEqual10(a, b, c, d, true, bUsed, cUsed, dUsed, current + "/" + a);
                }
                if(!bUsed){
                    findCombinationsThatEqual10(a, b, c, d, aUsed, true, cUsed, dUsed, current + "+" + b);
                    findCombinationsThatEqual10(a, b, c, d, aUsed, true, cUsed, dUsed, current + "-" + b);
                    findCombinationsThatEqual10(a, b, c, d, aUsed, true, cUsed, dUsed, current + "*" + b);
                    findCombinationsThatEqual10(a, b, c, d, aUsed, true, cUsed, dUsed, current + "/" + b);
                }
                if(!cUsed){
                    findCombinationsThatEqual10(a, b, c, d, aUsed, bUsed, true, dUsed, current + "+" + c);
                    findCombinationsThatEqual10(a, b, c, d, aUsed, bUsed, true, dUsed, current + "-" + c);
                    findCombinationsThatEqual10(a, b, c, d, aUsed, bUsed, true, dUsed, current + "*" + c);
                    findCombinationsThatEqual10(a, b, c, d, aUsed, bUsed, true, dUsed, current + "/" + c);
                }
                if(!dUsed){
                    findCombinationsThatEqual10(a, b, c, d, aUsed, bUsed, cUsed, true, current + "+" + d);
                    findCombinationsThatEqual10(a, b, c, d, aUsed, bUsed, cUsed, true, current + "-" + d);
                    findCombinationsThatEqual10(a, b, c, d, aUsed, bUsed, cUsed, true, current + "*" + d);
                    findCombinationsThatEqual10(a, b, c, d, aUsed, bUsed, cUsed, true, current + "/" + d);
                }
            }

        }


    }

    //stolen from https://stackoverflow.com/questions/3422673/how-to-evaluate-a-math-expression-given-in-string-form/26227947#26227947
    //Thanks Boann!
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;
            
            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }
            
            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }
            
            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }
            
            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)` | number
            //        | functionName `(` expression `)` | functionName factor
            //        | factor `^` factor
            
            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }
            
            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }
            
            double parseFactor() {
                if (eat('+')) return +parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus
                
                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Missing ')'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (eat('(')) {
                        x = parseExpression();
                        if (!eat(')')) throw new RuntimeException("Missing ')' after argument to " + func);
                    } else {
                        x = parseFactor();
                    }
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }
                
                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation
                
                return x;
            }
        }.parse();
    }
}