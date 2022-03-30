//
//  SuperSend.swift
//  SuperSend
//
//  Created by Shindalkar, Suraj on 12/03/22.
//

import Foundation
import shared
import UIKit

public class SuperSend {
    
    public static let shared = SuperSend()
    
    internal var superSendiOSAPI: IOSSSApi!
    
    private var launchOptions: [UIApplication.LaunchOptionsKey: Any]?

    public func configureWith(configuration: SuperSendSDKConfiguration, launchOptions: [UIApplication.LaunchOptionsKey: Any]?) {
        
        IOSSSApi.Companion.init().initialize()
        
        superSendiOSAPI = IOSSSApi.Companion.init().getInstance(apiKey: configuration.key, apiSecret: configuration.secret, apiBaseUrl: configuration.baseUrl, mutationHandler: IosMutationHandler())
        
        // Check if App launched from notification
        let notificationOption = launchOptions?[.remoteNotification]
        if let notification = notificationOption as? [String: AnyObject],
          let aps = notification["aps"] as? [String: AnyObject],
           let id = aps[AnalyticsConstants.id] as! String? {
            trackNotificationDidLaunchAppEvent(id: id)
        }
    }
    
    public func enableLogging() {
        IOSSSApi.Companion.init().enableLogging()
    }
    
}