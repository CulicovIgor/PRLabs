import javax.mail.internet.MimeMessage;

public interface IForm {
    void clearTable();
    void addMsgToTable(String subject, String from, String date, String attachment);
    void displayMessage(MimeMessage mimeMessage);
    void clearSenderForm();
    void deleteMessage(int pos);
}
