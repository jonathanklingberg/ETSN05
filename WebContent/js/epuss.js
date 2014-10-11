var oldUserName = "";
var oldPassword = "";
var oldGroup = "";

$(document).ready(
	function(){
		$("body").hide();
		$("body").fadeIn(1000);
		$("body").on("submit", function(){
			$("body").fadeOut(1000);
		});
		
		$('#logincredentials').html(  "admin - passwd <br>" +
				"testuser -  passwd <br>" +
				"devuser -  passwd <br>" +
				"pmuser -  passwd <br>");
	});

window.onload = function(){
	$("#createUser").dialog({
	    autoOpen: false,
	    buttons: { 
	        Add: function() {
	            var name = $("#name").val();
	            var group = $("#group").val();
	            var pm = $("#pm").is(":checked");
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


$("#editUser").dialog({
    autoOpen: false,
    maxWidth: 309,
    maxHeight: 310,
    minWidth: 309,
    minHeight: 310,
    open: function () {
        $("#oldUserName").val(oldUserName);
        $("#oldPassWord").val(oldPassword);
        $("#oldGroupName").val(oldGroup);
    },            
    buttons: {
        Edit: function () {
            var name = $("#oldUserName").val();
            var password = $("#oldPassWord").val();
            var group = $("#oldGroupName").val();
            var pm = $("#oldPM").is(":checked");
            $(this).dialog("close");
            var url = "AdministrationComponent?oldUserName=" + oldUserName + "&editUser=" + name +"&password=" + password + "&group=" + group + "&pm=" + pm;
            $(location).attr('href', url);
        },
        Cancel: function () {
            $(this).dialog("close");
        }
    }
});

//$(".editUserButton").click(function () {
//});


};

function editUser(name, password, group){
	oldUserName = name;
	oldPassword = password;
	oldGroup = group;
    $("#editUser").dialog("open");
}

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
