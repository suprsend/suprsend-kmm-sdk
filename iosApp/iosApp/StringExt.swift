//
//  StringExt.swift
//  iosApp
//
//  Created by Nikhilesh Patve on 28/09/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import SwiftUI

extension String{
    func load()-> UIImage{
        do{
            guard let url = URL(string: self) else{
                return UIImage()
            }
            let data:Data = try Data(contentsOf: url)
            return UIImage(data: data)
                ?? UIImage()
        }catch{
            
        }
        return UIImage()
    }
}
