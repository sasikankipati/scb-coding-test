package com.user.common;

import com.user.entity.User;

import java.util.List;

public interface UserTestSupport {

    static User prepareTestUser() {
        return new User(1, "Name", "City");
    }

    static List<User> prepareTestUsers() {
        return List.of(new User(1, "Name1", "City1"), new User(2, "Name2", "City2"));
    }
}
