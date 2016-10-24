angular.module('allin', []).controller('files', function($scope,$http,$sce) {
    
	console.log("Working!!");

	$scope.searchUrl = "api/search/";
	$scope.pv = "";

	$scope.loadFromSolr = function(){
		//$scope.pv="";
		var q = $scope.query;
		if(q == null || q != "")
			q = "*:*"
		$http.get($scope.searchUrl+$scope.query)
			.success(function(data){
				console.log("videos.size "+data.response.numFound);
				$scope.videos = data.response.docs;
			})
			.error(function(){
				console.log("Error in fetching videos");
			})

	}

	$scope.trustSrc = function(src){
		return $sce.trustAsResourceUrl(src);
	}

	$scope.displayVideo = function(id){
		console.log(id);
		var data = {
			content:"<source width='420' height='340' controls autoplay><video src='api/play/video/"+id.id+"' type='video/"+id.extension+"'>"
		}
		console.log(data.content);
		$scope.pv=$sce.trustAsHtml(data.content);
		console.log($scope.pv);
		/*$http.get("api/display/video/"+id)
		.success(function(data){
			console.log("success fetching file");
			//$scope.pv.content=$sce.trustAsHtml(data);
		})
		.error(function(){
			console.log("can not display video")
		})*/
	}
	
	$scope.displayVideo12 = function(video){
		console.log(video);
		var obj = {
				path:video.path,
				name:video.name
		}
		var req = {
				method:'POST',
				url:'api/display/video',
				headers:{
					ContentType:'application/json'
				},
				data:obj
		}
		console.log("sending request");
		$http(req).then(function success(response){
			if(success.status >= 400){
				
				$scope.response = "Something wrong happend!!!";
			}
			else{
			
				console.log("success");
				
			}
		})
		
	}
	
	$scope.getBooks = function(){
		console.log("getting books");
		$http.get("http://localhost:8080/jersey/api/bookservice/getBooks")
		.success(function(data,status,header,config){
			
			console.log(data);
			 $scope.books = data;
		})
		.error(function(){
			console.log("Error in fetching books !!");
		})
	}
	
	$scope.deleteBook = function(book){
		console.log(book.id);
			$http.delete("/jersey/api/bookservice/book/"+book.id).
			success(function(){
				var index = $scope.books.indexOf(book);
				$scope.books.splice(index,1);
			})
			.error(function(data,status){
				console.log("couldn't delete the book status : "+status);
			})
		}

		$scope.openEditBook = function(book){
			book.price = parseInt(book.price);
		book.publishedOn = new Date(book.publishedOn);

		$scope.edit = book;
		}
	
	$scope.editBook = function(book){
		
		

		var req = {
				method:'PUT',
				url:'/jersey/api/bookservice/book',
				headers:{
					ContentType:'application/json'
				},
				data:book
		}
		
		$http(req).then(function success(){
			var index = $scope.books.indexOf(book);
			$scope.books[index] = book;
			$scope.edit = null;
		})
	}
	
});