package strigops.account.ui.screens

import kotlinx.html.*
import strigops.account.ui.layouts.*
import strigops.account.ui.components.*
import strigops.account.ui.assets.*
import strigops.account.ui.components.Alert

fun HTML.loginPage(errorMessage: String? = null) {
    mainLayout(title = "Sign in - StrigoAccount") {
        div(classes = "min-h-screen flex flex-col items-center justify-center p-4") {
            div(classes = "w-full max-w-lg bg-white border border-gray-200 rounded-lg px-8 py-10 shadow-sm md:shadow-md") {
                div(classes = "text-center mb-8") {
                    h2(classes = "text-2xl font-semibold tracking-tight text-gray-800") {
                        +"Sign in"
                    }
                    p(classes = "mt-2 text-gray-600") {
                        +"Use your Strigo Account"
                    }
                }

                errorMessage?.let {
                    Alert(it, type = AlertType.ERROR)
                }

                form(action = "/auth/login", method = FormMethod.post, classes = "space-y-6") {
                    
                    Input(
                        label = "Email or phone",
                        name = "email",
                        inputType = InputType.email,
                        icon = StrigoIcon.EMAIL,
                        placeholder = "you@example.com"
                    )

                    div {
                        Input(
                            label = "Enter your password",
                            name = "password",
                            inputType = InputType.password,
                            icon = StrigoIcon.PASSWORD,
                            placeholder = "Password"
                        )
                        a(href = "/auth/reset-password", classes = "text-sm font-medium text-blue-600 hover:text-blue-500 mt-1 inline-block") {
                            +"Forgot password?"
                        }
                    }

                    div(classes = "flex flex-row-reverse items-center justify-between mt-10") {
                        Button(
                            text = "Next",
                            variant = ButtonVariant.PRIMARY,
                            type = ButtonType.submit,
                            fullWidth = false 
                        )
                        
                        a(href = "/auth/register", classes = "text-sm font-medium text-blue-600 hover:bg-blue-50 px-3 py-2 rounded transition-colors") {
                            +"Create account"
                        }
                    }
                }
            }
            
            div(classes = "max-w-lg w-full mt-4 flex justify-between text-xs text-gray-500 px-2") {
                span { +"English (United States)" }
                div(classes = "space-x-4") {
                    a(href = "#") { +"Help" }
                    a(href = "#") { +"Privacy" }
                    a(href = "#") { +"Terms" }
                }
            }
        }
    }
}