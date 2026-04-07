package strigops.account.ui.components

import kotlinx.html.*

enum class AlertType(val classes: String, val icon: String) {
    INFO("bg-blue-50 text-blue-800 border-blue-300", "ℹ️"),
    ERROR("bg-red-50 text-red-800 border-red-300", "🚫"),
    SUCCESS("bg-green-50 text-green-800 border-green-300", "✅"),
    WARNING("bg-yellow-50 text-yellow-800 border-yellow-300", "⚠️")
}

fun FlowContent.Alert(message: String, type: AlertType = AlertType.INFO) {
    div(classes = "flex items-center p-4 mb-4 text-sm border rounded-lg ${type.classes}") {
        role = "alert"
        span(classes = "mr-3 text-lg") { +type.icon }
        div {
            span(classes = "font-semibold") { +type.name.lowercase().replaceFirstChar { it.uppercase() } }
            +": $message"
        }
    }
}