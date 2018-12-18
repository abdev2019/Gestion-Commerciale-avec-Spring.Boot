var ms = ["Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Décembre"];
for(var i=0;i<12;i++)
	$("select[name='m']").append("<option value='"+(i+1)+"'>"+ms[i]+"</option>");

var charts = new Array();
function createCharts(nom,subnom,data, idDiv, axisx, axisy, type)
{console.log(data);
	if(type==null) type = 'line';
	var fsize = "8";
	var visitChart = new FusionCharts({
    type: type,
    renderAt: idDiv,
    width: '100%',
    height: '100%', 
    dataFormat: 'json',
    dataSource: {
      "chart": {
      	"toolTipBgColor": "#fff",
    	"toolTipColor": "#000",
        "theme": "fusion",
        "caption": nom,
        "subCaption": subnom,
        "xAxisName": axisx,
        "yAxisName": axisy, 
        "captionFontColor": "#AFF",
        "captionAlignment": "left",
        "bgColor": "#343a40",  
        "showValues": "0",
        "usePlotGradientColor": "0",
        "plotBorderAlpha": "0",
        "bgAlpha": "100",
        "placevaluesInside": "1",
        "rotatevalues": "1", 
        "showXAxisLine": "1",
        "xAxisLineColor": "#999999",
        "divlineColor": "#999999",
        "divLineIsDashed": "1",
        "showAlternateHGridColor": "0",
        "baseFontColor": "#FFF",    
        "showBorder": "1",
        "canvasBaseColor": "#343a40",
        "canvasBgColor": "#343a40",
      },
      "data": data
    }
  });

  visitChart.render();
  
  return visitChart;
} 


function createChartPie(nom,subnom,data, idDiv, axisx, axisy,subcap)
{ 
	var myChart = new FusionCharts({
	    type: "pie2d",
	    renderAt: idDiv, 
	    width: '100%',
	    height: '100%',
	    dataFormat: "json",
	    dataSource: {
	      chart: {
	        caption: nom,
	        plottooltext:subnom,
	        subCaption: subcap,
	        showlegend: "1",
	        showpercentvalues: "1",
	        legendposition: "bottom",
	        usedataplotcolorforlabels: "1",
	        theme: "fusion",
	        "bgColor": "#343a40",  
	        "captionFontColor": "#ffffff",   
	        "bgAlpha": "100",
	        "showBorder": "0"
	      },
	      data: data
	    }
	  }).render(); 
	return myChart;
} 


function getStatistiques()
{
	var jr = document.fs.j.value;
	var mo = document.fs.m.value;
	var an = document.fs.a.value; 
	var cg = document.fs.c.value; 
	var pr = document.fs.p.value;  
	 
	$.ajax({ 
		url : wwwpath+"/statistiques/getstatistiques", 
		data : { d:document.fs.d.value, j: jr, m: mo, a: an, p:pr }, 
		type : "POST",
		dataType : "json",  
		success : function( json ) {  
			console.log(json);
			var subcaption = ""; 
			if(pr!='') subcaption = "Quantité du produit "+$("option[value='"+pr+"']").text();
			else subcaption="Quantité de produits";
			
			var type = 'line';
			//if(jr!=''&&an!=''&mo!='') 
				type="column2d";
			
			charts["chart-ventes"] = createCharts("Ventes",subcaption+" vendu /"+(mo!=''?"Jour":"Mois"),
					json.sventes,'chart-ventes',mo!=''?"Jours":"Mois","Ventes",type);
			charts["chart-achats"] = createCharts("Achats",subcaption+" acheté  /"+(mo!=''?"Jour":"Mois")
					,json.sachats,'chart-achats',mo!=''?"Jours":"Mois","Achats",type);
 
			charts["chart-ventesPrix"] = createCharts("Ventes","Total TTC /"+(mo!=''?"Jour":"Mois"),
					json.sprixventes,'chart-ventesPrix',mo!=''?"Jours":"Mois","Ventes",type);
			charts["chart-achatsPrix"] = createCharts("Achats","Total TTC /"+(mo!=''?"Jour":"Mois")
					,json.sprixachats,'chart-achatsPrix',mo!=''?"Jours":"Mois","Achats",type);
			 
			var subcap = (jr!=''&&mo!=''&&an!='')?"Le "+jr+"/"+mo+"/"+an :
								((mo!=''&&an!='')?"Le mois "+$("select[name='m'] option:selected").text():(an!=''?"Année "+an:"")) ;
			 
			charts["chart-produitsv"] = createChartPie("Ventes", "<b>$percentValue</b> de <b>$label</b> vendu",
				json.produitsVendu,'chart-produitsv',mo!=''?"Jours":"Mois","",subcap);
			
			charts["chart-produitsa"] = createChartPie("Achats", "<b>$percentValue</b> de <b>$label</b> acheté",
				json.produitsAchete,'chart-produitsa',mo!=''?"Jours":"Mois","Achats",subcap); 
		},
		error : function( xhr, status ) { alert("Erreur dans le serveur..."); } 
	});
}

 






/*
var chartsHtml = $('#showChart,#chart-achats,#chart-ventes,#chart-achatsPrix,#chart-ventesPrix');
var test=false;
var actuelObject = null;
var lastId=null;
chartsHtml.click(function(e)
{      
	
	var id = $(this).attr("id");
	if(id=="showChart") {
		$("#"+lastId).html(actuelObject);
		$("#showChart").html(""); 
		$("#showChart").hide();
		test=false; 
	}
	else { 
		$("#showChart").show(); 
		actuelObject = $.extend({},$('#'+id+' span[id^="chartobject"'));
		console.log(actuelObject);
		$("#showChart").html(actuelObject); 
		actuelObject.css("height","100%"); 
		test=true;
	}  
	lastId = id;
});


var test2=false;
$('#chart-familles,#chart-produits').click(function(e)
{       
	/*if(test2){ 
		$("#"+$(this).attr("id")+" rect").css("cursor","zoom-in"); 
		$(this).removeClass("zoom"); 
		$("#"+$(this).attr("id")+"").css("height","350px"); 
		test2=false;
	} 
	else{ 
		$("#"+$(this).attr("id")+" rect").css("cursor","zoom-out");
		$(this).addClass("zoom"); 
		$("#"+$(this).attr("id")+"").css("height","100%");
		test2=true;
	}
});
*/


