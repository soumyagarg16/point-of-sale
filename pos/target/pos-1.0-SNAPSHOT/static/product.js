function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

var productDataTable = $('#product-table').DataTable({
                               scrollY: '45vh',
                               scrollCollapse: true,
                               paging: false,
                               searching: false,
                               info: false
                           });

//BUTTON ACTIONS
function addProduct(event){
	//Set the values to update
	var $form = $("#product-form");
	var json = toJson($form);
	//console.log(json);
	var url = getProductUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        getProductList();
	        $("#inputBrand").val("");
	        $("#inputCategory").val("");
	        $("#inputBarcode").val("");
	        $("#inputProduct").val("");
	        $("#inputMrp").val("");
	        $('#add-product-modal').modal('toggle');
	        toastr["success"]("Success!");
	   },
	   error: function(response){
            handleAjaxError(response);
       }
	});
}

function updateProduct(event){
	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();
	var url = getProductUrl() + "/" + id;

	//Set the values to update
	var $form = $("#product-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        $('#edit-product-modal').modal('toggle');
	        getProductList();
	   		toastr.success("Updated Successfully!");

	   },
	   error: function(response){
            handleAjaxError(response);
       }
	});

	return false;
}


function getProductList(){
	var url = getProductUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProductList(data);
	   },
	   error: function(response){
          handleAjaxError(response);
       }
	});
}

function deleteProduct(id){
	var url = getProductUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getProductList();
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
	var file = $('#productFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
    	var json = JSON.stringify(fileData);
    	var url = getProductUrl()+'s';
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
                $('#upload-product-modal').modal('toggle');
                getProductList();
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
    errorData = errorData.replaceAll(",","\n");
    errorData = errorData.replace("["," ");
    errorData = errorData.slice(0,-1);
    var element = document.createElement('a');
    element.setAttribute('href','data:text/plain;charset=utf-8,' + encodeURIComponent(errorData));
    element.setAttribute('download',"product_upload_errors.txt");
    element.click();
	//writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayProductList(data){
    productDataTable.destroy();
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();

    let count = 1;
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button class="btn btn-sm btn-primary" onclick="displayEditProduct(' + e.id + ')">Edit</button>'
		var row = '<tr>'
        + '<td>' + count + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>'  + e.barcode + '</td>'
		+ '<td>'  + e.name + '</td>'
		+ '<td>'  + e.mrp + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
        count = count +1;
	}
	productDataTable = $('#product-table').DataTable({
            scrollY: '45vh',
            scrollCollapse: true,
            paging: false,
            searching: false,
            info: false
        });
}

function displayEditProduct(id){
	var url = getProductUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProduct(data);
	   },
	   error: function(response){
          handleAjaxError(response);
      }
	});
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
}

function updateFileName(){
	var $file = $('#productFile');
	var fileName = $file.val();
	$('#productFileName').html(fileName);
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
	$('#upload-product-modal').modal('toggle');
}

function displayProduct(data){

	$("#product-edit-form input[name=brand]").val(data.brand);
	$("#product-edit-form input[name=category]").val(data.category);
	$("#product-edit-form input[name=barcode]").val(data.barcode);
	$("#product-edit-form input[name=id]").val(data.id);
	$("#product-edit-form input[name=name]").val(data.name);
	$("#product-edit-form input[name=mrp]").val(data.mrp);

	$('#edit-product-modal').modal('toggle');
}

function createProduct(){
    $('#add-product-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-product').click(addProduct);
	$('#create-product').click(createProduct);
	$('#update-product').click(updateProduct);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#productFile').on('change', updateFileName)
    $('.active').removeClass('active');
    $('#product-link').addClass('active');
}

$(document).ready(init);
$(document).ready(getProductList);



