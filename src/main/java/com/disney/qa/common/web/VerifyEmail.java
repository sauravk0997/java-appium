package com.disney.qa.common.web;

import com.disney.exceptions.OTPRetrievalException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.TestException;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zebrunner.carina.utils.common.CommonUtils.pause;
import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

/**
 * @author carney_d
 * @date 29 March 2016
 * @description Code to help verify an email in various email provider inbox's
 */

public class VerifyEmail {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Integer TRY_COUNT = 5;
    private static final Integer TRY_COUNT_ESPN = 3;
    private static final Integer TIME_IN_BETWEEN = 30;
    private static final Integer TIME_BUFFER = 60;
    private static final String HOST_GMAIL = "imap.gmail.com";
    private static final String HOST_YAHOO = "imap.mail.yahoo.com";
    private static final String PROTOCOL = "imaps";
    private static final String INBOX = "inbox";
    private static Integer emailIncrementor = 0;

    /**
     * Verify that an email was triggered
     *
     * @param emailAddress
     * @param emailPassword
     * @param expectedSubject
     * @param startTime
     * @param verifyBodyText
     */
    public void scanEmail(String emailAddress, String emailPassword,
                          String expectedSubject, Date startTime, String verifyBodyText) {

        String host = getEmailHost(emailAddress);

        LOGGER.info(String.format("Connect to %s as [%s] to verify expected Email was sent", host, emailAddress));

        Date startTimeAdjusted = DateUtils.addSeconds(startTime, -TIME_BUFFER);

        // CONNECT TO EMAIL
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props);

        Boolean found = false;

