//
//  SuperSend.swift
//  SuperSend
//
//  Created by Shindalkar, Suraj on 12/03/22.
//

import Foundation
import shared

public class SuperSend {
    
    public static let shared = SuperSend()
    
    internal var superSendiOSAPI: IOSSSApi!

    public func configureWith(key: String, secret: String, baseUrl: String) {
        IOSSSApi.Companion.init().initialize()
        superSendiOSAPI = IOSSSApi.Companion.init().getInstance(apiKey: key, apiSecret: secret, apiBaseUrl: baseUrl, mutationHandler: IosMutationHandler())
    }
    
    public func enableLogging() {
        IOSSSApi.Companion.init().enableLogging()
    }
    
}
