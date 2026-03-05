package com.matrimony.auth.mapper;

import com.matrimony.auth.dto.UserDto;
import com.matrimony.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {
    UserDto toResponse(User entity);

    User toEntity(UserDto dto);
}