#!/usr/bin/env python

import config
import logging
from datetime import datetime
import sys
from operator import attrgetter
import urllib

logging.basicConfig(level = logging.INFO)

def download_img(m, pos = 0):
	s = m
	search = '/fckeditor/editor/filemanager/img.php?file='
	pos = s.find(search, pos)
	#print pos
	if pos != -1:
		pos += len(search)
		#print pos
		pos2 = s.find('"', pos)
		if pos2 != -1:
			s = s[pos:pos2]
			print "Found image: " + s
			urllib.urlretrieve ("http://forge.universaal.org" + search + s, s + ".jpg")
			download_img(m, pos2)


##################################################
if __name__ == "__main__":

	if len(sys.argv) < 1:
		print "Please provide as argument the unix name of the gforge project"
		sys.exit(0)
	
	sys.argv[1] = sys.argv[1].rstrip()
	config.GFORGE_PROJECT = sys.argv[1]
	print "Processing project: " + config.GFORGE_PROJECT
	print "-------------------------------------------------"
	#print 'Argument List:', str(sys.argv)
	#sys.exit(0)
	
	# setup GForge SOAP endpoint
	from SOAPpy import SOAPProxy
	GFapi = SOAPProxy(config.GFORGE_ENDPOINT_URL, namespace=config.GFORGE_XML_NAMESPACE)
	# uncomment to see outgoing/incoming XML
	#GFapi.config.debug = 1

	# get GForge session
	GFsession = GFapi.login( config.GFORGE_LOGIN , config.GFORGE_PASSWORD )
	GFuserid = GFsession.split(":")[0]
	# get GForge project
	p = GFapi.getProjectByUnixName(GFsession, config.GFORGE_PROJECT)
	print p.project_id

	file = open(config.GFORGE_PROJECT + ".txt","w") 
	
	forums = GFapi.getForums(GFsession)
	print "Found " + str(len(forums)) + " forums"
	for f in forums:
		print "found forum: " + f.forum_name + "  " + str(f.forum_id) + "\t" + f.section + "\trdf-ID: " + str(f.ref_id)
		if f.ref_id != p.project_id:
			print "..not part of this project - continue"
			continue
		
		threads = GFapi.getForumThreads(GFsession, f.forum_id)
		print "Found " + str(len(threads)) + " threads for forum " + str(f.forum_id)
		i=0
		for t in threads:
			i += 1
			print "found thread (" + str(i) + "): " + t.thread_name + " (thread id: " + str(t.forum_thread_id) + ")"
	
			try:
				msg = GFapi.getForumMessages(GFsession, t.forum_thread_id)
				print "Found " + str(len(msg)) + " messages for thread " + str(t.forum_thread_id)
				for m in msg:
					print "found msg: " + m.subject
					s = ""
					s += str(f.forum_id) + "\t" + str(m.forum_thread_id) + "\t" + str(m.forum_message_id) + "\t" + str(m.parent_forum_message_id) + "\t" + m.post_date + "\t" + str(m.created_by) + "\t" + m.subject.encode('utf-8') + "\n"
					#file.write(str(f.forum_id) + "\t" + str(m.forum_thread_id) + "\t" + str(m.forum_message_id) + "\t" + str(m.parent_forum_message_id) + "\t" + m.post_date + "\t" + str(m.created_by) + "\t" + m.subject.encode('utf-8') + "\n")
					s += m.body.encode('utf-8')
					#file.write(m.body.encode('utf-8'))
					s += "\n--------------uhoatao484z5zq934zqzt84t\n"
					#file.write("\n--------------uhoatao484z5zq934zqzt84t\n")
					file.write(s)
					try:
						download_img(m.body)
					except Exception as err:
						print("Exception while getting image: {0}".format(err))
			except Exception as err:
				print("Exception while getting messages: {0}".format(err))
				#raise err

	file.close()

        print "Done."
