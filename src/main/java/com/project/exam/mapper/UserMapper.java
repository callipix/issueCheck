package com.project.exam.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.project.exam.dto.SignUpRequest;
import com.project.exam.dto.Users;

@Mapper
public interface UserMapper {

	boolean existsByLoginId(@Param("loginId")String loginId);
	boolean existsByNickName(@Param("nickname") String nickName);
	boolean existsByEmail(@Param("email") String email);
	int insert(Users users);

}
