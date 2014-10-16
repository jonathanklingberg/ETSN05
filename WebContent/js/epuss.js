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
var userNameToBePrinted = "";
var timeReportToBePrinted = "";

$(document).ready(
	function(){
		$("body").hide();
		$("body").fadeIn(1000);
		$("body").on("submit", function(){
			$("body").fadeOut(1000);
		});
		$('#grouplist').footable();
		
		$('#logincredentials').html(  "admin - adminpw <br>" +
				"testuser -  passwd <br>" +
				"devuser -  passwd <br>" +
				"pmuser -  passwd <br>"
		);
		$('.signedCheckbox').on('change', function(){
			var reportId = $(this).siblings('.timereportid').val();
			window.location = "projectmanagercomponent?signtimereport="+reportId;			
//			e.preventDefault();
//			var success = false;
//			var checked;
//			if($(this).prop('checked')){
//				checked = true;
//			}else{
//				checked = false;
//			}
//			var reportId = $(this).siblings('.timereportid').val();
//			var data = {
//					checked : checked,
//					reportId : reportId
//				};
//			console.log("Checked: " + checked);
//			console.log("reportId: " + reportId);
//			$.ajax({
//				  type: "POST",
//				  url:  "projectmanagercomponent",
//				  data: data,
//				  dataType: "html"
//				}).done(function(response) {
//					if(response != null && response == 1){ // Success!						
//						console.log("response: " + response);
//						//$('#testcont').html(response);
//						success = true;
//						alert( "success" );
//					}else{ // Something went wrong!
//						alert( "error" );
//					}
//				  })
//				  .fail(function( jqXHR, textStatus ) {
//				    alert( "error"  + textStatus);
//				  })
//				  .always(function() {
//				    alert( "complete" );
//				    if(success){
//				    	console.log("checkingbox");
//				    	if(checked == true){				
//				    		$(this).prop("checked", "checked");
//				    	}
//				    }else{
//				    	console.log("unchecking box");
//				    	$(this).prop("checked", "");
//				    }
//			  });
			});			
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
	    maxWidth: 360,
	    maxHeight: 300,
	    minWidth: 360,
	    minHeight: 300,
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
	            var url = "AdministrationComponent?oldUserName=" + oldUserName + "&editUser=" + name +"&password=" + password + "&group=" + group + "&role=" + role;
	            $(location).attr('href', url);
	        }
	    }
	});
	
	$("#editGroupName").dialog({
	    autoOpen: false,
	    minWidth: 309,
	    minHeight: 210,
	    maxWidth: 309,
	    maxHeight: 210,
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
	
	$("#editTimeReport").dialog({
		autoOpen: false,
	    maxWidth: 360,
	    maxHeight: 300,
	    minWidth: 360,
	    minHeight: 300,
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
	
	$("#addGroup").dialog({
	    autoOpen: false,
	    maxWidth: 309,
	    maxHeight: 216,
	    minWidth: 309,
	    minHeight: 216,    
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
}

function deleteTimeReport(timeReportId){
	document.getElementById("timeReportIDFix").innerHTML = "" + timeReportId;
	timeReportToBePrinted = timeReportId;
    $("#deleteTimeReport").dialog("open");
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

function editTimeReport(date, duration, number, id){
	oldDate = date;
	oldDuration = duration;
	oldNumber = number;
	oldId = id;
    $("#editTimeReport").dialog("open");
}

function deleteUser(userName) {
	userNameToBePrinted = userName;
	document.getElementById("userNameText").innerHTML = userName;
    $("#deleteUser").dialog("open");
    return false;
}
