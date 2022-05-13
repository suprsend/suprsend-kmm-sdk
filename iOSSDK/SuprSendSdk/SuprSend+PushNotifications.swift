//
//  SuprSend+PushNotifications.swift
//  SuprSend
//
//  Created by Shindalkar, Suraj on 12/03/22.
//

import Foundation
import UIKit
import SuprsendCore

@objc
public extension SuprSend {
    
    @objc func setPushNotificationToken(token: String) {
        suprSendiOSAPI.getUser().setIOSPush(token: token)
    }
    
    @objc func unSetPushNotificationToken(token: String) {
        suprSendiOSAPI.getUser().unSetIOSPush(token: token)
    }
    
    @objc func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse) {
         if let id = response.notification.request.content.userInfo[AnalyticsConstants.nid],
            response.isSuprSendNotification() {
             suprSendiOSAPI.track(eventName: AnalyticsConstants.notificationClicked, properties: [AnalyticsConstants.id: id])
         }
    }
    
    @objc
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        if let id = userInfo[AnalyticsConstants.nid],
           let viaSuperSend = userInfo[Constants.viaSuprSend] as? Bool, viaSuperSend == true {
            suprSendiOSAPI.track(eventName: AnalyticsConstants.notificationDelivered, properties: [AnalyticsConstants.id: id])
        }
    }
    
    @objc func registerForPushNotifications() {
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
            
        }
    }
    
    @objc func trackNotificationDidLaunchAppEvent(id: String) {
        suprSendiOSAPI.track(eventName: AnalyticsConstants.notificationClicked, properties: [AnalyticsConstants.id: id])
    }
    
}
