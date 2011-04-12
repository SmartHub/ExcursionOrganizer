#!/usr/bin/python

import os
import sys

os.chdir(sys.argv[2] + "/frontend/http_server/config")

daemonize = False
if len(sys.argv) >= 2:
    if sys.argv[1] == "-D":
        daemonize = True

if daemonize == True:
    fd_null = os.open("/dev/null", os.O_RDWR)
    os.dup2(fd_null, 0)
    os.dup2(fd_null, 1)
    os.dup2(fd_null, 2)

os.system("killall searchd")

if os.fork() == 0:
    os.execlp("searchd", "searchd", "-c", sys.argv[2] + "/common/script/exorg.sphinx")

if daemonize == True:
    if os.fork() == 0:
        os.execlp("java", "java", "-cp", sys.argv[3], "eo.common.util.Starter", "config.xml", sys.argv[2])
    else:
        sys.exit()

os.system("java -cp " + sys.argv[3] + " eo.common.util.Starter config.xml " + sys.argv[2])

