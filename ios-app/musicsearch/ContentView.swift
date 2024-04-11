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
            .ignoresSafeArea(.all, edges: .all)
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        Main_iosKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

#Preview {
    ContentView()
}
