
var sprtTimerId;

var sequence=0;
function setClassSeq() {
	clearInterval(sprtTimerId);
	var prev = sequence;
	if(sequence==0)
	{
		prev=30;
	}
	sequence++;
	var prevClass="animation"+prev;
	var thisClass="animation"+sequence;
	$(".animboard").removeClass(prevClass).addClass(thisClass)	;
	if(sequence==30)
	{
		sequence=0;
	}
	sprtTimerId = setInterval("setClassSeq()", 50);
}

$(document).ready(function() {

	
	
	
	$('.animboard').delay(115).fadeIn(5000);
	sprtTimerId = setInterval("setClassSeq()", 50);
	$(".animboard").hover(function(){
		$(this).prev().removeClass("bringdown").addClass("bringup");
	},
	function(){
		$(this).prev().removeClass("bringup").addClass("bringdown");
	});
});