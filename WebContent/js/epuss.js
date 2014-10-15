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
