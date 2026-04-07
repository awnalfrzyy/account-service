package strigops.account.ui.layouts

import kotlinx.html.*
import strigops.account.ui.components.*

fun HTML.mainLayout(title: String, content: FlowContent.() -> Unit) {
    head {
        meta { charset = "UTF-8" }
        meta (
            name = "viewport",
            content = "width=device-width, initial-scale=1.0"
        )
        title { +title }
        link(rel = "stylesheet", href = "https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css")
        link(rel = "stylesheet", href = "https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap")
        style {
            unsafe {
                +"body { font-family: 'Inter', sans-serif; }"
            }
        }
    }
    body(classes = "bg-gray-50 text-gray-900 antialiased") {
        content() 
        script(src = "https://unpkg.com/lucide@latest") {}
        script {
            unsafe {
                +"lucide.createIcons();"
            }
        }
    }
}