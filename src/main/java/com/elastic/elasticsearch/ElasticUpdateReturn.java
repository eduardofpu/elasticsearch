package com.elastic.elasticsearch;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ElasticUpdateReturn<T> implements Serializable{

     private T _index;
     private T _type;
     private T  _id;
     private T  _version;
     private T  result;
     private T  _shards;
}
