package com.aws.liujie.controllers;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Response<T> {
    private String message;
    private String status;
    private T data;
}
