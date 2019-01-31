/*
 * Copyright 2017 xtecuan.
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
package org.iadb.ws.timeandlabor.web.jpa.facade;

import static java.lang.System.out;
import java.util.Arrays;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.Session;
import javax.mail.Address;
import javax.mail.Message;
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
@Stateless
public class MailSender {

    @EJB
    private TemplateLoader templateLoader;

    @EJB
    private ConfigurationFacade configurationFacade;

    @Resource(mappedName = "java:jboss/mail/office365")
    private Session mailSession;

    private static final Logger LOGGER = Logger.getLogger(MailSender.class);

    public static Logger getLogger() {
        return LOGGER;
    }

    public void sendMail(String toMail) {
        try {
            MimeMessage m = new MimeMessage(mailSession);
            Address from = new InternetAddress(configurationFacade.getValue("mail.sender.from"));
            Address[] to = new InternetAddress[]{new InternetAddress(toMail)};

            //m.setFrom(from);
            m.setRecipients(Message.RecipientType.TO, to);
            m.setSubject("JBoss AS 7 Mail");
            m.setSentDate(new java.util.Date());
            m.setContent("Mail sent from JBoss AS 7", "text/plain");
            Transport.send(m);
            getLogger().info("Mail sent! to " + toMail);
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace();
            out.println("Error in Sending Mail: " + e);
        }
    }

    public Address[] getToAddressesForMailingProcess() throws AddressException {
        String[] toMails = configurationFacade.getValue("mail.executeSFHistoryProcessAsync.to").split(",");
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
            MimeMessage m = new MimeMessage(mailSession);
            Address from = new InternetAddress(configurationFacade.getValue("mail.sender.from"));
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
            MimeMessage m = new MimeMessage(mailSession);
            Address from = new InternetAddress(configurationFacade.getValue("mail.sender.from"));
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
        String emailBody = templateLoader.getFilledHtmlTemplate(data, CONFIRMATION_EMAIL_SURVEY_TEMPLATE);
        String emailSubject = configurationFacade.getValue("survey.creation.email.subject");
        Boolean isTextPlain = Boolean.FALSE;
        Boolean isHtml = Boolean.TRUE;
        sendMailWithResultsFromProcess(emailSubject, emailBody, email, isTextPlain, isHtml);
    }

    private static final String STEP_CONFIRMATION_EMAIL = "stepConfirmation.html";
    private static final String CONFIRMATION_EMAIL_TIME_AND_LABOR = "timeAndLaborConfirmation.html";

    @Asynchronous
    public void sendStepConfirmationEmail(Map<String, Object> data) {
        String[] emails = configurationFacade.getValue("step.Confirmation.Emails").split(",");
        String emailBody = templateLoader.getFilledHtmlTemplate(data, STEP_CONFIRMATION_EMAIL);
        String emailSubject = templateLoader.getFilledStringTemplate(data, 
                configurationFacade.getValue("step.Confirmation.email.subject"));
        Boolean isTextPlain = Boolean.FALSE;
        Boolean isHtml = Boolean.TRUE;
        sendMailWithResultsFromProcess(emailSubject, emailBody, emails, isTextPlain, isHtml);
    }

    @Asynchronous
    public void sendConfirmationExportTimeAndLabor(Map<String, Object> data) {
        String[] emails = configurationFacade.getValue("timeAndLabor.Confirmation.Emails").split(",");
        String emailBody = templateLoader.getFilledHtmlTemplate(data, CONFIRMATION_EMAIL_TIME_AND_LABOR);
        String emailSubject = configurationFacade.getValue("timeAndLabor.Confirmation.email.subject");
        Boolean isTextPlain = Boolean.FALSE;
        Boolean isHtml = Boolean.TRUE;
        sendMailWithResultsFromProcess(emailSubject, emailBody, emails, isTextPlain, isHtml);
    }
}
