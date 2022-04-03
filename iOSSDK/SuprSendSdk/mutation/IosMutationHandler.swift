//
//  IosMutationHandler.swift
//  sdk
//
//  Created by Niks on 06/11/21.
//

import Foundation

import Foundation
import shared

class IosMutationHandler : MutationHandler {
    var flush = false
    func isFlushing() -> Bool {
        flush
    }
    func setFlushing(value: Bool) {
        flush = value
    }
    
}
