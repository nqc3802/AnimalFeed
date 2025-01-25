package com.example.animal_feed.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Cacheable(value = "users", key = "#page + '-' + #size")
    public Page<UsersDTO> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Users> users = userRepository.findAll(pageable);
        return UsersMapper.INSTANCE.usersToUsersDTOPage(users);
    }

    @Cacheable(value = "user", key = "#id") 
    public UserDTO getUser(int id) {
        Users user = userRepository.findById(id);
        return UsersMapper.INSTANCE.userToUserDTO(user);
    }
}
