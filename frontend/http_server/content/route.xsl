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
    		<script src="http://maps.google.com/maps/api/js?sensor=false"></script> 
    		<script src="js/route_constructor.js"></script>
		<script src="js/jquery-latest.min.js"></script>
		<script src="js/ajax.js"></script>
	</xsl:template>

	<xsl:template name="template-init-function">
		<!--initialize(<xsl:value-of select="/poi/lat"/>{poi/lat}, {poi/lng}, '{poi/name}')-->
	</xsl:template>

	<xsl:template name="template-content">
		<table cellpadding = "10">
		<xsl:for-each select="/route/pois/poi">
			<tr>
				<td>
					<B><xsl:apply-templates select="name"/></B>
				</td>
				<td>
					тут будет адрес
				</td>
			</tr>
		</xsl:for-each>
		</table>
	</xsl:template>

	<xsl:template name="template-right-column">
		<div id="map">
		</div>
		<input type="checkbox" name="option3" value="a3">Показать достопримечательности рядом!_</input>
	</xsl:template>

	<xsl:include href="common.xsl"/>


</xsl:stylesheet>
