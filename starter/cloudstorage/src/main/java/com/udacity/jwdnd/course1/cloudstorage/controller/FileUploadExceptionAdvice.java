package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// a class that handles file upload size too large exception.
@ControllerAdvice
public class FileUploadExceptionAdvice {

    // a bean that gets the max size of file upload from spring config.
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleMaxSizeException(
            MaxUploadSizeExceededException exception,
            HttpServletRequest request,
            HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView("result");
        modelAndView.getModel().put("error", "Upload failed because you are trying to upload a file that is too big. " +
                "Please upload a file that is smaller than " + maxFileSize + ".");
        return modelAndView;
    }
}
