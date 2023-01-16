//
//  TabBarView.swift
//  ECommerceAppSwiftUI
//
//  Created by Ayush Gupta on 26/11/19.
//  Copyright © 2019 Ayush Gupta. All rights reserved.
//

import SwiftUI

class TabBarViewModel: ObservableObject {
    @Published var selectedTab: Int
    
    init(selectedTab: Int) {
        self.selectedTab = selectedTab
    }
}


struct TabBarView: View {
    
    @ObservedObject var viewModel: TabBarViewModel
        
    var body: some View {
        ZStack {
            Color.init(hex: "f9f9f9")
                .edgesIgnoringSafeArea(.all)
            TabView(selection: $viewModel.selectedTab) {
                HomeView()
                    .tabItem {
                        Image(systemName: "house.fill")
                        Text("Home")
                    }.tag(0)
                Shopping()
                    .tabItem {
                        Image(systemName: "cart.fill")
                        Text("Shop")
                    }.tag(1)
                FavoriteView()
                    .tabItem {
                        Image(systemName: "heart.fill")
                        Text("Favorite")
                    }.tag(2)
                BagView()
                    .tabItem {
                        Image(systemName: "bag.fill")
                        Text("Cart")
                    }.tag(3)
                ProfileView()
                    .tabItem {
                        Image(systemName: "ellipsis.circle.fill")
                        Text("More")
                    }.tag(4)
            }
        }
        .accentColor(Color.init(hex: "DB3022"))
        .navigationBarTitle("", displayMode: .inline)
        .navigationBarHidden(true)
        .navigationBarBackButtonHidden(true)
    }
}
