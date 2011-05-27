<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<!-- временный файл -->
	<xsl:output method="html" indent="yes" encoding="utf-8"/>

	<xsl:template name="template-title">
		Маршрут
	</xsl:template>

	<xsl:template name="template-header">
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />

		<link rel="stylesheet" href="css/map.css" type="text/css" media="all" />
    	<link rel="stylesheet" type="text/css" href="css/style_menu.css" />
		<style type="text/css">
			div#leftcolumn ul li a.route {color: #000;}
		</style>        
    	<script src="http://maps.google.com/maps/api/js?sensor=false"></script>
    	<script src="js/route_constructor.js"></script>
		<script src="js/route_show.js"></script>
		<script src="js/jquery-latest.min.js"></script>
        <script src="js/change_order.js"></script>
	</xsl:template>

	<xsl:template name="template-init-function">initialize();init_points_()</xsl:template>
	<xsl:template name="template-content">
        <div id="route-print">

        </div>
	</xsl:template>
	<xsl:template name="template-right-column">
		<div id="map">
		</div>
	</xsl:template>

	<xsl:include href="common.xsl"/>


</xsl:stylesheet>
