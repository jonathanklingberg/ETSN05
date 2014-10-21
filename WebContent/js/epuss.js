var oldUserName = "";
var oldPassword = "";
var oldGroup = "";
var oldGroupId = -1;
var oldGroupName = "";
var oldRole = "";
var roleList = ["Developer", "ProjectManager", "SystemArchitect", "Tester", "Unspecified"];
var deleteGroupHref = "";
var roleId = -1;
var userNameToBePrinted = "";
var timeReportToBePrinted = "";

var editDate = -1;
var editDuration = -1;
var editNumber = -1;
var editId = -1;
var editUrlComponent = "";
var deleteUrlComponent = "";
var createUrlComponent = "";
var deleteId = -1;

$(document).ready(
	function(){
		$("body").hide();
		$("body").fadeIn(1000);
		$("body").on("submit", function(){
			$("body").fadeOut(1000);
		});
		$('.signedCheckbox').on('change', function(){
			var reportId = $(this).siblings('.timereportid').val();
			window.location = "projectmanagercomponent?signtimereport="+reportId;			
			});			
		});

$(function () {
    $('.footable').footable({    	
			filter: {
				minimum: 1,
			}
    	}).bind({
		    'footable_filtered' : function(e) {
				countTotalTime(this);
			 }
    });
    
    var countTotalTime = function(table){
    	var totalContainer = $('#totalTime', table);
    	var totalTime = 0;
    	$(".duration-value", table).filter(":visible").each(function () {
    		totalTime += $(this).data("duration");
    	});
    	totalContainer.text(totalTime);
    }
    
    countTotalTime($('#reportstable'));
});

