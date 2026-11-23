import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the first number:");
        double operand1 = scanner.nextDouble();

        System.out.println("Enter the operation (+, -, *, /):");
        char operation = scanner.next().charAt(0);

        System.out.println("Enter the second number:");
        double operand2 = scanner.nextDouble();

        double result = 0;
        boolean hasResult = false;

        switch (operation) {
            case '+':
                result = operand1 + operand2;
                hasResult = true;
                break;
            case '-':
                result = operand1 - operand2;
                hasResult = true;
                break;
            case '*':
                result = operand1 * operand2;
                hasResult = true;
                break;
            case '/':
                if (operand2 == 0) {
                    System.out.println("Error: division by zero.");
                } else {
                    result = operand1 / operand2;
                    hasResult = true;
                }
                break;
            default:
                System.out.println("Error: unsupported operation.");
        }

        if (hasResult) {
            System.out.println("Result: " + result);
        }

        scanner.close();
    }
}
