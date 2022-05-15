//
//  AppConstants.swift
//  iosApp
//
//  Created by Nikhilesh Patve on 02/10/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import shared

struct AppConstants {
    static var productsList = Array(1...4).map { (element) -> (Product) in
        var product = Product()
        product.id = "P\(element)"
        product.title = "Product \(element)"
        
        var url = ""
        if(element % 3) == 0{
            url = "https://niksdevelop.herokuapp.com/images/346kb.jpg"
        }
        
        if(element % 3) == 1{
            url =  "https://niksdevelop.herokuapp.com/images/try-banner.jpg"
        }
        
        if(element % 3) == 2{
            url = "https://niksdevelop.herokuapp.com/images/welcome.gif"
        }
        
        product.url = url
        product.amount = Double(element) * 100
        return product
    }
    
    static var ssApi =  SSApiHelper().getInstance(apiKey: "asdasd",secret: "asd")
}
