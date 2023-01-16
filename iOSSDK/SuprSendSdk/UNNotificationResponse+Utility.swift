//
//  UNNotificationResponse+Utility.swift
//  SuperSend
//
//  Created by Shindalkar, Suraj on 28/03/22.
//

import Foundation
import NotificationCenter

@objc
public extension UNNotificationResponse {
    @objc func isSuprSendNotification() -> Bool {
        return notification.isSuperSendNotification()
    }
}

@objc
public extension UNNotification {
    @objc
    func isSuperSendNotification() -> Bool {
        if let viaSuprSend = request.content.userInfo[Constants.viaSuprSend] as? Bool {
            return viaSuprSend
        }
        return false
    }
}
