package com.coopera.cooperaApp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    private String id;
    private String name;
    private String rcNumber;
    private String address;
    private Cooperative cooperative;

}
