//
//  CheckOutView.swift
//  ECommerceAppSwiftUI
//
//  Created by Ayush Gupta on 12/12/19.
//  Copyright © 2019 Ayush Gupta. All rights reserved.
//

import SwiftUI

struct CheckOutView: View {
    
    @State var show = false
    
    @AppStorage("email")
    var storeageEmail: String = ""
    
    fileprivate func SubmitButton() -> some View {
        Button(action: {
            self.show.toggle()
        }) {
            Text("Submit Order")
                .fontWeight(.bold)
                .foregroundColor(.white)
                .frame(height: 50)
                .frame(minWidth: 0, maxWidth: .infinity)
                .background(LinearGradient(gradient: Gradient(colors: [Color.init(hex: "cb2d3e"), Color.init(hex: "ef473a")]), startPoint: .leading, endPoint: .trailing))
                .cornerRadius(25)
        }
        .padding(.horizontal, 10)
        .padding(.vertical, 10)
    }
    
    fileprivate func NavigationBarView() -> some View {
        return HStack {
            Text("")
        }
        .frame(width: UIScreen.main.bounds.width, height: 45)
        .overlay(
            Text("Checkout")
                .font(.headline)
                .padding(.horizontal, 10)
                .background(Color.init(hex: "f9f9f9"))
            , alignment: .center)
    }
    
    var body: some View {
        NavigationView {
            ZStack {
                Color.init(hex: "f9f9f9")
                    .edgesIgnoringSafeArea(.all)
                VStack {
                    NavigationLink(destination: SuccessView(), isActive: self.$show) {
                        Text("")
                    }
                    NavigationBarView()
                    ScrollView {
                        VStack(alignment: .leading) {
                            HStack {
                                Text("Shipping Address")
                                    .font(.headline)
                                Spacer(minLength: 20)
                                Button(action: {
                                    
                                }) {
                                    Text("Change")
                                        .foregroundColor(.red)
                                }
                            }
                            .padding(.top, 5)
                            
                            ZStack(alignment: .top) {
                                Rectangle()
                                    .foregroundColor(.white)
                                    .cornerRadius(5)
                                    .shadow(color: Color.init(hex: "dddddd"), radius: 2, x: 0.8, y: 0.8)
                                VStack(alignment: .leading) {
                                    Text(storeageEmail)
                                        .padding(.top, 15)
                                    Text("Flat No - 1001, Abc Society, Shivajinagar, Pune, MH Pin - 411034")
                                        .padding(.top, 10)
                                        .padding(.bottom, 10)
                                        .lineLimit(nil)
                                }.padding(.horizontal, 5)
                            }.frame(height: 110)
                            
                            HStack {
                                Text("Payment")
                                    .font(.headline)
                                Spacer(minLength: 20)
                                Button(action: {
                                    
                                }) {
                                    Text("Change")
                                        .foregroundColor(.red)
                                }
                            }
                            .padding(.top, 10)
                            
                            ZStack(alignment: .top) {
                                Rectangle()
                                    .foregroundColor(.white)
                                    .cornerRadius(5)
                                    .shadow(color: Color.init(hex: "dddddd"), radius: 2, x: 0.8, y: 0.8)
                                HStack {
                                    Image("card")
                                        .resizable()
                                        .aspectRatio(4/3, contentMode: .fit)
                                    Text("**** **** **** 1234")
                                    Spacer()
                                }
                            }.frame(height: 80)
                            
                            HStack {
                                Text("Order:")
                                    .foregroundColor(.gray)
                                Spacer()
                                Text("₹\(AppConstants.cartList.reduce(0, { $0 + $1.price }))")
                                    .bold()
                            }.padding(.top, 30)
                            
                            HStack {
                                Text("Delivery Charges:")
                                    .foregroundColor(.gray)
                                Spacer()
                                Text("₹0")
                                    .bold()
                            }.padding(.top, 15)
                            
                            HStack {
                                Text("Total:")
                                    .foregroundColor(.gray)
                                Spacer()
                                Text("₹\(AppConstants.cartList.reduce(0, { $0 + ($1.price - ($1.price * $1.discount)/100)}))")
                                    .bold()
                            }.padding(.top, 15)
                            Spacer()
                        }.padding(15)
                    }
                    SubmitButton()
                }
            }
            .navigationBarTitle(Text(""), displayMode: .inline)
            .navigationBarHidden(true)
            .navigationBarBackButtonHidden(true)
            .onAppear{
                CommonAnalyticsHandler.track(eventName: "place_order_screen_viewed")
            }
        }
    }
}

struct CheckOutView_Previews: PreviewProvider {
    static var previews: some View {
        CheckOutView()
    }
}
