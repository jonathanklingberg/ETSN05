var oldUserName = "";
var oldPassword = "";
var oldGroup = "";
var oldGroupId = -1;
var oldGroupName = "";
var oldRole = "";
var roleList = ["Developer", "ProjectManager", "SystemArchitect", "Tester", "Unspecified"];
var deleteGroupHref = "";
var oldId = -1;
var oldDate = -1;
var oldDuration = -1;
var oldNumber = -1;
var roleId = -1;
var userNameToBePrinted = "";
var timeReportToBePrinted = "";

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
	
	$("#createTimeReportWorker").dialog({
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
	            var url = "workercomponent?date="+ date + "&duration=" + duration + "&number=" + number + "&type=" + type;
	            $(location).attr('href',url);
	        }
	    }
	});

	$("#createTimeReportProjectManager").dialog({
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
	            var url = "projectmanagercomponent?date="+ date + "&duration=" + duration + "&number=" + number + "&type=" + type;
	            $(location).attr('href',url);
	        }
	    }
	});

	$("#createTimeReportButtonWorker").click(function () {
	    $("#createTimeReportWorker").dialog("open");
	});

	$("#createTimeReportButtonProjectManager").click(function () {
	    $("#createTimeReportProjectManager").dialog("open");
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
	
	$("#editTimeReportWorker").dialog({
		autoOpen: false,
		resizable: false,
		open: function () {
	        $("#oldDate").val(oldDate);
	        $("#oldDuration").val(oldDuration);
	        $("#oldNumber").val(oldNumber);
	    },
		buttons: {
	    	Cancel: function () {
	    		$(this).dialog("close");
	    	},
	        Edit: function() {
	            var date = $("#oldDate").val();
	            var duration = $("#oldDuration").val();
	            var number = $("#oldNumber").val();
	            var type = $( "#oldType option:selected" ).text();
	            $(this).dialog("close");
	            var url = "workercomponent?newDate="+ date + "&newDuration=" + duration + "&newNumber=" + number + "&newType=" + type + "&id=" + oldId;
	            $(location).attr('href',url);
	        }
	    }
	});
	
	$("#editTimeReportProjectManager").dialog({
		autoOpen: false,
		resizable: false,
		open: function () {
	        $("#oldDate").val(oldDate);
	        $("#oldDuration").val(oldDuration);
	        $("#oldNumber").val(oldNumber);
	    },
		buttons: {
	    	Cancel: function () {
	    		$(this).dialog("close");
	    	},
	        Edit: function() {
	            var date = $("#oldDate").val();
	            var duration = $("#oldDuration").val();
	            var number = $("#oldNumber").val();
	            var type = $( "#oldType option:selected" ).text();
	            $(this).dialog("close");
	            var url = "projectmanagercomponent?newDate="+ date + "&newDuration=" + duration + "&newNumber=" + number + "&newType=" + type + "&id=" + oldId;
	            $(location).attr('href',url);
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
	
	$("#deleteTimeReportWorker").dialog({
	    autoOpen: false,   
	    resizable: false,
	    buttons: {
	        No: function () {
	            $(this).dialog("close");
	        },
	        Yes: function () {
                var deleteTimeReportHref = "workercomponent?deletetimereport=" + timeReportToBePrinted;
	            $(this).dialog("close");
	            $(location).attr('href', deleteTimeReportHref);
	        }        
	    }
	});
	
	$("#deleteTimeReportProjectManager").dialog({
	    autoOpen: false,  
	    resizable: false,
	    buttons: {
	        No: function () {
	            $(this).dialog("close");
	        },
	        Yes: function () {
                var deleteTimeReportHref = "projectmanagercomponent?deletetimereport=" + timeReportToBePrinted;
	            $(this).dialog("close");
	            $(location).attr('href', deleteTimeReportHref);
	        }        
	    }
	});
}

function deleteTimeReportWorker(timeReportId){
	document.getElementById("timeReportIDFix").innerHTML = "" + timeReportId;
	timeReportToBePrinted = timeReportId;
    $("#deleteTimeReportWorker").dialog("open");
}

function deleteTimeReportProjectManager(timeReportId){
	document.getElementById("timeReportIDFix").innerHTML = "" + timeReportId;
	timeReportToBePrinted = timeReportId;
    $("#deleteTimeReportProjectManager").dialog("open");
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

function editTimeReportWorker(date, duration, number, id){
	oldDate = date;
	oldDuration = duration;
	oldNumber = number;
	oldId = id;
    $("#editTimeReportWorker").dialog("open");
}

function editTimeReportProjectManager(date, duration, number, id){
	oldDate = date;
	oldDuration = duration;
	oldNumber = number;
	oldId = id;
    $("#editTimeReportProjectManager").dialog("open");
}

function deleteUser(userName) {
	userNameToBePrinted = userName;
	document.getElementById("userNameText").innerHTML = userName;
    $("#deleteUser").dialog("open");
    return false;
}


