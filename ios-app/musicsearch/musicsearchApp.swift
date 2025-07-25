//
//  musicsearchApp.swift
//  musicsearch
//
//  Created by David Ly on 2024-04-04.
//

import SwiftUI
import shared
import FirebaseCore

class AppDelegate: NSObject, UIApplicationDelegate {

  func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil
  ) -> Bool {
    FirebaseApp.configure()
    return true
  }
}


@main
struct musicsearchApp: App {
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    init() {
        Main_iosKt.initializeApp()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
