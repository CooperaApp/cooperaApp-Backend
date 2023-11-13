package com.coopera.cooperaApp.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;



@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private String message;
    private boolean success;
    private T data;

}