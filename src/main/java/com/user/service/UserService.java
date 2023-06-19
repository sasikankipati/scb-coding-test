package com.user.service;

import com.user.exception.UserAlreadyExistsException;
import com.user.entity.User;
import com.user.exception.UserNotFoundException;
import com.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Function to find User by userId
     * @param userId - Id related to the existing user
     * @return - User details related to @param userId
     */
    private User findUserById(Integer userId){
        log.info("UserService > findUserById > Triggered [userId : {}]", userId);
        return userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException(String.valueOf(userId)));
    }

    /**
     * Function get User details by Id
     * If @param userId present then pull the specific User else get all Users
     * @param userId - Id related to the existing user
     * @return - User details related to @param userId or All Users
     * @param <T> - User or List<User>
     */
    public <T> T getUserByIdOrAll(final Integer userId) {
        log.info("UserService > getUserByIdOrAll > Start [userId : {}]", userId);
        T userDeatils;
        if(null != userId) {
            userDeatils = (T) findUserById(userId);
        } else {
            userDeatils = (T) userRepository.findAll();
        }
        log.info("UserService > getUserByIdOrAll > End");
        return userDeatils;
    }

    /**
     * Function to save User
     * @param userId - Id of User
     * @param name - Name of User
     * @param city - City of User
     */
    @Transactional
    public void saveUser(Integer userId, String name, String city){
        log.info("UserService > saveUser > Start [userId : {}]", userId);
        userRepository.findById(userId).ifPresent(user -> {throw new UserAlreadyExistsException(String.valueOf(userId));});
        userRepository.save(new User(userId, name, city));
        log.info("UserService > saveUser > End");
    }

    /**
     * Function to update User
     * @param userId - Id of User
     * @param name - New Name of User
     * @param city - New City of User
     */
    @Transactional
    public void updateUserById(Integer userId, String name, String city){
        log.info("UserService > updateUserById > Start [userId : {}]", userId);
        User userToUpdate = findUserById(userId);
        Optional.ofNullable(name).ifPresent(userToUpdate::setName);
        Optional.ofNullable(city).ifPresent(userToUpdate::setCity);
        userRepository.save(userToUpdate);
        log.info("UserService > updateUserById > End");
    }

    /**
     * Function to delete user
     * @param userId - Id of User to be deleted
     */
    @Transactional
    public void deleteUserById(Integer userId){
        log.info("UserService > deleteUserById > Start [userId : {}]", userId);
        User userToDelete = findUserById(userId);
        userRepository.delete(userToDelete);
        log.info("UserService > deleteUserById > End");
    }
}