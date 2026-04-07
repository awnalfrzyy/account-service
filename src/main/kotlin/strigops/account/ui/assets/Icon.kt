package strigops.account.ui.assets

import kotlinx.html.*

enum class StrigoIcon(val lucideName: String) {
    USER("user"),
    EMAIL("mail"),
    PASSWORD("lock"),
    OTP("shield-check"),
    ALERT_ERROR("circle-alert"),
    ALERT_SUCCESS("circle-check"),
    ARROW_RIGHT("arrow-right")
}

fun FlowContent.strigoIcon(
    icon: StrigoIcon,
    size: Int = 20, 
    classes: String = "",
    colorClass: String = "text-current" 
) {
    i {
        attributes["data-lucide"] = icon.lucideName
        attributes["style"] = "width: ${size}px; height: ${size}px;"
        attributes["class"] = "$colorClass $classes transition-all duration-300"
    }
}