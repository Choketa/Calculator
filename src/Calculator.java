import java.util.ArrayList;
import java.util.Scanner;

public class Calculator {
    /*
    Supported operations:
     + (Addition)
     - (Subtraction)
     * (Multiplication)
     / (Division)
     ! (Factorial)
     ^ (Exponentiation)
     % (Modulo)
    */
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        System.out.println("Input an equation:");
        String input = scn.nextLine();
        while (!input.equalsIgnoreCase("OFF")) {
            System.out.println(compute(input));
            System.out.println("Input an equation:");
            input = scn.nextLine();
        }
    }

    public static double compute(String str) {
        ArrayList<String> sep = split(str);
//        for (String s : sep)
//            System.out.print(s + " ");

        computeBrackets(sep);
        return computeList(sep);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private static double computeList(ArrayList<String> sep) {
        while (firstComputation(sep, '^')) {}
        while (firstComputation(sep, '!')) {}
        while (firstComputation(sep, '%')) {}
        while (firstComputation(sep, '*')) {}
        while (firstComputation(sep, '/')) {}
        while (firstComputation(sep, '-')) {}
        while (firstComputation(sep, '+')) {}
        return Double.parseDouble(sep.getFirst());
    }

    //Computes the first occurrence of the given operation in the list
    //Returns true if there was any computation
    private static boolean firstComputation(ArrayList<String> list, char c) {
        if (list.get(0).equals("-") && c == '-') {
            list.set(0, String.valueOf(-Double.parseDouble(list.get(1))));
            list.remove(1);
        }
        for (int i = 1; i < list.size() - 1; i++) {
            if (!String.valueOf(c).equals(list.get(i))) continue;
            double res = getRes(list, c, i);
            list.set(i, String.valueOf(res));
            list.remove(i - 1);
            if (c != '!')
                list.remove(i);
            return true;
        }
        return false;
    }
//    private static void convertToNum(ArrayList<String> list) {
//        for (int i = 0; i < list.size()-3)
//    }

    private static double getRes(ArrayList<String> list, char c, int i) {
        double res = 0;
        double prev = Double.parseDouble(list.get(i - 1));
        double next = 0;
        if (c != '!')
            next = Double.parseDouble(list.get(i + 1));
        switch (c) {
            case '+' -> res = prev + next;
            case '-' -> res = prev - next;
            case '*' -> res = prev * next;
            case '/' -> res = prev / next;
            case '^' -> res = Math.pow(prev, next);
            case '%' -> res = prev % next;
            case '!' -> res = basicFactorial(prev);
        }
        return res;
    }

    //Splits an equation into an arraylist
    private static ArrayList<String> split(String str) {
        //Temp is NaN so operations with the number 0 would work
        double temp = Double.NaN;
        int startIndex = 0;
        final ArrayList<String> toReturn = new ArrayList<>();
        if (str.charAt(0) == '-') {
            toReturn.add("-");
            startIndex = 1;
        }
        boolean shouldAddDecimals = false;
        double multiplier = 0.1;
        for (int i = startIndex; i < str.length(); i++) {
            if (str.charAt(i) == ' ') continue;
            //Number
            if (!isSign(str.charAt(i))) {
                //Will start adding decimals
                if (str.charAt(i) == '.') {
                    if (Double.isNaN(temp)) temp = 0.0;
                    shouldAddDecimals = true;
                    multiplier = 0.1;
                } else if (shouldAddDecimals) {
                    temp += (str.charAt(i) - '0') * multiplier;
                    multiplier /= 10;
                } else { /*Integers*/
                    if (Double.isNaN(temp)) temp = 0.0;
                    temp = temp * 10 + (str.charAt(i) - '0');
                }
            //Not a number
            } else {
                final boolean hasNumber = !Double.isNaN(temp);
                if (hasNumber) {
                    toReturn.add(String.valueOf(temp));
                    if (str.charAt(i) == '(') toReturn.add("*");
                }
                shouldAddDecimals = false;
                multiplier = 0.1;
                toReturn.add(String.valueOf(str.charAt(i)));
                if (hasNumber && str.charAt(i) == ')') toReturn.add("*");
                temp = Double.NaN;
            }
        }
        if (!Double.isNaN(temp)) toReturn.add(String.valueOf(temp));
        if (toReturn.size() == 2 && toReturn.get(1).equals("!")) {
            toReturn.add("+");
        }
        return toReturn;
    }

    private static void computeBrackets(ArrayList<String> list) {
        boolean shouldRemoveFromList = false;
        ArrayList<String> toAdd = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i != -1 && list.get(i).equals(")")) {
                shouldRemoveFromList = false;
                list.set(i, String.valueOf(computeList(toAdd)));
                toAdd.clear();
            }
            if (shouldRemoveFromList) {
                toAdd.add(list.get(i));
                list.remove(i);
                i--;
            } else if (i != -1 && list.get(i).equals("(")) {
                shouldRemoveFromList = true;
                list.remove(i);
                i--;
            }
        }
    }

    private static boolean isSign(char c) {
        return c == '+' || c == '-' || c == '/' || c == '*' || c == '^' || c == '!' || c == '(' || c == ')' || c == '%';
    }

    private static double basicFactorial(double num) {
        double result = 1;
        for (int i = 2; i <= num; i++)
            result *= i;
        return result;
    }
//    private static double recursiveFactorial(double num) {
//        if (num == 1 || num == 0) return 1;
//        return recursiveFactorial(num-1) * num;
//    }
}
