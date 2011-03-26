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
    		<!--script type="text/javascript" src="map.js"></script--> 

		<script type="text/javascript">
      function initialize_() {
	//alert('Map map map');  
      }
		</script>

    		<script type="text/javascript">

      function initialize(lat, long, caption) {
	//alert('Map map map');
        var myLatlng = new google.maps.LatLng(lat, long, name);
        var myOptions = {
          zoom: 16,
          center: myLatlng,
          mapTypeId: google.maps.MapTypeId.ROADMAP
        }
        var map = new google.maps.Map(document.getElementById("map"), myOptions);
    
        var marker = new google.maps.Marker({
          position: myLatlng, 
          map: map,
          title: caption
        });   
      }
	</script> 
	</head>
	<body onload="initialize({poi/lat}, {poi/lng}, '{poi/name}')">
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
        				<li><a href="index.html" title="">Готовые экскурсии</a></li>
        				<li><a href="constructor.html" title="">Конструктор маршрута</a></li>
        				<li><a href="editor.html" title="">Редактирование</a></li>
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
					<INPUT TYPE="button" onload="alert('this')" ></INPUT>

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
