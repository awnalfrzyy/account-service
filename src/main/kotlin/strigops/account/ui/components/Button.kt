package strigops.account.ui.components

import kotlinx.html.*

enum class ButtonVariant (val classes: String){
    PRIMARY("bg-blue-600 hover:bg-blue-700 text-white"),
    DANGER("bg-red-600 hover:bg-red-700 text-white"),
    SUCCESS("bg-green-600 hover:bg-green-700 text-white"),
    GHOST("bg-transparent border border-gray-300 hover:bg-gray-100 text-gray-700")
}

fun FlowContent.Button(
    text: String,
    variant: ButtonVariant = ButtonVariant.PRIMARY,
    type: ButtonType = ButtonType.button,
    onClick: String = "",
    fullWidth: Boolean = false,
) {
    button(type = type, classes = "${variant.classes} px-5 py-2.5 rounded-lg font-medium transition-all duration-200 focus:ring-4 focus:outline-none ${if (fullWidth) "w-full" else ""}") 
    {
        if (onClick.isNotEmpty()) attributes["onclick"] = onClick
        +text
    }
}