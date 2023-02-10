const orderSoFar = [];
let flag = 1;
const idBarcodeMapping = new Map();
let orderDataTable = $('#order-table').DataTable({
        scrollY: '45vh',
        scrollCollapse: false,
        paging: false,
        searching: false,
        info: false,
        language: {
                    emptyTable: "",
                    }
    });
let viewOrderItemDataTable = $('#view-order-item-table').DataTable({
        scrollY: '45vh',
        scrollCollapse: false,
        ordering: false,
        paging: false,
        searching: false,
        info: false
    });
let orderItemDataTable = $('#order-item-table').DataTable({
        scrollY: '25vh',
        scrollCollapse: false,
        ordering: false,
        paging: false,
        searching: false,
        info: false,
        language: {
        			emptyTable: "No items in the cart",
        		}
    });

function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

function addOrder(event){
	json = JSON.stringify(orderSoFar);
	console.log(json);
	var url = getOrderUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        $('#inputBarcode').val("");
	        $('#inputQuantity').val("");
	        $('#inputSellingPrice').val("");
	        $('#order-item-table').find('tbody').empty();
	        $('#add-order-modal').modal('toggle');
	   		getOrderList();
	   		orderSoFar.length = 0;
            toastr["success"]("Order Successfully placed!");

	   },
	   error: function(response){
	       handleAjaxError(response);
	   }

	});
}

function addOrderItem(){
    var $form = $("#order-form");
    var json = toJson($form);
    console.log(json);
    var entry = JSON.parse(json);
//    if(idBarcodeMapping.has(entry[barcode])){
//        //
//    }
    orderSoFar.push(entry);
    //idBarcodeMapping.set(entry.barcode,orderSoFar.length-1);
    console.log(orderSoFar[0]);
    $('#inputBarcode').val("");
    $('#inputQuantity').val("");
    $('#inputSellingPrice').val("");
    //table pe show order item
    displayOrderItemList();
}

function createOrderModal(){
    if(flag){
        orderItemDataTable.destroy();
    }
    $('#add-order-modal').modal('toggle');
    flag=0;

}

function cancelOrder(){
    if(flag){
        orderItemDataTable.destroy();
    }
    $('#order-item-table').find('tbody').empty();
    $("#add-order-modal").modal('toggle');
    orderSoFar.length = 0;
    flag=0;
}

function getOrderItemList(id){
	var url = getOrderUrl() + "/" + id;
	console.log(url);
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	        console.log(data);
	   		viewOrderItemList(data);
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS

function viewOrderItemList(data){
    viewOrderItemDataTable.destroy();
    $("#view-order-modal").modal('toggle');
    var $tbody = $('#view-order-item-table').find('tbody');
    	$tbody.empty();
    	let count = 1;
    	for(var i in data){
    		var item = data[i];
    		console.log(item);
    		var row = '<tr>'
    		+ '<td>' + count + '</td>'
    		+ '<td>' + item.barcode + '</td>'
    		+ '<td>'  + item.quantity + '</td>'
    		+ '<td>'  + item.sellingPrice + '</td>'
    		+ '</tr>';
    		console.log(row);
            $tbody.append(row);
            count = count +1;
    	}
    	viewOrderItemDataTable = $('#view-order-item-table').DataTable({
                scrollY: '45vh',
                scrollCollapse: false,
                paging: false,
                searching: false,
                info: false
            });
}

//addition ke time wala display
function displayOrderItemList(){
    if(flag){
        orderItemDataTable.destroy();
    }

    var $tbody = $('#order-item-table').find('tbody');
    $tbody.empty();
    let count = 1;
    for(var i in orderSoFar){
        var item = orderSoFar[i];
        var buttonHtml = '<button class="btn btn-primary" onclick="editOrderItem(' + i +')" style="margin-right:8px"><i class="fa-solid fa-pencil" style="color:white"></i></button>'+
                         '<button class="btn btn-danger" onclick="removeOrderItem(' + i +')"><i class="fa-solid fa-trash" style="color:white"></i></button>';
        var row = '<tr>'
        + '<td>' + count + '</td>'
        + '<td>' + item.barcode + '</td>'
        + '<td>'  + item.quantity + '</td>'
        + '<td>'  + item.sellingPrice + '</td>'
        + '<td>'  + buttonHtml + '</td>'
        + '</tr>';
        $tbody.append(row);
        count = count +1;
    }
    orderItemDataTable = $('#order-item-table').DataTable({
            scrollY: '25vh',
            scrollCollapse: false,
            ordering: false,
            paging: false,
            searching: false,
            info: false,
            language: {
                        emptyTable: "No items in the cart",
                    }
        });
        flag = 1;
}

function removeOrderItem(row){
    orderSoFar.splice(row,1);
    displayOrderItemList();

}
function editOrderItem(row){
    var item = orderSoFar[row];
    $('#inputBarcode').val(item.barcode);
    $('#inputQuantity').val(item.quantity);
    $('#inputSellingPrice').val(item.sellingPrice);
    removeOrderItem(row);

}

function getOrderList(){
    var url = getOrderUrl();
        $.ajax({
           url: url,
           type: 'GET',
           success: function(data) {
                displayOrderList(data);

           },
           error: function(response){
                handleAjaxError(response);
           }
        });

}

function displayOrderList(data){
    orderDataTable.destroy();
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	let count = 1;
	for(var i in data){
		var singleEntry = data[i];
		var buttonHtml = '<button class="btn btn-primary" onclick="getOrderItemList(' + singleEntry.id + ')">View</button>';
		var row = '<tr>'
		+ '<td>' + count + '</td>'
		+ '<td>'  + singleEntry.time + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		count = count +1;
        $tbody.append(row);
	}
	orderDataTable = $('#order-table').DataTable({
            scrollY: '45vh',
            scrollCollapse: false,
            paging: false,
            searching: false,
            info: false,
            language: {
                        emptyTable: " ",
                    }
        });

}

//INITIALIZATION CODE
function init(){
    $('#create-order').click(createOrderModal);
    $('#add-order-item').click(addOrderItem);
	$('#add-order').click(addOrder);
	$('#cancel-order').click(cancelOrder);
	getOrderList();
    $('.active').removeClass('active');
    $('#order-link').addClass('active');
}

$(document).ready(init);
