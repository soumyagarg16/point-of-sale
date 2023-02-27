
function showMsg(){
   var message = $('#msg').text();
   if(message == ""){
        $('#errorLabel').attr('hidden',true);
   }
   else{
       $('#errorLabel').attr('hidden',false);
   }
}

function init(){
    showMsg();
}

$(document).ready(init);