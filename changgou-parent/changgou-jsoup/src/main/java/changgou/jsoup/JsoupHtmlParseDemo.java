package changgou.jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @author mike ling
 * @description
 * @date 2021/6/18 11:32
 */
public class JsoupHtmlParseDemo {

    public static void main(String[] args) throws IOException {

        // 177: http://www.177pic.info/html/2021/05/4345627.html/23/
        // 京东: https://item.jd.com/10031111011718.html

        // 文章页面
        String articlePage = "http://www.177pic.info/html/2021/05/4345627.html/1/";
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 13274);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, inetSocketAddress);
        Connection proxyConnection = Jsoup.connect(articlePage).proxy(proxy);
        Document document = proxyConnection.get();

        // 获取文章标题  [作者名称] wenzhang[223P] - 第23页 | 177
        Elements titleElement = document.getElementsByTag("title");
        String title = titleElement.text();
        String filePath = "F://comic//greenPan";

        // 创建文件夹 作者 / 作品名称



        // 文章内容
//        Elements contentElement = document.getElementsByClass("single-content");
//        Elements picListElement = contentElement.get(0).select("[data-lazy-src]");
//        startPageDownload(proxy, picListElement);
    }

    private static void startPageDownload(Proxy proxy, Elements picListElement) throws IOException {
        for (int i = 0; i < picListElement.size(); i++) {
            Element imgElement = picListElement.get(i);
            String attr = imgElement.attr("data-lazy-src");
            System.out.println(attr);
            Connection imgConnect = Jsoup.connect(attr);
            Connection.Response response = imgConnect.method(Connection.Method.GET).proxy(proxy).ignoreContentType(true).timeout(10*1000).execute();
            BufferedInputStream bufferedInputStream = response.bodyStream();
            System.out.println(response.contentType());
            saveFile(bufferedInputStream, "F://comic//greenPan//" + i + ".jpg");
        }
    }

    /**
     *
     * @param bufferedInputStream
     * @param filePath 保存文件地址
     * @param fileName 文件名
     */
    public static void saveFile(BufferedInputStream bufferedInputStream,String  filePath) {
        try {
            byte[] buffer = new byte[1024];
            int readLength;
            File file = new File(filePath);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fos);
            while ((readLength = bufferedInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, readLength);
            }
            bufferedOutputStream.close();
            fos.close();
            bufferedInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  获取文章信息
     * @param title
     */
    public void getComicInfo(String title) {
        String[] strList = title.split(" ");
        // 作者名
        String authorName = strList[0].replace("[", "").replace("]", "");

        // 文章名
        String[] aritcleStrings = strList[1].split("\\[");
        String aricleName = aritcleStrings[0];

        // 页数
        Integer totalPic = Integer.valueOf(aritcleStrings[1].replace("P]", ""));
    }


}
