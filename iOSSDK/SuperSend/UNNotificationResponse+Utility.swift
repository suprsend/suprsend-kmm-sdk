//
//  UNNotificationResponse+Utility.swift
//  SuperSend
//
//  Created by Shindalkar, Suraj on 28/03/22.
//

import Foundation
import NotificationCenter

public extension UNNotificationResponse {
    func isSuperSendNotification() -> Bool {
        return notification.isSuperSendNotification()
    }
}

public extension UNNotification {
    func isSuperSendNotification() -> Bool {
        if let host = request.content.userInfo["host"] {
            return host as! String == AnalyticsConstants.host
        }
        return false
    }
}
