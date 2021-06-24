package changgou.controller;

import changgou.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author mike ling
 * @description
 * @date 2021/6/24 9:51
 */
@RestController
public class Article177Controller {

    @Autowired
    private IArticleService articleService;

    @GetMapping("/article")
    public void getArticleByHtmlHomePage(@RequestParam(name = "homePage") String homePage) throws IOException {
        articleService.getArticleByHtmlHomePage(homePage);
    }

}
