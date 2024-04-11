//
//  ContentView.swift
//  musicsearch
//
//  Created by David Ly on 2024-04-04.
//

import SwiftUI
import shared

struct ContentView: View {
    let appComponent = AppComponent()
    
    var body: some View {
        ComposeView()
            .ignoresSafeArea(.all)
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        let controller = Main_iosKt.MainViewController()
        return controller
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}

#Preview {
    ContentView()
}