        try {
            Store store = session.getStore(PROTOCOL);
            LOGGER.info(emailAddress);
            LOGGER.info(emailPassword);
            store.connect(host, emailAddress, emailPassword);

            // AFTER CONNECTING.. Attempt to find the email
            resetEmailCount();

            while (!found && emailIncrementor < TRY_COUNT) {
                found = parseMailBox(store, emailAddress, expectedSubject, startTimeAdjusted, verifyBodyText);
                incrementEmailCounter();
                if (!found) {
                    wrappedTime();
                    LOGGER.info("Could not find the email.  Will pause and rescan mailbox again");
                }
            }

            store.close();

            if (!found) {
                Assert.fail(String.format("Could not find the '%s' email.  Tried '%s' times.  Now out of attempts.", expectedSubject, emailIncrementor));
            }

        } catch (MessagingException ex) {
            LOGGER.error(String.format("Error Retrieving Information from Inbox: %s", ex.getMessage()), ex);
        } catch (IOException ex1) {
            LOGGER.error(String.format("I/O Error Encountered Retrieving Information from Inbox: %s", ex1.getMessage()), ex1);
        }
    }

    /**
     * Verify that an email was triggered
     *
     * @param emailAddress
     * @param emailPassword
     * @param expectedSubject
     * @param startTime
     */
    public void scanEmail(String emailAddress, String emailPassword,
                          String expectedSubject, Date startTime) {

        String host = getEmailHost(emailAddress);

        LOGGER.info(String.format("Connect to %s as [%s] to verify expected Email was sent", host, emailAddress));

        Date startTimeAdjusted = DateUtils.addSeconds(startTime, -TIME_BUFFER);

        // CONNECT TO EMAIL
        Properties props = new Properties();
        Session session = Session.getInstance(props);

        Boolean found = false;

        try {
            Store store = session.getStore(PROTOCOL);
            store.connect(host, emailAddress, emailPassword);

            // AFTER CONNECTING.. Attempt to find the email
            resetEmailCount();

            while (!found && emailIncrementor < TRY_COUNT) {
                found = parseMailBox(store, emailAddress, expectedSubject, startTimeAdjusted);
                incrementEmailCounter();
                if (!found) {
                    wrappedTime();
                    LOGGER.info("Could not find the email.  Will pause and rescan mailbox again");
                }
            }

            store.close();

            if (!found) {
                Assert.fail(String.format("Could not find the '%s' email.  Tried '%s' times.  Now out of attempts.", expectedSubject, emailIncrementor));
            }

        } catch (MessagingException ex) {
            LOGGER.error(String.format("Error Retrieving Information from Inbox: %s", ex.getMessage()), ex);
        } catch (IOException ex1) {
            LOGGER.error(String.format("I/O Error Encountered Retrieving Information from Inbox: %s", ex1.getMessage()), ex1);
        }
    }

    /**
     * Provide a start time for a test that can be used for email verification
     */
    public Date getStartTime() {

        Calendar cal = Calendar.getInstance();
        String time = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyy").format(cal.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy");
        Date startTime = null;

        try {
            startTime = sdf.parse(time);
        } catch (ParseException ex) {
            LOGGER.error(String.format("Parse Exception: %s", ex.getMessage()), ex);
        }

        LOGGER.info(String.format("Returning time [%s]", startTime));

        return startTime;
    }

    private boolean parseMailBox(Store store, String emailAddress, String expectedSubject, Date startTimeAdjusted, String providedText) throws MessagingException, IOException {

        Folder inbox = store.getFolder(INBOX);
        inbox.open(Folder.READ_ONLY);

        int messageCountEnd = inbox.getMessageCount();
        int messageCountStart = (5 > messageCountEnd) ? 0 : messageCountEnd - 4;

        Boolean found = false;

        LOGGER.info(String.format("Total Message Count: %03d New Message Count: %03d", messageCountEnd, messageCountStart));

        for (Message message : inbox.getMessages(messageCountStart, messageCountEnd)) {
            // GET SUBJECT / DATE TIME OF LATEST EMAIL
            String subject = message.getSubject();
            Date receiveTime = message.getReceivedDate();

            logEmailInfo(emailAddress, expectedSubject, subject, startTimeAdjusted, receiveTime, message.getMessageNumber());

            if (parseEmailBody(message.getContent(), escapeHtml(providedText))) {
                LOGGER.info(String.format("Found E-Mail with Provided Body Text: %s", providedText));
                found = true;
                break;
            } else {
                LOGGER.info(String.format("Unable to find E-Mail with Provided Body Text: %s", providedText));
            }

            LOGGER.info(String.format("Verified subject [%s] contains value [%s]", subject, expectedSubject));
            LOGGER.info(String.format("Verified time: [%s] is after: [%s]", receiveTime, startTimeAdjusted));

            if (!found && emailIncrementor < TRY_COUNT) {
                LOGGER.info("Could not find the email.");
            }
        }

        inbox.close(true);

        return found;
    }

    private boolean parseMailBox(Store store, String emailAddress, String expectedSubject, Date startTimeAdjusted) throws MessagingException, IOException {

        Folder inbox = store.getFolder(INBOX);
        inbox.open(Folder.READ_ONLY);

        int messageCountEnd = inbox.getMessageCount();
        int messageCountStart = (5 > messageCountEnd) ? 0 : messageCountEnd - 4;

        Boolean found = false;

        for (Message message : inbox.getMessages(messageCountStart, messageCountEnd)) {
            // GET SUBJECT / DATE TIME OF LATEST EMAIL
            String subject = message.getSubject();
            Date receiveTime = message.getReceivedDate();

            logEmailInfo(emailAddress, expectedSubject, subject, startTimeAdjusted, receiveTime, message.getMessageNumber());

            if (parseEmailBody(message.getSubject(), escapeHtml(expectedSubject))) {
                LOGGER.info(String.format("Found E-Mail with Provided Subject: %s", expectedSubject));
                found = true;
                break;
            } else {
                LOGGER.info(String.format("Unable to find E-Mail with Provided Subject: %s", expectedSubject));
            }

            LOGGER.info(String.format("Verified subject [%s] contains value [%s]", subject, expectedSubject));
            LOGGER.info(String.format("Verified time: [%s] is after: [%s]", receiveTime, startTimeAdjusted));

            if (!found && emailIncrementor < TRY_COUNT) {
                LOGGER.info("Could not find the email.");
            }
        }
        inbox.close(true);

        return found;
    }

    private boolean parseEmailBody(Object message, String providedText) throws MessagingException, IOException {

        boolean res = false;
        if (message instanceof Multipart) {
            Multipart mp = (Multipart) message;
            for (int x = 0; x < mp.getCount(); x++) {
                BodyPart part = mp.getBodyPart(x);
                String disposition = part.getDisposition();
                if (disposition != null && !"ATTACHMENT".equalsIgnoreCase(disposition) && part.getContent().toString().contains(providedText)) {
                    LOGGER.info("instance of Multipart " + part.getContent());
                    res = true;
                }
            }

        } else if (message instanceof String) {
            String content = (String) message;

            for (int i = 0; i < content.length(); i++) {
                if (content != null && content.contains(providedText)) {
                    res = true;
                } else {
                    res = false;
                }
            }
        }
        return res;
    }

    private void logEmailInfo(String emailAddress, String expectedSubject, String subject, Date startTimeAdjusted, Date receiveTime, int currentEmailNumber) {
        LOGGER.info("EMAIL: Email Address............: " + emailAddress);
        LOGGER.info("EMAIL: Expected Mail Subject....: " + expectedSubject);
        LOGGER.info("EMAIL: Actual Mail Subject......: " + subject);
        LOGGER.info("EMAIL: Time buffer set (s)......: " + TIME_BUFFER);
        LOGGER.info("EMAIL: Test Start Time w/ Buffer: " + startTimeAdjusted);
        LOGGER.info("EMAIL: Mail Received Time.......: " + receiveTime);
        LOGGER.info("EMAIL: Current Number...........: " + currentEmailNumber);
    }

    //========================================================================================================================================== //

    private void wrappedTime() {
        try {
            TimeUnit.SECONDS.sleep(TIME_IN_BETWEEN);
        } catch (Exception ex) {
            LOGGER.error("An error occurred while attempting to wait.", ex);
        }
    }

    private String getEmailHost(String email) {

        if (email.contains("@") && email.contains(".com")) {
            switch (email.toLowerCase().substring(email.indexOf('@') + 1, email.indexOf(".com"))) {
                case "gmail":
                    return HOST_GMAIL;
                case "yahoo":
                    return HOST_YAHOO;
                default:
                    throw new TestException("An unknown email provider address has been used, please review and update if necessary");
            }
        } else {
            throw new TestException("An invalid email address was passed, please review that it is following correct naming conventions");
        }
    }

    private synchronized void incrementEmailCounter() {
        emailIncrementor++;
    }

    private synchronized void resetEmailCount() {
        emailIncrementor = 0;
    }

    //======================================================================================//

    //Scan Email Methods for ESPN Purchase Flows & Forgot Password, by Date Received

    public void scanEspnEmail(String emailAddress, String emailPassword,
                              String expectedSubject, Date startTime) {

        String host = getEmailHost(emailAddress);

        Date startTimeAdjusted = DateUtils.addSeconds(startTime, -TIME_BUFFER);

        // CONNECT TO EMAIL
        Properties props = new Properties();
        Session session = Session.getInstance(props);

        Boolean found = false;

        try {
            Store store = session.getStore(PROTOCOL);
            store.connect(host, emailAddress, emailPassword);

            // AFTER CONNECTING.. Attempt to find the email
            resetEmailCount();

            while (!found && emailIncrementor < TRY_COUNT_ESPN) {
                found = parseEspnMailBox(store, emailAddress, expectedSubject, startTimeAdjusted);
                incrementEmailCounter();
                if (!found) {
                    wrappedTime();
                    LOGGER.info("Could not find the email.  Will pause and rescan mailbox again");
                }
            }

            store.close();

            if (!found) {
                LOGGER.info(String.format("Could not find the '%s' email.  Tried '%s' times.  Now out of attempts.", expectedSubject, emailIncrementor));

            }

        } catch (MessagingException ex) {
            LOGGER.error(String.format("Error Retrieving Information from Inbox: %s", ex.getMessage()), ex);
        }
    }

    private boolean parseEspnMailBox(Store store, String emailAddress, String expectedSubject, Date startTimeAdjusted) throws MessagingException {

        Folder inbox = store.getFolder(INBOX);
        inbox.open(Folder.READ_ONLY);

        int messageCountEnd = inbox.getMessageCount();
        int messageCountStart = (5 > messageCountEnd) ? 0 : messageCountEnd - 4;

        Boolean found = false;

        for (Message message : inbox.getMessages(messageCountStart, messageCountEnd)) {
            // GET SUBJECT / DATE TIME OF LATEST EMAIL
            String subject = message.getSubject();
            Date receiveTime = message.getReceivedDate();

            logEmailInfo(emailAddress, expectedSubject, subject, startTimeAdjusted, receiveTime, message.getMessageNumber());

            LOGGER.info("Verify the subject and date/time of the latest email");
            boolean matchedProvidedText = subject.contains(expectedSubject);
            boolean matchedReceiveTime = receiveTime.after(startTimeAdjusted);

            if (matchedProvidedText && matchedReceiveTime) {
                LOGGER.info(String.format("Found E-Mail with Provided Text: %s", expectedSubject));
                found = true;
                break;
            } else {
                LOGGER.info(String.format("Unable to find E-Mail.  Text Verification (%s), Time Verification (%s)"
                        , matchedProvidedText, matchedReceiveTime));
            }

            LOGGER.info(String.format("Verified subject [%s] contains value [%s]", subject, expectedSubject));
            LOGGER.info(String.format("Verified time: [%s] is after: [%s]", receiveTime, startTimeAdjusted));

            if (!found && emailIncrementor < TRY_COUNT) {
                LOGGER.info("Could not find the email.");
            }
        }
        inbox.close(true);

        return found;
    }

    public Folder retrieveGmailInbox(String email, String password) {
        var props = new Properties();
        var session = Session.getInstance(props, new GMailAuthenticator(email, password));
        try {
            var store = session.getStore(PROTOCOL);
            store.connect(getEmailHost(email), email, password);
            return store.getFolder(INBOX);
        } catch (Exception e) {
            throw new OTPRetrievalException("Unable to connect to Gmail Inbox: " + e);
        }
    }

    public String getDisneyOTP(String email, String password, String subject, Date startTime) {
        var inbox = retrieveGmailInbox(email, password);
        try {
            inbox.open(Folder.READ_ONLY);
            var count = 0;
            while (count < 50) {
                Message[] messages = inbox.getMessages();
                int start = messages.length > 5 ? messages.length - 4 : 0;
                for (int i = start; i < messages.length; i++) {
                    var toRecipient = messages[i].getRecipients(Message.RecipientType.TO)[0].toString();
                    if (toRecipient.equalsIgnoreCase(email) && messages[i].getSubject().contains(subject) && messages[i].getReceivedDate().after(startTime))
                        return retrieveOTPFromMessage(messages[i], "This passcode can only be used once and will expire in 15 minutes:", "If you did not make this request, please visit our", false);
                }
                pause(1);
                count++;
            }
        } catch (MessagingException e) {
            LOGGER.error("Unable to retrieve messages from inbox: {}", e.toString());
        }
        throw new OTPRetrievalException("Unable to retrieve messages from inbox");
    }

    /**
     * This method will be used for localization OTP retrieval
     * This is not thread-safe
     *
     * @param email     - email
     * @param password  - password
     * @param startTime - start time
     * @return - Disney OTP
     */
    public String getDisneyOTP(String email, String password, Date startTime) {
        var inbox = retrieveGmailInbox(email, password);
        try {
            var count = 0;
            while (count < 60) {
                if (inbox.isOpen()) {
                    inbox.close(true);
                }
                inbox.open(Folder.READ_ONLY);
                Message[] messages = inbox.getMessages();
                int lastEmail = messages.length - 1;
                var toRecipient = messages[lastEmail].getRecipients(Message.RecipientType.TO)[0].toString();
                if (toRecipient.equalsIgnoreCase(email) && messages[lastEmail].getReceivedDate().after(startTime))
                    return retrieveOTPFromMessage(messages[lastEmail], "td align=\"left\"", "END Rich_Text_White", true);
                pause(1);
                count++;
            }
        } catch (MessagingException e) {
            LOGGER.error("Unable to retrieve messages from inbox: {}", e.toString());
        }
        throw new OTPRetrievalException("Unable to retrieve messages from inbox");
    }

    private String retrieveOTPFromMessage(Message message, String open, String close, boolean isLocalization) {
        try {
            Object content = message.getContent();
            if (content instanceof MimeMultipart) {
                MimeMultipart multipart = (MimeMultipart) content;
                if (multipart.getCount() > 0) {
                    BodyPart part = multipart.getBodyPart(0);
                    content = part.getContent();
                }
            }
            content = StringUtils.substringBetween(content.toString(), open, close);
            Pattern pattern;
            Matcher matcher;
            if (content != null) {
                pattern = Pattern.compile("\\d{6}");
                matcher = pattern.matcher(content.toString());
                List<String> list = new ArrayList<>();
                while (matcher.find())
                    list.add(matcher.group());
                return isLocalization ? list.get(1) : list.get(0);
            }
        } catch (Exception e) {
            LOGGER.error("OTP not retrieved from body: {}", e.toString());
        }
        throw new OTPRetrievalException("OTP not retrieved from body");
    }

    private static class GMailAuthenticator extends Authenticator {
        String user;
        String pw;

        public GMailAuthenticator(String username, String password) {
            super();
            this.user = username;
            this.pw = password;
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, pw);
        }
    }
}