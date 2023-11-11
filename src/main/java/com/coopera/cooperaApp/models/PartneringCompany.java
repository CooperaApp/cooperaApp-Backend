package com.coopera.cooperaApp.models;

import com.coopera.cooperaApp.enums.PartneringCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartneringCompany {

    private  String id;
    private String name;
    private String rcNumber;
    private PartneringCategory partneringCategory;
    private String address;
    private String number;
    private String email;
}
