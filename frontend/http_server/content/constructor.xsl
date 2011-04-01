<!-- временный файл -->

<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" indent="yes" encoding="utf-8"/>

	<xsl:template name="template-title">
		Информация о POI
	</xsl:template>

	<xsl:template name="template-header">
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />

		<link rel="stylesheet" href="css/map.css" type="text/css" media="all" />
    		<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script> 
    		<script type="text/javascript" src="js/poi_map.js"></script>
	</xsl:template>

	<xsl:template name="template-init-function">
		initialize(<xsl:value-of select="/poi/lat"/>{poi/lat}, {poi/lng}, '{poi/name}')
	</xsl:template>

	<xsl:template name="template-content">
		<table cellpadding = "10">
			<tr>
				<td>
					<B><xsl:apply-templates select="poi/name"/></B>
					<br/>
					<br/>				
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
	</xsl:template>

	<xsl:template name="template-right-column">
		<div id="map">
		</div>
		<input type="checkbox" name="option3" value="a3">Показать достопримечательности рядом!_</input>
	</xsl:template>

	<xsl:include href="common.xsl"/>


</xsl:stylesheet>
