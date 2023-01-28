package it.unipi.lsmsd.nysleep.DAO;

import it.unipi.lsmsd.nysleep.model.RegisteredUser;

public interface UserDAO {
    void register(RegisteredUser user);
    void modifyAccountInfo(RegisteredUser oldUser, RegisteredUser newUser);
    void deleteAccount(RegisteredUser user);
}
