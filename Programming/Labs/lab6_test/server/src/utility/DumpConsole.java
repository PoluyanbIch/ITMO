package utility;

import java.util.Scanner;

public class DumpConsole implements Console {
    private static Scanner fileScanner = null;
    private static Scanner consoleScanner = new Scanner(System.in);
    private static final String prompt = "> ";

    @Override
    public void print(Object obj) {
        System.out.print(obj);
    }

    @Override
    public void println(Object obj) {
        System.out.println(obj);
    }

    @Override
    public String readln() {
        if (fileScanner != null) {
            return fileScanner.nextLine();
        }
        return consoleScanner.nextLine();
    }

    @Override
    public boolean hasNextLine() {
        if (fileScanner != null) {
            return fileScanner.hasNextLine();
        }
        return consoleScanner.hasNextLine();
    }

    @Override
    public void printErr(Object obj) {
        System.err.print(obj);
    }

    @Override
    public void printTable(Object obj1, Object obj2) {
        System.out.printf("%-30s%-30s%n", obj1, obj2);
    }

    @Override
    public void prompt() {
        System.out.print(prompt);
    }

    @Override
    public String getPrompt() {
        return prompt;
    }

    @Override
    public void selectFileScanner(Scanner scanner) {
        fileScanner = scanner;
    }

    @Override
    public void selectConsoleScanner() {
        fileScanner = null;
    }
}