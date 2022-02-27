//
//  AppDelegate.swift
//  ECommerceAppSwiftUI
//
//  Created by Ayush Gupta on 26/11/19.
//  Copyright © 2019 Ayush Gupta. All rights reserved.
//

import UIKit

import sdk

//import Firebase
//import FirebaseMessaging
import UserNotifications

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate,UNUserNotificationCenterDelegate {
    
    func registerForPushNotifications() {
        UNUserNotificationCenter.current()
          .requestAuthorization(
            options: [.alert, .sound, .badge]) { [weak self] granted, _ in
            print("Permission granted: \(granted)")
            guard granted else { return }
            self?.getNotificationSettings()
          }
    }
    
    func getNotificationSettings() {
      UNUserNotificationCenter.current().getNotificationSettings { settings in
        print("Notification settings: \(settings)")
          guard settings.authorizationStatus == .authorized else { return }
          DispatchQueue.main.async {
            UIApplication.shared.registerForRemoteNotifications()
          }
      }
    }
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        print("app : App init")
        SSAPI.initialize(
            apiKey: "2XwhDGHS9QtuwrbXHfqp",
            apiSecret: "RWGx8ybPFFAHaqTImHs0",
            apiBaseUrl: "https://collector-staging.suprsend.workers.dev")
//        SSAPI.enableLogging()
        
        // Override point for customization after application launch.
        
//        FirebaseApp.configure()
        
//        Messaging.messaging().delegate  = self
//        UNUserNotificationCenter.current().delegate = self
//
//        UNUserNotificationCenter.current().requestAuthorization(options: [.alert,.sound,.badge]) { success, _ in
//            guard success else{
//                return
//            }
//            print("app : APNS registered successfully")
//        }
//
//        application.registerForRemoteNotifications()
        
        registerForPushNotifications()
        
        // Check if launched from notification
        let notificationOption = launchOptions?[.remoteNotification]

        // 1
        if
          let notification = notificationOption as? [String: AnyObject],
          let aps = notification["aps"] as? [String: AnyObject] {
//          // 2
//          NewsItem.makeNewsItem(aps)
//
//          // 3
//          (window?.rootViewController as? UITabBarController)?.selectedIndex = 1
            print("aps: \(aps)")
        }

        return true
    }
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        
        print("\(userInfo)")
    }
    func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        print("App Delegate Open Url : \(url)")
        return true
    }
    
//    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
//        messaging.token{token,_ in
//            guard let token = token else {
//                return
//            }
//            print("app : APNS Token : \(token)")
//        }
//    }
    
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        
        let tokenParts = deviceToken.map { data in String(format: "%02.2hhx", data) }
          let token = tokenParts.joined()
          print("Device Token: \(token)")
    }
    
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        print("Device token - failed")
    }
}
