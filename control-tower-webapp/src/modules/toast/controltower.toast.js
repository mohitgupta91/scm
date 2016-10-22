define({
	name : "controltower.toast",
	extend : "spamjs.view",
	modules : ["jQuery"]
}).as(function(TOAST, jQuery) {
	return {
		_init_ : function() {
			var self = this;
			toastr.options = {
				closeButton : true,
				debug : false,
				newestOnTop : true,
				progressBar : false,
				positionClass : "toast-top-right",
				preventDuplicates : false,
				onclick : null,
				showDuration : "300",
				hideDuration : "1000",
				timeOut : "5000",
				extendedTimeOut : "1000",
				showEasing : "swing",
				hideEasing : "linear",
				showMethod : "fadeIn",
				hideMethod : "fadeOut"
			};
		},
		prepareOptions : function(options) {
			var self = this;
			options = options?options:{};
			options.type = options.type?options.type:"success";
			options.message = options.message?options.message:options.type;
			options.title = options.title?options.title:"";
			return options;
		},
		raise : function(options) {
			var self = this;
			options = self.prepareOptions(options);
			toastr[options.type](options.message, options.title);
		}
	};
}); 