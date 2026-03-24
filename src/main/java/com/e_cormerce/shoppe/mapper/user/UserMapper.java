package com.e_cormerce.shoppe.mapper.user;

import com.e_cormerce.shoppe.dto.common.user.UserDto;
import com.e_cormerce.shoppe.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
  @Mapping(target = "role", ignore = true)
  UserDto toUserDTO(User user);
}
