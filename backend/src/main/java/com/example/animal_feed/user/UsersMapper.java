package com.example.animal_feed.user;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    UsersMapper INSTANCE = Mappers.getMapper(UsersMapper.class);

    UsersDTO usersToUsersDTO(Users user);
    Users usersDTOToUsers(UsersDTO userDTO);

    UserDTO userToUserDTO(Users user);
    Users userDTOToUser(UserDTO userDTO);

    UsersDeactivateDTO userToUserDeactivateDTO(Users user);
    Users userDeactivateDTOToUser(UsersDeactivateDTO userDTO);

    default Page<UsersDTO> usersToUsersDTOPage(Page<Users> users) {
        return users.map(this::usersToUsersDTO);
    }
    default Page<Users> usersDTOToUsersPage(Page<UsersDTO> usersDTO) {
        return usersDTO.map(this::usersDTOToUsers);
    }
}
