package learn.plain.algos;

import java.util.Stack;

public class ReversePolishNotationJava {


    public static void main(String[] arg){
        System.out.println(
            "Result is: " + naiveStack("512+4*+3-".toCharArray())
        );
    }

    private static int naiveStack(char[] input) {
        Stack<Integer> stack = new Stack();
        for (char c: input) {
            if(Character.isDigit(c)) stack.push(Character.getNumericValue(c));
            else {
                stack.push(operation(c, stack.pop(), stack.pop()));
            }
        }
        if(stack.size() == 1) return stack.pop();
        else throw new IllegalArgumentException("Illegal input: " + new String(input));
    }

    private static int operation(char op, int second, int first) {
        if(op == '+') return first + second;
        if(op == '-') return first - second;
        if(op == '*') return first * second;
        if(op == '/') return first * second;
        throw new IllegalArgumentException("Illegal operator: " + op);

    }
}