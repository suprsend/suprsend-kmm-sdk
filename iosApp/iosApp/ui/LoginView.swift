//
//  LoginView.swift
//  iosApp
//
//  Created by Nikhilesh Patve on 02/10/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import SwiftUI


struct LoginView:View {
    
    @State var email = ""
    
    @State var password = ""
    
    @State var navigateToHome = false
    
    
    @State var showAlert = false
    @State var alertMsg = ""
    var alert: Alert {
        Alert(title: Text(""), message: Text(alertMsg), dismissButton: .default(Text("OK")))
    }
    
    
    var body: some View {
        
        VStack {
            Form{
                Section{
                    TextField("Email",text:$email)
                    SecureField("Password",text:$password)
                }
            }
            .background(Color.green)
            .foregroundColor(Color.green)
            NavigationLink(
                destination: HomeView(), isActive: $navigateToHome,
                label: {
                    Button(action: {
                        print("Email : \(email) Password : \(password)")
                        if(email.isEmpty || password.isEmpty){
                            self.alertMsg = "Please enter email and passowrd"
                            self.showAlert = true
                        }else{
                            UserDefaults.standard.set(true, forKey: "login")
                            self.navigateToHome = true
                        }
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
            .alert(isPresented: $showAlert, content: { self.alert })
            Spacer()
        }
        .offset(y : -60)
        .background(Color.green)
        .onAppear{
//            navigateToHome = UserDefaults.standard.bool(forKey: "login")
        }
    }
}
