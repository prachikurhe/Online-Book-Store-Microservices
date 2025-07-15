package com.codewithsachin.auth.mapper;

import com.codewithsachin.auth.dto.RegisterRequest;
import com.codewithsachin.auth.dto.UserResponse;
import com.codewithsachin.auth.entity.UserEntity;
import org.mapstruct.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    default UserEntity toUser(RegisterRequest dto, PasswordEncoder encoder) {
        if (dto == null) return null;

        return UserEntity.builder()
                .username(dto.getUsername())
                .password(encoder.encode(dto.getPassword()))  // ✅ Encode inline
                .name(dto.getName())
                .build();
    }
    @Mapping(target = "roles", expression = "java(user.getRoles().stream()" +
            ".map(com.codewithsachin.auth.entity.RoleEntity::getName)" +
            ".collect(java.util.stream.Collectors.toSet()))")
    UserResponse toResponse(UserEntity user);
}
