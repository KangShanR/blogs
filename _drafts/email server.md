# email server

邮箱服务器

## 概述

- 接收邮件协议：POP3(post office protocol), IMAP(internet message access protocol)
- 发送邮件协议：SMTP(simple mail transfer protocol)

## 使用 javax.mail 实现邮件的收发

发送邮件

```java
public static void email(String email, String subject, String content) throws MessagingException {
    Properties properties = new Properties();
    properties.setProperty("mail.transport.protocol", "SMTP");
    properties.setProperty("mail.host", "smtp.yeah.net");
    properties.setProperty("mail.smtp.auth", "true");
    Authenticator authenticator = new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("email name", "password");
        }
    };
    Session session = Session.getInstance(properties, authenticator);
    MimeMessage message = new MimeMessage(session);
    message.setFrom(new InternetAddress("kangshan123@yeah.net"));
    message.setRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(email)));
    message.setSubject(subject);
    message.setContent(content, "text/html;charset=utf8");
    Transport.send(message);
}
```

接收邮件

```java
/**
* receiving the email
* todo parameter the message number
* @see {@link Folder#getMessage}
* @date    2020/3/10 11:15
*/
public static void receiving() throws MessagingException {
    Properties properties = new Properties();
    properties.setProperty("mail.pop3.host", "pop.yeah.net");
    Session session = Session.getInstance(properties);
    Store   store    = session.getStore("pop3s");
    store.connect("pop.yeah.net", "email name", "password");
    Folder      inbox   = store.getFolder("inbox");
    inbox.open(Folder.READ_ONLY);

    int         messageCount = inbox.getMessageCount();
    Message[]   messages = inbox.getMessages(messageCount - 2, messageCount);
    Arrays.stream(messages).forEach(m -> {
        System.out.println("----------------------");
        try {
            System.out.println("subject:"+ m.getSubject());
            System.out.println("content:"+ m.getContent());
            System.out.println("from:"+ m.getFrom());
            System.out.println("received time:"+ m.getReceivedDate());
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("----------------------");
    });
}
```
