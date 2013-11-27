$("#dvlpmntCldmnch").on('click', function(e) {
	
		
		$("#runSrvcs").hide();
		$("#dvlpmntSrvcs").show();
		$("#testSrvcs").hide();
		$(".tblCntntTabSlctd").removeClass("tblCntntTabSlctd");
		$(this).addClass("tblCntntTabSlctd");

	});
		$("#runSrvcCldmnch").on('click', function(e) {
			$("#runSrvcs").show();
		$("#dvlpmntSrvcs").hide();
		$("#testSrvcs").hide();
		$(".tblCntntTabSlctd").removeClass("tblCntntTabSlctd");
		$(this).addClass("tblCntntTabSlctd");

	});
		
		$("#testSrvcCldmnch").on('click', function(e) {
			$("#runSrvcs").hide();
		$("#dvlpmntSrvcs").hide();
		$("#testSrvcs").show();
		$(".tblCntntTabSlctd").removeClass("tblCntntTabSlctd");
		$(this).addClass("tblCntntTabSlctd");

	});