//
//  CommonAnalyticsHandler.swift
//  ECommerceAppSwiftUI
//
//  Created by Niks on 06/11/21.
//  Copyright © 2021 Ayush Gupta. All rights reserved.
//

import sdk

struct CommonAnalyticsHandler {
    static func identify(identity: String) {
        AppConstants.ssApi.identify(uniqueId: identity)
        AppConstants.ssApi.getUser().setEmail(email: identity)
    }
    
    static func track(eventName: String) {
        AppConstants.ssApi.track(eventName: eventName)
    }
    
    static func track(eventName: String, properties: [String : Any]) {
        AppConstants.ssApi.track(eventName: eventName,properties: properties)
    }
    
    static func set(key: String, value: String) {
        AppConstants.ssApi.getUser().set(key: key, value:  value)
    }
    
    static func set(properties: [String : Any]) {
        AppConstants.ssApi.getUser().set(properties: properties)
    }
    
    static func increment(key: String, value: Any) {
        AppConstants.ssApi.getUser().increment(key: key, value: value)
    }
    
    static func increment(properties: [String:Any]) {
        AppConstants.ssApi.getUser().increment(properties:properties)
    }
    
    static func append(key: String, value: String) {
        AppConstants.ssApi.getUser().append(key: key, value: value)
    }
    
    static func remove(key: String, value: String) {
        AppConstants.ssApi.getUser().remove(key: key, value: value)
    }
    
    static func unset(key: String) {
        AppConstants.ssApi.getUser().unSet(key:key)
    }
    
    static func reset() {
        AppConstants.ssApi.reset()
    }
    
    static func setOnce(key: String, value: Any) {
        AppConstants.ssApi.getUser().setOnce(key: key, value: value)
    }
    
    static func setOnce(properties: [String : Any]) {
        AppConstants.ssApi.getUser().setOnce(properties:properties)
    }
    
    static func setSuperProperties(key: String, value: Any) {
        AppConstants.ssApi.setSuperProperty(key: key, value: value)
    }
    
    static func setSuperProperties(jsonObject: [String:Any]) {
        AppConstants.ssApi.setSuperProperties(properties: jsonObject)
    }
    
    static func unSetSuperProperties(key: String) {
        AppConstants.ssApi.unSetSuperProperty(key:key)
    }
    
    static func purchaseMade(properties: [String : Any]) {
        AppConstants.ssApi.purchaseMade(properties:properties)
    }
}
