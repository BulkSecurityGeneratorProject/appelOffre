package fr.monappeloffre.app.repository;

import fr.monappeloffre.app.domain.Provider;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Provider entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProviderRepository extends JpaRepository<Provider,Long> {
    
}
