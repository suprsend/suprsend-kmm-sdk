//
//  SuprSend+Utilities.swift
//  SuperSend
//
//  Created by Shindalkar, Suraj on 12/03/22.
//

import Foundation

@objc
extension SuprSend {
    
    @objc public func identify(identity: String) {
        suprSendiOSAPI.identify(uniqueId: identity)
    }
    
    @objc public func track(eventName: String) {
        suprSendiOSAPI.track(eventName: eventName)
    }
    
    @objc public func track(eventName: String, properties: [String : Any]) {
        suprSendiOSAPI.track(eventName: eventName,properties: properties)
    }
    
    @objc public func set(key: String, value: String) {
        suprSendiOSAPI.getUser().set(key: key, value:  value)
    }
    
    @objc public func set(properties: [String : Any]) {
        suprSendiOSAPI.getUser().set(properties: properties)
    }
    
    @objc public func increment(key: String, value: Float) {
        suprSendiOSAPI.getUser().increment(key: key, value: value)
    }
    
    @objc public func increment(properties: [String: Float]) {
        suprSendiOSAPI.getUser().increment(properties:properties)
    }
    
    @objc public func append(key: String, value: Any) {
        suprSendiOSAPI.getUser().append(key: key, value: value)
    }
    
    @objc public func append(properties: [String: Any]) {
        suprSendiOSAPI.getUser().append(properties: properties)
    }
    
    @objc public func remove(properties: [String: Any]) {
        suprSendiOSAPI.getUser().remove(properties: properties)
    }
    
    @objc public func remove(key: String, value: Any) {
        suprSendiOSAPI.getUser().remove(key: key, value: value)
    }
    
    @objc public func unSet(key: String) {
        suprSendiOSAPI.getUser().unSet(key:key)
    }
    
    @objc public func unSet(keys: [String]) {
        suprSendiOSAPI.getUser().unSet(keys:keys)
    }
    
    @objc public func purchaseMade(properties: [String: Any]) {
        suprSendiOSAPI.purchaseMade(properties: properties)
    }
    
    @objc public func setOnce(properties: [String: Any]) {
        suprSendiOSAPI.getUser().setOnce(properties: properties)
    }
    
    @objc public func setOnce(key: String, value: Any) {
        suprSendiOSAPI.getUser().setOnce(key: key, value: value)
    }
    
    @objc public func setSuperProperty(key: String, value: Any) {
        suprSendiOSAPI.setSuperProperty(key: key, value: value)
    }
    
    @objc public func setSuperProperties(properties: [String: Any]) {
        suprSendiOSAPI.setSuperProperties(properties: properties)
    }

    @objc public func unSetSuperProperty(key: String) {
        suprSendiOSAPI.unSetSuperProperty(key:key)
    }
    
    @objc public func reset(unsubscribeNotification: Bool) {
        suprSendiOSAPI.reset(unSubscribeNotification: unsubscribeNotification)
    }
    
    @objc public func setEmail(emailId: String) {
        suprSendiOSAPI.getUser().setEmail(email: emailId)
    }
    
    @objc public func unSetEmail(emailId: String) {
        suprSendiOSAPI.getUser().unSetEmail(email: emailId)
    }
    
    @objc public func setSms(mobileNumber: String) {
        suprSendiOSAPI.getUser().setSms(mobile: mobileNumber)
    }
    
    @objc public func unSetSms(mobileNumber: String) {
        suprSendiOSAPI.getUser().unSetSms(mobile: mobileNumber)
    }
    
    @objc public func setWhatsApp(mobileNumber: String) {
        suprSendiOSAPI.getUser().setWhatsApp(mobile: mobileNumber)
    }
    
    @objc public func unSetWhatsApp(mobileNumber: String) {
        suprSendiOSAPI.getUser().unSetWhatsApp(mobile: mobileNumber)
    }
    
    @objc public func flush() {
        suprSendiOSAPI.flush()
    }
    
}
