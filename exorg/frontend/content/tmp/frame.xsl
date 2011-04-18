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
	<body>  			
  				<ul list-style="none">
					<xsl:for-each select="pois/poi">
    					<li>
						<H3><xsl:value-of select="name"/></H3><br/><br/>
						<a>
							<xsl:attribute name="onClick">parent.location.href='test-poi?id=<xsl:value-of select="id" />'</xsl:attribute>
								<img><xsl:attribute name="src"><xsl:value-of select="img__url" /></xsl:attribute></img>
						</a>
						<input type="checkbox" name="option1" value="a1"/>Хочу посетить!<br/>
						<br/>
    					</li>
					</xsl:for-each>
				</ul>
	</body>
</html>

</xsl:template>
</xsl:stylesheet>
