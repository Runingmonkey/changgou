package changgou.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author mike ling
 * @description 文章内容
 * @date 2021/6/24 9:37
 */
@Data
public class ArticleContent implements Serializable {

    private Integer id;

    private Integer articleId;

    private Integer articlePage;

    private Integer articleOriginal;

    private Integer originalUrl;

    private Integer status;

    private LocalDateTime created;

}
