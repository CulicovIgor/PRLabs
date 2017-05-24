import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import org.apache.commons.io.FilenameUtils;

import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GUIForm implements IForm {
    private JPanel container;
    private JPanel webHolder;
    private JButton send;
    private JTextArea msgArea;
    private JTable table1;
    private JButton refresh;
    private JButton openAttach;
    private JTextField subject;
    private JButton delete;
    private JTable table2;
    private JPanel mainPanel;
    private JButton clear;
    private JScrollPane headers;
    private ISender sender;
    private IClient receiver;
    private WebView webView;
    private MimeBodyPart attachment;

    public static void main(String[] args) {
        JFrame frame = new JFrame("GUIForm");
        frame.setContentPane(new GUIForm().container);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public GUIForm() {
        prepareMailTable();
        prepareWebView();
        receiver = new Client("wp.wreed@gmail.com", "12345", this);
        sender = new Sender("igory@gmail.com", this);
        send.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println("SEND CLICK!");
                //
                sender.send(subject.getText(), msgArea.getText(), new File("D:\\UTM\\pls.PNG"));
            }
        });
        refresh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println("REFRESH CLICK!");
                receiver.read();
            }
        });
        openAttach.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    JFileChooser fileChooser = new JFileChooser();
                    int result = fileChooser.showSaveDialog(container);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        System.out.println("SAVING TO: " + fileChooser.getSelectedFile().getAbsolutePath());
                        String ext = FilenameUtils.getExtension(attachment.getFileName());
                        attachment.saveFile(fileChooser.getSelectedFile().getAbsolutePath() + "." + ext);
                        Desktop.getDesktop().open(new File(fileChooser.getSelectedFile().getAbsolutePath() + "." + ext));
                    }
                } catch (Exception ex) {

                }
            }
        });
        delete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                receiver.deleteMessageByPos(table1.getSelectedRow());
            }
        });
    }

    private void prepareWebView() {
        final JFXPanel jfxPanel = new JFXPanel();
        webHolder.add(jfxPanel);
        Platform.runLater(() -> initFX(jfxPanel));
    }

    private void initFX(final JFXPanel fxPanel) {
        Group group = new Group();
        Scene scene = new Scene(group);
        fxPanel.setScene(scene);
        webView = new WebView();
        webView.setPrefSize(416, 340);
        group.getChildren().add(webView);
    }

    private void prepareMailTable() {
        DefaultTableModel tableModel = (DefaultTableModel) table1.getModel();
        tableModel.addColumn("Subject");
        tableModel.addColumn("From");
        tableModel.addColumn("Date");
        tableModel.addColumn("#att");
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table1.getSelectionModel().addListSelectionListener(e -> {
            if (table1.getSelectedRow() < 0) return;
            DefaultTableModel dm = (DefaultTableModel) table2.getModel();
            int rowCount = dm.getRowCount();
            for (int i = rowCount - 1; i >= 0; i--) {
                dm.removeRow(i);
            }
            Map<String, String> map = receiver.getHeadersByPos(table1.getSelectedRow());
            for (String key : map.keySet())
                dm.addRow(new String[]{key, map.get(key)});
            displayMessage(receiver.getMessageByPos(table1.getSelectedRow()));
        });
        tableModel = (DefaultTableModel) table2.getModel();
        tableModel.addColumn("Title");
        tableModel.addColumn("Value");
        table2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    public void clearTable() {
        DefaultTableModel dm = (DefaultTableModel) table1.getModel();
        int rowCount = dm.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            dm.removeRow(i);
        }
    }

    @Override
    public void addMsgToTable(String subject, String from, String date, String attachment) {
        System.out.println("TRYING TO ADD");
        DefaultTableModel tableModel = (DefaultTableModel) table1.getModel();
        tableModel.addRow(new String[]{subject, from, date, attachment});
        //tableModel.addRow(new String[]{subject,from,date, attachment?"+":"-"});
    }

    @Override
    public void displayMessage(final MimeMessage mimeMessage) {
        Platform.runLater(() -> {
            try {
                Multipart multipart = (Multipart) mimeMessage.getContent();
                webView.getEngine().loadContent(
                        multipart.getBodyPart(0).getContent().toString());
                if (multipart.getCount() > 1 && Part.ATTACHMENT.equalsIgnoreCase(multipart.getBodyPart(1).getDisposition())) {
                    attachment = (MimeBodyPart) multipart.getBodyPart(1);
                    openAttach.setVisible(true);
                } else {
                    openAttach.setVisible(false);
                    attachment = null;
                }
            } catch (Exception e) {
            }
        });
    }

    @Override
    public void clearSenderForm() {
        DefaultTableModel dm = (DefaultTableModel) table1.getModel();
        int rowCount = dm.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            dm.removeRow(i);
        }
    }

    @Override
    public void deleteMessage(int pos) {
        DefaultTableModel dm = (DefaultTableModel) table1.getModel();
        dm.removeRow(pos);
    }
}
