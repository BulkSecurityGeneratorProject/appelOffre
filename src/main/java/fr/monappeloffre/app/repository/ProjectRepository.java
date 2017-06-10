package fr.monappeloffre.app.repository;

import fr.monappeloffre.app.domain.Project;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Project entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
    
}
