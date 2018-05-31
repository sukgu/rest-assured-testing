package com.framework.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailUtil {
	
	public static void sendMail(String text) {
		final String username=Config.mailSender;
		final String password=Config.senderPassword;
		
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
 
		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username,password);
				}
			});
 
		try {
			createZip();
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(Config.mailSender));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(Config.mailReciever));
			message.setSubject(Config.mailSubject);
			Multipart mp = new MimeMultipart();
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(text, "text/html");
			MimeBodyPart htmlPart1 = new MimeBodyPart();
			try {
				htmlPart1.attachFile(System.getProperty("user.home")+"/Reports.zip");
			} catch (IOException e) {
				e.printStackTrace();
			}
			mp.addBodyPart(htmlPart);
			mp.addBodyPart(htmlPart1);
			message.setContent(mp);
			Transport.send(message);
			
			//delete report zip file
			deleteFile();
 
			//System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static void createZip() {
		final int BUFFER = 2048;
		FileOutputStream dest=null;
		BufferedInputStream origin = null;
		byte data[] = new byte[BUFFER];
		FileInputStream fi=null;
		ZipOutputStream out=null;
		try {
			dest = new FileOutputStream(System.getProperty("user.home")+"/Reports.zip");
			out = new ZipOutputStream(new BufferedOutputStream(dest));
			File f = new File("reports/");
			String files1[] = f.list();
			for (int j=0; j<files1.length; j++) {
				File file = new File("reports/"+files1[j]);
				String files[] = file.list();
				for (int i=0; i<files.length; i++) {
					fi = new FileInputStream("reports/"+files1[j]+"/"+files[i]);
					origin = new BufferedInputStream(fi, BUFFER);
					ZipEntry entry = new ZipEntry("reports/"+files1[j]+"/"+files[i]);
					out.putNextEntry(entry);
					int count;
					while((count = origin.read(data, 0, BUFFER)) != -1) {
						out.write(data, 0, count);
						}
					origin.close();
					}
				}
			out.flush();
			out.close();
			} catch (Exception e) {
				e.printStackTrace();
				}
		}
	
	private static void deleteFile() {
		File file=new File(System.getProperty("user.home")+"/Reports.zip");
		if(file.exists()) {
			file.delete();
		}
	}
}
