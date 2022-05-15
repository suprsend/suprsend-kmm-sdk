//
//  CommonAnalyticsHandler.swift
//  ECommerceAppSwiftUI
//
//  Created by Niks on 06/11/21.
//  Copyright © 2021 Ayush Gupta. All rights reserved.
//

import SuprSendSdk

struct CommonAnalyticsHandler {
    
    static func identify(identity: String) {
        SuprSend.shared.identify(identity: identity)
        SuprSend.shared.setEmail(emailId: identity)
    }
    
    static func track(eventName: String) {
        SuprSend.shared.track(eventName: eventName)
    }
    
    static func track(eventName: String, properties: [String : Any]) {
        SuprSend.shared.track(eventName: eventName,properties: properties)
    }
    
    static func set(key: String, value: String) {
        SuprSend.shared.set(key: key, value:  value)
    }
    
    static func set(properties: [String : Any]) {
        SuprSend.shared.set(properties: properties)
    }
    
    static func increment(key: String, value: Float) {
        SuprSend.shared.increment(key: key, value: value)
    }
    
    static func increment(properties: [String: Float]) {
        SuprSend.shared.increment(properties: properties)
    }
    
    static func append(key: String, value: String) {
        SuprSend.shared.append(key: key, value: value)
    }
    
    static func remove(key: String, value: String) {
        SuprSend.shared.remove(key: key, value: value)
    }
    
    static func unset(key: String) {
        SuprSend.shared.unSet(key:key)
    }
    
    static func reset() {
        SuprSend.shared.reset()
    }
    
    static func setOnce(key: String, value: Any) {
        SuprSend.shared.setOnce(key: key, value: value)
    }
    
    static func setOnce(properties: [String : Any]) {
        SuprSend.shared.setOnce(properties:properties)
    }
    
    static func setSuperProperties(key: String, value: Any) {
        SuprSend.shared.setSuperProperty(key: key, value: value)
    }
    
    static func setSuperProperties(jsonObject: [String:Any]) {
        SuprSend.shared.setSuperProperties(properties: jsonObject)
    }
    
    static func unSetSuperProperties(key: String) {
        SuprSend.shared.unSetSuperProperty(key:key)
    }
    
    static func purchaseMade(properties: [String : Any]) {
        SuprSend.shared.purchaseMade(properties:properties)
    }
}
