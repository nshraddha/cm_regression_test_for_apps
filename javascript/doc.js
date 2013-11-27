
$(function() {
$("#tree").treeview({
collapsed: true,
animated: "medium",
control:"#sidetreecontrol",
persist: "location"
});
})

/***************************TreeView*********************************/
$('.docContent').hide();
showContent();
function showContent (controlId) {
	
	if(!controlId)
	{
		controlId="integrating-your-project-with-cloudmunch-platform";
	}
	
  $.ajax({
        cache: false,   
        type: "GET",
        async: false,
        url: "/index.php/"+controlId,   
        contentType: "text/html",
        dataType: "html",     
        success: function (result) {
           // console.log(result);
		
            $("#mainContainer").empty();
            $("#mainContainer").html(result);           
        },
        error: function (errorMsg) {
		
		alert(errorMsg);
		alert(errorMsg.Message);
            alert("Not Implemented"); //TODO:Lets see
        }
    });

}
/*
$(".tree li").click(function(event) {
	event.stopPropagation();
	//$('.docContent').hide();
	var controlId=$(this).attr("id");
	//var loc = location.host;
	//location.href = location.href.replace('index.php/component/content/article?id=127','index.php/'+controlId);
	//alert(location.href);
	//alert(controlId);
	//$('#'+controlId+'_def').show();
    showContent(controlId);
	$(".selected").removeClass("selected");
	$(this).addClass("selected");
	
});
*/

$(window).hashchange( function(){
	if(location.hash != "") {
		var sctx = location.hash.substring(1);
		console.log(sctx);
		showContent(sctx);
		$(".selected").removeClass("selected");
		var id_parts = sctx.split('/');
		var last = id_parts[id_parts.length - 1];
		console.log(last);
		$("#"+last).addClass("selected");
	}else{
	}
});


$(window).hashchange();