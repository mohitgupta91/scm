/**
 * Description
 * @method exports
 * @param {} grunt
 * @return
 */
module.exports = function (grunt) {

  var pkg = grunt.file.readJSON('package.json');
  // Project configuration.
  grunt.initConfig({
    pkg: pkg,
    uglify: {
      options: {
        compress: false,
        mangle: false,
        //beautify: true,
        banner: '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
      },
      my_target: {
        options: {
          footer: ""
        },
        files: {
        }
      }
    },
      sass: {
          options: {
              sourceMap: false,
              outputStyle: 'nested',
              sourceComments: false
          },
          dist: {
              files: {
                  'dist/style/main.css': 'src/scss/main.scss'
              }
          },
          extfiles :{
              files: {
                  'src/external/spamjs-datatable/spamjs.datatable.css': 'src/external/spamjs-datatable/spamjs.datatable.scss'
              }
          }
      },
      sprite: {
          all: {
              src: 'src/img/uniicons/*.png',
              dest: 'src/img/uniicons-' + new Date().getTime() + '.png',
              destCss: 'src/scss/_sprites.scss',
              padding: 10,
              cssTemplate: 'src/img/uniicons/template.handlebars'
          }
      },
      watch: {
          sass: {
              files: [
                  'src/modules/**/*.scss'
              ],
              tasks: ['sass:dist'],
              options: {
                  // Sets livereload to true for livereload to work
                  // (livereload is not covered in this article)
                  livereload: false
              }
          }
      },
    bootloader: {
      options: {
        indexBundles: ["webmodules/bootloader", "controltower/app"],// ["webmodules/bootloader","unicom/external","unicom/abstracts"],
        src: "./",
        dest: "dist",
        resourcesFile: "resource.json",
        resourceJson: "dist/resource.json",
        resourcesInline : true,
        livereloadUrl: "http://localhost:8081/livereload.js",
        bootServer: {
          port: 8087
        }
      }
    },
    jsbeautifier: {
      files: ["src/**/*.js", "!src/external/components/**/*.js"],
      options: pkg.jsbeautifier
    },
    jshint: {
      files: ["src/**/*.js", "!src/external/components//**/*.js"],
      options: {
        globals: {
          jQuery: true,
          define: true,
          module: true,
          _importStyle_: true,
          is: true,
          when: true
        }
      }
    },
    webfont: {
      icons: {
        src: 'src/img/custom-icons/*.svg',
        dest: 'src/fonts/',
        destCss: 'src/fonts/style',
        options: {
          font: 'icons',
          stylesheet: 'scss',
          relativeFontPath: "../../src/fonts/",
          htmlDemo: false,
          hashes: true
        }
      }
    },
    cssmin: {
      options: {
        target: "control-tower-webapp/dist/style",
        advanced: true,
        keepSpecialComments: 0
      },
      target: {
        files: {
          'dist/style/library.css': ['src/external/components/webmodules-bootstrap/css/bootstrap.min.css',
            'src/external/components/jqmodules-x-editable/dist/bootstrap3-editable/css/bootstrap-editable.css',
            'src/external/components/jqtags-jq-date/jqtags.date.css',
            'src/external/components/webmodules-jqtag/jqtag.css',
            'src/external/components/jqmodules-bootstrap-select/dist/css/bootstrap-select.min.css',
            'src/external/components/jqmodules-select2/select2.css',
            'src/external/components/bootstrap-material-design/dist/css/ripples.min.css',
            'src/external/spamjs-datatable/spamjs.datatable.css'
           ]
        }
      }
    }
  });

  // Load the plugin that provides the "uglify" task.
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-connect');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-jsbeautifier');
  grunt.loadNpmTasks('grunt-webfont');
  grunt.loadNpmTasks('grunt-contrib-cssmin');
  grunt.loadNpmTasks('grunt-bootloader');
  grunt.loadNpmTasks('grunt-spritesmith');
  grunt.loadNpmTasks('grunt-sass');

  // Default task(s).
  grunt.registerTask('default', ['uglify', 'cssmin']);

  // Custom task
  grunt.registerTask('start-cdn-server', ['bootloader:server', 'watch']);
  grunt.registerTask('scan', ['bootloader:scan:skip','sass:dist', 'cssmin']);
  grunt.registerTask('bundlify', ['bootloader:bundlify', 'sass:dist', 'cssmin']);
  grunt.registerTask('build', ['bundlify']);
  grunt.registerTask('check', ["jshint", 'jsbeautifier']);

};