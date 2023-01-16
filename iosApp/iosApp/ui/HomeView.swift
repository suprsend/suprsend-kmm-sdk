//
//  HomeView.swift
//  iosApp
//
//  Created by Nikhilesh Patve on 02/10/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct HomeView: View {
    
    let layout = [
        GridItem(.flexible()),
        GridItem(.flexible())
    ]
    
    @State var goToProductDetails = false

    var body: some View {
        ScrollView{
            LazyVGrid(columns: layout,spacing:20){
                ForEach(AppConstants.productsList,id: \.self){item in
                    VStack{
                        RemoteImage(url: item.url!)
                            .aspectRatio(contentMode: .fit)
                        Text(item.title ?? "None")
                        NavigationLink(destination: ProductDetailView(), isActive: $goToProductDetails) {
                            EmptyView()
                        }
                    }
                    .onTapGesture {
                        print("Helo \(item.title ?? "None")")
                        goToProductDetails = true
                    }
                }
            }
        }
     
    }
    
}
