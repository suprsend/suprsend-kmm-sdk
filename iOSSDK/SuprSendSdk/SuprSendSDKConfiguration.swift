//
//  SuprSendSDKConfiguration.swift
//  SuprSend
//
//  Created by Shindalkar, Suraj on 13/03/22.
//

import Foundation

public struct SuprSendSDKConfiguration {
    
    let key: String
    let secret: String
    let baseUrl: String
    
    public init(key: String, secret: String, baseUrl: String) {
        self.key = key
        self.secret = secret
        self.baseUrl = baseUrl
    }
    
}
