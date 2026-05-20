package edu.itmo.piikt.server.command.server;

import edu.itmo.piikt.common.sc.ServerResponse;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public class EmailSender {
	private static final String USERNAME = System.getenv("GMAIL_USERNAME");
	private static final String PASSWORD = System.getenv("GMAIL_APP_PASSWORD");

	private static final String HTML_TEMPLATE = """
			<!DOCTYPE html>
			<html>
			<head>
			    <meta charset="UTF-8">
			    <title>WorkerFlow - Email Verification</title>
			    <style>
			        body {
			            font-family: 'Segoe UI', Arial, sans-serif;
			            background-color: #3d2b1a;
			            margin: 0;
			            padding: 20px;
			        }
			        .container {
			            max-width: 500px;
			            margin: 0 auto;
			            background: #d4c4a8;
			            border-radius: 12px;
			            padding: 30px;
			            box-shadow: 0 8px 20px rgba(0,0,0,0.2);
			            border: 1px solid #8b7355;
			        }
			        .header {
			            text-align: center;
			            margin-bottom: 20px;
			        }
			        .header h1 {
			            color: #2c1a0e;
			            font-size: 28px;
			            letter-spacing: 2px;
			            margin: 0;
			        }
			        .header p {
			            color: #5c4a32;
			            font-size: 12px;
			            margin: 5px 0 0;
			        }
			        .content {
			            background: #e8ddc6;
			            border-radius: 8px;
			            padding: 25px;
			        }
			        .content h2 {
			            color: #2c1a0e;
			            margin-top: 0;
			            border-left: 4px solid #8b5e3c;
			            padding-left: 15px;
			        }
			        .content p {
			            color: #3d2b1a;
			            font-size: 16px;
			        }
			        .code {
			            font-size: 36px;
			            font-weight: bold;
			            background: #d4c4a8;
			            display: inline-block;
			            padding: 15px 30px;
			            border-radius: 8px;
			            letter-spacing: 6px;
			            margin: 20px 0;
			            color: #2c1a0e;
			            border: 1px solid #8b7355;
			            font-family: monospace;
			        }
			        .footer {
			            text-align: center;
			            color: #8b7355;
			            font-size: 10px;
			            margin-top: 20px;
			        }
			        hr {
			            border: none;
			            border-top: 1px solid #b8a070;
			            margin: 20px 0;
			        }
			    </style>
			</head>
			<body>
			    <div class="container">
			        <div class="header">
			            <h1>WORKERFLOW</h1>
			            <p>secure worker management system</p>
			        </div>
			        <div class="content">
			            <h2>Confirm your email</h2>
			            <p>Your verification code:</p>
			            <div class="code">%s</div>
			            <p>The code expires in 30 minutes.</p>
			            <hr>
			            <p>If you didn't request this, please ignore this message.</p>
			        </div>
			        <div class="footer">
			            <p>2026 WorkerFlow • All rights reserved</p>
			        </div>
			    </div>
			</body>
			</html>
			""";

	public static ServerResponse sendVerificationCode(String toEmail, String code) {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME, PASSWORD);
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(USERNAME));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			message.setSubject("WorkerFlow — Email Verification");

			String htmlContent = String.format(HTML_TEMPLATE, code);
			message.setContent(htmlContent, "text/html; charset=UTF-8");

			Transport.send(message);
			return ServerResponse.successfulCompletion("Сообщение успешно отправлено");
		} catch (MessagingException e) {
			return ServerResponse.error("Сообщение не было отправлено");
		}
	}
}
