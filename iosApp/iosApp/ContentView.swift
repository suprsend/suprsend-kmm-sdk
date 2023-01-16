import SwiftUI
import shared


struct ContentView: View {
   
    var isLogin  = false //UserDefaults.standard.bool(forKey: "login")
    
    
    var body: some View {
        NavigationView{
            if(isLogin){
                HomeView()
                    .offset(y: -60)
            }else{
                WalkThroughtView()
                    .offset(y: -60)
            }
                
            
        }
    }
}


struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

//NavigationView{
//    VStack {
//        Text(greet)
//        Button("Flush") {
//            sSApiHelper.flush()
//        }
//        Button("Event") {
//            sSApiHelper.track(eventName:"Button Clicked",properties: [:])
//        }
//        .navigationTitle("Login")
//        .offset(y:-60)
//        NavigationLink(
//            destination: /*@START_MENU_TOKEN@*/Text("Destination")/*@END_MENU_TOKEN@*/,
//
//            label: {/*@START_MENU_TOKEN@*/Text("Navigate")/*@END_MENU_TOKEN@*/})
//    }
//}
