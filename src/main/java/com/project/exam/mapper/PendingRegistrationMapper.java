package com.project.exam.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.project.exam.dto.PendingRegistration;

@Mapper
public interface PendingRegistrationMapper {

	int insert(PendingRegistration registration);
	int deletePendingRegistration(@Param("tokenHash")String tokenHash);
	PendingRegistration findByTokenHash(@Param("tokenHash") String tokenHash);

}
