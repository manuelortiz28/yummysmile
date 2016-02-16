package com.visiontech.yummysmile.models.transformers;

import com.google.common.base.Function;
import com.visiontech.yummysmile.models.User;
import com.visiontech.yummysmile.repository.api.dto.UserDTO;

/**
 * @author manuel.ortiz
 *
 * Transformer class to convert between different types of objects related to Users
 *
 */
public final class UserTransform {

    private UserTransform() {
    }

    private static Function<UserDTO, User> transformUserDtoToUser = new Function<UserDTO, User>() {
        public User apply(UserDTO userDTO) {

            if (userDTO == null) {
                return null;
            }

            User newUser = new User();
            newUser.setId(userDTO.getId());
            newUser.setName(userDTO.getName());
            newUser.setLastName(userDTO.getLastName());
            newUser.setEmail(userDTO.getEmail());
            newUser.setToken(userDTO.getToken());

            return newUser;
        }
    };

    private static Function<User, UserDTO> transformUserToUserDTO = new Function<User, UserDTO>() {
        @Override
        public UserDTO apply(User user) {
            if (user == null) {
                return null;
            }

            UserDTO newUserDTO = new UserDTO();
            newUserDTO.setId(user.getId());
            newUserDTO.setName(user.getName());
            newUserDTO.setLastName(user.getLastName());
            newUserDTO.setEmail(user.getEmail());
            newUserDTO.setToken(user.getToken());

            return newUserDTO;
        }
    };

    /**
     * Function to transform a UserDTO object into a User
     */
    public static Function<UserDTO, User> getTransformUserDtoToUser() {
        return transformUserDtoToUser;
    }

    /**
     * Function to transform a User object into a UserDTO
     */
    public static Function<User, UserDTO> getTransformUserToUserDTO() {
        return transformUserToUserDTO;
    }
}
