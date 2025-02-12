package com.example.animal_feed.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public UserDTO getUser(int id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails userDetails) {
            if (userDetails.getId() != id) {
                throw new AccessDeniedException("You are not authorized to access this resource");
            }
        } else {
            throw new AccessDeniedException("Invalid authentication principal");
        }
        Users user = userRepository.findById(id);
        return UsersMapper.INSTANCE.userToUserDTO(user);
    }

    public UsersDeactivateDTO deactivateUser(int id) {
        Users existingUser = userRepository.findById(id);
        if (existingUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        existingUser.setState("INACTIVE");
        userRepository.save(existingUser);
        return UsersMapper.INSTANCE.userToUserDeactivateDTO(existingUser);
    }

    public UsersDeactivateDTO activateUser(int id) {
        Users existingUser = userRepository.findById(id);
        if (existingUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        existingUser.setState("ACTIVE");
        userRepository.save(existingUser);
        return UsersMapper.INSTANCE.userToUserDeactivateDTO(existingUser);
    }
}
