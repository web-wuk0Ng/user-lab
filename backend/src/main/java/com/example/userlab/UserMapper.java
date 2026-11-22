package com.example.userlab;
import java.util.List;
import org.apache.ibatis.annotations.*;
@Mapper
public interface UserMapper {
@Select("SELECT id,name,email FROM users ORDER BY id")
List<User> findAll();
@Select("SELECT id,name,email FROM users WHERE id=#{id}")
User findById(@Param("id") Long id);
@Insert("INSERT INTO users(name,email) VALUES(#{name},#{email})")
@Options(useGeneratedKeys=true, keyProperty="id")
int insert(User user);
@Update("UPDATE users SET name=#{name},email=#{email} WHERE id=#{id}")
int update(User user);
@Delete("DELETE FROM users WHERE id=#{id}")
int delete(@Param("id") Long id);
}
