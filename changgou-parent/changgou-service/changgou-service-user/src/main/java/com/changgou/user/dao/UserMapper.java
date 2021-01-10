package com.changgou.user.dao;
import com.changgou.user.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/****
 * @Author:itheima
 * @Description:User的Dao
 *****/
public interface UserMapper extends Mapper<User> {

    @Update("update tb_user set points = points + #{point} where username = #{username}")
    int addUserPoint(@Param("username") String username, @Param("point") Integer point);

}
