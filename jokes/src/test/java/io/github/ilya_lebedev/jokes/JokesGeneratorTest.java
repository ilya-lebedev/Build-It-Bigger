package io.github.ilya_lebedev.jokes;

import org.junit.Test;

/**
 * JokesGeneratorTest
 */
public class JokesGeneratorTest {

    @Test
    public void test() {
        JokesGenerator jokesGenerator = new JokesGenerator();
        assert jokesGenerator.getNextJoke().length() != 0;
    }

}
