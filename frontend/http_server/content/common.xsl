<xsl:stylesheet version = '1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>
    <xsl:template match="/">

        <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	        <head>
		        <title>
                    <xsl:call-template name="template-title"/>
                </title>
		        <link rel="stylesheet" type="text/css" href="style.css" />
		        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
                <xsl:call-template name="template-header"/>
	        </head>
	        <body>
                <xsl:attribute name="onLoad">
                    <xsl:call-template name="template-init-function"/>
                </xsl:attribute>
    	        <!-- Begin Wrapper -->
		        <div id="wrapper">
        	        <!-- Begin Header -->
                    <div id="header">
                        Ваш город:
                        <select name="guidelinks">
                            <option value="spb">
                                Санкт-Петербург
                            </option>
                        </select>
                    </div>
                    <!-- End Header -->

                    <!-- Begin Main -->
                    <div id="main">
                        <!-- Begin Left column -->
                        <div id="leftcolumn">
                            <ul>
                                <li>
                                    <a href="#" title="">
                                        Готовые экскурсии
                                    </a>
                                </li>
                                <li>
                                    <a href="poi_types.html" title="">
                                        Конструктор
                                    </a>
                                </li>
                                <li>
                                    <a href="#" title="">
                                        Маршрут
                                    </a>
                                </li>
                            </ul>
                        </div>
                        <!-- End Left column -->

                        <!-- Begin Content -->
                        <div id="content">
                            <!-- div class="innertube">Content</div -->
                            <xsl:call-template name="template-content"/>
                        </div>
                        <!-- End Content -->
                    </div>
                    <!-- End Main -->
                    <div id="rightcolumn">
                        <div class="innertube">
                            <xsl:call-template name="template-right-column"/>
                        </div>
                    </div>
		        </div>
            <!-- Begin Footer -->
			<div id="footer">
                Footer
            </div>
            <!-- End Footer -->
	    </body>
    </html>

    </xsl:template>
</xsl:stylesheet>
