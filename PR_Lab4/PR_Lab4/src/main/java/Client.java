import org.apache.commons.net.pop3.POP3Client;
import org.apache.commons.net.pop3.POP3MessageInfo;

import javax.mail.Header;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Client implements IClient {
    private String username, password;
    private IForm form;
    private POP3Client client;
    private Map<Integer, MessageHolder> messageMap;
    public Client(String username, String password, IForm form){
        this.username = username;
        this.password = password;
        this.form = form;
    }
    @Override
    public void read(){
        System.out.println("Client simplu de posta – citire mesaj");
        form.clearSenderForm();
        try {
            messageMap = new HashMap<>();
            client = new POP3Client();
            client.connect("127.0.0.1", 110);
            if (client.login(username, password)){
                POP3MessageInfo[] messages = client.listMessages();
                System.out.println("Mesaje: " + messages.length);
                System.out.println("Primul mesaj");
                for (int i = messages.length-1; i>=0; i--) {
                    Session session = Session.getDefaultInstance(new Properties());
                    Reader r = client.retrieveMessage(messages[i].number);
                    BufferedReader br = new BufferedReader(r);
                    String content = "";
                    String line;
                    while ((line = br.readLine()) != null) {
                        content += line+"\n";
                    }
                    String attachment = " ";
                    //System.out.println(content);
                    try {
                        InputStream is = new ByteArrayInputStream(content.getBytes());
                        MimeMessage message = new MimeMessage(session, is);
                        if (message.getContent() instanceof Multipart) {
                            Multipart multipart = (Multipart) message.getContent();
                            if (multipart.getCount()>1) {
                                if (Part.ATTACHMENT.equalsIgnoreCase(multipart.getBodyPart(1).getDisposition())) {
                                    System.out.println("HAS ATTACHMENT!");
                                    attachment = multipart.getBodyPart(1).getFileName();
                                }
                            }
                        }
                        messageMap.put(messages.length-i-1,new MessageHolder(messages[i].number,message));
                        System.out.println("HEADERS:");
                        SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                        String date = dt.format(message.getSentDate());
                        String from = message.getFrom()[0].toString();
                        String subject = message.getHeader("Subject")[0];
                        form.addMsgToTable(subject,from,date,attachment);
                    } catch (Exception ee){
                        ee.printStackTrace();
                    }
                }
            } else {
                System.out.println("Logare fara succes...");
            }
            client.logout();
            client.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void deleteMessageByPos(int id) {
        try {
            client.connect("127.0.0.1",110);
            if (!client.login(username,password)) return;
            client.deleteMessage(messageMap.get(id).id);
            System.out.println("POS = "+id+" ID = "+messageMap.get(id).id);
            messageMap.remove(id);
            Map<Integer, MessageHolder> newMap = new HashMap<>();
            for (int index : messageMap.keySet())
                if (index<id)
                    newMap.put(index,messageMap.get(index));
                else
                    newMap.put(index-1,messageMap.get(index));
            messageMap = newMap;
            form.deleteMessage(id);
            client.logout();
            client.disconnect();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, String> getHeadersByPos(int pos) {
        MimeMessage mimeMessage = messageMap.get(pos).mimeMessage;
        try {
            Map<String, String> map = new HashMap<>();
            Enumeration headers = mimeMessage.getAllHeaders();
            while (headers.hasMoreElements()){
                Header header = (Header) headers.nextElement();
                map.put(header.getName(),header.getValue());
            }
            return map;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public MimeMessage getMessageByPos(int id) {
        System.out.println("GOT = "+id);
        return messageMap.get(id).mimeMessage;
    }

    public static class MessageHolder{
        private MimeMessage mimeMessage;
        private int id;
        private MessageHolder(int id, MimeMessage mimeMessage){
            this.id = id;
            this.mimeMessage = mimeMessage;
        }
    }

}
