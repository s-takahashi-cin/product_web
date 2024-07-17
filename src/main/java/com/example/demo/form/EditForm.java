package com.example.demo.form;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EditForm implements Serializable {

    @NotNull
    private Long id;

    @NotBlank
    private String lastName;

    @NotBlank
    private String firstName;

    @NotBlank
    private String email;

    private String phone;

    @NotNull
    private Long storeId;

    @NotNull
    private Long positionId;

}
