//
//  SuperSend+PushNotifications.swift
//  SuperSend
//
//  Created by Shindalkar, Suraj on 12/03/22.
//

import Foundation
import UIKit
import shared

public extension SuperSend {
    
    func setPushNotificationToken(token: String) {
        superSendiOSAPI.getUser().setIOSPush(token: token)
    }
    
    // For iOS < 10
    func application(_ application: UIApplication, didReceiveRemoteNotification
                     userInfo: [AnyHashable: Any]) {
        if let id = userInfo[AnalyticsConstants.id] {
            superSendiOSAPI.track(eventName: AnalyticsConstants.notificationClicked, properties: [AnalyticsConstants.id: id])
        }
    }
    
     func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse) {
         if let id = response.notification.request.content.userInfo[AnalyticsConstants.id] {
             superSendiOSAPI.track(eventName: AnalyticsConstants.notificationClicked, properties: [AnalyticsConstants.id: id])
         }
    }
    
     func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification) {
         if let id = notification.request.content.userInfo[AnalyticsConstants.id] {
             superSendiOSAPI.track(eventName: AnalyticsConstants.notificationDelivered, properties: [AnalyticsConstants.id: id])
         }
     }
    
    func registerForPushNotifications() {
        
        if #available(iOS 10, *) {
          let center = UNUserNotificationCenter.current()
            center.delegate = UIApplication.shared.delegate as? UNUserNotificationCenterDelegate
          var options: UNAuthorizationOptions = [.alert, .sound, .badge]
          if #available(iOS 12.0, *) {
            options = UNAuthorizationOptions(rawValue: options.rawValue | UNAuthorizationOptions.provisional.rawValue)
          }
            
          center.requestAuthorization(options: options) { (granted, error) in
              if granted {
                  DispatchQueue.main.async {
                    UIApplication.shared.registerForRemoteNotifications()
                  }
              }
          }
            
        } else {
          let types : UIUserNotificationType = [.alert, .badge, .sound]
          let setting : UIUserNotificationSettings = UIUserNotificationSettings(types:types, categories:nil)
            DispatchQueue.main.async {
            UIApplication.shared.registerUserNotificationSettings(setting)
              UIApplication.shared.registerForRemoteNotifications()
            }
        }
    }
    
    func trackNotificationDidLaunchAppEvent(id: String) {
        superSendiOSAPI.track(eventName: AnalyticsConstants.notificationClicked, properties: [AnalyticsConstants.id: id])
    }
    
}
