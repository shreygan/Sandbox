package ui;

import model.exceptions.EndProgramException;

public class Main {

    public static void main(String[] args) {
        try {
            new Sandbox();
        } catch (EndProgramException e) {
            System.out.println("Program Ended.");
        }
    }
}
