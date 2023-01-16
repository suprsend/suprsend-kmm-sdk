//
//  SuprSend.swift
//  SuprSend
//
//  Created by Shindalkar, Suraj on 12/03/22.
//

import Foundation
import SuprsendCore
import UIKit

@objc
public class SuprSend: NSObject {
    
    @objc public static let shared = SuprSend()
    
    private let reachability = try? Reachability()
    
    internal var suprSendiOSAPI: IOSSSApi!
    
    private var launchOptions: [UIApplication.LaunchOptionsKey: Any]?

    @objc public func configureWith(configuration: SuprSendSDKConfiguration, launchOptions: [UIApplication.LaunchOptionsKey: Any]?) {
        
        let api = IOSSSApi.Companion.init()
            api.initialize()
        do {
            suprSendiOSAPI = try api.getInstance(apiKey: configuration.getKey(), apiSecret: configuration.getSecret(), apiBaseUrl: configuration.getBaseUrl(), mutationHandler: IosMutationHandler())
        } catch {
            print("Error = \(error)")
        }
        
    }
    
    @objc public func enableLogging() {
        IOSSSApi.Companion.init().enableLogging()
    }
        
}
