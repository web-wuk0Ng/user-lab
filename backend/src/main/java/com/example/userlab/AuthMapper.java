package com.example.userlab;
import java.util.List;
import org.apache.ibatis.annotations.*;
@Mapper
public interface AuthMapper {
@Select("SELECT id,username,password_hash AS passwordHash,enabled FROM auth_user WHERE username=#{name}")
AuthAccount find(@Param("name") String name);
@Select("SELECT DISTINCT p.code FROM auth_user u JOIN auth_user_role ur ON ur.user_id=u.id JOIN role_permission rp ON rp.role_id=ur.role_id JOIN permission p ON p.id=rp.permission_id WHERE u.username=#{name} AND u.enabled=1")
List<String> permissions(@Param("name") String name);
}
