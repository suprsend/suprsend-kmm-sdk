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
    private let secret: String
    private let baseUrl: String?
    
    init(key: String, secret: String, baseUrl: String? = nil) {
        self.key = key
        self.secret = secret
        self.baseUrl = baseUrl
    }
    
    @objc public convenience init(withKey key: String, secret: String, baseUrl: String? = nil) {
        self.init(key: key, secret: secret, baseUrl: baseUrl)
    }
    
    func getKey() -> String {
        return self.key
    }
    
    func getSecret() -> String {
        return self.secret
    }
    
    func getBaseUrl() -> String {
        
      if let url = baseUrl {
        return url
      }
        
      var baseUrl = ""
      #if DEBUG
        baseUrl = "https://collector-staging.suprsend.workers.dev/"
      #else
        baseUrl = "https://hub.suprsend.com/"
      #endif
        return baseUrl
    }
    
}
