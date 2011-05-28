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
      <script src="js/route_show.js"></script>
      <script src="js/jquery-latest.min.js"></script>
      <script src="js/ajax.js"></script>
      ]]>
    </xsl:text>
  </xsl:template>


  <!-- писать только одной строкой! -->
  <xsl:template name="template-init-function">initialize();change_frame('музей');init_points();init_inner_frame();</xsl:template>
  

  <xsl:template name="template-content">
    <div class="menu" id = "menu_obj">
      <ul>
	<xsl:for-each select="page/data/type">
	  <li>
        <!--a>
		    <xsl:attribute name="onClick">parent.location.href='poi.html?id=<xsl:value-of select="@id" />'</xsl:attribute>
			<img><xsl:attribute name="src"><xsl:value-of select="img-url" /></xsl:attribute></img>
		</a-->

	    <a href="#" id="{name}" title="{name}" onClick="document.getElementById('inner-frame').src= 'constructor_frame.html?name={name}';loadPoiList('');">
              <img><xsl:attribute name="src"><xsl:value-of select="icon" /></xsl:attribute></img>
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
  </xsl:template>
    
  
  <xsl:include href="common.xsl"/>
</xsl:stylesheet>
