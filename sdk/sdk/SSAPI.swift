//
//  SSAPI.swift
//  sdk
//
//  Created by Niks on 02/11/21.
//

import Foundation
import shared

class IosMutation : MutationHandler {
    var flush = false
    func isFlushing() -> Bool {
        flush
    }
    func setFlushing(value: Bool) {
        flush = value
    }
    
}


public class SSAPI {
    public init() {
        
    }
    public func getInstance(
        apiKey:String,
        apiSecret:String,
        apiBaseUrl:String
    )  -> IOSSSApi{
        IOSSSApi
            .Companion
            .init()
            .getInstance(
                apiKey: apiKey,
                apiSecret: apiSecret,
                apiBaseUrl: apiBaseUrl,
                mutationHandler: IosMutation()
            )
    }
    public func initialize(){
        IOSSSApi.Companion.init().initialize()
    }
    
    public func enableLogging(){
        IOSSSApi.Companion.init().enableLogging()
    }
}
