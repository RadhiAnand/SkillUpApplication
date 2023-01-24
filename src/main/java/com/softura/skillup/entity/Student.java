package com.softura.skillup.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name="student")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message ="Name cannot be blank")
    @Size(max=30,message="Name should not exceed 30 characters")
    private String name;

    @NotEmpty(message = "Address cannot be blank")
    @Size(max = 250, message = "Address should not exceed 250 characters")
    private String address;

}
