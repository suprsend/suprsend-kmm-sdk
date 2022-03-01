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
    
    public static func initialize(apiKey:String,
                                  apiSecret:String,
                                  apiBaseUrl:String){
        IOSSSApi.Companion.init().initialize( apiKey: apiKey,
                                              apiSecret: apiSecret,
                                              apiBaseUrl: apiBaseUrl)
    }
    
    public static func getInstance(
)  -> IOSSSApi{
        IOSSSApi
            .Companion
            .init()
            .getInstance(
                isFromCache: false,mutationHandler: IosMutationHandler()
            )
    }
    
    public static func enableLogging(){
        IOSSSApi.Companion.init().enableLogging()
    }
}
