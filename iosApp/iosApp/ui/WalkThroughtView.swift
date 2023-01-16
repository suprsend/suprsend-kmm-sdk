//
//  WalkThroughtView.swift
//  iosApp
//
//  Created by Nikhilesh Patve on 02/10/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct WalkThroughtView: View {
    @State var selection = 1
    
    @State var navigateToLogin = false
    
    var body: some View {
        
        VStack{
            Text("Page : \(selection)")
            ZStack {
                TabView(selection: $selection){
                    ForEach((1...10), id: \.self) {row in
                        VStack{
                            RemoteImage(url: "https://niksdevelop.herokuapp.com/images/346kb.jpg")
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 350,height: 350)
                        }
                    }
                }.onTapGesture {
                    AppConstants.ssApi.track(eventName: "Banner Clicked", properties: ["position":"\(selection)"])
                }
            }
            NavigationLink(
                destination: LoginView(), isActive: $navigateToLogin,
                label: {
                    Button(action: {
                        self.navigateToLogin = true
                        print("Selection \(selection)" )
                    }, label: {
                        Text("Login")
                            .frame(
                                width: 200,
                                height: 50,
                                alignment: /*@START_MENU_TOKEN@*/.center/*@END_MENU_TOKEN@*/
                            )
                            .background(Color.blue)
                            .foregroundColor(Color.white)
                            .cornerRadius(8)
                    })
                    
                }
            )
            
            
        }
        .tabViewStyle(PageTabViewStyle())
        .onAppear {
            setupAppearance()
//            navigateToLogin = UserDefaults.standard.bool(forKey: "login")
        }
        .background(Color.gray)
    }
    
    func setupAppearance() {
        UIPageControl.appearance().currentPageIndicatorTintColor = .black
        UIPageControl.appearance().pageIndicatorTintColor = UIColor.black.withAlphaComponent(0.2)
    }
}
