package com.kk.javajokes;

public class Jokes {

    private static final String[] sJokes = {
            "Can a kangaroo jump higher than a house? \n" +
                    "-\n" +
                    "Of course, a house doesn’t jump at all.",
            "Doctor: \"I'm sorry but you suffer from a terminal illness and have only 10 to live.\"\n" +
                    "\n" +
                    "Patient: \"What do you mean, 10? 10 what? Months? Weeks?!\"\n" +
                    "\n" +
                    "Doctor: \"Nine.",
            "Patient: Oh doctor, I’m just so nervous. This is my first operation.\n" +
                    "-\n" +
                    "Doctor: Don't worry. Mine too."
    };
    private static int counter = 0;

    public static String getJoke() {
        return sJokes[counter++ % sJokes.length];
    }

}
