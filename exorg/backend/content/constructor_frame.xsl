<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html" indent="yes" encoding="utf-8"/>
<xsl:template match="/">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<script type="text/Javascript"> 
			function load(url)
			{
				location.href=url;
			}

		</script>
	</head>
	<body onLoad = "parent.init_inner_frame()">  			
  				<ul list-style="none">
					<xsl:for-each select="pois/poi">
    					<li>
						<H3><a href="#"><xsl:attribute name="onClick">parent.location.href='test-poi.html?id=<xsl:value-of select="id" />'</xsl:attribute><xsl:value-of select="name"/></a></H3>
						<a>
							<xsl:attribute name="onClick">parent.location.href='test-poi.html?id=<xsl:value-of select="id" />'</xsl:attribute>
								<img><xsl:attribute name="src"><xsl:value-of select="img__url" /></xsl:attribute></img>
						</a>
						<input type="checkbox" onclick="parent.add_poi(this.id, this.checked)"><xsl:attribute name="id"><xsl:value-of select="id" /></xsl:attribute></input>Хочу посетить!<br/>
						<HR/>

    					</li>
					</xsl:for-each>
				</ul>
	<input type="checkbox" id = "cb"/>
	</body>
</html>

</xsl:template>
</xsl:stylesheet>
