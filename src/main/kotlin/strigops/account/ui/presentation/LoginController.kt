package strigops.account.ui.presentation

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import kotlinx.html.html
import kotlinx.html.stream.appendHTML
import strigops.account.ui.screens.loginPage

@Controller // Pakai @Controller, bukan @Component
class AuthViewController {

    @GetMapping("/auth/login") // Pakai GetMapping supaya lepas dari prefix Jersey /api
    @ResponseBody // Supaya balikin String (HTML), bukan nyari file template .jsp/.html
    fun showLoginPage(@RequestParam(value = "error", required = false) error: String?): String {
        return buildString {
            appendHTML().html { 
                loginPage(error) 
            }
        }
    }
}