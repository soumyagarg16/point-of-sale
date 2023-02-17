function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}
var inventoryDataTable = $('#inventory-table').DataTable({
                            scrollY: '45vh',
                            scrollCollapse: false,
                            paging: false,
                            searching: false,
                            info: false
                        });

//BUTTON ACTIONS
function addInventory(event){
	var $form = $("#inventory-form");
	var json = toJson($form);
	console.log(json);
    //$form.reportValidity();
	var url = getInventoryUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        $("#inputBarcode").val("");
	        $("#inputQuantity").val("");
            $('#add-inventory-modal').modal('toggle');
	   		getInventoryList();
	   		toastr["success"]("Success!");
	   },
	   error: function(response){
           handleAjaxError(response);
       }
	});
}

function updateInventory(event){
	//Get the ID
	var id = $("#inventory-edit-form input[name=id]").val();
	var url = getInventoryUrl() + "/" + id;

	//Set the values to update
	var $form = $("#inventory-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        $('#edit-inventory-modal').modal('toggle');
	   		getInventoryList();
	   		toastr["success"]("Updated Successfully!");

	   },
	   error: function(response){
           handleAjaxError(response);
       }
	});
}


function getInventoryList(){
	var url = getInventoryUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventoryList(data);
	   },
	   error: function(response){
           handleAjaxError(response);
       }
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];

function processData(){
	var file = $('#inventoryFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
    var json = JSON.stringify(fileData);
    var url = getInventoryUrl()+'s';
    console.log("here");
        $.ajax({
           url: url,
           type: 'POST',
           data: json,
           headers: {
            'Content-Type': 'application/json'
           },
           success: function() {
                toastr.success("Uploaded Successfully!");
                $('#upload-inventory-modal').modal('toggle');
                getInventoryList();
           },
           error: function(response){
                toastr.error("Errors in the file uploaded. Download the error file!", "Error: ", {
                        "closeButton": true,
                        "timeOut": "0",
                        "extendedTimeOut": "0"
                    });
                errorData=response.responseJSON.message;
                $('#download-errors').removeAttr('hidden');
           }
        });
}

function downloadErrors(){
    errorData = errorData.replaceAll(",","\n");
    errorData = errorData.replace("["," ");
    errorData = errorData.slice(0,-1);
    var element = document.createElement('a');
    element.setAttribute('href','data:text/plain;charset=utf-8,' + encodeURIComponent(errorData));
    element.setAttribute('download',"inventory_errors.txt");
    element.click();
	$('#download-errors').attr('hidden',true);
}

//UI DISPLAY METHODS

function displayInventoryList(data){
    inventoryDataTable.destroy();
	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button class="btn btn-sm btn-primary" onclick="displayEditInventory(' + e.id + ')">Edit</button>';
		var row = '<tr>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	inventoryDataTable = $('#inventory-table').DataTable({
                        scrollY: '45vh',
                        scrollCollapse: false,
                        paging: false,
                        searching: false,
                        info: false
                    });
}

function displayEditInventory(id){
	var url = getInventoryUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventory(data);
	   },
	   error: handleAjaxError(data)
	});
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#inventoryFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");
	$('#download-errors').attr('hidden',true);
    $('#process-data').attr('disabled',true);
}


function updateFileName(){
	var $file = $('#inventoryFile');
	var fileName = $file.val();
	if(fileName.slice(-4)!=".tsv"){
                toastr.error("File must be in .tsv format!", "Error: ", {
                	    "closeButton": true,
                	    "timeOut": "0",
                	    "extendedTimeOut": "0"
                	});
                	return;
        	}
        	$('#inventoryFileName').html(fileName); // putting file name on label
        	$("#process-data").removeAttr('disabled');
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
	$('#upload-inventory-modal').modal('toggle');
}

function displayInventory(data){
	$("#inventory-edit-form input[name=barcode]").val(data.barcode);
	$("#inventory-edit-form input[name=quantity]").val(data.quantity);
	$("#inventory-edit-form input[name=id]").val(data.id);
	$('#edit-inventory-modal').modal('toggle');
}

function createInventory(){
    $('#add-inventory-modal').modal('toggle');
}

//INITIALIZATION CODE
function init(){
	$('#add-inventory').click(addInventory);
	$('#update-inventory').click(updateInventory);
	$('#create-inventory').click(createInventory);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#inventoryFile').on('change', updateFileName);
    $('#process-data').attr('disabled',true);
    $('#download-errors').attr('hidden',true);
    setActive();
}

$(document).ready(init);
$(document).ready(getInventoryList);

