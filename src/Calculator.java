import java.util.ArrayList;
import java.util.Scanner;

public class Calculator {
    /*
    Operations supported: +, -, *, /, ^ (Exponents)
    */
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        System.out.println("Input an equation");
        String input = scn.nextLine();
        while (!input.equalsIgnoreCase("OFF")) {
            ArrayList<String> sep = separate(input);
            computeBrackets(sep);
            System.out.println(computeList(sep));
            System.out.println("Input an equation");
            input = scn.nextLine();
        }
    }

    public static double computeList(ArrayList<String> sep) {
        while (firstComputation(sep, '^')) {
            firstComputation(sep, '^');
        }
        while (firstComputation(sep, '*')) {
            firstComputation(sep, '*');
        }
        while (firstComputation(sep, '/')) {
            firstComputation(sep, '/');
        }
        while (firstComputation(sep, '-')) {
            firstComputation(sep, '-');
        }
        while (firstComputation(sep, '+')) {
            firstComputation(sep, '+');
        }
        return Double.parseDouble(sep.get(0));
    }

    //Computes the first occurrence of the given operation in the list
    //Returns true if there was any computation
    public static boolean firstComputation(ArrayList<String> list, char c) {
        if (list.get(0).equals("-") && c == '-') {
            list.set(0, String.valueOf(-Double.parseDouble(list.get(1))));
            list.remove(1);
        }
        for (int i = 1; i < list.size() - 1; i++) {
            if (!String.valueOf(c).equals(list.get(i))) continue;
            double res = 0;
            double prev = Double.parseDouble(list.get(i - 1));
            double next = Double.parseDouble(list.get(i + 1));
            switch (c) {
                case '+' -> res = prev + next;
                case '-' -> res = prev - next;
                case '*' -> res = prev * next;
                case '/' -> res = prev / next;
                case '^' -> res = Math.pow(prev, next);
            }
            list.set(i, String.valueOf(res));
            list.remove(i - 1);
            list.remove(i);
            return true;
        }
        return false;
    }

    //Separates an equation into an arraylist
    public static ArrayList<String> separate(String str) {
        //Temp is non-primitive so operations with 0 would work
        Double temp = null;
        int startIndex = 0;
        ArrayList<String> toReturn = new ArrayList<>();
        if (str.charAt(0) == '-') {
            toReturn.add("-");
            startIndex = 1;
        }
        boolean hasDot = false;
        double multiplier = 0.1;
        for (int i = startIndex; i < str.length(); i++) {
            if (str.charAt(i) == ' ') continue;
            if (!isSign(str.charAt(i))) {
                if (str.charAt(i) == '.') {
                    hasDot = true;
                    multiplier = 0.1;
                } else if (hasDot) {
                    temp += (str.charAt(i) - '0') * multiplier;
                    multiplier /= 10;
                } else {
                    if (temp == null) temp = 0.0;
                    temp = temp * 10 + (str.charAt(i) - '0');
                }
            } else {
                if (temp != null) {
                    toReturn.add(String.valueOf(temp));
                }
                hasDot = false;
                multiplier = 0.1;
                toReturn.add(String.valueOf(str.charAt(i)));
                temp = null;
            }
        }
        if (temp != null) toReturn.add(String.valueOf(temp));
        return toReturn;
    }

    public static void computeBrackets(ArrayList<String> list) {
        boolean shouldRemoveFromList = false;
        ArrayList<String> toAdd = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i != -1 && list.get(i).equals(")")) {
                shouldRemoveFromList = false;
                list.set(i, String.valueOf(computeList(toAdd)));
            }
            if (shouldRemoveFromList) {
                toAdd.add(list.get(i));
                list.remove(i);
                if (i != 0)
                    i--;
                else i = -1;
            } else if (i != -1 && list.get(i).equals("(")) {
                list.remove(i);
                if (i != 0)
                    i--;
                else i = -1;
                shouldRemoveFromList = true;
            }
        }
    }

    public static boolean isSign(char c) {
        return c == '+' || c == '-' || c == '/' || c == '*' || c == '^' || c == '(' || c == ')';
    }
}
