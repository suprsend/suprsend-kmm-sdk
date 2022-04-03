//
//  AppDelegate.swift
//  ECommerceAppSwiftUI
//
//  Created by Ayush Gupta on 26/11/19.
//  Copyright © 2019 Ayush Gupta. All rights reserved.
//

import UIKit

import SuprSendSdk

import Firebase
import FirebaseMessaging
import UserNotifications

@UIApplicationMain
class AppDelegate: UIResponder {
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        print("app : App init")
        
        let suprSendConfiguration = SuprSendSDKConfiguration(key: "2XwhDGHS9QtuwrbXHfqp",
                                                               secret: "RWGx8ybPFFAHaqTImHs0",
                                                               baseUrl: "https://collector-staging.suprsend.workers.dev")

        SuprSend.shared.configureWith(configuration: suprSendConfiguration  , launchOptions: launchOptions)
        SuprSend.shared.enableLogging()

        SuprSend.shared.registerForPushNotifications()
    
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
        
        SuprSend.shared.setPushNotificationToken(token: token)
    }
    
}

extension AppDelegate: UNUserNotificationCenterDelegate {
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        
        if response.isSuprSendNotification() {
            SuprSend.shared.userNotificationCenter(center, didReceive: response)
        }
        
        completionHandler()
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        
        if notification.isSuperSendNotification() {
            SuprSend.shared.userNotificationCenter(center, willPresent: notification)
        }
        
        if #available(iOS 14.0, *) {
            completionHandler([.banner, .badge, .sound])
        } else {
            // Fallback on earlier versions
            completionHandler([.alert, .badge, .sound])
        }
    }
}
