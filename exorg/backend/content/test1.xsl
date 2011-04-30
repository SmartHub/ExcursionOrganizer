<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" indent="yes" encoding="utf-8"/>

    <xsl:template match="/">
	<html>
        <head>
		    <title>Session test</title>
        </head>

	    <body>
            <p> Your Session ID is <xsl:value-of select="page/data/sid"/> </p>
            <p> <a href="test2.html">Click here to check whether it work</a> </p>
        </body>
    </html>
    </xsl:template>
</xsl:stylesheet>
