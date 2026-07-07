package com.adrian.tradeengine.web;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HealthResourceTest {

    @Test
    void shouldReturnHealthMessage() {
        HealthResource resource = new HealthResource();

        String result = resource.health();

        assertEquals("Trade Engine Lite is running", result);
    }
}