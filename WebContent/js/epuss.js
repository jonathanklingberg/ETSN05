$(document).ready(function(){
	$("body").hide();
	$("body").fadeIn(1000);
	$("body").on("submit", function(){
		$("body").fadeOut(1000);
	});
});
