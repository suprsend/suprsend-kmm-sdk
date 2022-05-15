//
//  LoginView.swift
//  ECommerceAppSwiftUI
//
//  Created by Niks on 05/11/21.
//  Copyright © 2021 Ayush Gupta. All rights reserved.
//

import SwiftUI

struct LoginView: View {
    @AppStorage("isLoggedIn")
    var isLoggedIn: Bool = false
    
    @AppStorage("email")
    var storeageEmail: String = ""
    
    @State var email: String = "niks@suprsend.com"
    @State var password: String = "12345678"
    
    var body: some View {
        VStack {
            Spacer()
            ABCLogo()
                .scaleEffect(1.1)
            Spacer()
            VStack(spacing: 20) {
                Button(action: {}, label: {
                    Label(
                        title: { Text("Join using Facebook") },
                        icon: { Image("facebook").resizable()
                            .scaledToFit()
                            .frame(width: 20, height: 20)
                        })
                        .font(.system(size: 20, weight: .semibold))
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .frame(height: 50)
                        .background(Color.redar)
                        .cornerRadius(5)
                })
                
                Button(action: {}, label: {
                    Label(
                        title: { Text("Join using Google") },
                        icon: { Image("google").resizable()
                            .scaledToFit()
                            .frame(width: 20, height: 20)
                        })
                        .font(.system(size: 20, weight: .semibold))
                        .foregroundColor(.black)
                        .frame(maxWidth: .infinity)
                        .frame(height: 50)
                        .background(Color.white)
                        .cornerRadius(5)
                        .shadow(radius: 0.9)
                })
                
                Button(action: {}, label: {
                    Label(
                        title: { Text("Join using Apple") },
                        icon: { Image("apple").resizable()
                            .scaledToFit()
                            .foregroundColor(.white)
                            .frame(width: 20, height: 20)
                        })
                        .font(.system(size: 20, weight: .semibold))
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .frame(height: 50)
                        .background(Color.black)
                        .cornerRadius(5)
                })
                
                TextField("Email", text: $email)
                SecureField("Password", text: $password)
                
                Button(action: {
                    if(!email.isEmpty){
                        
                        CommonAnalyticsHandler.identify(identity: email)
                        CommonAnalyticsHandler.increment(key: "login_count",value: 1)
                        CommonAnalyticsHandler.setOnce(key: "first_login_at", value: Date().formatDate())
                        CommonAnalyticsHandler.setSuperProperties(key: "user_type", value: "user")
                        
                        storeageEmail = email
                        self.isLoggedIn.toggle()
                    }
                }, label: {
                    Label("Join using Email", systemImage: "envelope.fill")
                        .font(.system(size: 20, weight: .semibold))
                        .foregroundColor(.black)
                        .frame(maxWidth: .infinity)
                        .frame(height: 50)
                        .background(Color(.tertiaryLabel))
                        .cornerRadius(5)
                })
            }
            .padding()
            Spacer()
            
            VStack(spacing: 15) {
                NavigationLink(
                    destination: LoginView()) {
                    Text("Existing User? Get in")
                        .font(.system(size: 20, weight: .semibold))
                        .foregroundColor(.black)
                }
                Text("By tapping Create Account, I agree to abc Group's Terms and Privacy Policy")
                    .font(.caption)
                    .foregroundColor(.secondary)
                    .multilineTextAlignment(.center)
            }
            .padding()
            .padding(.vertical)
            .frame(maxWidth: .infinity)
            .background(Color(.tertiaryLabel).ignoresSafeArea(.all, edges: .bottom))
        }
        .navigationTitle("")
        .navigationBarHidden(true)
        .onAppear{
            CommonAnalyticsHandler.track(eventName: "login_screen_viewed")
        }
    }
}

struct LoginView_Previews: PreviewProvider {
    static var previews: some View {
        LoginView()
    }
}
struct ABCLogo: View {
    var body: some View {
        Image("abclogo")
            .resizable()
            .scaledToFit()
            .frame(width: 110)
    }
}
