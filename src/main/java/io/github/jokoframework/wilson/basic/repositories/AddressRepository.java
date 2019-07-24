package io.github.jokoframework.wilson.basic.repositories;

import io.github.jokoframework.wilson.basic.entities.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Address Data Access
 *
 * @author bsandoval
 */
@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

}
