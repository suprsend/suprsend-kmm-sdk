//
//  SSAPI.swift
//  sdk
//
//  Created by Niks on 02/11/21.
//

import Foundation
import shared

public class SSAPI {
    
    private init() {
        
    }
    
    public static func initialize(){
        IOSSSApi.Companion.init().initialize()
    }
    
    public static func getInstance(
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
                mutationHandler: IosMutationHandler()
            )
    }
    
    public static func enableLogging(){
        IOSSSApi.Companion.init().enableLogging()
    }
}
