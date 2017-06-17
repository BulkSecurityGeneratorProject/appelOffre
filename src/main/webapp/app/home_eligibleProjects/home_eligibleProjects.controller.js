(function() {
    'use strict';

    angular
        .module('monAppelOffreApp')
        .controller('home_eligibleProjectsController', home_eligibleProjectsController);

    home_eligibleProjectsController.$inject = ['$http', '$scope', 'Principal', 'LoginService', '$state', 'EligibleProject'];

    function home_eligibleProjectsController ($http, $scope, Principal, LoginService, $state, EligibleProject) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.photos = [];
        
        // Ajouter la liste des projets eligibles dans le model d'Angular. Par defaut liste vide
        vm.eligibleProjects = [];
        
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();
        
        getEligibleProjects();
        
        // Fonction qui ramene les projets elegible depuis le controller SpringMVC (ProjectResource), et appeler la methode /api/eligibleProjects
        // Le JSON retournee va etre affecte au model vm.eligibleProjects = [];
        function getEligibleProjects() {
        	EligibleProject.query(function(result) {
                vm.eligibleProjects = result;
                getAllPhotoForAllProject(result);
            });
        }
        
        function getAllPhotoForAllProject(projects)
        {
        	angular.forEach(projects, function (value, prop, obj) {
        		console.log("id project: " + value.id); // Todd, UK
        		getAllPhoto(value.id);
        	});
        }
        
        function getAllPhoto(idProject)
        {
        	if (idProject==0) {
				console.log("Pas de project");
			}
			else{
				console.log("recherche photo..");
				var objectToSend = new FormData();
		
				objectToSend.append("idProject", idProject);
				
				console.log("object envoye :",objectToSend);
		
				var req = $http.post('/api/get-photo',objectToSend, {

				transformRequest: angular.identity,
				headers: {
				'Content-Type': undefined,
				}
				
				}).success(function(data, status, headers, config) {
					console.log("Success");
					console.log("retour 2",data);
					angular.forEach(data, function (value, prop, obj) {
						vm.photos.push({idproject : value.projectPIC.id, link:value.link});
		            });
					console.log("array after push: ",vm.photos);
					return true;
				})
				.error(function(err) {
				console.log("ERROR", err);
					return false;
				});
			}
        }

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
      
        function register(){
        	$state.go('register');
        }
        
    }
})();