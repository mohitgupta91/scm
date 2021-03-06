define({
  name: "spamjs.modal",
  extend: "spamjs.view",
  modules: ["jqtag"]
}).as(function(modal, jqtag) {

  /**
   *       this.add(modal.instance({
   *          id : "my_module",
   *          options : {
   *          title : "My Module",
   *          module : MyModule.instance()
   *        }
   *      }));
   *
   */
  return {
    events: {
      // "hidden.bs.modal #myModal" : "modalClosed"
    },
    _init_: function() {
      var self = this;

      var dfd = $.Deferred();

      this.$$.loadView(this.path("modal.html"), this.options).done(function() {
        if (is.String(self.options.module)) {
          module(self.options.module, function(embedModule) {
            self.add("#module-body", embedModule.instance(self.options.moduleOptions));
            dfd.resolve();
          });
        } else if (self.options.src !== "" && self.options.src !== undefined) {
          self.load({
            selector: ".modal-body",
            src: "../modules/" + self.options.src,
            data: (self.options.data !== undefined) ? self.options.data : {}
          }).done(function() {
            dfd.resolve();
          });
        } else {
          self.add("#module-body", self.options.module);
          dfd.resolve();
        }
        self.$modal = this.find("#myModal").modal();
        self.$modal.on('hidden.bs.modal', function(e) {
          console.error("----");
          jQuery(this).data('bs.modal', null);
          jqtag.trigger(self.$$[0], "spamjs.model.closed");
        });
      });
      return dfd.promise();
    },
    _remove_: function() {
      console.error("helo", this.$$.find("#myModal"), this.$modal);
      this.$modal.modal("hide");
      console.error("after");
    },
    modalClosed: function() {
      console.error("modal closed");
    },

    /**
     * confirm: Provides the javascript window.confirm functionality using bootstrap modal
     *
     * $this - view obj where you want to add
     * options: 
     * title - Title to be displayed
     * desc - Description
     * buttonLabel - Name of the button
     *
     * @output - Provides true if clicked on buttonLabel button else false
     */
    confirm: function($this, options) {
      var _options = {
        title: "",
        desc: "",
        buttonLabel: ""
      };
      options = $.extend(true, _options, options);
      var _dfd_ = $.Deferred();

      var modalInstance = this.instance({
        src: "../external/modal.confirm.html",
        data: options
      });

      $this.add("#myModal", modalInstance).done(function() {
        var self = this;
        var $modal = $('#myModal');
        $modal.on('hidden.bs.modal', function() {
          $(this).closest('view').remove();
        });
        self.$$.find('.css-delete-buttons span').on('click', function() {
          if ($(this).find('button').length > 0) {
            _dfd_.resolve(true, self.$$);
          } else {
            _dfd_.resolve(false, self.$$);
          }
          $modal.modal('hide');

          $(this).off('click');
        });
      });
      return _dfd_.promise();

    }
  };

});
