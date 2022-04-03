//
//  SuprSend.swift
//  SuprSend
//
//  Created by Shindalkar, Suraj on 12/03/22.
//

import Foundation
import shared
import UIKit

public class SuprSend {
    
    public static let shared = SuprSend()
    
    internal var suprSendiOSAPI: IOSSSApi!
    
    private var launchOptions: [UIApplication.LaunchOptionsKey: Any]?

    public func configureWith(configuration: SuprSendSDKConfiguration, launchOptions: [UIApplication.LaunchOptionsKey: Any]?) {
        
        IOSSSApi.Companion.init().initialize()
        
        suprSendiOSAPI = IOSSSApi.Companion.init().getInstance(apiKey: configuration.key, apiSecret: configuration.secret, apiBaseUrl: configuration.baseUrl, mutationHandler: IosMutationHandler())
        
        // Check if App launched from notification
        let notificationOption = launchOptions?[.remoteNotification]
        if let notification = notificationOption as? [String: AnyObject],
           let fromSuprSend = notification[Constants.fromSuprSend] as? Bool,
           fromSuprSend == true,
           let id = notification[AnalyticsConstants.id] as! String? {
            trackNotificationDidLaunchAppEvent(id: id)
        }
    }
    
    public func enableLogging() {
        IOSSSApi.Companion.init().enableLogging()
    }
    
}
