package com.pavilion.hello;

import static org.junit.Assert.assertEquals;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import org.junit.Test;

public class HelloHandlerTest {

    @Test
    public void returnsHelloMessage() {
        HelloHandler handler = new HelloHandler();

        APIGatewayV2HTTPResponse response = handler.handleRequest(
                APIGatewayV2HTTPEvent.builder().build(), null);

        assertEquals(200, response.getStatusCode());
        assertEquals("{\"message\": \"Hello from Lambda\"}", response.getBody());
    }
}
