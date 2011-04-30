<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html" indent="yes" encoding="utf-8"/>


    <xsl:template match="/">
        <html>
            <head>
                <title>
                    <xsl:text>WORDS.</xsl:text>
                </title>
                <script type="text/javascript" src="jquery.js"></script>
                <script type="text/javascript" src="test.js"></script>
            </head>
            <body  link='red' vlink='red' alink='red'>

                <h3>
                    <xsl:text>WORDS.</xsl:text>
                </h3>

                <input type = "text" name = "t" id = "t" value=""/>

                <p id="demo"></p>

            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>