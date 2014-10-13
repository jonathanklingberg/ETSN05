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
//		$('#grouplist').footable();
		
		$('#logincredentials').html(  "admin - passwd <br>" +
				"testuser -  passwd <br>" +
				"devuser -  passwd <br>" +
				"pmuser -  passwd <br>");
	});

$(function () {

    $('.footable').footable({    	
			filter: {
				minimum: 1,
			}
    });

});

window.onload = function(){
	$("#createUser").dialog({
	    autoOpen: false,
	    maxWidth: 309,
	    maxHeight: 310,
	    minWidth: 309,
	    minHeight: 310,
	    buttons: { 
	    	Cancel: function () {
	    		$(this).dialog("close");
	    	},
	        Add: function() {
	            var name = $("#name").val();
	            var group = $("#group").val();
	            var role = $("#myselect").val();
	            $(this).dialog("close");
	            var url = "AdministrationComponent?addNewUser="+ name + "&group=" +  group + "&role=" + role;
	            $(location).attr('href',url);
	        }
	    }
	});
	
	$("#createUserButton").click(function () {
	    $("#createUser").dialog("open");
	});
	
	$("#createTimeReport").dialog({
	    autoOpen: false,
	    buttons: { 
	    	Cancel: function () {
	    		$(this).dialog("close");
	    	},
	        Submit: function() {
	            var date = $("#date").val();
	            var duration = $("#duration").val();
	            var type = $("#type").val();
	            $(this).dialog("close");
	            var url = "workercomponent?addNewTimeReport="+ date + "&duration=" + duration + "&type=" + type;
	            $(location).attr('href',url);
	        }
	    }
	});
	
	$("#createTimeReportButton").click(function () {
	    $("#createTimeReport").dialog("open");
	});

	$("#editUser").dialog({
	    autoOpen: false,
	    maxWidth: 318,
	    maxHeight: 337,
	    minWidth: 318,
	    minHeight: 337,
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
	            var role = $( "#myselect2 option:selected" ).text();
	            $(this).dialog("close");
	            var url = "AdministrationComponent?oldUserName=" + oldUserName + "&editUser=" + name +"&password=" + password + "&group=" + group + "&role=" + role;
	            $(location).attr('href', url);
	        },
	        Cancel: function () {
	            $(this).dialog("close");
	        }
	    }
	});
}

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
