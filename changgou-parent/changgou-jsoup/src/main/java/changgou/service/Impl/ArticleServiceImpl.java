package changgou.service.Impl;

import changgou.dao.AuthorMapper;
import changgou.domain.ArticleInfo;
import changgou.domain.Author;
import changgou.service.IArticleService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.LocalDateTime;

/**
 * @author mike ling
 * @description
 * @date 2021/6/24 9:56
 */
@Service
public class ArticleServiceImpl implements IArticleService {

    @Autowired
    private AuthorMapper authorMapper;

    /**
     *  根据html
     * @param homePage
     */
    @Override
    public void getArticleByHtmlHomePage(String articlePage) throws IOException {
        // 文章页面
        articlePage = "http://www.177pic.info/html/2021/05/4345627.html/1/";
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 13274);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, inetSocketAddress);
        Connection proxyConnection = Jsoup.connect(articlePage).proxy(proxy);
        Document document = proxyConnection.get();

        // 获取文章标题  [作者名称] wenzhang[223P] - 第23页 | 177
        Elements titleElement = document.getElementsByTag("title");
        String title = titleElement.text();

        // 查作者; 不在就创建文件夹
        ArticleInfo articleInfo = new ArticleInfo();
        getComicInfo(title, articleInfo);

        String filePath = "F://comic//greenPan";

        // 文章内容
        Elements contentElement = document.getElementsByClass("single-content");
        Elements picListElement = contentElement.get(0).select("[data-lazy-src]");
//        startPageDownload(proxy, picListElement, null);

    }


    private static Integer startPageDownload(Proxy proxy, Elements picListElement, Integer usedIndex) throws IOException {
        for (int i = 1; i <= picListElement.size(); i++) {
            Element imgElement = picListElement.get(i);
            String attr = imgElement.attr("data-lazy-src");
            System.out.println(attr);
            Connection imgConnect = Jsoup.connect(attr);
            Connection.Response response = imgConnect.method(Connection.Method.GET).proxy(proxy).ignoreContentType(true).timeout(10*1000).execute();
            BufferedInputStream bufferedInputStream = response.bodyStream();
            System.out.println(response.contentType());
            usedIndex += i;
            saveFile(bufferedInputStream, "F://comic//greenPan//" + usedIndex + ".jpg");
        }
        return usedIndex;
    }

    /**
     *
     * @param bufferedInputStream
     * @param filePath 保存文件地址
     * @param fileName 文件名
     */
    public static void saveFile(BufferedInputStream bufferedInputStream, String  filePath) {
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
     *  获取文章信息, 返回存储地址
     * @param title
     */
    public String getComicInfo(String title, ArticleInfo articleInfo) {
        String[] strList = title.split(" ");
        // 作者名
        String authorName = strList[0].replace("[", "").replace("]", "");

        // 文章名
        String[] articleStrings = strList[1].split("\\[");
        String articleName = articleStrings[0];

        // 页数
        Integer totalPic = Integer.valueOf(articleStrings[1].replace("P]", ""));

        String authorFilePath = "";
        // 根据名称查找作者信息, 没有则新增, 并相应创建文件夹
        Author author = authorMapper.findAuthorByName(authorName);
        if (author != null) {

        } else {
            // 生成作者文章 存储文件夹
            Author insertAuthor = new Author();
            authorMapper.insert(insertAuthor);
        }
        articleInfo.setAuthorId(author.getId());
        articleInfo.setTitle(articleName);
        articleInfo.setPage(totalPic);
        articleInfo.setOriginal("177pic");
        articleInfo.setCreated(LocalDateTime.now());

        return authorFilePath;
    }


}
