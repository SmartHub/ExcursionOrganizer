<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" indent="yes" encoding="utf-8"/>


	<xsl:template name="template-title">
		Конструктор маршрута
	</xsl:template>


	<xsl:template name="template-header">
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />

		<link rel="stylesheet" href="css/map.css" type="text/css" media="all" />
    		<link rel="stylesheet" type="text/css" href="css/style_menu.css" />
    		<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script> 
    		<script type="text/javascript" src="js/route_constructor.js"></script>
		<script type="text/javascript" src="js/jquery-latest.min.js"></script>
		<script type="text/javascript" src="js/ajax.js"></script>
	</xsl:template>


	<!-- писать только одной строкой! -->
	<xsl:template name="template-init-function">initialize()</xsl:template>


	<xsl:template name="template-content">
		<div class="menu">
			<ul>
				<xsl:for-each select="poi-types/type">
					<li>
						<a href="#">
							<xsl:attribute name="onClick">document.getElementById('inner-frame').src = 'type.html?name=<xsl:value-of select="name"/>'</xsl:attribute>
                                            		<xsl:value-of select="name"/>
						</a>
					</li>
				</xsl:for-each>
  			</ul>
		</div>

		<div class="innertube">
 			<iframe frameborder="0" id="inner-frame" src="type.html?name=музей" width="100%" height="80%"></iframe>
		</div>
	</xsl:template>


	<xsl:template name="template-right-column">
		<div id="map">
		</div>
		<div id="poi-print">
			Selected pois:
		</div>
	</xsl:template>


	<xsl:include href="common.xsl"/>

</xsl:stylesheet>
