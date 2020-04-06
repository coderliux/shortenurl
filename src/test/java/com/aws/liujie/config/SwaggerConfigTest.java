package com.aws.liujie.config;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SwaggerConfigTest {

    @Test
    public void swaggerDocket() {
        SwaggerConfig config = new SwaggerConfig();
        assertEquals("Short-URL-Demo", config.swaggerDocket().getGroupName());
    }
}