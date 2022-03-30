//
//  AppDelegate.swift
//  ECommerceAppSwiftUI
//
//  Created by Ayush Gupta on 26/11/19.
//  Copyright © 2019 Ayush Gupta. All rights reserved.
//

import UIKit

import SuperSendSdk

import Firebase
import FirebaseMessaging
import UserNotifications

@UIApplicationMain
class AppDelegate: UIResponder {
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        print("app : App init")
        
        let superSendConfiguration = SuperSendSDKConfiguration(key: "2XwhDGHS9QtuwrbXHfqp",
                                                               secret: "RWGx8ybPFFAHaqTImHs0",
                                                               baseUrl: "https://collector-staging.suprsend.workers.dev")

        SuperSend.shared.configureWith(configuration: superSendConfiguration, launchOptions: launchOptions)
        SuperSend.shared.enableLogging()

        SuperSend.shared.registerForPushNotifications()
    
        return true
    }
    
    func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        print("App Delegate Open Url : \(url)")
        return true
    }

}

extension AppDelegate: UIApplicationDelegate {
    
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        
        let tokenParts = deviceToken.map { data in String(format: "%02.2hhx", data) }
        let token = tokenParts.joined()
        
        print("Device Token: \(token)")
        
        SuperSend.shared.setPushNotificationToken(token: token)
    }
    
    func application(_ application: UIApplication, didReceiveRemoteNotification
                     userInfo: [AnyHashable: Any],
      fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        guard (userInfo["aps"] as? [String: AnyObject]) != nil else {
        completionHandler(.failed)
        return
      }
        
        if let host = userInfo["host"] as? String, host == "com.supersend" {
            SuperSend.shared.application(application, didReceiveRemoteNotification: userInfo)
        }
        
        completionHandler(.noData)
    }

    
}

extension AppDelegate: UNUserNotificationCenterDelegate {
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        
        if response.isSuperSendNotification() {
            SuperSend.shared.userNotificationCenter(center, didReceive: response)
        }
        
        completionHandler()
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        
        if notification.isSuperSendNotification() {
            SuperSend.shared.userNotificationCenter(center, willPresent: notification)
        }
        
        if #available(iOS 14.0, *) {
            completionHandler([.banner, .badge, .sound])
        } else {
            // Fallback on earlier versions
            completionHandler([.alert, .badge, .sound])
        }
    }
}
