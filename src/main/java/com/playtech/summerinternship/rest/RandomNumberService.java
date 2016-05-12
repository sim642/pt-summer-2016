package com.playtech.summerinternship.rest;

import com.playtech.summerinternship.RandomNumberListener;
import com.playtech.summerinternship.RandomNumbers;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("randomnumbers")
public class RandomNumberService {
    @Context
    private ServletContext servletContext;

    @Path("/{after}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public RandomNumbers randomNumbers(@PathParam("after") long after) {
        return new RandomNumbers(((RandomNumberListener) servletContext.getAttribute("random")).getAfter(after));
    }
}
