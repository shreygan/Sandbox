package ui;

import java.util.Scanner;

// Handles user input
public class Input extends Thread {

    private String nextIn;
    private boolean hasNewInput;


    // MODIFIES: this
    // EFFECTS: checks and processes user input
    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            if (sc.hasNext()) {
                hasNewInput = true;

                nextIn = sc.next();

                if (nextIn.equals("f")) {
                    break;
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: returns true if user has inputted something,
    //          false otherwise
    public boolean hasNewInput() {
        if (hasNewInput) {
            hasNewInput = false;
            return true;
        }

        return false;
    }

    // MODIFIES: this
    // EFFECTS: returns what the user inputted
    public String getNextIn() {
        String temp = nextIn;
        nextIn = "";

        if (temp.equals("f")) {
            interrupt();
        }

        return temp;
    }
}
