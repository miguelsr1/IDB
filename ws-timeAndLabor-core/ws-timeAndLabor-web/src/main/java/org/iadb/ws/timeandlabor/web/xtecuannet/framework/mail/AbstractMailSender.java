/*
 * Copyright 2018 xtecuan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.iadb.ws.timeandlabor.web.xtecuannet.framework.mail;

import static java.lang.System.out;
import java.util.Arrays;
import java.util.Map;
import javax.ejb.Asynchronous;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;
import org.iadb.ws.timeandlabor.web.xtecuannet.framework.configuration.ConfigurationFacade;
import org.iadb.ws.timeandlabor.web.xtecuannet.framework.templates.TemplateLoader;

/**
 *
 * @author xtecuan
 */
public abstract class AbstractMailSender {

    protected abstract TemplateLoader getTemplateLoader();

    protected abstract ConfigurationFacade getConfigurationFacade();

    protected abstract Session getMailSession();

    private static final Logger LOGGER = Logger.getLogger(AbstractMailSender.class);

    public static Logger getLogger() {
        return LOGGER;
    }

    public void sendMail(String toMail, String subject, String contentText) {
        try {
            MimeMessage m = new MimeMessage(getMailSession());
            Address from = new InternetAddress(getConfigurationFacade().getValue("mail.sender.from"));
            Address[] to = new InternetAddress[]{new InternetAddress(toMail)};

            //m.setFrom(from);
            m.setRecipients(Message.RecipientType.TO, to);
            m.setSubject(subject);
            m.setSentDate(new java.util.Date());
            m.setContent(contentText, "text/plain");
            Transport.send(m);
            getLogger().info("Mail sent! to " + toMail);
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace();
            out.println("Error in Sending Mail: " + e);
        }
    }

    public Address[] getToAddressesForMailingProcess(String[] toEmails) throws AddressException {
        String[] toMails = toEmails;
        Address[] to = new InternetAddress[toMails.length];
        for (int i = 0; i < toMails.length; i++) {
            String toMail = toMails[i];
            to[i] = new InternetAddress(toMail);
        }
        return to;
    }

    public Address[] getToAddressForMailing(String email) throws AddressException {
        return new InternetAddress[]{new InternetAddress(email)};
    }

    public Address[] getToAddressForMailing(String[] emails) throws AddressException {
        InternetAddress[] addresses = new InternetAddress[emails.length];
        for (int i = 0; i < emails.length; i++) {
            String email = emails[i];
            addresses[i] = new InternetAddress(email);
        }
        return addresses;
    }

    public void sendMailWithResultsFromProcess(String emailSubject, String emailBody, String email, Boolean isTextPlain, Boolean isHtml) {
        try {
            MimeMessage m = new MimeMessage(getMailSession());
            Address from = new InternetAddress(getConfigurationFacade().getValue("mail.sender.from"));
            Address[] to = getToAddressForMailing(email);
            m.setRecipients(Message.RecipientType.TO, to);
            m.setSubject(emailSubject);
            m.setSentDate(new java.util.Date());
            String mime = null;
            if (isHtml) {
                mime = "text/html; charset=utf-8";
            }
            if (isTextPlain) {
                mime = "text/plain";
            }
            m.setContent(emailBody, mime);
            Transport.send(m);
            getLogger().info("Mail sent! to " + Arrays.asList(to).toString());
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace();
            out.println("Error in Sending Mail: " + e);
        }
    }

    public void sendMailWithResultsFromProcess(String emailSubject, String emailBody, String[] emails, Boolean isTextPlain, Boolean isHtml) {
        try {
            MimeMessage m = new MimeMessage(getMailSession());
            Address from = new InternetAddress(getConfigurationFacade().getValue("mail.sender.from"));
            Address[] to = getToAddressForMailing(emails);
            m.setRecipients(Message.RecipientType.TO, to);
            m.setSubject(emailSubject);
            m.setSentDate(new java.util.Date());
            String mime = null;
            if (isHtml) {
                mime = "text/html; charset=utf-8";
            }
            if (isTextPlain) {
                mime = "text/plain";
            }
            m.setContent(emailBody, mime);
            Transport.send(m);
            getLogger().info("Mail sent! to " + Arrays.asList(to).toString());
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace();
            out.println("Error in Sending Mail: " + e);
        }
    }

    private static final String CONFIRMATION_EMAIL_SURVEY_TEMPLATE = "surveyConfirmation.html";

    public void sendConfirmationEmailForSurveyCreation(String email, Map<String, Object> data) {
        String emailBody = getTemplateLoader().getFilledHtmlTemplate(data, CONFIRMATION_EMAIL_SURVEY_TEMPLATE);
        String emailSubject = getConfigurationFacade().getValue("survey.creation.email.subject");
        Boolean isTextPlain = Boolean.FALSE;
        Boolean isHtml = Boolean.TRUE;
        sendMailWithResultsFromProcess(emailSubject, emailBody, email, isTextPlain, isHtml);
    }

    private static final String STEP_CONFIRMATION_EMAIL = "stepConfirmation.html";

    @Asynchronous
    public void sendStepConfirmationEmail(Map<String, Object> data, String[] emails) {

        String emailBody = getTemplateLoader().getFilledHtmlTemplate(data, STEP_CONFIRMATION_EMAIL);
        String emailSubject = getTemplateLoader().getFilledStringTemplate(data,
                getConfigurationFacade().getValue("step.Confirmation.email.subject"));
        Boolean isTextPlain = Boolean.FALSE;
        Boolean isHtml = Boolean.TRUE;
        sendMailWithResultsFromProcess(emailSubject, emailBody, emails, isTextPlain, isHtml);
    }

}
