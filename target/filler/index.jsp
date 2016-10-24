<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!Doctype html>
<html>
<head>
		<title>Allin | Filler</title>
		<script src="static/js/angular.js"></script> 
<script src="static/js/createBookCtrl.js"></script>

<link rel="stylesheet" href="static/css/bootstrap.css"></link>
<script src="static/js/jquery.min.js"></script>
  <script src="static/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="static/css/style.css"> 
<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/normalize/5.0.0/normalize.min.css"/>

	</head>
	<body>
		
		<div class="wrapper" ng-app="allin" ng-controller="files">
			<div class="wrapper_with_padding">
				<h1>Index page</h1>
					<div class="content">
						<nav>
						<input type="text" id="searchBox" name="query" plcaholder="Search Me" ng-model="query">
						<input type="submit" name="submit" ng-click="loadFromSolr()"><br>
						</nav>
						<div id="results">
						<section>
							<div ng-bind-html="pv"></div>

						</section><article></article>
						<aside>
							<div class="videoWrapper" ng-repeat="video in videos"> <div ng-click="displayVideo(video)">
								<div class="video">
								<span ng-bind="video.name"></span>
								</div>
								</div>
								<!-- <video width="720" height="520" controls>  
									<source ng-src="{{trustSrc(video.path)}}" type="video/{{video.extension}}">
								</video> -->
							</div>
						<aside>
						</div>
					</div>
				
			</div>
		</div>
	</body>	
</html>