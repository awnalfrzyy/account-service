package strigops.account.ui.components

import kotlinx.html.*
import strigops.account.ui.assets.StrigoIcon
import strigops.account.ui.assets.strigoIcon

fun FlowContent.Input(
    label: String,
    name: String,
    inputType: InputType = InputType.text,
    placeholder: String = "",
    value: String = "",
    errorMessage: String? = null,
    icon: StrigoIcon? = null,
    required: Boolean = true,
    disabled: Boolean = false
) {
    val isError = errorMessage != null
    val borderClass = if (isError) "border-red-500 focus:ring-red-500" else "border-gray-300 focus:ring-blue-500"
    val inputId = "input-$name"

    div(classes = "mb-5") {
        label(classes = "block mb-2 text-sm font-medium ${if (isError) "text-red-700" else "text-gray-900"}") {
            attributes["for"] = inputId
            +label
        }

    div(classes = "relative") {
            if (icon != null) {
                div(classes = "absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none") {
                    strigoIcon(
                        icon,
                        size = 18, 
                        colorClass = if(isError) "text-red-500" else "text-gray-400"
                        )
                }
            }

        input(type = inputType, name = name, classes = "bg-gray-50 border text-gray-900 text-sm rounded-lg block w-full p-2.5 outline-none focus:ring-2 $borderClass") {
            id = inputId
            this.placeholder = placeholder
            this.value = value
            this.required = required
            this.disabled = disabled
            if (disabled) classes += " opacity-50 cursor-not-allowed bg-gray-200"
        
            when {
                    name.contains("email") -> attributes["autocomplete"] = "email"
                    name.contains("password") -> attributes["autocomplete"] = "current-password"
                    name.contains("otp") -> attributes["autocomplete"] = "one-time-code"
                }
        }
        if (name.contains("email", ignoreCase = true))
        attributes["autocomplete"] = "email"

        if (name.contains("password", ignoreCase = true))
        attributes["autocomplete"] = "current-password"

        if (isError) {
            p(classes = "mt-2 text-sm text-red-600 font-medium") {
                +errorMessage!!
            }
        }
        }
    }
}