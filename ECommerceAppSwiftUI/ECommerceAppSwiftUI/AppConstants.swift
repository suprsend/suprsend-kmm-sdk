//
//  AppConstants.swift
//  ECommerceAppSwiftUI
//
//  Created by Nikhilesh Patve on 09/10/21.
//  Copyright © 2021 Ayush Gupta. All rights reserved.
//

import Foundation
import sdk

struct AppConstants {
//    static var ssApi =  SSApiHelper().getInstance(apiKey: "2XwhDGHS9QtuwrbXHfqp",secret: "RWGx8ybPFFAHaqTImHs0")
    
    static var ssApi =  SSAPI()
         .getInstance(
         apiKey: "2XwhDGHS9QtuwrbXHfqp",
                     apiSecret: "RWGx8ybPFFAHaqTImHs0",
             apiBaseUrl: "https://collector-staging.suprsend.workers.dev")
    //https://collector-staging.suprsend.workers.dev
    
}
