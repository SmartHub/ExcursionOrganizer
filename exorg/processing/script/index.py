#!/usr/bin/python

import os
import sys

db_user = "exorg"
db_pwd = ""

if (len(sys.argv) >= 3):
    db_user = sys.argv[2];
if (len(sys.argv) >= 4):
    db_pwd = sys.argv[3];

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
  sql_pass = %(DB_PWD)s
  sql_db   = excursion_organizer
  sql_port = 3306	# optional, default is 3306

  # main document fetch query
  # mandatory, integer document ID field MUST be the first selected column


  sql_query = \
    SELECT poi.id, poi.id id, poi.name, poi_type.name type, poi.address, poi_descr.descr descr, poi_descr.src_url descr_ref, \
            poi_image.img_url, poi.lat, poi.lng, \
            poi.cluster_id, poi.is_head, poi.sq_n square_num\
      FROM place_of_interest poi \
           LEFT JOIN poi_descr ON poi.id = poi_descr.poi_id \
           LEFT JOIN poi_type ON poi.type_id = poi_type.id  \
           LEFT JOIN poi_image ON poi_image.poi_id = poi.id

  # It took me about an hour to learn that this is nessesary :(
  sql_query_pre = SET CHARSET utf8

  sql_field_string = id
  sql_field_string = name
  sql_field_string = type
  sql_field_string = address
  sql_field_string = descr
  sql_field_string = descr_ref
  sql_field_string = img_url
  sql_field_string = lat
  sql_field_string = lng
  sql_field_string = cluster_id
  sql_field_string = is_head
  sql_field_string = square_num
}

source poi_type
{

  type = mysql

  sql_host = localhost
  sql_user = %(DB_USER)s
  sql_pass = %(DB_PWD)s
  sql_db   = excursion_organizer
  sql_port = 3306

  sql_query = \
    SELECT poi_type.id, poi_type.id type_id, name FROM poi_type

  sql_query_pre = SET CHARSET utf8

  sql_field_string = type_id
  sql_field_string = name

}

################################################################################
## Index definition
################################################################################

index poi_index
{
  source  = poi

  path    = %(EO_PATH)s/backend/index/poi
  docinfo = extern

  mlock   = 0

  morphology = none
  min_word_len = 1

  charset_type = utf-8
}

index poi_type_index
{
  source = poi_type

  path = %(EO_PATH)s/backend/index/poi_type
  docinfo = extern

  mlock = 0

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

sphinx_cfg_path = sys.argv[1] + "/backend/exorg.sphinx"

sphx_cfg = open(sphinx_cfg_path, "w")
sphx_cfg.write(
    sphinx_cfg_template % {
        "EO_PATH" : sys.argv[1],
        "DB_USER" : db_user,
		"DB_PWD"  : db_pwd,
        }
    )
sphx_cfg.close()

os.system("rm %(EO_ROOT)s/backend/index/*" % { "EO_ROOT" : sys.argv[1] });
os.system("indexer --all -c " + sphinx_cfg_path);