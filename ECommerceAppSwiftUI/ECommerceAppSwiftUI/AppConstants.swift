//
//  AppConstants.swift
//  ECommerceAppSwiftUI
//
//  Created by Nikhilesh Patve on 09/10/21.
//  Copyright © 2021 Ayush Gupta. All rights reserved.
//

import Foundation
import sdk
import SwiftUI

struct AppConstants {
    //    static var ssApi =  SSApiHelper().getInstance(apiKey: "2XwhDGHS9QtuwrbXHfqp",secret: "RWGx8ybPFFAHaqTImHs0")
    
    static var ssApi =  SSAPI
        .getInstance()
    //https://collector-staging.suprsend.workers.dev
    static var cartList :[BagModel] = []
    
    
    
}
