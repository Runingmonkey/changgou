package changgou.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author mike ling
 * @description 文章信息
 * @date 2021/6/24 9:33
 */
@Data
public class ArticleInfo implements Serializable {

    private Integer id;

    private Integer authorId;

    private String title;

    private Integer page;

    private String original;

    private LocalDateTime created;

}
