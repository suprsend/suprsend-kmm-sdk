//
//  AppDelegate.swift
//  ECommerceAppSwiftUI
//
//  Created by Ayush Gupta on 26/11/19.
//  Copyright © 2019 Ayush Gupta. All rights reserved.
//

import UIKit

import SuprSendSdk
import UserNotifications

@UIApplicationMain
class AppDelegate: UIResponder {
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        print("app : App init")
        
        let suprSendConfiguration = SuprSendSDKConfiguration(
        withKey: "kfWdrPL1nFqs7OUihiBn",
        secret: "From1HA1ZiSXs3ofBHXh",
        baseUrl: "https://collector-staging.suprsend.workers.dev/")

        SuprSend.shared.configureWith(configuration: suprSendConfiguration  , launchOptions: launchOptions)
        SuprSend.shared.enableLogging()
        
        var options: UNAuthorizationOptions = [.badge, .alert, .sound]

        if #available(iOS 12.0, *) {
          options = UNAuthorizationOptions(rawValue: options.rawValue | UNAuthorizationOptions.provisional.rawValue)
        }

        
        SuprSend.shared.registerForPushNotifications(options: options)
    
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
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        SuprSend.shared.application(application, didReceiveRemoteNotification: userInfo)

        completionHandler(.newData)
    }
    
    func application(_ app: UIApplication, open url: URL,
                     options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        if let scheme = url.scheme,
            scheme.localizedCaseInsensitiveCompare("com.ecommerceApp") == .orderedSame,
            let view = url.host {
            
            var parameters: [String: String] = [:]
            URLComponents(url: url, resolvingAgainstBaseURL: false)?.queryItems?.forEach {
                parameters[$0.name] = $0.value
            }
            
        }
        return true
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

        if #available(iOS 14.0, *) {
            completionHandler([.banner, .badge, .sound])
        } else {
            // Fallback on earlier versions
            completionHandler([.alert, .badge, .sound])
        }
    }

}
