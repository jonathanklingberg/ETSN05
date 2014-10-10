$(document).ready(
	function(){
		$("body").hide();
		$("body").fadeIn(1000);
		$("body").on("submit", function(){
			$("body").fadeOut(1000);
		});
	});

window.onload = function(){
	$("#createUser").dialog({
	    autoOpen: false,
	    buttons: { 
	        Add: function() {
	            var name = $("#name").val();
	            var group = $("#group").val();
	            var pm = $("#pm").val() == "on";
	            $(this).dialog("close");
	            var url = "AdministrationComponent?addNewUser="+ name + "&group=" +  group + "&pm=" + pm;
	            $(location).attr('href',url);
	        },
	        Cancel: function () {
	            $(this).dialog("close");
	        }
	    }
	});
	
	$("#createUserButton").click(function () {
	    $("#createUser").dialog("open");
	});
};

function editGroup(link) { 
	var name = prompt('Please enter a new name for the group.'); 
	if (name != null) { 
		link.href= link.href+"&groupname="+name; 
		return true; 
	} 
	return false;
}

function createGroup(link) { 
	var name = prompt('Please enter a name for the new group.');  
	if (name != null) { 
		link.href = link.href+name; 
		return true; 
	} 
	return false;
}