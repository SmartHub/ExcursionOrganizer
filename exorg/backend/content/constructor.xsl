<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" indent="yes" encoding="utf-8"/>


  <xsl:template name="template-title">
    Конструктор маршрута
  </xsl:template>

  <xsl:template name="template-header">
    <style type="text/css">
		div#leftcolumn ul li a.constructor {color: #000;}
	</style>
    <xsl:text disable-output-escaping="yes">
      <![CDATA[
      <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    
      <link rel="stylesheet" href="css/map.css" type="text/css" media="all" />
      <link rel="stylesheet" type="text/css" href="css/style_menu.css" />
      <script src="http://maps.google.com/maps/api/js?sensor=false"></script> 
      <script src="js/route_constructor.js"></script>
      <script src="js/jquery-latest.min.js"></script>
      <script src="js/ajax.js"></script>
      ]]>
    </xsl:text>
  </xsl:template>


  <!-- писать только одной строкой! -->
  <xsl:template name="template-init-function">initialize();change_frame('музей');init_points();</xsl:template>
  

  <xsl:template name="template-content">
    <div class="menu" id = "menu_obj">
      <ul>
	<xsl:for-each select="page/data/type">
	  <li>
	    <a href="#" id="{name}" onClick="document.getElementById('inner-frame').src= 'constructor_frame.html?name={name}';">
              <xsl:value-of select="name"/>
	      </a>
	  </li>
	</xsl:for-each>
      </ul>
    </div>

    <div class="innertube">
      <iframe frameborder="0" id="inner-frame" src="constructor_frame.html?name=музей" width="100%" height="80%"></iframe>
    </div>
  </xsl:template>
  

  <xsl:template name="template-right-column">
    <div id="map">
    </div>
    <div id="poi-print">
        <br/>Выбранные для посещения POI:<br/><hr/>
        <li>
            <xsl:for-each select="page/data/route_point">
                <xsl:value-of select="name"/>
                <route_point>
                        <xsl:attribute name="order">
                            <xsl:value-of select="@order"/>
                        </xsl:attribute>
                        <xsl:attribute name="lat">
                            <xsl:value-of select="@lat"/>
                        </xsl:attribute>
                        <xsl:attribute name="lng">
                            <xsl:value-of select="@lng"/>
                        </xsl:attribute>
                        <xsl:attribute name="poi-id">
                            <xsl:value-of select="@poi-id"/>
                        </xsl:attribute>
                        <xsl:attribute name="name">
                            <xsl:value-of select="name"/>
                        </xsl:attribute>
                </route_point>


                

                <br/><hr/>
            </xsl:for-each>
        </li>
    </div>
  </xsl:template>
    
  
  <xsl:include href="common.xsl"/>
</xsl:stylesheet>
