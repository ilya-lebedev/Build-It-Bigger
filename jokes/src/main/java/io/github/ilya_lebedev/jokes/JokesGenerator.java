package io.github.ilya_lebedev.jokes;

public class JokesGenerator {

    private int jokeNumber;

    public String getNextJoke() {
        jokeNumber++;

        return "Very funny joke #" + jokeNumber;
    }

}
