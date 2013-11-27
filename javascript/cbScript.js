function tryCB() {
$.ajax({
		   url:"/index.php/testo",
		   type:'POST',
		   async:false,
		   data:{"context":"tryCloudMunch"}
	});
}