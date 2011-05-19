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
            <script src="js/jquery-latest.min.js"></script>
	</xsl:template>


	<!-- писать только одной строкой! -->
	<xsl:template name="template-init-function">initializeFull(<xsl:value-of select="page/data/poi/@id" />, <xsl:value-of select="page/data/poi/@lat"/>, <xsl:value-of select="page/data/poi/@lng"/>, '<xsl:value-of select="page/data/poi/name"/>', '<xsl:value-of select="page/data/poi/address"/>', 'poi.html?id='+'<xsl:value-of select="page/data/poi/@id" />')</xsl:template>


	<xsl:template name="template-content">
		<table cellpadding = "10" width = "100%">
			<tr>
				<td>
                    <B><xsl:apply-templates select="page/data/poi/name"/></B>
                    <br/>
                    <br/>
					<img src="{page/data/poi/img-url}" alt="pic1" />
					<!--br/>
					<br/-->
				</td>
                <td>
                    <div id="div_button" align = "right">
                            <input type="button" value="Показать &#13;достопримечательности &#13;рядом" id="poi_button" style="height: 100px; width: 250px; font:bold 16px Arial" onclick="showNearestPoiButton();">
                            </input>
                    </div>                      
                </td>
			</tr>
			<tr>
				<td>
						<xsl:apply-templates select="page/data/poi/description"/>
							<a href="{page/data/poi/description-url}" color = "blue">
								<font color = "blue">
									[дальше]
								</font>
							</a>
						<br/>
                        <br/>
						<B>Адрес:</B>
						<xsl:apply-templates select="page/data/poi/address"/>
				</td>
			</tr>
		</table>
	</xsl:template>


	<xsl:template name="template-right-column">
		<div id="map">
		</div>
	</xsl:template>


	<xsl:include href="common.xsl"/>

</xsl:stylesheet>
