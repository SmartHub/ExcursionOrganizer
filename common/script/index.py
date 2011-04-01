#!/usr/bin/python

import os
import sys

db_user = "exorg"
db_name = "excursion_organizer"

if (len(sys.argv) >= 2):
    db_user = sys.argv[1];
if (len(sys.argv) >= 3):
    db_name = sys.argv[2];

sphinx_cfg_template = """
################################################################################
## Data source description
################################################################################

source poi
{
  # data source type. mandatory, no default value
  # known types are mysql, pgsql, mssql, xmlpipe, xmlpipe2, odbc
  type = mysql

  # SQL settings (for 'mysql' and 'pgsql' types)
  sql_host = localhost
  sql_user = %(DB_USER)s
  sql_pass =
  sql_db   = %(DB_NAME)s
  sql_port = 3306	# optional, default is 3306

  # main document fetch query
  # mandatory, integer document ID field MUST be the first selected column

  sql_query = \
    SELECT poi.id, poi.id id, poi.name, poi_type.name type, poi_raw_geo.address, poi_raw_descr.descr, poi_raw_images.img_url, poi_raw_geo.lat, poi_raw_geo.lng \
      FROM place_of_interest poi \
           LEFT JOIN poi_raw_descr ON poi.id = poi_raw_descr.poi_id \
           LEFT JOIN poi_raw_geo ON poi.id = poi_raw_geo.poi_id     \
           LEFT JOIN poi_type ON poi.type_id = poi_type.id          \
           LEFT JOIN poi_raw_images ON poi_raw_images.poi_id = poi.id
           
  # It took me about an hour to learn that this is nessesary :(
  sql_query_pre = SET CHARSET utf8

  sql_field_string = id
  sql_field_string = name
  sql_field_string = type
  sql_field_string = address
  sql_field_string = descr
  sql_field_string = img_url
  sql_field_string = lat
  sql_field_string = lng
}

################################################################################
## Index definition
################################################################################

index poi_index
{	
  source  = poi

  path    = %(EO_PATH)s/frontend/index/poi
  docinfo = extern

  mlock   = 0

  morphology = none
  min_word_len = 1

  charset_type = utf-8
}

#############################################################################
## indexer settings
#############################################################################

indexer
{
  # memory limit, in bytes, kiloytes (16384K) or megabytes (256M)
  # optional, default is 32M, max is 2047M, recommended is 256M to 1024M
  mem_limit = 32M	
}

#############################################################################
## searchd settings
#############################################################################

searchd
{
  # [hostname:]port[:protocol], or /unix/socket/path to listen on
  # known protocols are 'sphinx' (SphinxAPI) and 'mysql41' (SphinxQL)
  listen = localhost:9312
  # listen = 9306:mysql41	

  # client read timeout, seconds
  # optional, default is 5
  read_timeout = 5

  # request timeout, seconds
  # optional, default is 5 minutes
  client_timeout = 300

  # maximum amount of children to fork (concurrent searches to run)
  # optional, default is 0 (unlimited)
  max_children = 0

  # max amount of matches the daemon ever keeps in RAM, per-index
  # WARNING, THERE'S ALSO PER-QUERY LIMIT, SEE SetLimits() API CALL
  # default is 1000 (just like Google)
  max_matches = 1000

  pid_file = searchd
}
"""

sphx_cfg = open("exorg.sphinx", "w")
sphx_cfg.write(
    sphinx_cfg_template % {
        "EO_PATH" : os.environ["EO_ROOT"],  
        "DB_USER" : db_user,
        "DB_NAME" : db_name,
        }
    )
sphx_cfg.close()

os.system("rm %(EO_ROOT)s/frontend/index/*" % { "EO_ROOT" : os.environ["EO_ROOT"] });
os.system("indexer --all -c exorg.sphinx");
