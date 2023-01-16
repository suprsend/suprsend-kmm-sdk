//
//  DecideViewModel.swift
//  ECommerceAppSwiftUI
//
//  Created by Shindalkar, Suraj Manmath on 9/22/22.
//  Copyright © 2022 Suprsend. All rights reserved.
//

import Foundation
import Combine

class DecideViewModel: ObservableObject {
    @Published var selectedTab: Int
    
    init(selectedTab: Int) {
        self.selectedTab = selectedTab
    }
}
