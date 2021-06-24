package changgou.service;

import java.io.IOException;

/**
 * @author mike ling
 * @description
 * @date 2021/6/24 9:56
 */
public interface IArticleService {

    void getArticleByHtmlHomePage(String homePage) throws IOException;

}
