//
//  UNNotificationResponse+Utility.swift
//  SuperSend
//
//  Created by Shindalkar, Suraj on 28/03/22.
//

import Foundation
import NotificationCenter

public extension UNNotificationResponse {
    func isSuprSendNotification() -> Bool {
        return notification.isSuperSendNotification()
    }
}

public extension UNNotification {
    func isSuperSendNotification() -> Bool {
        if let viaSuprSend = request.content.userInfo[Constants.viaSuprSend] as? Bool {
            return viaSuprSend
        }
        return false
    }
}
