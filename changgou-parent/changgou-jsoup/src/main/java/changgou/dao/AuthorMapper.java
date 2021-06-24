package changgou.dao;

import changgou.domain.Author;

import java.util.List;

/**
 * @author mike ling
 * @description
 * @date 2021/6/24 10:29
 */
public interface AuthorMapper extends Mapper<Author> {

    Author findAuthorByName(String name);

}

