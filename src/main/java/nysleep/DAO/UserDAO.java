package nysleep.DAO;

import nysleep.model.Customer;
import nysleep.model.RegisteredUser;

public interface UserDAO {
    RegisteredUser register(RegisteredUser user);
    RegisteredUser authenticate(String username, String password);
    void modifyAccountInfo(RegisteredUser user, RegisteredUser modifiedUser);
}