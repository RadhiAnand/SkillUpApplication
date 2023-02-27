package com.softura.skillup.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "student")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @Id
    private Long id;

    @Size(max = 30, message = "Name should not exceed 30 characters")
    private String name;

    @Size(max = 250, message = "Address should not exceed 250 characters")
    private String address;

}
