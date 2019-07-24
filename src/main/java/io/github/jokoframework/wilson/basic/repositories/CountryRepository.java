package io.github.jokoframework.wilson.basic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.jokoframework.wilson.basic.entities.CountryEntity;

/**
 * Created by danicricco on 2/25/18.
 */
public interface CountryRepository extends
        JpaRepository<CountryEntity, String> {
}


