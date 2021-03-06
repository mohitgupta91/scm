
module.exports = function(router){
	// router.on({
		// "url": "/data/{module}/{submodule}/{metrics}/chartdata/{level}",
		// "type": "json"
	// }, function(module, submodule, metrics, level) {
		// switch(level) {
			// case "level-1":
				// return this.json(module+"/"+submodule+"/"+metrics+"/chartdata.res");
				// break;
			// case "level-2":
				// break;
			// case "level-3":
				// break;
			// case "level-4":
				// break;
			// default:
				// return this.json(module+"/"+submodule+"/"+metrics+"/chartdata");
				// break;
		// }
	// });
  router.on({
    "url" : "/view/{fname}/{lname}",
    "type" : "json",
    "role" : ["ADMIN","USER"]
  },function(lname,fname){
    console.log(fname,lname);
    //this.response.write("helllo "+ fname + " " + lname);
    //console.log("this",this.view)
    return this.view("test.html",{ fname : fname,lname : lname});
  });

  router.on({
    "url" : "/json/template/{fname}/{lname}",
    "type" : "json"
  },function(lname,fname){
    console.log(fname,lname);
    //this.response.write("helllo "+ fname + " " + lname);
    //console.log("this",this.view)
    var oldFname = this.user.get("fname");
    this.user.set("fname",fname);
    return this.json("test.json",{ fname : fname,lname : lname, oldFname : oldFname});
  });

  router.on({
    "url" : "/json/direct/{fname}/{lname}",
    "type" : "json"
  },function(lname,fname){
    console.log(fname,lname);
    //this.response.write("helllo "+ fname + " " + lname);
    //console.log("this",this.view)
    var oldFname = this.user.get("fname");
    this.user.set("fname",fname);
    return this.json({ "fname" : fname, "lname" : lname, "oldFname" : oldFname || null});
  });

};