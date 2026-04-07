package strigops.account.ui.components

import kotlinx.html.*

fun FlowContent.OtpGroup(name: String = "otp_code") {
    div(classes = "flex space-x-2 justify-center mb-6") {
        for (i in 1..6) {
            input(type = InputType.text, classes = "w-12 h-14 text-center text-2xl font-bold border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none") {
                attributes["maxlength"] = "1"
                attributes["data-otp-index"] = i.toString()
                // Tambahin script buat auto-tab antar input nanti
            }
        }
        input(type = InputType.hidden, name = name) {
            id = "otp_full_value"
        }
    }
}