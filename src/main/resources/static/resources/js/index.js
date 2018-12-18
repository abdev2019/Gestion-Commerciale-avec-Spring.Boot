	var ok = true;
	
	function openNav() {  
		if(ok) closeNav();
		else{
		    document.getElementById("mySidenav").style.width = "260px";
		    document.getElementById("main").style.marginLeft = "260px"; 
		    ok=true;
		}
	}
	
	function closeNav() {
		//$("#btnNav").show();
	    document.getElementById("mySidenav").style.width = "0";
	    document.getElementById("main").style.marginLeft= "0"; 
	    ok=false;
	} 
	
	$(window).resize(function(e) {
		var viewportWidth = $(this).width();
		if(viewportWidth<=920) closeNav();
		if(viewportWidth>=920) openNav(); 
	}); 
	 
	$(document).ready(function(){
		$(".fa-angle-up,.fa-angle-down").parent().click(function(){ 
			$(this).find("i.fa-angle-down,i.fa-angle-up").toggleClass("fa-angle-down fa-angle-up");
		});
	}); 
	
	function setTheme(){
		if(sessionStorage.getItem("color")==0){
			$(".montheme").addClass("bg-dark");
			$("#mySidenav").removeClass("bgsidenav1");
			$("#mySidenav").addClass("bgsidenav0");
			$("#myTopMenu").addClass("card-header0");
			$("#myTopMenu").removeClass("card-header1");  
			$("footer").removeClass("card-bottom"); 
			$("footer").addClass("card-bottom0"); 
			$(".titlemenu").addClass("text-info");
			$(".card .card-header").addClass("card-header0");
			$(".montheme .card ").addClass("text-light bg-dark");
		}
		else {
			$(".montheme").removeClass("bg-dark");
			$("#mySidenav").addClass("bgsidenav1"); 
			$("#mySidenav").removeClass("bgsidenav0"); 
			$("#myTopMenu").removeClass("card-header0"); 
			$("#myTopMenu").addClass("card-header1");  
			$("footer").removeClass("card-bottom0"); 
			$("footer").addClass("card-bottom"); 
			$(".titlemenu").removeClass("text-info");
			$(".card .card-header").removeClass("card-header0");
			$(".montheme .card ").removeClass("text-light bg-dark");
		}
	}