//
//  CommonAnalyticsHandler.swift
//  ECommerceAppSwiftUI
//
//  Created by Niks on 06/11/21.
//  Copyright © 2021 Ayush Gupta. All rights reserved.
//

import SuperSendSdk

struct CommonAnalyticsHandler {
    
    static func identify(identity: String) {
        SuperSend.shared.identify(identity: identity)
        SuperSend.shared.setEmail(emailId: identity)
    }
    
    static func track(eventName: String) {
        SuperSend.shared.track(eventName: eventName)
    }
    
    static func track(eventName: String, properties: [String : Any]) {
        SuperSend.shared.track(eventName: eventName,properties: properties)
    }
    
    static func set(key: String, value: String) {
        SuperSend.shared.set(key: key, value:  value)
    }
    
    static func set(properties: [String : Any]) {
        SuperSend.shared.set(properties: properties)
    }
    
    static func increment(key: String, value: Any) {
        SuperSend.shared.increment(key: key, value: value)
    }
    
    static func increment(properties: [String:Any]) {
        SuperSend.shared.increment(properties:properties)
    }
    
    static func append(key: String, value: String) {
        SuperSend.shared.append(key: key, value: value)
    }
    
    static func remove(key: String, value: String) {
        SuperSend.shared.remove(key: key, value: value)
    }
    
    static func unset(key: String) {
        SuperSend.shared.unSet(key:key)
    }
    
    static func reset() {
        SuperSend.shared.reset()
    }
    
    static func setOnce(key: String, value: Any) {
        SuperSend.shared.setOnce(key: key, value: value)
    }
    
    static func setOnce(properties: [String : Any]) {
        SuperSend.shared.setOnce(properties:properties)
    }
    
    static func setSuperProperties(key: String, value: Any) {
        SuperSend.shared.setSuperProperty(key: key, value: value)
    }
    
    static func setSuperProperties(jsonObject: [String:Any]) {
        SuperSend.shared.setSuperProperties(properties: jsonObject)
    }
    
    static func unSetSuperProperties(key: String) {
        SuperSend.shared.unSetSuperProperty(key:key)
    }
    
    static func purchaseMade(properties: [String : Any]) {
        SuperSend.shared.purchaseMade(properties:properties)
    }
}
