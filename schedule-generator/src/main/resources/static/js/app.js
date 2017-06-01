var app = angular.module('myApp', []);
         
app.directive('fileModel', ['$parse', function ($parse) {
	return {
	   restrict: 'A',
	   link: function(scope, element, attrs) {
	      var model = $parse(attrs.fileModel);
	      var modelSetter = model.assign;
	      
	      element.bind('change', function(){
	             scope.$apply(function(){
	                modelSetter(scope, element[0].files[0]);
	             });
	          });
	       }
	   };
}]);
  
app.service('fileUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function(file, uploadUrl, isNumericSet, $scope){
       var fd = new FormData();
       fd.append('file', file);
       fd.append('isNumericSet', isNumericSet);
       $http.post(uploadUrl, fd, {
          transformRequest: angular.identity,
          headers: {'Content-Type': undefined}
       }).then(
		   function successCallback(response) {   
			   $scope.error = null;
			   
			   if (response.data.status) {
				   $scope.stbList = response.data.data;	   
			   } else {
				   $scope.error = response.data.data; 
			   }	
			  
		   }, 
		   function errorCallback(error) {
			   if (error.status == '404')
				   $scope.error = '404 - Server cannot be reached, please make sure jar is running.';
			  
		   }).finally(function(){
				$scope.showLoader = false;
		   });
        }    
    	
}]); 
  
app.controller('myCtrl', ['$scope', 'fileUpload', '$http', function($scope, fileUpload, $http){
	$scope.$watch('myFile', function() {
		 $scope.isInvalidFile = false;
		 $scope.stbList={};
		 $scope.error=null;
		 var file = $scope.myFile;
		 if (angular.isObject(file))
			 $scope.fileName = file.name;
    });
	
	$scope.showLoader;
	$scope.error;
	$scope.isExcelGenerated;
	$scope.stbList = {};
	
	$scope.isNumericSet = true;
	$scope.uploadFile = function(){
		$scope.error = null;
		var file = $scope.myFile; 
		var fileExtension = file.name.substring(file.name.lastIndexOf('.') + 1).toLowerCase();
		if (fileExtension.indexOf('xls') < 0 && fileExtension.indexOf('XLS') <0 &&
				fileExtension.indexOf('x') < 0 && fileExtension.indexOf('XLSX') <0 ) {
			$scope.isInvalidFile = true;
			return;
		}
		$scope.isInvalidFile = false;
		$scope.showLoader = true;
		$scope.isExcelGenerated = false;
		$scope.stbList ={};
       
		var uploadUrl = "uploadperforma";
		
		fileUpload.uploadFileToUrl(file, uploadUrl, $scope.isNumericSet, $scope);
    };
    
    $scope.updateEtyRemark = function(){
    	$scope.excelLoader = true;
    	return $http({
			method  : 'POST',
			url     : 'generateExcel',
			data    : $scope.stbList
		}).then(
		function successCallback(response) {

			if (response.data.status) {
				$scope.isExcelGenerated=response.data.status;			   
			} else {
				if (response.data.data == null) {
					$scope.error = 'Something wrong went on server. Please contact rechnical team.';
				} else {
					$scope.error = response.data.data; 
				}				
			}	
		}, 
		function errorCallback(error) {
			$scope.error = error.status + " - " + error.statusText;
		}).finally(function(){
			$scope.excelLoader = false;
		});
    }
 }]); 

