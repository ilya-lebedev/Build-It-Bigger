package com.udacity.gradle.builditbigger.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import javax.inject.Named;

import io.github.ilya_lebedev.jokes.JokesGenerator;

/** An endpoint class we are exposing */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.builditbigger.gradle.udacity.com",
                ownerName = "backend.builditbigger.gradle.udacity.com",
                packagePath = ""
        )
)
public class MyEndpoint {

    private JokesGenerator jokesGenerator = new JokesGenerator();

    /** An endpoint method that return joke */
    @ApiMethod(name = "getJoke")
    public JokeBean getJoke() {
        JokeBean response = new JokeBean();

        response.setJoke(jokesGenerator.getNextJoke());

        return response;
    }

}
