package com.bachar.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

//AuthenticationEntryPoint is interface implemented by
// exceptiontranslation filter which is filter which is first entrypoint in spring security.
// It allows us to catch the exception and do what we want to do with it

@Component
public class DelegatedAuthEntryPoint implements AuthenticationEntryPoint {


    //this is interface can resolve exception during handler mapping or execution
    private final HandlerExceptionResolver handlerExceptionResolver;

    public DelegatedAuthEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }


    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        handlerExceptionResolver.resolveException(
                request, response, null, authException
        );
    }
}
