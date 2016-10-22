define({
  name: "UserService",
  modules: ["DataService", "jQuery"]
}).as(function(UserService, DataService, jQuery) {
  return {
    $MetaDataResponse: null,
    MetaData: null,
    _ready_: function() {
      // api call
      this.init();
    },
    init: function() {
      var self = this;
      if (!self.$MetaDataResponse) {
        self.$MetaDataResponse = DataService.get("userDetails").done(function(resp) {
          self.MetaData = resp;
        });
      }
      return self.$MetaDataResponse.then(function() {
        return self.MetaData;
      });
    },
    ACCESS_CACHE : {},
    isAllowed : function () {
      var self = this;
      var keyStar = "/" + [].join.call(arguments,"/");
      if(self.MetaData.permissions.indexOf("/") > -1) {
      	this.ACCESS_CACHE[keyStar] = true;
      	return this.ACCESS_CACHE[keyStar];
      }
      if(this.ACCESS_CACHE[keyStar]!==undefined){
          return this.ACCESS_CACHE[keyStar];
      }
      var key = keyStar+"/";
      var access =  !!this.MetaData.permissions.filter(function(_key){
        return _key == keyStar || (_key.indexOf(key) == 0);
      })[0];
      if(!access){
        var keys = keyStar.split("/"),
        		iteration = keys.length;
        for(var i=0; i<iteration-1; i++){
          keys.pop();
          var _key = keys.join("/");
          if(this.MetaData.permissions.indexOf(_key)>-1){
            access = true;
            break;
          }
        }
      }
      this.ACCESS_CACHE[keyStar] = access;
      return access;
    },
    getUserDetails: function () {
      var self = this;
      return {
        name : self.MetaData.name,
        email : self.MetaData.email
      };
    }
  };
});
