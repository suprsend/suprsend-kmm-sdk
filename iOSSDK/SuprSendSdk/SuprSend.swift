//
//  SuprSend.swift
//  SuprSend
//
//  Created by Shindalkar, Suraj on 12/03/22.
//

import Foundation
import shared
import UIKit

@objc
public class SuprSend: NSObject {
    
    @objc public static let shared = SuprSend()
    
    internal var suprSendiOSAPI: IOSSSApi!
    
    private var launchOptions: [UIApplication.LaunchOptionsKey: Any]?

    @objc public func configureWith(configuration: SuprSendSDKConfiguration, launchOptions: [UIApplication.LaunchOptionsKey: Any]?) {
        
        IOSSSApi.Companion.init().initialize()
        
        suprSendiOSAPI = IOSSSApi.Companion.init().getInstance(apiKey: configuration.getKey(), apiSecret: configuration.getSecret(), apiBaseUrl: configuration.getBaseUrl(), mutationHandler: IosMutationHandler())
        
        // Check if App launched from notification
        let notificationOption = launchOptions?[.remoteNotification]
        if let notification = notificationOption as? [String: AnyObject],
           let viaSuprSend = notification[Constants.viaSuprSend] as? Bool,
           viaSuprSend == true,
           let id = notification[AnalyticsConstants.id] as! String? {
            trackNotificationDidLaunchAppEvent(id: id)
        }
    }
    
    @objc public func enableLogging() {
        IOSSSApi.Companion.init().enableLogging()
    }
    
}
