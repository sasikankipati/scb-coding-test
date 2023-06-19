package com.user.service;

import com.user.common.UserTestSupport;
import com.user.exception.UserAlreadyExistsException;
import com.user.entity.User;
import com.user.exception.UserNotFoundException;
import com.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    public void testGetUserByIdOrAllWithIdInput() {
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(UserTestSupport.prepareTestUser()));
        User user = (User) userService.getUserByIdOrAll(1);
        Assertions.assertEquals(1, user.getUserId());
    }

    @Test
    public void testGetUserByIdOrAllWithoutIdInput() {
        Mockito.when(userRepository.findAll()).thenReturn(UserTestSupport.prepareTestUsers());
        List<User> users = (List<User>) userService.getUserByIdOrAll(null);
        Assertions.assertEquals(2, users.size());
    }

    @Test
    public void testGetUserByIdOrAllWithInvalidIdInput() {
        Mockito.when(userRepository.findById(1)).thenThrow(new UserNotFoundException("1"));
        Exception exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.getUserByIdOrAll(1);
        });
        Assertions.assertEquals("User not found with Id : 1", exception.getMessage());
    }

    @Test
    public void testSaveUser() {
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.ofNullable(null));
        Mockito.when(userRepository.save(UserTestSupport.prepareTestUser())).thenReturn(UserTestSupport.prepareTestUser());
        userService.saveUser(1, "Name", "City");
        Mockito.verify(userRepository, Mockito.times(1)).save(UserTestSupport.prepareTestUser());
    }

    @Test
    public void testSaveUserWithExistingId() {
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(UserTestSupport.prepareTestUser()));
        Exception exception = Assertions.assertThrows(UserAlreadyExistsException.class, () -> {
            userService.saveUser(1, "Name", "City");
        });
        Assertions.assertEquals("User already exists with id : 1", exception.getMessage());
    }

    @Test
    public void testUpdateUserByIdWithName() {
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(UserTestSupport.prepareTestUser()));
        Mockito.when(userRepository.save(UserTestSupport.prepareTestUser())).thenReturn(UserTestSupport.prepareTestUser());
        userService.updateUserById(1, "Name", null);
        Mockito.verify(userRepository, Mockito.times(1)).save(UserTestSupport.prepareTestUser());
    }

    @Test
    public void testUpdateUserByIdWithCity() {
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(UserTestSupport.prepareTestUser()));
        Mockito.when(userRepository.save(UserTestSupport.prepareTestUser())).thenReturn(UserTestSupport.prepareTestUser());
        userService.updateUserById(1, null, "City");
        Mockito.verify(userRepository, Mockito.times(1)).save(UserTestSupport.prepareTestUser());
    }

    @Test
    public void testUpdateUserByInvalidId() {
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.ofNullable(null));
        Exception exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.updateUserById(1, "Name", null);
        });
        Assertions.assertEquals("User not found with Id : 1", exception.getMessage());
    }

    @Test
    public void testDeleteUserByValidId() {
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(UserTestSupport.prepareTestUser()));
        Mockito.doNothing().when(userRepository).delete(UserTestSupport.prepareTestUser());
        userService.deleteUserById(1);
        Mockito.verify(userRepository, Mockito.times(1)).delete(UserTestSupport.prepareTestUser());
    }

    @Test
    public void testDeleteUserByInvalidId() {
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.ofNullable(null));
        Exception exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUserById(1);
        });
        Assertions.assertEquals("User not found with Id : 1", exception.getMessage());
    }

}