//
//  Other.swift
//  ECommerceAppSwiftUI
//
//  Created by Niks on 06/11/21.
//  Copyright © 2021 Ayush Gupta. All rights reserved.
//

import Foundation
import SwiftUI

extension Date {
    func formatDate(format : String = "YYYY-MM-dd'T'HH:mm:ss.SSS") -> String {
        let date = Date()
        let formatter = DateFormatter()
        formatter.dateFormat = format
        return formatter.string(from: date)
    }
}

extension Color {
    static let dark = Color("dark")
    
    static let redar = Color("redar")
}

