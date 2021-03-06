/*
 * Licensed Materials - Property of tenxcloud.com
 * (C) Copyright 2017 TenxCloud. All Rights Reserved.
 *
 */

package com.tenxcloud.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientResponseException;

import java.io.IOException;

@Slf4j
@Component
public class DefaultErrorHandlerWrapper extends DefaultResponseErrorHandler implements ResponseErrorHandler {

    private final HttpStatusForwarder forwarder;

    public DefaultErrorHandlerWrapper(HttpStatusForwarder forwarder) {
        this.forwarder = forwarder;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        try {
            super.handleError(response);
        } catch (RestClientResponseException ex) {
            log.error("response body: {}", ex.getResponseBodyAsString());
            throw forwarder.forward(ex);
        }
    }

}
