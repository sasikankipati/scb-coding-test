package com.user.rest;

import com.user.common.UserTestSupport;
import com.user.common.constants.AppConstants;
import com.user.entity.User;
import com.user.exception.UserNotFoundException;
import com.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    @Test
    public void testGetUserByIdOrAllWithIdInput() {
        Mockito.when(userService.getUserByIdOrAll(1)).thenReturn(UserTestSupport.prepareTestUser());
        User user = (User) userController.getUserByIdOrAll("dummyAuth", 1).getBody();
        Assertions.assertEquals(1, user.getUserId());
    }

    @Test
    public void testGetUserByIdOrAllWithoutIdInput() {
        Mockito.when(userService.getUserByIdOrAll(null)).thenReturn(UserTestSupport.prepareTestUsers());
        List<User> users = (List<User>) userController.getUserByIdOrAll("dummyAuth", null).getBody();
        Assertions.assertEquals(2, users.size());
    }

    @Test
    public void testGetUserByIdOrAllWithInvalidIdInput() {
        Mockito.when(userService.getUserByIdOrAll(1)).thenThrow(new UserNotFoundException("1"));
        Exception exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            userController.getUserByIdOrAll("dummyAuth", 1).getBody();
        });
        Assertions.assertEquals("User not found with Id : 1", exception.getMessage());
    }

    @Test
    public void testSaveUserByValidInputs() {
        Mockito.doNothing().when(userService).saveUser(1, "Name", "City");
        String saveResponse = userController.createUser("dummyAuth", 1, "Name", "City").getBody();
        Assertions.assertEquals(AppConstants.SUCCESS, saveResponse);
    }

    @Test
    public void testUpdateUserByIdWithName() {
        Mockito.doNothing().when(userService).updateUserById(1, "Name", null);
        String saveResponse = userController.updateUser("dummyAuth", 1, "Name", null).getBody();
        Assertions.assertEquals(AppConstants.SUCCESS, saveResponse);
    }

    @Test
    public void testUpdateUserByIdWithCity() {
        Mockito.doNothing().when(userService).updateUserById(1, null, "City");
        String saveResponse = userController.updateUser("dummyAuth", 1, null, "City").getBody();
        Assertions.assertEquals(AppConstants.SUCCESS, saveResponse);
    }

    @Test
    public void testDeleteUser() {
        Mockito.doNothing().when(userService).deleteUserById(1);
        String saveResponse = userController.deleteUser("dummyAuth", 1).getBody();
        Assertions.assertEquals(AppConstants.SUCCESS, saveResponse);
    }

}
