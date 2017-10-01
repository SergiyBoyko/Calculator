package com.example.android.calculatorlytogo;

/**
 * Created by fbrsw on 29.09.2017.
 */

public enum Operation {
    ADD,
    SUB,
    MUL,
    DIV,
    REM;

    public String getSymbol() {
        switch (this) {
            case ADD:
                return "+";
            case SUB:
                return "-";
            case MUL:
                return "*";
            case DIV:
                return "/";
            case REM:
                return "%";
        }
        return null;
    }

    public static String getAllOperations() {
        return "+-*/%";
    }

    public double doOperation(double leftPart, double rightPart) {
        switch (this) {
            case ADD:
                return leftPart + rightPart;
            case SUB:
                return leftPart - rightPart;
            case MUL:
                return leftPart * rightPart;
            case DIV:
                if (leftPart == 0) throw new ArithmeticException();
                return leftPart / rightPart;
            case REM:
                if (leftPart == 0) throw new ArithmeticException();
                return leftPart % rightPart;
        }
        return 0;
    }
}
