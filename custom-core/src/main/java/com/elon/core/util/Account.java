package com.elon.core.util;

public class Account {
    public void operation() {
        System.out.println("operation...");
    }

    public static void say(int number)
    {
        SecurityChecker.checkSecurity();
    }
}
