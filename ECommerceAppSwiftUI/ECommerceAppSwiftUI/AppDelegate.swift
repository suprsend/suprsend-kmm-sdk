//
//  AppDelegate.swift
//  ECommerceAppSwiftUI
//
//  Created by Ayush Gupta on 26/11/19.
//  Copyright © 2019 Ayush Gupta. All rights reserved.
//

import UIKit
import sdk

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        print("app : App init")
        SSAPI.initialize()
        SSAPI.enableLogging()
        return true
    }
    
    // MARK: UISceneSession Lifecycle
    
    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        print("app : App New Session Created")
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }
    
    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        print("app : App User discard scene session")
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }
    func applicationDidEnterBackground(_ application: UIApplication) {
        print("app : applicationDidEnterBackground")
        
    }
    func applicationWillTerminate(_ application: UIApplication) {
        print("app : applicationWillTerminate")
    }
    func applicationDidBecomeActive(_ application: UIApplication) {
        print("app : applicationDidBecomeActive")
    }
    func applicationWillResignActive(_ application: UIApplication) {
        print("app : applicationWillResignActive")
    }
    func applicationDidFinishLaunching(_ application: UIApplication) {
        print("app : applicationDidFinishLaunching")
    }
    func applicationWillEnterForeground(_ application: UIApplication) {
        print("app : applicationWillEnterForeground")
    }
}
