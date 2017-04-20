{{! This is a Bootstrap navbar, configured to always be at the top of the page}}
{{! NB: the ID is 'nav' and anything in it that we need to find by ID also has an ID of 'nav'. The basename of this flie is also 'nav'. If we had any custom styles for this navbar, I would name them 'nav-...'}}
<nav id="nav" class="navbar navbar-default navbar-fixed-top">
	<div class="container-fluid">
		{{! When on a small screen, the NavBar will consist only of an 'expand' button and our 'brand', which is CSE 216.}}
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#">Luna's Phase 4</a>
		</div>


	{{!  This div will either be showing 'large screen' or will be a menu that gets expanded when we click the 'expand' button.--}}
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li><a id="navWelcomeBtn" href="#">{{account}}</a></li>
				<li><a id="navContentBtn" href="#">Content</a></li>
				<li><a id="navAdminBtn" href="#">Admin Page</a></li>
			</ul>
		</div>
	</div>
</nav>