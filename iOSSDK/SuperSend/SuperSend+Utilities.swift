//
//  SuperSend+Utilities.swift
//  SuperSend
//
//  Created by Shindalkar, Suraj on 12/03/22.
//

import Foundation

extension SuperSend {
    
    public func identify(identity: String) {
        superSendiOSAPI.identify(uniqueId: identity)
        superSendiOSAPI.getUser().setEmail(email: identity)
    }
    
    public func track(eventName: String) {
        superSendiOSAPI.track(eventName: eventName)
    }
    
    public func track(eventName: String, properties: [String : Any]) {
        superSendiOSAPI.track(eventName: eventName,properties: properties)
    }
    
    public func set(key: String, value: String) {
        superSendiOSAPI.getUser().set(key: key, value:  value)
    }
    
    public func set(properties: [String : Any]) {
        superSendiOSAPI.getUser().set(properties: properties)
    }
    
    public func increment(key: String, value: Any) {
        superSendiOSAPI.getUser().increment(key: key, value: value)
    }
    
    public func increment(properties: [String:Any]) {
        superSendiOSAPI.getUser().increment(properties:properties)
    }
    
    public func append(key: String, value: String) {
        superSendiOSAPI.getUser().append(key: key, value: value)
    }
    
    public func remove(key: String, value: String) {
        superSendiOSAPI.getUser().remove(key: key, value: value)
    }
    
    public func unSet(key: String) {
        superSendiOSAPI.getUser().unSet(key:key)
    }
    
    public func purchaseMade(properties: [String: Any]) {
        superSendiOSAPI.purchaseMade(properties: properties)
    }
    
    public func setOnce(properties: [String: Any]) {
        superSendiOSAPI.getUser().setOnce(properties: properties)
    }
    
    public func setOnce(key: String, value: Any) {
        superSendiOSAPI.getUser().setOnce(key: key, value: value)
    }
    
    public func setSuperProperty(key: String, value: Any) {
        superSendiOSAPI.setSuperProperty(key: key, value: value)
    }
    
    public func setSuperProperties(properties: [String: Any]) {
        superSendiOSAPI.setSuperProperties(properties: properties)
    }

    public func unSetSuperProperty(key: String) {
        superSendiOSAPI.unSetSuperProperty(key:key)
    }
    
    public func reset() {
        superSendiOSAPI.reset()
    }
    
    public func setEmail(emailId: String) {
        superSendiOSAPI.getUser().setEmail(email: emailId)
    }
    
    public func setiOSPushNotificationToken(token: String) {
        superSendiOSAPI.getUser().setIOSPush(token: token)
    }
    
    public func flush() {
        superSendiOSAPI.flush()
    }
    
}
