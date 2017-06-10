package fr.monappeloffre.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.monappeloffre.app.domain.ProviderEligibility;

import fr.monappeloffre.app.repository.ProviderEligibilityRepository;
import fr.monappeloffre.app.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ProviderEligibility.
 */
@RestController
@RequestMapping("/api")
public class ProviderEligibilityResource {

    private final Logger log = LoggerFactory.getLogger(ProviderEligibilityResource.class);

    private static final String ENTITY_NAME = "providerEligibility";

    private final ProviderEligibilityRepository providerEligibilityRepository;

    public ProviderEligibilityResource(ProviderEligibilityRepository providerEligibilityRepository) {
        this.providerEligibilityRepository = providerEligibilityRepository;
    }

    /**
     * POST  /provider-eligibilities : Create a new providerEligibility.
     *
     * @param providerEligibility the providerEligibility to create
     * @return the ResponseEntity with status 201 (Created) and with body the new providerEligibility, or with status 400 (Bad Request) if the providerEligibility has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/provider-eligibilities")
    @Timed
    public ResponseEntity<ProviderEligibility> createProviderEligibility(@RequestBody ProviderEligibility providerEligibility) throws URISyntaxException {
        log.debug("REST request to save ProviderEligibility : {}", providerEligibility);
        if (providerEligibility.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new providerEligibility cannot already have an ID")).body(null);
        }
        ProviderEligibility result = providerEligibilityRepository.save(providerEligibility);
        return ResponseEntity.created(new URI("/api/provider-eligibilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /provider-eligibilities : Updates an existing providerEligibility.
     *
     * @param providerEligibility the providerEligibility to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated providerEligibility,
     * or with status 400 (Bad Request) if the providerEligibility is not valid,
     * or with status 500 (Internal Server Error) if the providerEligibility couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/provider-eligibilities")
    @Timed
    public ResponseEntity<ProviderEligibility> updateProviderEligibility(@RequestBody ProviderEligibility providerEligibility) throws URISyntaxException {
        log.debug("REST request to update ProviderEligibility : {}", providerEligibility);
        if (providerEligibility.getId() == null) {
            return createProviderEligibility(providerEligibility);
        }
        ProviderEligibility result = providerEligibilityRepository.save(providerEligibility);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, providerEligibility.getId().toString()))
            .body(result);
    }

    /**
     * GET  /provider-eligibilities : get all the providerEligibilities.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of providerEligibilities in body
     */
    @GetMapping("/provider-eligibilities")
    @Timed
    public List<ProviderEligibility> getAllProviderEligibilities() {
        log.debug("REST request to get all ProviderEligibilities");
        return providerEligibilityRepository.findAll();
    }

    /**
     * GET  /provider-eligibilities/:id : get the "id" providerEligibility.
     *
     * @param id the id of the providerEligibility to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the providerEligibility, or with status 404 (Not Found)
     */
    @GetMapping("/provider-eligibilities/{id}")
    @Timed
    public ResponseEntity<ProviderEligibility> getProviderEligibility(@PathVariable Long id) {
        log.debug("REST request to get ProviderEligibility : {}", id);
        ProviderEligibility providerEligibility = providerEligibilityRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(providerEligibility));
    }

    /**
     * DELETE  /provider-eligibilities/:id : delete the "id" providerEligibility.
     *
     * @param id the id of the providerEligibility to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/provider-eligibilities/{id}")
    @Timed
    public ResponseEntity<Void> deleteProviderEligibility(@PathVariable Long id) {
        log.debug("REST request to delete ProviderEligibility : {}", id);
        providerEligibilityRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
