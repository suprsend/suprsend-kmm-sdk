//
//  SuprSend+Utilities.swift
//  SuperSend
//
//  Created by Shindalkar, Suraj on 12/03/22.
//

import Foundation

extension SuprSend {
    
    public func identify(identity: String) {
        suprSendiOSAPI.identify(uniqueId: identity)
        suprSendiOSAPI.getUser().setEmail(email: identity)
    }
    
    public func track(eventName: String) {
        suprSendiOSAPI.track(eventName: eventName)
    }
    
    public func track(eventName: String, properties: [String : Any]) {
        suprSendiOSAPI.track(eventName: eventName,properties: properties)
    }
    
    public func set(key: String, value: String) {
        suprSendiOSAPI.getUser().set(key: key, value:  value)
    }
    
    public func set(properties: [String : Any]) {
        suprSendiOSAPI.getUser().set(properties: properties)
    }
    
    public func increment(key: String, value: Any) {
        suprSendiOSAPI.getUser().increment(key: key, value: value)
    }
    
    public func increment(properties: [String:Any]) {
        suprSendiOSAPI.getUser().increment(properties:properties)
    }
    
    public func append(key: String, value: String) {
        suprSendiOSAPI.getUser().append(key: key, value: value)
    }
    
    public func remove(key: String, value: String) {
        suprSendiOSAPI.getUser().remove(key: key, value: value)
    }
    
    public func unSet(key: String) {
        suprSendiOSAPI.getUser().unSet(key:key)
    }
    
    public func purchaseMade(properties: [String: Any]) {
        suprSendiOSAPI.purchaseMade(properties: properties)
    }
    
    public func setOnce(properties: [String: Any]) {
        suprSendiOSAPI.getUser().setOnce(properties: properties)
    }
    
    public func setOnce(key: String, value: Any) {
        suprSendiOSAPI.getUser().setOnce(key: key, value: value)
    }
    
    public func setSuperProperty(key: String, value: Any) {
        suprSendiOSAPI.setSuperProperty(key: key, value: value)
    }
    
    public func setSuperProperties(properties: [String: Any]) {
        suprSendiOSAPI.setSuperProperties(properties: properties)
    }

    public func unSetSuperProperty(key: String) {
        suprSendiOSAPI.unSetSuperProperty(key:key)
    }
    
    public func reset() {
        suprSendiOSAPI.reset()
    }
    
    public func setEmail(emailId: String) {
        suprSendiOSAPI.getUser().setEmail(email: emailId)
    }
    
    public func flush() {
        suprSendiOSAPI.flush()
    }
    
}
