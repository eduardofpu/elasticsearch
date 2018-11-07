package com.elastic.elasticsearch;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ElasticSearchResponse<T> implements Serializable {
    private T took;
    private T timed_out;
    private T _shards;
    private T hits;
}