package pl.umk.bugclassification.scmparser.gerrit.parsers
import scala.util.parsing.combinator.RegexParsers

object EventStreamParser extends RegexParsers {

}
/*
{"type":"patchset-created",
  "change":{
	"project":"tmp",
	"branch":"master",
	"id":"I8f035600d20835224889d69e572cdc8546d39721",
	"number":"55",
	"subject":"8th",
	"owner":{
	  "name":"Mikołaj     Fejzer",
	  "email":"mfejzer@gmail.com",
	  "username":"mfejzer"
	  },
	"url":"http://machina:8080/55"
	},
  "patchSet":{
	  "number":"1",
	  "revision":"a6402e9705e3bb62d94262f29b002274478acb4b",
	  "parents":["ad54e62a6c87a32e45e29a87b2aec6eb18a3c539"],
	  "ref":"refs/changes/55/55/1","uploader":{
	    "name":"Mikołaj Fejzer",
	    "email":"mfejzer@gmail.com",
	    "username":"mfejzer"
	    },
	  "createdOn":1365177814,
	  "author":{
	      "name":"Mikołaj Fejzer",
	      "email":"mfejzer@gmail.com",
	      "username":"mfejzer"
	      },
	   "sizeInsertions":1,
	   "sizeDeletions":0},
  "uploader":{
	 "name":"Mikołaj Fejzer",
	 "email":"mfejzer@gmail.com",
	 "username":"mfejzer"
	 }
  }
  */
