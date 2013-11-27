var videoSrc="http://www.youtube.com/embed/BQpCBy_Evfo?rel=0&autoplay=1";
$(function() {
	loadEvents();
});
function loadEvents() {
	$(".videoLinksContnr li").find('iframe').attr('src','');
	$(".playBtn,.dirctnArrowContnr ").on('click', function(e) {
		if($(".videoLinksContnr li img").is(":visible")){														   
			$(".playBtn").hide();
			$(".videoLinksContnr li img").hide();
			$(".videoLinksContnr li,.videoLinksContnr li img,.videoLinksContnr li iframe").hide();
			$(".videoLinksContnr li").show();
			$(".videoLinksContnr li").find('iframe').attr('src',videoSrc);
			$(".videoLinksContnr li img").next('iframe').show();
		}
	});
}