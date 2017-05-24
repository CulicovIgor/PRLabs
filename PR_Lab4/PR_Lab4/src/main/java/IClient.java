import javax.mail.internet.MimeMessage;
import java.util.Map;

/**
 * Created by Alexandr on 30.04.2017.
 */
public interface IClient {
    void read();
    void deleteMessageByPos(int pos);
    Map<String, String> getHeadersByPos(int pos);
    MimeMessage getMessageByPos(int pos);

}
