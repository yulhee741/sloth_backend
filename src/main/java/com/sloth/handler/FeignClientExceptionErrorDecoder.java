package com.sloth.handler;

import com.sloth.exception.FeignClientException;
import feign.Response;
import feign.codec.ErrorDecoder;
import feign.codec.StringDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignClientExceptionErrorDecoder implements ErrorDecoder {

    private StringDecoder stringDecoder = new StringDecoder();

    @Override
    public FeignClientException decode(final String methodKey, Response response) {
        log.info(response.toString());
        String message = response.reason();
        return new FeignClientException(response.status(), message, response.headers());
    }

}
