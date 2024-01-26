package com.productiontracking.dto.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class ServiceResponse<T> implements Serializable {
    private Boolean hasExceptionError;
    private Boolean isSuccessful = false;
    private @JsonProperty String exceptionMessage;
    private Long count;
    private @JsonProperty T entity;
    private List<T> list;
    private Object data;

    public ServiceResponse(Boolean _pIsSuccessful, String _pExceptionMessage, T _pEntity) {
        list = new java.util.ArrayList<T>();
        this.isSuccessful = _pIsSuccessful;
        this.exceptionMessage = _pExceptionMessage;
        if (_pIsSuccessful.equals(false)) {
            this.hasExceptionError = true;
        }
        this.entity = _pEntity;
    }

    public ServiceResponse() {
        list = new java.util.ArrayList<T>();
    }

    public void setListItem(T _pItem) {
        list.add(_pItem);
    }

    public ServiceResponse<T> setData(Object _pData) {
        this.data = _pData;
        return this;
    }

    public ServiceResponse<T> setHasExceptionError(Boolean _pHasExceptionError) {
        this.hasExceptionError = _pHasExceptionError;
        return this;
    }

    public ServiceResponse<T> setIsSuccessful(Boolean _pIsSuccessful) {
        this.isSuccessful = _pIsSuccessful;
        return this;
    }

    public ServiceResponse<T> setExceptionMessage(String _pExceptionMessage) {
        exceptionMessage = _pExceptionMessage;
        this.hasExceptionError = true;
        return this;
    }

    public ServiceResponse<T> setCount(Long _pCount) {
        this.count = _pCount;
        return this;
    }

    public ServiceResponse<T> setEntity(T _pEntity) {
        this.entity = _pEntity;
        return this;
    }

    public ServiceResponse<T> setList(List<T> _pList) {
        this.list = _pList;
        return this;
    }

    public Long getCount() {
        return Long.valueOf(list.size()) == 0 ? this.count : Long.valueOf(list.size());
    }
}