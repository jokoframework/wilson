package io.github.jokoframework.wilson.profile.manager;

import io.github.jokoframework.wilson.exceptions.UserException;
import io.github.jokoframework.wilson.web.dto.UserAuthDTO;
import io.github.jokoframework.wilson.profile.dto.UserDTO;

import java.util.List;

public interface UserManager {
    
    /**
     * Get user list dto
     *
     * @return user list
     */
    List<UserDTO> findAll();
    
    
    /**
     * Get user by username
     *
     * @param username user identifier
     * @return user json format
     * @throws UserException
     */
    UserDTO getByUsername(String username) throws UserException;
    
    /**
     * Verify user password
     *
     * @param username user name
     * @param password password
     * @return verification
     */
    boolean checkEndUser(String username, String password);

    /**
     * Check user and return UserAuthDTO
     * @param username
     * @param password
     * @return
     * @throws UserException
     */
    UserAuthDTO checkUser(String username, String password) throws UserException;
    
    /**
     * 
     * @return byte[]
     */
    
    /**
     * Creates a csv file with all existing users
     * 
     * @param users, you must provide the list of users
     * that you want to export
     * @param columns, you must provide the columns that
     * you want to appear in the file
     * @return byte[]
     */
    byte[] exportUsersListToCsv(List<UserDTO> users, List<String> columns);
    
}
