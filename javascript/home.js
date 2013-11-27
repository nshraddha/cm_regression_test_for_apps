var videoSrc="http://www.youtube.com/embed/N101MNFEDk8?rel=0&amp;fs=1&amp;wmode=transparent&autoplay=1";
var fadeInterval
var pause_time=10000;
$(function() {
	loadEvents();
	var rol=0;
	$('#slidesDesc ul li').hide();
	$('#slidesDesc ul li').eq(rol).show();
	fadeInterval=setInterval(function(){
		$('#slidesDesc ul li').eq(rol).hide();
		rol++;		
		if(rol>4){
			rol=0;
		}
		$('#slidesDesc ul li').eq(rol).fadeIn(500);		
	},pause_time);
});
function loadEvents{
	$(".videoLinksContnr li").find('iframe').attr('src','');
	$(".playBtn,.dirctnArrowContnr ").on('click', function(e) {
		if($(".videoLinksContnr li img").is(":visible")){														   
			$(".playBtn").hide();
			$(".videoLinksContnr li img").hide();
			$(".videoLinksContnr li,.videoLinksContnr li img,.videoLinksContnr li iframe").hide();
			$(".videoLinksContnr li).show();
			$(".videoLinksContnr li).find('iframe').attr('src',videoSrc);
			$(".videoLinksContnr li img").next('iframe').show();
		}
	});
	
	$('#dsktopSliderBlk .pagination li a').on('click',function(e){
		var index=$(this).attr('href').replace('#','');
		$('#slidesDesc ul li').hide();
		$('#slidesDesc ul li').eq(index).fadeIn(500);
		rol=index;
		clearInterval(fadeInterval);
		//var time_out=setTimeout(function(){
			fadeInterval=setInterval(function(){
				$('#slidesDesc ul li').eq(rol).hide();
				rol++;		
				if(rol>4){
				rol=0;
				}
				$('#slidesDesc ul li').eq(rol).fadeIn(500);		
				},pause_time);
		//	},pause_time);
					
		});
		
}