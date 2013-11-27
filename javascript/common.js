
$('a[href="'+window.location.pathname+'"]').addClass('presentLink');

var wantsToLogin=true;
   var wants_help=false;
   var onSol=false;
$(".loginBtn").click(function(){
	$(".loginPopCntnr").slideDown('fast');
		$("#lognCnnt").show();
	$(".frgtPswrdCntnt").hide();
	$(".successcMsg").hide();

});
$(".loginpopUpBtn").click(function(){
	$(".loginPopCntnr").slideUp('fast');
	
});

$('.loginPopCntnr').click(function(e){
 e.stopPropagation();					 
});
$(function(){

	if($('#errorMessage').html() != ""){
	 $(".loginPopCntnr").slideDown('fast');
	 $("#lognCnnt").show(); 
	}
$(document).click(function(){	
	if(!wants_help) $('.soln').fadeOut(500);
if(!wantsToLogin)	{
	wantsToLogin=true;
	$(".loginPopCntnr").slideUp();
	
	
}});
$(document).on({
    mouseenter: function () {
        wantsToLogin = true;
    },
    mouseleave: function () {
		if($(".loginPopCntnr").is(':visible'))
		{
        	wantsToLogin = false;
		}
		else{
				 wantsToLogin = true;
			}
    }
}, '.loginPopCntnr');
});
$("#forgot").click(function(){
	$("#lognCnnt").hide();
	$(".frgtPswrdCntnt").show();
});
$("#cancel").click(function(){
	$("#lognCnnt").show();
	$(".frgtPswrdCntnt").hide();
});

function checkSubmitForgotPassword(e)
{
		if(e && e.keyCode == 13)
		{
			var email = $("#useremail").val();
			/*$("#lognCnnt").hide();
			$(".frgtPswrdCntnt").hide();
			$(".successcMsg").show();
			$("#EmailLabel").html(email);*/
			
			
			document.getElementById("errorMsgForgotPwd").innerHTML="";
			if($("#useremail").val().length==0){
			 document.getElementById("errorMsgForgotPwd").innerHTML="Email is empty!";
			}else{
		$.ajax({
				   url:"/templates/cloudmunch-template3/cbsite.php",
				   async:false,
				   type:'POST',
				   data:{"context":"forgotPassword", "email":$("#useremail").val()},
				   success: function(msg){	
				   	if(msg.indexOf("NotConfirmed")!=-1){
				   		document.getElementById("errorMsgForgotPwd").innerHTML = "Your registration is not confirmed yet.";
				   	}else if(msg.indexOf("NotFound")!=-1){
				   		document.getElementById("errorMsgForgotPwd").innerHTML = "Entered mail ID is not found in our database.";
				   	}else if(msg.indexOf("MailSendFailed")!=-1){
				   		document.getElementById("errorMsgForgotPwd").innerHTML = "Unable to mail the reset Link";
				   	}else{
				   		/*document.getElementById("resetLabel").innerHTML = "Your reset password link has been sent"; */
						$("#lognCnnt").hide();
						$(".frgtPswrdCntnt").hide();
						$(".successcMsg").show();
						$("#EmailLabel").html(email);
				   	}
		  },
		  error:function(XMLHttpRequest, textStatus, errorThrown) {
		     alert("some error");
		  }
				   });
			}
		}
}	

$("#snd").click(function(event){
	var email = $("#useremail").val();
	/*$("#lognCnnt").hide();
	$(".frgtPswrdCntnt").hide();
	$(".successcMsg").show();
	$("#EmailLabel").html(email);*/
	
	
	document.getElementById("errorMsgForgotPwd").innerHTML="";
	if($("#useremail").val().length==0){
	 document.getElementById("errorMsgForgotPwd").innerHTML="Email is empty!";
	}else{
$.ajax({
		   url:"/templates/cloudmunch-template3/cbsite.php",
		   async:false,
		   type:'POST',
		   data:{"context":"forgotPassword", "email":$("#useremail").val()},
		   success: function(msg){	
		   	if(msg.indexOf("NotConfirmed")!=-1){
		   		document.getElementById("errorMsgForgotPwd").innerHTML = "Your registration is not confirmed yet.";
		   	}else if(msg.indexOf("NotFound")!=-1){
		   		document.getElementById("errorMsgForgotPwd").innerHTML = "Entered mail ID is not found in our database.";
		   	}else if(msg.indexOf("MailSendFailed")!=-1){
		   		document.getElementById("errorMsgForgotPwd").innerHTML = "Unable to mail the reset Link";
		   	}else{
		   		/*document.getElementById("resetLabel").innerHTML = "Your reset password link has been sent"; */
				$("#lognCnnt").hide();
				$(".frgtPswrdCntnt").hide();
				$(".successcMsg").show();
				$("#EmailLabel").html(email);
		   	}
  },
  error:function(XMLHttpRequest, textStatus, errorThrown) {
     alert("some error");
  }
		   });
	}

});

$(".getstrtdContnr").click(function () {
	tryCB();
	window.location = "/index.php/get-started" ;	 		
});
    /*

   	$('#soln,.solnLinks').click(function(){		
		$(".soln").fadeIn(500);
	});
	$('#soln').hover(function(){ 
        wants_help=true; 
        
    }, function(){ 
        wants_help=false; 
    });
    $('.solnLink').hover(function(){ 
        wants_help=true; 
		$(".solnHighlight").removeClass("solnHighlight");
		$(this).addClass("solnHighlight");
        
    }, function(){ 
        wants_help=false; 
        $(".solnHighlight").removeClass("solnHighlight");
    });
  */  
$(document).on({
    mouseenter: function () {
    	wants_help=false;
        onSol=true;
        $(".soln").stop(true,true).fadeIn(500);
        
    },
    mouseleave: function () {
    	onSol=false;
    	setTimeout(function(){
    		if(!wants_help){
    			$(".soln").stop(true,true).fadeOut(500);
    		}
    	});
		 
    }
}, '#soln');

$(document).on({
    mouseenter: function () {
    	wants_help=true;
        $(".soln").show();
        
    },
    mouseleave: function () {
		setTimeout(function(){
    		if(!onSol){     					
    			$(".soln").stop(true,true).fadeOut(500);
    		}
    	},500);
    }
}, '.solnLinks');

    $('.solnLink').hover(function(){ 
     
		$(".solnHighlight").removeClass("solnHighlight");
		$(this).addClass("solnHighlight");
        
    }, function(){ 
      
        $(".solnHighlight").removeClass("solnHighlight");
    });
    
    $('.sltnsBanner a img').hover(function(){ 
        
		$(".clk2exp").show();
		
        
    }, function(){ 
      
       $(".clk2exp").hide();
    });
    
    
    $(function() {
    	  $('#ltboximg').lightBox();
    	  });
    
    
    $(".community").click(function () {
    	$(".loginPopCntnr").slideDown('fast');
		$("#lognCnnt").show();
	$(".frgtPswrdCntnt").hide();
	$(".successcMsg").hide();
	$("#errorMessage").html("Please Log In!");
    });
    
    
    $('#docs').click(function () {
    	
    	$.ajax({
            cache: false,   
            type: "GET",
            async: false,
            url: "/index.php/cloudmunch-docs",   
            contentType: "text/html",
            dataType: "html",     
            success: function (result) {
                console.log(result);
    		
                //$("#docsContainer").empty();
                $("#docsContainer").html(result);           
            },
            error: function (errorMsg) {
    		
    		alert(errorMsg);
    		alert(errorMsg.Message);
                alert("Not Implemented"); //TODO:Lets see
            }
        });
    });