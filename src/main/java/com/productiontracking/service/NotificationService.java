package com.productiontracking.service;

import com.google.firebase.messaging.Message;
import com.productiontracking.model.NotificationModel;

public interface NotificationService {
    Message sendNotification(NotificationModel _pNotificationModel);
}
