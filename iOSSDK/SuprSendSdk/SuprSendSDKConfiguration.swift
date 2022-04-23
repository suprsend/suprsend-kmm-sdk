//
//  SuprSendSDKConfiguration.swift
//  SuprSend
//
//  Created by Shindalkar, Suraj on 13/03/22.
//

import Foundation

@objc
public class SuprSendSDKConfiguration: NSObject {
    
    private let key: String
    private var secret: String
    
    init(key: String, secret: String) {
        self.key = key
        self.secret = secret
    }
    
    @objc public convenience init(withKey key: String, secret: String) {
        self.init(key: key, secret: secret)
    }
    
    func getKey() -> String {
        return self.key
    }
    
    func getSecret() -> String {
        return self.secret
    }
    
    func getBaseUrl() -> String {
      var baseUrl = ""
      #if DEBUG
        baseUrl = "https://collector-staging.suprsend.workers.dev/"
      #else
        baseUrl = "https://hub.suprsend.com/"
      #endif
        return baseUrl
    }
    
}
