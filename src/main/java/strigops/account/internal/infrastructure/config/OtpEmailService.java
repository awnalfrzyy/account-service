package strigops.account.internal.infrastructure.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class OtpEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired(required = false)
    private TemplateEngine templateEngine;

    @Value("${spring.mail.from:noreply@strigops.com}")
    private String fromEmail;

    @Value("${app.support-url:https://support.strigops.com}")
    private String supportUrl;

    @Value("${app.privacy-url:https://strigops.com/privacy}")
    private String privacyUrl;

    @Value("${app.terms-url:https://strigops.com/terms}")
    private String termsUrl;

    @Value("${app.verify-url:strigops://verify-otp}")
    private String verifyUrl;

    public void sendOtpEmail(String toEmail, String userName, String otp, int validityMinutes)
            throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject("Kode Verifikasi OTP Anda - Strigops Account");

        Map<String, Object> variables = new HashMap<>();
        variables.put("name", userName);
        variables.put("otp", otp);
        variables.put("validity_minutes", validityMinutes);
        variables.put("support_url", supportUrl);
        variables.put("privacy_url", privacyUrl);
        variables.put("terms_url", termsUrl);
        variables.put("verify_link", verifyUrl + "?otp=" + otp + "&email=" + toEmail);

        String htmlContent = generateHtmlContent(variables);

        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }

    private String generateHtmlContent(Map<String, Object> variables) {
        if (templateEngine != null) {
            Context context = new Context();
            context.setVariables(variables);
            return templateEngine.process("otp-email", context);
        } else {
            return replaceTemplateVariables(getHtmlTemplate(), variables);
        }
    }

    private String replaceTemplateVariables(String template, Map<String, Object> variables) {
        String result = template;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            result = result.replace("[[" + entry.getKey() + "]]",
                    String.valueOf(entry.getValue()));
        }
        return result;
    }

    private String getHtmlTemplate() {
        var stream = this.getClass().getClassLoader()
                .getResourceAsStream("templates/otp-email.html");
        if (stream != null) {
            try {
                return new String(stream.readAllBytes());
            } catch (Exception e) {
                return getDefaultHtmlTemplate();
            }
        }
        return getDefaultHtmlTemplate();
    }

    private String getDefaultHtmlTemplate() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { 
                        font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; 
                        margin: 0; 
                        padding: 0; 
                        background-color: #f4f4f4; 
                        color: #333; 
                    }
                    .container { 
                        max-width: 600px; 
                        margin: 20px auto; 
                        background-color: #ffffff; 
                        border: 1px solid #ddd; 
                        box-shadow: 0 2px 4px rgba(0,0,0,0.1); 
                    }
                    .header { 
                        background-color: #1a1a1a; 
                        color: #ffffff; 
                        padding: 30px 20px; 
                        text-align: center; 
                        border-bottom: 1px solid #ddd; 
                    }
                    .header h1 { 
                        margin: 0; 
                        font-size: 24px; 
                        font-weight: 600; 
                        letter-spacing: 1px; 
                    }
                    .content { 
                        padding: 30px 20px; 
                        line-height: 1.6; 
                    }
                    .greeting { 
                        font-size: 16px; 
                        margin-bottom: 20px; 
                    }
                    .otp-section { 
                        text-align: center; 
                        margin: 30px 0; 
                    }
                    .otp-label { 
                        font-size: 14px; 
                        color: #666; 
                        margin-bottom: 10px; 
                        text-transform: uppercase; 
                        letter-spacing: 1px; 
                    }
                    .otp-code { 
                        font-family: 'Courier New', monospace; 
                        font-size: 32px; 
                        font-weight: bold; 
                        color: #1a1a1a; 
                        background-color: #f9f9f9; 
                        border: 2px solid #ddd; 
                        padding: 15px; 
                        display: inline-block; 
                        letter-spacing: 4px; 
                        margin: 10px 0; 
                    }
                    .validity { 
                        font-size: 14px; 
                        color: #666; 
                        margin-top: 15px; 
                    }
                    .warning { 
                        background-color: #fff3cd; 
                        border: 1px solid #ffeaa7; 
                        padding: 15px; 
                        margin: 20px 0; 
                        font-size: 14px; 
                        color: #856404; 
                    }
                    .footer { 
                        background-color: #f8f8f8; 
                        padding: 20px; 
                        text-align: center; 
                        border-top: 1px solid #ddd; 
                        font-size: 12px; 
                        color: #666; 
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>OTP Verification Code</h1>
                    </div>
                    <div class="content">
                        <p class="greeting">Dear <strong>[[name]]</strong>,</p>
                        <p>We have received a request to verify your identity. Please use the One-Time Password (OTP) below to complete the verification process. This code is valid for this session only and cannot be reused.</p>
                        <div class="otp-section">
                            <div class="otp-label">Your Verification Code</div>
                            <div class="otp-code">[[otp]]</div>
                            <div class="validity">This code expires in [[validity_minutes]] minutes.</div>
                        </div>
                        <div class="warning">
                            <strong>Security Notice:</strong> Do not share this OTP code with anyone, including individuals claiming to be Strigops representatives. Our team will never request this code via phone, chat, or email.
                        </div>
                        <p>If you did not request this verification code or do not recognize this activity, please ignore this email and contact our support team immediately.</p>
                    </div>
                    <div class="footer">
                        <p>This email was sent automatically. Please do not reply to this email.</p>
                        <p>&copy; 2026 Strigops Inc. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """;
    }
}
