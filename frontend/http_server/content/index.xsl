<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" indent="yes" encoding="utf-8"/>

	<xsl:template name="template-title">
		Главная страница
	</xsl:template>

	<xsl:template name="template-header">
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />

		<style type="text/css">
			div#rotator {position:relative; height:150px; margin-left: 15px;}
			div#rotator ul li {float:left; position:absolute; list-style: none;}
			div#rotator ul li.show {z-index:500;}
		</style>
		<script type="text/javascript" src="js/jquery-latest.min.js"></script>
		<script type="text/javascript" src="js/rotator.js"></script>
	</xsl:template>

	<xsl:template name="template-init-function">
		init_rotator()
	</xsl:template>

	<xsl:template name="template-content">
		<input type="button" id="prev" name="prev" value="Сюда --"/>
		<input type="button" id="next" name="next" value="Туда --"/>
		<div id="rotator">
  			<ul>
				<xsl:for-each select="routes/route">
    					<li> <!-- class="show"-->
						<a>
							<xsl:attribute name="href">route?id=<xsl:value-of select="id" />
							</xsl:attribute>
							<img>
								<xsl:attribute name="src">
									<xsl:value-of select="img" />
								</xsl:attribute>
							</img>
						</a>	
						<p><xsl:value-of select="descr" /></p>					
						<p><xsl:value-of select="id" /></p>
    					</li>
				</xsl:for-each>
			</ul>
		</div>
	</xsl:template>

	<xsl:template name="template-right-column">
	</xsl:template>

	<xsl:include href="common.xsl"/>


</xsl:stylesheet>
