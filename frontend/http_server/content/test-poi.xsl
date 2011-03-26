<xsl:stylesheet version = '1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>
<xsl:template match="/">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title>POI</title>
		<link rel="stylesheet" type="text/css" href="style.css" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />

		<link rel="stylesheet" href="map.css" type="text/css" media="all" />
    		<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script> 
    		<!--script type="text/javascript" src="poi_map.js"></script--> 

		<script type="text/javascript">
      	function initialize_() {
			//alert('Map map map');  
      	}
		</script>
		<script type="text/javascript">
		var map;

		function show_infowindow(marker, name_poi, address, url){
			var contentString = '<div style="color: black;">' + name_poi + ' Адрес: ' + address + '<a href="' + url + '" >подробнее...</a></div>';
  			var infowindow = new google.maps.InfoWindow({
  				content: contentString
  			}); 
  		
 			google.maps.event.addListener(marker, 'click', function() {
  				infowindow.open(map,marker);
  			}); 
		}

 		function initialize(lat, long, name_poi, address, url) {
  
  			var posLatlng = new google.maps.LatLng(lat, long, name_poi);
  			var options = {
    			zoom:16,
    			mapTypeId: google.maps.MapTypeId.ROADMAP,
    			center: posLatlng
  			}

  			map = new google.maps.Map(document.getElementById("map"), options);
			var marker = new google.maps.Marker({
				position: posLatlng,
				map: map,
				title: name_poi
			});
			show_infowindow(marker, name_poi, address, url);
		}

	</script> 
	</head>
	<body onload="initialize({poi/lat}, {poi/lng}, '{poi/name}', '{poi/name}', 'http://www.google.ru')">
    	<!-- Begin Wrapper -->
		<div id="wrapper">
        	<!-- Begin Header -->
			<div id="header">
            	Ваш город:
		<SELECT name="guidelinks"> 
			<OPTION value="spb">Санкт-Петербург
			</OPTION>
		</SELECT> 
            </div>
            <!-- End Header -->

            <!-- Begin Main -->
			<div id="main">

            <!-- Begin Left column -->
			<div id="leftcolumn">
      				<ul>
        				<li><a href="#" title="">Готовые экскурсии</a></li>
        				<li><a href="poi_types.html" title="">Конструктор маршрута</a></li>
        				<li><a href="#" title="">Редактирование</a></li>
				</ul>
			</div>
            <!-- End Left column -->

            	<!-- Begin Content -->
				<div id="content">
					<!-- div class="innertube">Content</div -->
					<table cellpadding = "10">
						<tr>
							<td>
								<B><xsl:apply-templates select="poi/name"/></B>
								<br/><br/>

								<img src="{poi/img__url}"  alt="pic1" />
							</td>
						</tr>
						<tr>
							<td>
								<xsl:apply-templates select="poi/description"/>
								<br/><B>Адрес:</B>
								<xsl:apply-templates select="poi/address"/>
							</td>
						</tr>
					</table>
				</div>
                <!-- End Content -->
			</div>
            <!-- End Main -->
			<div id="rightcolumn">
				<div class="innertube">
					<div id="map"></div>
				</div>
				<input type="checkbox" name="option3" value="a3"/>Показать достопримечательности рядом!	
			</div>
		</div>
            <!-- Begin Footer -->
			<div id="footer">Footer</div>
            <!-- End Footer -->
	</body>
</html>

</xsl:template>
</xsl:stylesheet>
