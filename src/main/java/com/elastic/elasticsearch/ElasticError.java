package com.elastic.elasticsearch;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ElasticError<T> implements Serializable {

    private T error;
    private T root_cause;
    private T type;
    private T status;
}
