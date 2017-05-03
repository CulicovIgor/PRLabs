import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class Main {
    public static void main(String[] args) {
        String link = "curs.md";
        String keyword = "dol";
        int lvl = 2;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter http link: ");
            link = br.readLine();

            System.out.print("Enter keyword: ");
            keyword = br.readLine();
            System.out.print("Enter level: ");
            lvl = Integer.parseInt(br.readLine());
        }catch (Exception e){
            e.getStackTrace();
        }
        Crawler crawler = new Crawler();
        crawler.processLinks("http://" + link, lvl, keyword);
        crawler.printLink();
    }
}
