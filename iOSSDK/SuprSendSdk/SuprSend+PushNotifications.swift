//
//  SuprSend+PushNotifications.swift
//  SuprSend
//
//  Created by Shindalkar, Suraj on 12/03/22.
//

import Foundation
import UIKit
import SuprsendCore
import SwiftUI

@objc
public extension SuprSend {
    
    @objc func setPushNotificationToken(token: String) {
        suprSendiOSAPI.getUser().setIOSPush(token: token)
    }
    
    @objc func unSetPushNotificationToken(token: String) {
        suprSendiOSAPI.getUser().unSetIOSPush(token: token)
    }
    
    // Notification Click
    @objc func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse) {
         if let id = response.notification.request.content.userInfo[AnalyticsConstants.nid],
            response.isSuprSendNotification() {
             suprSendiOSAPI.track(eventName: AnalyticsConstants.notificationClicked, properties: [AnalyticsConstants.id: id])
             
             self.suprSendiOSAPI.flush()

             // Check if Deep link exists
             if let deeplink = response.notification.request.content.userInfo[AnalyticsConstants.globalActionUrl] as? String,
                 let url = URL(string: deeplink) {
                 UIApplication.shared.open(url)
             }
             
             if let badgeNumber = response.notification.request.content.badge {
                 UIApplication.shared.applicationIconBadgeNumber = badgeNumber.intValue
             }
         }
    }
    
    // Notification Delivered
    @objc
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any]) {
        if let id = userInfo[AnalyticsConstants.nid],
           let viaSuperSend = userInfo[Constants.viaSuprSend] as? Bool, viaSuperSend == true {
            suprSendiOSAPI.track(eventName: AnalyticsConstants.notificationDelivered, properties: [AnalyticsConstants.id: id])
            self.suprSendiOSAPI.flush()
        }
    }
    
    @objc func registerForPushNotifications(options: UNAuthorizationOptions) {
        if #available(iOS 10, *) {
          let center = UNUserNotificationCenter.current()
            center.delegate = UIApplication.shared.delegate as? UNUserNotificationCenterDelegate
            center.requestAuthorization(options: options) { (granted, error) in
              if granted {
                  DispatchQueue.main.async {
                    UIApplication.shared.registerForRemoteNotifications()
                  }
              }
          }
        }
    }
    
    @objc func trackNotificationDidLaunchAppEvent(id: String) {
        suprSendiOSAPI.track(eventName: AnalyticsConstants.notificationClicked, properties: [AnalyticsConstants.id: id])
    }
    
}
