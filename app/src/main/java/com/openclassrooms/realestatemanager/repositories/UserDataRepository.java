package com.openclassrooms.realestatemanager.repositories;

import com.openclassrooms.realestatemanager.database.dao.UserDao;
import com.openclassrooms.realestatemanager.models.User;

public class UserDataRepository {
    private final UserDao mUserDao;

    public UserDataRepository(UserDao userDao) {
        mUserDao = userDao;
    }
    // --- CREATE ---
    public long createUser(User user){
        return mUserDao.insertUser(user);
    }
}
