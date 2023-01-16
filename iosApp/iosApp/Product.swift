//
//  Product.swift
//  iosApp
//
//  Created by Nikhilesh Patve on 02/10/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation

struct Product : Hashable  {
    static func == (lhs: Product, rhs: Product) -> Bool {
        return lhs.id == rhs.id
    }
    
    var id :String?
    var title: String?
    var url: String?
    var amount: Double?
}
