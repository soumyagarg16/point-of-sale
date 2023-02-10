
function getUserUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/admin/user";
}

//BUTTON ACTIONS
function addRole(){
	var $form = $("#role-form");
	var json = toJson($form);
	var url = getUserUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	        toastr.success("Role added successfully!")
	   		getUserList();    
	   },
	   error: function(response){
	        handleAjaxError(response);
	   }
	});

	return false;
}

function getUserList(){
console.log("here..")
	var url = getUserUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayUserList(data);   
	   },
	   error: function(response){
	        handleAjaxError(response);
	   }
	});
}

function deleteUser(id){
	var url = getUserUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getUserList();    
	   },
	   error: function(response){
                handleAjaxError(response);
           }
	});
}

//UI DISPLAY METHODS

function displayUserList(data){
	var $tbody = $('#role-table').find('tbody');
	$tbody.empty();
	var count =1;
	for(var i in data){
		var e = data[i];
		var buttonHtml ='<button class="btn btn-outline-danger" onclick="deleteUser(' + e.id + ')">Remove</button>'
		var row = '<tr>'
		+ '<td>' + count + '</td>'
		+ '<td>' + e.email + '</td>'
		+ '<td>' + e.role + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
        count = count +1;
	}
}
function createRole(){
    $('#add-role-modal').modal('toggle');
}

//INITIALIZATION CODE
function init(){
	$('#create-role').click(createRole);
	$('#add-role').click(addRole);
}

$(document).ready(init);
$(document).ready(getUserList);

