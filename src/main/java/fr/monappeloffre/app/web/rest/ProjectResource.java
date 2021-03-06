package fr.monappeloffre.app.web.rest;

import com.codahale.metrics.annotation.Timed;

import fr.monappeloffre.app.domain.Activity;
import fr.monappeloffre.app.domain.Customer;
import fr.monappeloffre.app.domain.Project;
import fr.monappeloffre.app.domain.ProjectActivity;
import fr.monappeloffre.app.domain.ProjectPic;
import fr.monappeloffre.app.domain.Provider;
import fr.monappeloffre.app.domain.ProviderActivity;
import fr.monappeloffre.app.domain.User;
import fr.monappeloffre.app.repository.ActivityRepository;
import fr.monappeloffre.app.repository.CustomerRepository;
import fr.monappeloffre.app.repository.ProjectActivityRepository;
import fr.monappeloffre.app.repository.ProjectPicRepository;
import fr.monappeloffre.app.repository.ProjectRepository;
import fr.monappeloffre.app.repository.ProviderRepository;
import fr.monappeloffre.app.repository.UserRepository;
import fr.monappeloffre.app.security.SecurityUtils;
import fr.monappeloffre.app.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing Project.
 */
@RestController
@RequestMapping("/api")
public class ProjectResource {

	private final Logger log = LoggerFactory.getLogger(ProjectResource.class);

	private static final String ENTITY_NAME = "project";

	private final ProjectRepository projectRepository;

	public ProjectResource(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	@Autowired
	ProviderRepository providerRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	ProjectPicRepository projectPicRepository;
	@Autowired
	ActivityRepository activityRepository;
	@Autowired
	ProjectActivityRepository projectActivityRepository;

	/**
	 * POST  /projects : Create a new project.
	 *
	 * @param project the project to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new project, or with status 400 (Bad Request) if the project has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/projects")
	@Timed
	public ResponseEntity<Project> createProject(@RequestBody Project project) throws URISyntaxException {
		log.debug("REST request to save Project : {}", project);
		if (project.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new project cannot already have an ID")).body(null);
		}
		//Date du jour du système
		project.setDateSend(LocalDate.now());

		Project result = projectRepository.save(project);
		return ResponseEntity.created(new URI("/api/projects/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /projects : Updates an existing project.
	 *
	 * @param project the project to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated project,
	 * or with status 400 (Bad Request) if the project is not valid,
	 * or with status 500 (Internal Server Error) if the project couldn't be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/projects")
	@Timed
	public ResponseEntity<Project> updateProject(@RequestBody Project project) throws URISyntaxException {
		log.debug("REST request to update Project : {}", project);
		if (project.getId() == null) {
			return createProject(project);
		}
		Project result = projectRepository.save(project);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, project.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /projects : get all the projects.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of projects in body
	 */
	@GetMapping("/projects")
	@Timed
	public List<Project> getAllProjects() {
		log.debug("REST request to get all Projects");
		return projectRepository.findAll();
	}

	/**
	 * GET  /projects/:id : get the "id" project.
	 *
	 * @param id the id of the project to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the project, or with status 404 (Not Found)
	 */
	@GetMapping("/projects/{id}")
	@Timed
	public ResponseEntity<Project> getProject(@PathVariable Long id) {
		log.debug("REST request to get Project : {}", id);
		Project project = projectRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(project));
	}

	/**
	 * DELETE  /projects/:id : delete the "id" project.
	 *
	 * @param id the id of the project to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/projects/{id}")
	@Timed
	public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
		log.debug("REST request to delete Project : {}", id);
		projectRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}
	@GetMapping("/eligiblePojects")
	@Timed
	@Transactional
	public List<Project> getEligibleProjects() {
		log.debug("REST request to get eligible Projects");
		List<Activity> activities = new ArrayList<>();

		Optional<User> optional = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
		Long idUser = optional.isPresent() ? optional.get().getId() : 0l;


		log.debug("id User logged : "+idUser);
		Optional<Provider> optionalProvider = providerRepository.findByidUser(idUser);
		if (optionalProvider.isPresent())
		{
			Provider provider = optionalProvider.get();

			// parcourir les providerActivity 
			for(ProviderActivity providerActivity : provider.getProviderativityPROVIDERS()){
				activities.add(providerActivity.getActivityProvider());
			}
		}


		return projectRepository.findByProjectactivityPROJECTS_ActivityProjectIn(activities);
	}

	//Obtenir tous mes projets (Customer connecté)
	@GetMapping("/myProjects")
	@Timed
	@Transactional
	public List<Project> getmyProjects() {
		log.debug("REST request to get all my Projects (customer)");
		
		Optional<User> optional = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
		Long idUser = optional.isPresent() ? optional.get().getId() : 0l;

		log.debug("id User logged : "+idUser);
		Optional<Customer> optionalCustomer = customerRepository.findByidUser(idUser);
		Customer customer = optionalCustomer.isPresent() ? optionalCustomer.get() : null;

		return projectRepository.findBycustomerP(customer);
	}
	
	@PostMapping("/create-new-project")
	@Timed
	public ResponseEntity<Project> createProject(@RequestParam("images") MultipartFile[] picFile,
			@RequestParam("activities") List<Long> idActivity,
			@RequestParam("description") String description,
			@RequestParam("title") String title
			) throws URISyntaxException { 	
		log.debug("REST request to save Project : {}" + title + " " + description);
		
		Project project = new Project();
		
		Optional<User> optional = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
		Long idUser = optional.isPresent() ? optional.get().getId() : 0l;

		log.debug("id User logged : "+idUser);
		Optional<Customer> optionalCustomer = customerRepository.findByidUser(idUser);
		Customer customer = optionalCustomer.isPresent() ? optionalCustomer.get() : null;
		
		//Date du jour du système
		project.setDateSend(LocalDate.now());
		project.setDescription(description);
		project.setTitle(title);
		project.setCity(customer.getCity());
		project.setComplementStreet(customer.getComplementStreet());
		project.setCustomerP(customer);
		project.setPostalCode(customer.getPostalCode());
		project.setStreet(customer.getStreet());
		project.setStreetNumber(customer.getStreetNumber());
		
		Project result = projectRepository.save(project);
		
		//Ajouter des photos au projet
		for(MultipartFile file : picFile){
			ProjectPic photo = new ProjectPic();
			try {
				File destinationFichier = new File(System.getProperty("user.dir")+"/src/main/webapp/content/images/"+file.getOriginalFilename());
				log.info("Dir to save: "+destinationFichier);
				file.transferTo(destinationFichier);
				photo.setLink("content/images/" + file.getOriginalFilename());
				photo.setProjectPIC(result);
				projectPicRepository.save(photo);
			} catch (Exception ex) {
				log.error("Failed to upload", ex);
			}
		}
		
		//Ajouter des activitées au projet
		for(Long idActivitee : idActivity){
			Activity activity = activityRepository.findOne(idActivitee);
			ProjectActivity projectActivity = new ProjectActivity();
			projectActivity.setActivityProject(activity);
			projectActivity.setProjectACTIVITY(result);
			projectActivityRepository.save(projectActivity);
		}
		
		
		return ResponseEntity.created(new URI("/api/create-new-project/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}
}
