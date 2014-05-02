package br.com.devschool.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class Ferramentas {

	// Função para criar hash da senha informada
	public static String md5Critografia(String senha) {
		String sen = "";
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			BigInteger hash;
			hash = new BigInteger(1, md.digest(senha.getBytes("UTF-8")));
			sen = hash.toString(16);
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sen.toUpperCase();
	}

	/**
	 * Este método recebe os parâmentros do email, configura e autentica a
	 * sessão no servidor de e-mail, monta e envia a mensagem (E-mail)
	 * 
	 * @author 	ATILLA
	 * @date 	13/01/2013
	 * @param String nome
	 * @param String email
	 * @param String telefone
	 * @param String assunto
	 * @param String mensagem
	 * @param String Gmail do remetente
	 * @param String Senha do remetente
	 * @return Boolean - E-mail enviado ou não
	 * @throws NegocioException 
	 */
	public static boolean entrarEmContato(String nome, String email,
			String telefone, String assunto, String mensagem,
			final String emailFrom, final String senha) throws NegocioException {

		Properties p = new Properties();
		p.put("mail.smtp.host", "smtp.gmail.com");
		p.put("mail.smtp.auth", "true");
		p.put("mail.smtp.port", "465");
		p.put("mail.smtp.socketFactory.port", "465");
		p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		p.put("mail.smtp.host", "smtp.gmail.com");

		Authenticator auth = new Authenticator() {
			@Override
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailFrom, senha);
			}
		};

		Session session = Session.getInstance(p, auth);
		session.setDebug(false);

		try {
			// cria a mensagem
			MimeMessage msg = new MimeMessage(session);
			msg.setSubject(assunto);
			msg.setFrom(new InternetAddress(email));
			msg.setRecipients(Message.RecipientType.TO,
					" josemar.rincon@gmail.com");

			StringBuilder mensagemBuilder = new StringBuilder();
			mensagemBuilder.append("Nome: " + nome + "\n");
			mensagemBuilder.append("E-mail: " + email + "\n");
			mensagemBuilder.append("Telefone: " + telefone + "\n");
			mensagemBuilder.append("Mensagem: " + mensagem + "\n");

			// cria a primeira parte da mensagem
			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setText(mensagemBuilder.toString());

			// cria a Multipart
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);

			// adiciona a Multipart na mensagem
			msg.setContent(mp);

			// configura a data: cabecalho
			msg.setSentDate(new Date());

			// enviando o e-mail
			Transport.send(msg);

			return true;
		} catch (AddressException ae) {
			throw new NegocioException("erro.geral", "AddressException");
		} catch (MessagingException me) {
			throw new NegocioException("erro.geral", "MessagingException");
		} catch (Exception e) {
			throw new NegocioException("erro.geral");
		}
	}

	public static StreamedContent mostraImagem(byte[] imagem, EntidadeGeral entidade) {
		try {
			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			if (imagem != null) {
				BufferedImage bufferedImage = ImageIO.read(ImagemUtil.byteToInputStream(imagem));
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				if (entidade != null) {
					File file = new File(servletContext.getRealPath("//images//curso//" + entidade.getId() + ".png"));
					FileOutputStream out = new FileOutputStream(file);
					ImageIO.write(bufferedImage, "png", out);
				}

				ImageIO.write(bufferedImage, "png", os);

				return new DefaultStreamedContent(new ByteArrayInputStream(os.toByteArray()), "image/png");
			}

			String relativeWebPath = "/images/";
			String absoluteDiskPath = servletContext.getRealPath(relativeWebPath);

			File file = new File(absoluteDiskPath + "/java.jpg");
			return new DefaultStreamedContent(new ByteArrayInputStream(ImagemUtil.fileToByte(file)), "image/png");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Este método formata a data para o padrão de data brasileiro
	 * 
	 * @author Wendel Nunes Donizete
	 * @since 19/03/2013
	 * @param date - Data a ser formata
	 * @param pattern - Pattern que deverá ser formatada
	 * @return String - Data formatada
	 */
	public String formatDate(Date date, String pattern) {
	    if (date == null) {
	        return null;
	    }

	    if (pattern == null) {
	        throw new NullPointerException("pattern");
	    }

	    Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
	    return new SimpleDateFormat(pattern, locale).format(date);
	}
}
