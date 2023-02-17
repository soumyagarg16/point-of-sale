function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}
let brandDataTable = $('#brand-table').DataTable({
        scrollY: '45vh',
        scrollCollapse: false,
        paging: false,
        searching: false,
        info: false
    });

//BUTTON ACTIONS
function addBrand(event){
	var $form = $("#brand-form");
	var json = toJson($form);
	var url = getBrandUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        console.log(response);
	        $("#inputBrand").val("");
	        $("#inputCategory").val("");
	        $('#add-brand-modal').modal('toggle');
	   		getBrandList();
            toastr["success"]("Success!");
	   },
	   error: function(response){

	       handleAjaxError(response);
	   }
	});

	//return false;
}

function updateBrand(event){
	//Get the ID
	var id = $("#brand-edit-form input[name=id]").val();
	var url = getBrandUrl() + "/" + id;

	//Set the values to update
	var $form = $("#brand-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        $('#edit-brand-modal').modal('toggle');
	   		getBrandList();
	   		toastr.success("Updated Successfully!");
	   },
	   error: function(response){
               handleAjaxError(response);
           }
	});

}


function getBrandList(){
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrandList(data);
	   },
	   error: function(response){
            handleAjaxError(response);
       }
	});
}

function deleteBrand(id){
	var url = getBrandUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getBrandList();
	   },
	   error: function(response){
            handleAjaxError(response);
       }
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData;

function processData(){
     console.log("no reload");
 	var file = $('#brandFile')[0].files[0];
	console.log(file);
	readFileData(file, readFileDataCallback);

}

function readFileDataCallback(results){
	fileData = results.data;
	var json = JSON.stringify(fileData);
	var url = getBrandUrl()+'s';

        	//Make ajax call
        	$.ajax({
        	   url: url,
        	   type: 'POST',
        	   data: json,
        	   headers: {
               	'Content-Type': 'application/json'
               },
        	   success: function() {
        	        toastr.success("Uploaded Successfully!");
        	        $('#upload-brand-modal').modal('toggle');
        	   		getBrandList();
        	   },
        	   error: function(response){
                    toastr.error("Errors in the file uploaded. Download the error file!", "Error: ", {
                    	    "closeButton": true,
                    	    "timeOut": "0",
                    	    "extendedTimeOut": "0"
                    	});
                    errorData=response.responseJSON.message;
        	   }
        	});
}

function downloadErrors(){
    var element = document.createElement('a');
    element.setAttribute('href','data:text/plain;charset=utf-8,' + encodeURIComponent(errorData));
    element.setAttribute('download',"brand_errors.txt");
    element.click();
	//writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayBrandList(data){
	var $tbody = $('#brand-table').find('tbody');
	brandDataTable.destroy();
	$tbody.empty();
	let count = 1;
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button class="btn btn-sm btn-primary" onclick="displayEditBrand(' + e.id + ')">Edit</button>'
		var row = '<tr>'
		+ '<td>' + count + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
        count = count+1;
	}
	brandDataTable = $('#brand-table').DataTable({
            scrollY: '45vh',
            scrollCollapse: false,
            paging: false,
            searching: false,
            info: false
        });
}

function displayEditBrand(id){
	var url = getBrandUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrand(data);
	   },
	   error: function(response){
          handleAjaxError(response);
      }
	});
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#brandFile');
	$file.val('');
	$('#brandFileName').html("Choose File");
}

function updateFileName(){
	var $file = $('#brandFile');
	var fileName = $file.val();
	$('#brandFileName').html(fileName);

}

function displayUploadData(){
    if(userRole=="operator"){
    toastr.error("You are not authorized to upload!", "Error: ", {
    	    "closeButton": true,
    	    "timeOut": "0",
    	    "extendedTimeOut": "0"
    	});
        return;
    }
    resetUploadDialog();
	$('#upload-brand-modal').modal('toggle');
}

function displayBrand(data){
	$("#brand-edit-form input[name=brand]").val(data.brand);
	$("#brand-edit-form input[name=category]").val(data.category);
	$("#brand-edit-form input[name=id]").val(data.id);
	$('#edit-brand-modal').modal('toggle');
}

function createBrand(){
    $('#add-brand-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-brand').click(addBrand);
    $('#create-brand').click(createBrand);
	$('#update-brand').click(updateBrand);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName);
    getBrandList();
    setActive();
}

$(document).ready(init);



