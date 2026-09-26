import KotlinRuntime
import SharedUi
import SwiftUI
import UIKit

extension NSObject {
    @objc(_Kotlin_SwiftExport_wrapIntoExistential:)
    private static func _kotlinGetExistentialType(markerType: AnyObject.Type) -> KotlinBase.Type {
        func wrap<T>(_ cls: T.Type) -> KotlinBase.Type {
            _KotlinExistential<T>.self
        }

        func openAndWrap(_ markerType: AnyObject.Type) -> KotlinBase.Type {
            return _openExistential(markerType, do: wrap)
        }

        return openAndWrap(markerType)
    }
}

public class _KotlinExistential<Wrapped>: KotlinBase {}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        SharedUi.com.arsildo.merrpatenten.shared.ui.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
