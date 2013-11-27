var urls={"tab4":"http://www.youtube.com/embed/ckzGm0W0ooQ?rel=0&autoplay=1" , 
"tab5":"http://www.youtube.com/embed/BQpCBy_Evfo?rel=0&autoplay=1"};
//https://www.youtube.com/watch?v=ckzGm0W0ooQ&feature=player_embedded
$(function() {
	loadEvents();

});
var sliderMade=false;
function loadEvents() {
	$(".videoLinksContnr >li").eq(3).find('iframe').attr('src','');
	$(".videoLinksContnr >li").eq(4).find('iframe').attr('src','');
	$(".playBtn,.dirctnArrowContnr ").on('click', function(e) {
		$(".playBtn").hide();
		for( i = 3; i <5; i++) {
			if($(".videoLinksContnr>li").eq(i).find('img').is(":visible")) {
				//$(".videoLinksContnr li img").eq(i).hide();
				$(".videoLinksContnr > li,.videoLinksContnr >li >img,.videoLinksContnr li iframe").hide();
				$(".videoLinksContnr> li").eq(i).show();
				var tabNo=i+1;
				if(urls['tab'+tabNo]){
					$(".videoLinksContnr >li").eq(i).find('iframe').attr('src',urls['tab'+tabNo]);
				}	
				
				$(".videoLinksContnr >li").eq(i).find('iframe').show();
				break;
			}
		}
		
	});
	$("#getStartDetails .vidBtnContrTop li").on('click', function() {
																  
		$(".playBtn,.tourVidContr,.getStartdDescBlk").show();
		$(".sliderBox").hide();
		$('.getStartedBlk').removeClass('fixHeight');		
		var vidBtnContr = $("#getStartDetails .vidBtnContrTop > li");
		var videoLinksContnr = $("#getStartDetails .videoLinksContnr > li");
		var VidDescContainer = $("#getStartDetails .getStartdDescBlk > li");
		vidBtnContr.removeClass('btnActvVid');
		videoLinksContnr.hide();
		videoLinksContnr.children('img').hide();
		//videoLinksContnr.children('iframe').attr('src','');
		videoLinksContnr.children('iframe').hide();
		VidDescContainer.hide();
		var index = vidBtnContr.index(this);
	
		if(index<3){
			$(".playBtn").hide();
		    if(index===0){		    	
		    	$(".sliderBox").show();
				$('.getStartedBlk').addClass('fixHeight');
		    	$(".tourVidContr,.getStartdDescBlk").hide();
		    }
		}	
		videoLinksContnr.each(function(ind){
					if(ind==index){
						
						}
						else{
								$(this).find('iframe').attr('src','');
							}
									   })
		vidBtnContr.eq(index).addClass('btnActvVid');
		videoLinksContnr.eq(index).show();
		VidDescContainer.eq(index).show();
		videoLinksContnr.eq(index).children('img').show();
		
			if(index==2 && !sliderMade){
			
			$('#componentsSliderBlk').slides({
			
			play : 4000,
			pause : 4000,
			hoverPause : true
		});
		sliderMade=true;
		}

	});
}