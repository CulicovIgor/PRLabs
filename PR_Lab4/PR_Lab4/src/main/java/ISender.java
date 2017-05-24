import java.io.File;

/**
 * Created by Alexandr on 30.04.2017.
 */
public interface ISender {
    void send(String subject, String msg);
    void send(String subject, String msg, File attachment);
}
