package com.coopera.cooperaApp.dtos.responses;

import lombok.*;


@NoArgsConstructor
@Builder
@ToString
@AllArgsConstructor
public class ApiResponse<T> {

    private String message;
    private boolean status;
    private T data;
}
