package com.coopera.cooperaApp.dtos.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class SavingsCollection {
    private List<SavingsResponse> savings;
}
