package io.github.jokoframework.wilson.profile.repositories;

import io.github.jokoframework.wilson.profile.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * User Data Access
 *
 * @author bsandoval
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
    UserEntity getByUsername(String username);
    
    UserEntity getByPersonId(Long personId);
    
}
