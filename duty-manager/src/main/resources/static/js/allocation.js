angular.module('app')
.controller('allocation', function($rootScope, $scope, $location){
	if ($rootScope.nav == null) {
		$rootScope.nav = {isAllocation: true};
	}
	$scope.date = new Date();;
})