window.onload = function(){
	$("#createUser").dialog({
	    autoOpen: false,
	    resizable: false,
	    buttons: { 
	    	Cancel: function () {
	    		$(this).dialog("close");
	    	},
	        Add: function() {
	            var name = $("#name").val();
	            var password = $("#password").val();
	            var group = $("#group").val();
	            var role = $("#pmCheckbox").prop('checked') ? "ProjectManager" : "Unspecified";
	            $(this).dialog("close");
	            var url = "AdministrationComponent?addNewUser="+ name + "&password=" + password + "&group=" +  group + "&role=" + role;
	            $(location).attr('href',url);
	        }
	    }
	});
	
	$("#createUserButton").click(function () {
	    $("#createUser").dialog("open");
	});

	$("#createTimeReport").dialog({
	    autoOpen: false,
	    resizable: false,
	    buttons: {
	    	Cancel: function () {
	    		$(this).dialog("close");
	    	},
	        Submit: function() {
	            var date = $("#date").val();
	            var duration = $("#duration").val();
	            var number = $("#number").val();
	            var type = $( "#myType option:selected" ).val();
	            $(this).dialog("close");
	            var url =  createUrlComponent + "?date="+ date + "&duration=" + duration + "&number=" + number + "&type=" + type;
	            $(location).attr('href',url);
	        }
	    }
	});
	
	$('.create-timereport-btn').on('click', function(){
		createUrlComponent = $(this).data("create-url-component");
		$("#createTimeReport").dialog("open");
	});

	$("#editUser").dialog({
	    autoOpen: false,
	    resizable: false,
	    open: function () {
	        $("#oldUserName").val(oldUserName);
	        $("#oldPassWord").val(oldPassword);
	        $("#oldGroupName").val(oldGroup);
	        $( "#myselect2>option:eq(" + roleList.indexOf(oldRole) + ")" ).attr("selected", "selected");
	    },            
	    buttons: {
	    	Cancel: function () {
	    		$(this).dialog("close");
	    	},
	        Edit: function () {
	            var name = $("#oldUserName").val();
	            var password = $("#oldPassWord").val();
	            var group = $("#oldGroupName").val();
	            var role = $( "#myselect2 option:selected" ).text();
	            $(this).dialog("close");
	            var url = "AdministrationComponent?oldUserName=" + oldUserName + "&editUser=" + name +"&newPassword=" + password + "&group=" + group + "&role=" + role;
	            $(location).attr('href', url);
	        }
	    }
	});
	
	$("#editRole").dialog({
	    autoOpen: false,
	    resizable: false,
        //('#editRole').title += 
	    buttons: { 
	    	Cancel: function () {
	    		$(this).dialog("close");
	    	},
	        Submit: function() {
	            var role = $( "#roleType option:selected" ).val();
	            $(this).dialog("close");
	            var url = "projectmanagercomponent?roleId=" +roleId + "&role=" + role;
	            $(location).attr('href',url);
	        }
	    }
	});
	
//http://jsfiddle.net/5b3cufzx/129/
	
	$("#editGroupName").dialog({
	    autoOpen: false,
	    resizable: false,
	    open: function () {
	    	$("#newGroupName").val(oldGroupName);
	    },            
	    buttons: {        
	        Cancel: function () {
	            $(this).dialog("close");
	        },
	        Edit: function () {
	            var newGroupName = $("#newGroupName").val();
	            var url = "AdministrationComponent?editgroup=" + oldGroupId + "&groupname=" + newGroupName;
	            $(location).attr('href', url);
	        }
	    }
	});
	
	$("#deleteGroup").dialog({
	    autoOpen: false,   
	    resizable: false,
	    buttons: {
	        No: function () {
	            $(this).dialog("close");
	        },
	        Yes: function () {
	            $(this).dialog("close");
	            $(location).attr('href', deleteGroupHref);
	        }        
	    }
	});
	
	$("#addGroup").dialog({
	    autoOpen: false,
	    resizable: false,
	    buttons: {
	        Cancel: function () {
	            $(this).dialog("close");
	        },
	        Add: function () {
	            var newGroupName = $("#addGroupName").val();
	            $(this).dialog("close");
	            var url = "AdministrationComponent?addNewGroup=" + newGroupName;
	            $(location).attr('href', url);
	        }
	    }
	});

	$("#deleteUser").dialog({
	    autoOpen: false,   
	    resizable: false,
	    buttons: {
	        No: function () {
	            $(this).dialog("close");
	        },
	        Yes: function () {
                var deleteUserHref = "administrationcomponent?deleteuser=" + userNameToBePrinted;
	            $(this).dialog("close");
	            $(location).attr('href', deleteUserHref);
	        }        
	    }
	});
	
	$("#deleteTimeReport").dialog({
	    autoOpen: false,  
	    resizable: false,
	    buttons: {
	        No: function () {
	            $(this).dialog("close");
	        },
	        Yes: function () {
                var deleteTimeReportHref = deleteUrlComponent + "?deletetimereport=" + deleteId;
	            $(this).dialog("close");
	            $(location).attr('href', deleteTimeReportHref);
	        }        
	    }
	});
	
	$('.delete-timereport-btn').on('click', function(){
		deleteId = $(this).data("delete-id");
		document.getElementById("timeReportIDFix").innerHTML = "" + deleteId;
		deleteUrlComponent = $(this).data("delete-url-component");
		$("#deleteTimeReport").dialog("open");
	});
	
	$("#editTimeReport").dialog({
    	autoOpen:false,
		resizable: false,
		open: function () {
	        $("#edit-date").val(editDate);
	        $("#edit-duration").val(editDuration);
	        $("#edit-number").val(editNumber);
	    },
		buttons: {
	    	Cancel: function () {
	    		$(this).dialog("close");
	    	},
	        Edit: function() {
	            var date = $("#edit-date").val();
	            var duration = $("#edit-duration").val();
	            var number = $("#edit-number").val();
	            var type = $("#edit-type option:selected").val();
	            $(this).dialog("close");
	            var url = editUrlComponent+ "?newDate="+ date + "&newDuration=" + duration + "&newNumber=" + number + "&newType=" + type + "&id=" + editId;
	            $(location).attr('href',url);
	        }
	    }
	});
	
	$('.edit-timereport-btn').on('click', function(){
		editDate = $(this).data("edit-date");
		editDuration = $(this).data("edit-duration");
		editNumber = $(this).data("edit-number");
		editId = $(this).data("edit-id");
		editUrlComponent = $(this).data("edit-url-component");
		$("#editTimeReport").dialog("open");
	});
}

function editRole(id){
	roleId = id;
    $("#editRole").dialog("open");
    return false;
}

function deleteGroup(link, groupName) {
	deleteGroupHref = link.href;
	document.getElementById("text").innerHTML = groupName;
    $("#deleteGroup").dialog("open");
    return false;
}

function editUser(name, password, group, role){
	oldUserName = name;
	oldPassword = password;
	oldGroup = group;
	oldRole = role;
    $("#editUser").dialog("open");
}

function editGroup(groupId, oldName) { 
	oldGroupId = groupId;
	oldGroupName = oldName;
	$("#editGroupName").dialog("open");
}

function createGroup() { 
	$("#addGroup").dialog("open");
}

function deleteUser(userName) {
	userNameToBePrinted = userName;
	document.getElementById("userNameText").innerHTML = userName;
    $("#deleteUser").dialog("open");
    return false;
}


