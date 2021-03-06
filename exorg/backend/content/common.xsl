<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>
    <xsl:template match="/">

        <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
            <head>
                <title>
                    <xsl:call-template name="template-title"/>
                </title>

                <xsl:text disable-output-escaping="yes">
                    <![CDATA[
	  <link rel="stylesheet" type="text/css" href="css/style.css" />
	  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
          ]]>
                </xsl:text>
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
                        <div id="city">
                        Ваш город:
                        <select name="guidelinks">
                            <option value="spb">
                                Санкт-Петербург
                            </option>
                        </select>
                        </div>
                        <div id="logo">
                            <img src="img/logo.gif" alt="logo"></img>
                        </div>
                    </div>
                    <!-- End Header -->

                    <!-- Begin Main -->
                    <div id="main">
                        <!-- Begin Left column -->
                        <div id="leftcolumn">
                            <ul>
                                <li>
                                    <a href="index.html" title="" class="index">
                                        Готовые экскурсии
                                    </a>
                                </li>
                                <li>
                                    <a href="constructor.html" title="" class="constructor">
                                        Конструктор
                                    </a>
                                </li>
                                <li>
                                    <a href="route.html" title="" class="route">
                                        Маршрут
                                    </a>
                                </li>
                            </ul>
                            <p id="poi-print">
                            </p>                            
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
                    Над проектом работали : Алексей Гуревич, Александр Карташов, Светлана Марченко, Екатерина Полищук, Павел Синай.
                </div>
                <!-- End Footer -->
            </body>
        </html>

    </xsl:template>
</xsl:stylesheet>
