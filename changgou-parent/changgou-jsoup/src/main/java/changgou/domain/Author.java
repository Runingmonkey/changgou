package changgou.domain;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author mike ling
 * @description
 * @date 2021/6/24 10:29
 */
@Data
public class Author implements Serializable {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

}
