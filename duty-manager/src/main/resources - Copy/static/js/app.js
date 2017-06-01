angular.module('app',['ngRoute'])
.config(function ($routeProvider){
  $routeProvider
  .when("/allocation", {
     templateUrl: "html/allocation.html",
     controller: "allocation"
  })  
  .when("/exchange", {
     templateUrl: "html/exchange.html"
  })
  .otherwise("/allocation")
})

.service('navService', function (){
	var nav = {
		isAllocation: true,
		isExchange: false,
		isAdmin:false
	}
	
	this.activate = function (tab) {
		nav.isAllocation = false;
		nav.isExchange = false;
		nav.isAdmin = false;
		
		if (tab == 'allocation') {
			nav.isAllocation = true;
		} else if (tab == 'exchange') {
			nav.isExchange = true;
		} else if (tab == 'admin') {
			nav.isAdmin = true;
		}	
		return nav;
	}
	
}) 

.controller('navController', function($scope, $location, navService){
	$scope.navigate = function(to){
		$scope.nav = navService.activate(to);
		$location.path(to);
	}
})

.controller('allocation', function($rootScope, $scope, $location){
	if ($rootScope.nav == null) {
		$rootScope.nav = {isAllocation: true};
	}
	$scope.date = new Date();;
})


