//
//  musicsearchUITests.swift
//  musicsearchUITests
//
//  Created by David Ly on 2024-04-04.
//

import XCTest

final class musicsearchUITests: XCTestCase {

    override func setUpWithError() throws {
        // Put setup code here. This method is called before the invocation of each test method in the class.

        // In UI tests it is usually best to stop immediately when a failure occurs.
        continueAfterFailure = false

        // In UI tests it’s important to set the initial state - such as interface orientation - required for your tests before they run. The setUp method is a good place to do this.
    }

    override func tearDownWithError() throws {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }
    
    @MainActor func testAppScreenshots() throws {
        // UI tests must launch the application that they test.
        let app = XCUIApplication()
        setupSnapshot(app)
        app.activate()
        
        let returnKey = app/*@START_MENU_TOKEN@*/.buttons["Return"]/*[[".otherElements",".buttons[\"return\"]",".buttons[\"Return\"]"],[[[-1,2],[-1,1],[-1,0,1]],[[-1,2],[-1,1]]],[0]]@END_MENU_TOKEN@*/.firstMatch
        
        app/*@START_MENU_TOKEN@*/.textViews["TEXT_FIELD"]/*[[".textViews.containing(.staticText, identifier: \"Search\")",".otherElements.textViews[\"TEXT_FIELD\"]",".textViews[\"TEXT_FIELD\"]"],[[[-1,2],[-1,1],[-1,0]]],[0]]@END_MENU_TOKEN@*/.firstMatch.tap()
        app/*@START_MENU_TOKEN@*/.textViews["TEXT_FIELD"]/*[[".textViews",".containing(.image, identifier: \"Clear search field\")",".containing(.button, identifier: \"Clear search field\")",".containing(.staticText, identifier: \"Search\")",".otherElements",".textViews[\"zutoma\"]",".textViews[\"TEXT_FIELD\"]"],[[[-1,6],[-1,5],[-1,4,2],[-1,0,1]],[[-1,3],[-1,2],[-1,1]],[[-1,6],[-1,5]]],[0]]@END_MENU_TOKEN@*/.firstMatch.typeText("zutto")
        returnKey.tap()
        
        snapshot("1_search_artist")
        
        let element = app/*@START_MENU_TOKEN@*/.staticTexts["ずっと真夜中でいいのに。 (ZUTOMAYO, Japanese pop band)"]/*[[".buttons.staticTexts[\"ずっと真夜中でいいのに。 (ZUTOMAYO, Japanese pop band)\"]",".staticTexts[\"ずっと真夜中でいいのに。 (ZUTOMAYO, Japanese pop band)\"]"],[[[-1,1],[-1,0]]],[0]]@END_MENU_TOKEN@*/.firstMatch
        element.tap()
        
        snapshot("2_artist_details")
        
        let releaseGroupsTab = app/*@START_MENU_TOKEN@*/.buttons.staticTexts["Release Groups"]/*[[".buttons[\"Release Groups\"].staticTexts",".buttons.staticTexts[\"Release Groups\"]",".staticTexts[\"Release Groups\"]"],[[[-1,2],[-1,1],[-1,0]]],[1]]@END_MENU_TOKEN@*/.firstMatch
        releaseGroupsTab.tap()
        
        let element3 = app/*@START_MENU_TOKEN@*/.images["More actions"]/*[[".buttons[\"More actions\"].images",".buttons.images[\"More actions\"]",".images[\"More actions\"]"],[[[-1,2],[-1,1],[-1,0]]],[0]]@END_MENU_TOKEN@*/.firstMatch
        element3.tap()
        app/*@START_MENU_TOKEN@*/.buttons["Sort"]/*[[".buttons.containing(.staticText, identifier: \"Sort\")",".otherElements.buttons[\"Sort\"]",".buttons[\"Sort\"]"],[[[-1,2],[-1,1],[-1,0]]],[0]]@END_MENU_TOKEN@*/.firstMatch.tap()
        app/*@START_MENU_TOKEN@*/.buttons["Type alphabetically"].staticTexts/*[[".buttons[\"Type alphabetically\"].staticTexts",".buttons.staticTexts[\"Type alphabetically\"]",".staticTexts[\"Type alphabetically\"]"],[[[-1,2],[-1,1],[-1,0]]],[2]]@END_MENU_TOKEN@*/.firstMatch.tap()
        
        let element4 = app/*@START_MENU_TOKEN@*/.images["Back"]/*[[".buttons[\"Back\"].images",".buttons.images[\"Back\"]",".images[\"Back\"]"],[[[-1,2],[-1,1],[-1,0]]],[0]]@END_MENU_TOKEN@*/.firstMatch
        element4.tap()
        element.tap()
        releaseGroupsTab.tap()
        
        snapshot("3_artist_release_groups")
        
        app/*@START_MENU_TOKEN@*/.images["Filter"]/*[[".buttons[\"Filter\"].images",".buttons.images[\"Filter\"]",".images[\"Filter\"]"],[[[-1,2],[-1,1],[-1,0]]],[0]]@END_MENU_TOKEN@*/.firstMatch.tap()
        app/*@START_MENU_TOKEN@*/.textViews["FILTER_TEXT_FIELD"].firstMatch/*[[".otherElements",".textViews[\"h\"].firstMatch",".textViews[\"FILTER_TEXT_FIELD\"].firstMatch",".textViews",".containing(.button, identifier: \"Clear filter text\").firstMatch",".containing(.other, identifier: nil).firstMatch",".containing(.button, identifier: \"FILTER_BACK\").firstMatch",".firstMatch"],[[[-1,2],[-1,1],[-1,3,1],[-1,0,2]],[[-1,7],[-1,6],[-1,5],[-1,4]],[[-1,2],[-1,1]]],[0]]@END_MENU_TOKEN@*/.typeText("hi")
        returnKey.tap()
        
        snapshot("4_artist_release_groups_filter")
        
        // When recording on iPad Pro 13-inch (M4), we crash somewhere below
        
        app/*@START_MENU_TOKEN@*/.images["Cancel"]/*[[".buttons[\"FILTER_BACK\"].images",".buttons.images[\"Cancel\"]",".images[\"Cancel\"]"],[[[-1,2],[-1,1],[-1,0]]],[0]]@END_MENU_TOKEN@*/.firstMatch.tap()
        app/*@START_MENU_TOKEN@*/.staticTexts["沈香学 (Jinkougaku)"]/*[[".buttons.staticTexts[\"沈香学 (Jinkougaku)\"]",".staticTexts[\"沈香学 (Jinkougaku)\"]"],[[[-1,1],[-1,0]]],[0]]@END_MENU_TOKEN@*/.firstMatch.tap()
        let releasesTab = app/*@START_MENU_TOKEN@*/.buttons["Releases"].staticTexts/*[[".buttons[\"Releases\"].staticTexts",".buttons.staticTexts[\"Releases\"]",".staticTexts[\"Releases\"]"],[[[-1,2],[-1,1],[-1,0]]],[2]]@END_MENU_TOKEN@*/.firstMatch
        releasesTab.tap()
        app/*@START_MENU_TOKEN@*/.buttons.containing(.staticText, identifier: "沈香学")/*[[".buttons",".containing(.staticText, identifier: \"🌐 XW・2023-06-06\")",".containing(.button, identifier: \"Add 沈香学 to collection\")",".containing(.staticText, identifier: \"沈香学\")",".otherElements.buttons[\"沈香学\\n🌐 XW・2023-06-06\\nずっと真夜中でいいのに。\"]",".buttons[\"沈香学\\n🌐 XW・2023-06-06\\nずっと真夜中でいいのに。\"]"],[[[-1,5],[-1,4],[-1,0,1]],[[-1,3],[-1,2],[-1,1]]],[2,0]]@END_MENU_TOKEN@*/.firstMatch.tap()
        
        snapshot("5_release_details")
        
        app/*@START_MENU_TOKEN@*/.staticTexts["Tracks"]/*[[".buttons[\"Tracks\"].staticTexts",".buttons.staticTexts[\"Tracks\"]",".staticTexts[\"Tracks\"]"],[[[-1,2],[-1,1],[-1,0]]],[0]]@END_MENU_TOKEN@*/.firstMatch.tap()
        
        snapshot("6_release_tracks")
        
        app/*@START_MENU_TOKEN@*/.buttons["TopBarSubtitle"]/*[[".otherElements",".buttons[\"Release by ずっと真夜中でいいのに。\"]",".buttons[\"TopBarSubtitle\"]"],[[[-1,2],[-1,1],[-1,0,1]],[[-1,2],[-1,1]]],[0]]@END_MENU_TOKEN@*/.firstMatch.tap()
        app/*@START_MENU_TOKEN@*/.staticTexts["ずっと真夜中でいいのに。"]/*[[".buttons[\"ずっと真夜中でいいのに。\"].staticTexts",".buttons.staticTexts[\"ずっと真夜中でいいのに。\"]",".staticTexts[\"ずっと真夜中でいいのに。\"]"],[[[-1,2],[-1,1],[-1,0]]],[0]]@END_MENU_TOKEN@*/.firstMatch.tap()
        releaseGroupsTab.tap()
        
        app/*@START_MENU_TOKEN@*/.images["Add 沈香学 to collection"]/*[[".buttons[\"Add 沈香学 to collection\"].images",".buttons.images[\"Add 沈香学 to collection\"]",".images[\"Add 沈香学 to collection\"]"],[[[-1,2],[-1,1],[-1,0]]],[0]]@END_MENU_TOKEN@*/.firstMatch.tap()
        
        let createCollectionButton = app/*@START_MENU_TOKEN@*/.images["Create a collection"]/*[[".buttons.images[\"Create a collection\"]",".images",".images[\"Create a collection\"]"],[[[-1,2],[-1,1],[-1,0]]],[0]]@END_MENU_TOKEN@*/.firstMatch
        createCollectionButton.tap()
        app/*@START_MENU_TOKEN@*/.textViews.containing(.staticText, identifier: "Name")/*[[".textViews",".containing(.image, identifier: \"Clear search field\")",".containing(.button, identifier: \"Clear search field\")",".containing(.staticText, identifier: \"Name\")",".otherElements.textViews[\"Listene\"]",".textViews[\"Listene\"]"],[[[-1,5],[-1,4],[-1,0,1]],[[-1,3],[-1,2],[-1,1]]],[2,0]]@END_MENU_TOKEN@*/.firstMatch.typeText("Listened")
        
        let okButton = app/*@START_MENU_TOKEN@*/.staticTexts["OK"]/*[[".buttons[\"OK\"].staticTexts",".buttons.staticTexts[\"OK\"]",".staticTexts[\"OK\"]"],[[[-1,2],[-1,1],[-1,0]]],[0]]@END_MENU_TOKEN@*/.firstMatch
        okButton.tap()
        app/*@START_MENU_TOKEN@*/.buttons.containing(.staticText, identifier: "Listened")/*[[".buttons",".containing(.staticText, identifier: \"0\")",".containing(.staticText, identifier: \"Listened\")",".otherElements.buttons[\"Listened\\n0\"]",".buttons[\"Listened\\n0\"]"],[[[-1,4],[-1,3],[-1,0,1]],[[-1,2],[-1,1]]],[2,0]]@END_MENU_TOKEN@*/.firstMatch.tap()
        app/*@START_MENU_TOKEN@*/.images["Add 潜潜話 to collection"]/*[[".buttons[\"Add 潜潜話 to collection\"].images",".buttons.images[\"Add 潜潜話 to collection\"]",".images[\"Add 潜潜話 to collection\"]"],[[[-1,2],[-1,1],[-1,0]]],[0]]@END_MENU_TOKEN@*/.firstMatch.tap()
        app/*@START_MENU_TOKEN@*/.buttons.containing(.staticText, identifier: "Listened")/*[[".buttons",".containing(.staticText, identifier: \"1\")",".containing(.staticText, identifier: \"Listened\")",".otherElements.buttons[\"Listened\\n1\"]",".buttons[\"Listened\\n1\"]"],[[[-1,4],[-1,3],[-1,0,1]],[[-1,2],[-1,1]]],[2,0]]@END_MENU_TOKEN@*/.firstMatch.tap()
        app/*@START_MENU_TOKEN@*/.images["Add 潜潜話 to another collection"]/*[[".buttons[\"Add 潜潜話 to another collection\"].images",".buttons.images[\"Add 潜潜話 to another collection\"]",".images[\"Add 潜潜話 to another collection\"]"],[[[-1,2],[-1,1],[-1,0]]],[0]]@END_MENU_TOKEN@*/.firstMatch.tap()
        createCollectionButton.tap()
        app/*@START_MENU_TOKEN@*/.textViews.containing(.staticText, identifier: "Name")/*[[".textViews",".containing(.image, identifier: \"Clear search field\")",".containing(.button, identifier: \"Clear search field\")",".containing(.staticText, identifier: \"Name\")",".otherElements.textViews[\"Listen Lat\"]",".textViews[\"Listen Lat\"]"],[[[-1,5],[-1,4],[-1,0,1]],[[-1,3],[-1,2],[-1,1]]],[2,0]]@END_MENU_TOKEN@*/.firstMatch.typeText("Listen Later")
        okButton.tap()
        
        snapshot("7_collection_management")
    }

//    func testLaunchPerformance() throws {
//        if #available(macOS 10.15, iOS 13.0, tvOS 13.0, watchOS 7.0, *) {
//            // This measures how long it takes to launch your application.
//            measure(metrics: [XCTApplicationLaunchMetric()]) {
//                XCUIApplication().launch()
//            }
//        }
//    }
}
