package com.coopera.cooperaApp.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;



@Builder
@Setter
@ToString
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private String message;
    private boolean success;
    private T data;

}