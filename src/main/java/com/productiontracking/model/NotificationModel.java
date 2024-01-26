package com.productiontracking.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class NotificationModel {
    @Getter
    @Setter
    Long UserId;
    @Getter
    @Setter
    String messageTitle;
    @Getter
    @Setter
    String messageBody;
    Map<String, String> data;

    public NotificationModel() {
        data = new HashMap<String, String>();
    }

    public NotificationModel(String _pTitle, String _pBody) {
        data = new HashMap<String, String>();
        this.messageTitle = _pTitle;
        this.messageBody = _pBody;
    }

    public NotificationModel putData(String _pKey, String _pValue) {
        data.put(_pKey, _pValue);
        return this;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public Map<String, String> getData() {
        return data;
    }
}
