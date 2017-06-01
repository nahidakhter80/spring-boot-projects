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
  .when("/dataLoad", {
     template: "Data Load Section"
  })
  .when("/xyz", {
     template: "xyz Section"
  })
  .otherwise("/allocation")
})

.service('navService', function (){
	var nav = {
		isAllocation: true,
		isExchange: false,
		isAdmin: false,
		isDataLoad: false
	}
	
	this.activate = function (tab) {
		nav.isAllocation = false;
		nav.isExchange = false;
		nav.isAdmin = false;
		nav.isDataLoad = false;
		nav.isXyz = false;
		
		if (tab == 'allocation') {
			nav.isAllocation = true;
		} else if (tab == 'exchange') {
			nav.isExchange = true;
		} else if (tab == 'dataLoad') {
			nav.isAdmin = true;
			nav.isDataLoad = true;
		} else if (tab == 'xyz') {
			nav.isAdmin = true;
			nav.isXyz = true;
		}		
		return nav;
	}
	
}) 

.controller('mainCtrl', function($scope, $location, navService){
	$scope.navigate = function(to){
		$scope.nav = navService.activate(to);
		$location.path(to );
	}
})


