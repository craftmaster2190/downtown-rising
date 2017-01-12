//GLOBALS
var gulp = require('gulp'),
    plugins = require('gulp-load-plugins')(),
    mainBowerFiles = require('main-bower-files'),
    series = require('stream-series'),
    del = require('del'),
    jsonfile = require('jsonfile'),
    xml2js = require('xml2js'),
    fs = require('fs');

//PATHS
paths = {};
paths.gulpfile = 'gulpfile.js';
paths.index = 'src/index.html';
paths.js = 'src/**/*.js';
paths.scss = 'src/**/*.scss';
paths.indexScss = 'src/index.scss';
paths.images = 'src/images/*';
paths.componentsHtml = 'src/components/**/*.html';
paths.dist = '../src/main/resources/static';
paths.distLib = paths.dist + '/lib';
paths.distFonts = paths.dist + '/fonts';
paths.distHtml = paths.dist + '/components';
paths.distImage = paths.dist + '/images';

//PIPE FUNCTIONS
function cssDist() {
    return gulp.src(paths.indexScss)
        .pipe(plugins.plumber())
        .pipe(plugins.sass())
        .pipe(plugins.autoprefixer())
        .pipe(gulp.dest(paths.dist))
        .pipe(plugins.print());
}

function cssProd() {
    return gulp.src(paths.scss)
        .pipe(plugins.plumber())
        .pipe(plugins.sass())
        .pipe(plugins.autoprefixer())
        .pipe(plugins.cssnano())
        .pipe(plugins.concat('index.min.css'))
        .pipe(gulp.dest(paths.dist))
        .pipe(plugins.print());
}

function jsDist() {
    return gulp.src(paths.js)
        .pipe(plugins.plumber())
        .pipe(plugins.jshint())
        .pipe(plugins.jshint.reporter('default'))
        .pipe(gulp.dest(paths.dist))
        .pipe(plugins.print());
}

function jsProd() {
    return gulp.src(paths.js)
        .pipe(plugins.plumber())
        .pipe(plugins.jshint())
        .pipe(plugins.jshint.reporter('default'))
        .pipe(plugins.iife())
        .pipe(plugins.ngAnnotate())
        .pipe(plugins.concat('index.min.js'))
        .pipe(plugins.uglify())
        .pipe(gulp.dest(paths.dist))
        .pipe(plugins.print());
}

function htmlDist() {
    return gulp.src(paths.componentsHtml)
        .pipe(plugins.plumber())
        .pipe(gulp.dest(paths.distHtml))
        .pipe(plugins.print());
}

function htmlProd() {
    return gulp.src(paths.componentsHtml)
        .pipe(plugins.plumber())
        // .pipe(plugins.htmlmin({
        //     collapseWhitespace: false
        // }))
        .pipe(plugins.ngTemplates({
            filename: 'templates.js',
            standalone: false,
            module: 'rising',
            path: function (path, base) {
                return path.replace(base, 'components/');
            }
        }))
        .pipe(gulp.dest(paths.dist))
        .pipe(plugins.print());
}

function imageDist() {
    return gulp.src(paths.images)
        .pipe(plugins.plumber())
        .pipe(gulp.dest(paths.distImage))
        .pipe(plugins.print());
}

function imageProd() {
    return gulp.src(paths.images)
        .pipe(plugins.plumber())
        .pipe(plugins.imagemin())
        .pipe(gulp.dest(paths.distImage))
        .pipe(plugins.print());
}

function bowerDist() {
    return gulp.src(mainBowerFiles())
        .pipe(plugins.plumber())
        .pipe(plugins.order(['jquery.js', 'bootstrap.js', 'angular.js']))
        .pipe(gulp.dest(paths.distLib))
        .pipe(plugins.print());
}

function bowerProd() {
    var bowerJs = gulp.src(mainBowerFiles('**/*.js'))
        .pipe(plugins.plumber())
        .pipe(plugins.order(['jquery.js', 'bootstrap.js', 'angular.js']))
        .pipe(plugins.concat('vendor.min.js'))
        .pipe(plugins.uglify())
        .pipe(gulp.dest(paths.dist))
        .pipe(plugins.print());
    var bowerCss = gulp.src(mainBowerFiles('**/*.css'))
        .pipe(plugins.cssnano())
        .pipe(plugins.concat('vendor.min.css'))
        .pipe(gulp.dest(paths.dist))
        .pipe(plugins.print());
    return series(bowerJs, bowerCss);
}


function fontsDist() {
    return gulp.src('bower_components/bootstrap/dist/fonts/*')
        .pipe(plugins.plumber())
        .pipe(gulp.dest(paths.distFonts))
        .pipe(plugins.print());
}

function indexHtmlDist() {
    return gulp.src(paths.index)
        .pipe(plugins.plumber())
        .pipe(gulp.dest(paths.dist));
}

function indexHtmlBuiltDist() {
    return indexHtmlDist()
        .pipe(plugins.plumber())
        .pipe(plugins.inject(
            series(bowerDist(), cssDist(), jsDist()), {
                relative: true
            }))
        .pipe(plugins.injectReload())
        .pipe(gulp.dest(paths.dist))
        .pipe(plugins.print());
}

function indexHtmlBuiltProd() {
    return indexHtmlDist()
        .pipe(plugins.plumber())
        .pipe(plugins.inject(
            series(bowerProd(), cssProd(), jsProd(), htmlProd()), {
                relative: true
            }))
        .pipe(gulp.dest(paths.dist))
        .pipe(plugins.print());
}

function buildDist() {
    return series(indexHtmlBuiltDist(), htmlDist(), fontsDist(), imageDist());
}

function buildProd() {
    return series(indexHtmlBuiltProd(), fontsDist(), imageDist());
}

function validateGulpfile() {
    return gulp.src(paths.gulpfile)
        .pipe(plugins.plumber())
        .pipe(plugins.jshint())
        .pipe(plugins.jshint.reporter('default'))
        .pipe(plugins.print());
}

function bumpVersion(gulpDone) {
    var package = jsonfile.readFileSync('package.json', 'utf8'),
        bower = jsonfile.readFileSync('bower.json', 'utf8'),
        pom = fs.readFileSync('../pom.xml', 'utf8'),
        xmlOptions = {
            preserveChildrenOrder: true
        },
        jsonOptions = {
            spaces: 2
        },
        initialVersion = package.version;

    transformVersion();
    updateJsonFiles();
    updatePomFile();

    console.log(initialVersion + ' => ' + package.version);
    gulpDone();

    function transformVersion() {
        var version = package.version.split('.');
        for (var i = version.length - 1; i >= 0; i--) {
            version[i]++;
            if (i != 0 && version[i] >= 10) {
                version[i] = 0;
            } else {
                break;
            }
        }
        package.version = version.join('.');
    }

    function updateJsonFiles() {
        bower.version = package.version;
        jsonfile.writeFileSync('package.json', package, jsonOptions);
        jsonfile.writeFileSync('bower.json', bower, jsonOptions);
    }

    function updatePomFile() {
        // xml2js callback is synchronous
        new xml2js.Parser(xmlOptions).parseString(pom, function (err, result) {
            result.project.version[0] = package.version;
            var xml = new xml2js.Builder(xmlOptions).buildObject(result);
            fs.writeFileSync('../pom.xml', xml);
        });
    }
}

//TASKS
gulp.task('js:dist', jsDist);
gulp.task('js:prod', jsProd);
gulp.task('css:dist', cssDist);
gulp.task('css:prod', cssProd);
gulp.task('image:dist', imageDist);
gulp.task('image:prod', imageProd);
gulp.task('bower:dist', bowerDist);
gulp.task('bower:prod', bowerProd);
gulp.task('html:dist', htmlDist);
gulp.task('html:prod', htmlProd);

//BUILDERS
gulp.task('clean', function () {
    return del(paths.dist + '/*', {
        force: true
    });
});
gulp.task('build:dist', ['clean', 'validate-gulp-file'], buildDist);

gulp.task('build:prod', ['clean', 'validate-gulp-file'], buildProd);

//WATCHERS
gulp.task('watch', ['build:dist'], function () {
    plugins.watch(paths.js, function (vinyl) {
        handleAddFiles(vinyl.event);
        return jsDist();
    });
    plugins.watch(paths.scss, function (vinyl) {
        return cssDist();
    });
    plugins.watch(paths.images, function (vinyl) {
        return imageDist();
    });
    plugins.watch(paths.componentsHtml, function (vinyl) {
        handleAddFiles(vinyl.event);
        return htmlDist();
    });
    plugins.watch(paths.index, function (vinyl) {
        handleAddFiles(vinyl.event);
        return indexHtmlBuiltDist();
    });

    function handleAddFiles(event) {
        if (event === 'add' || event === 'unlink') {
            indexHtmlBuiltDist();
        }
    }
});

//MISC
gulp.task('validate-gulp-file', validateGulpfile);
gulp.task('default', ['watch']);

gulp.task('bump', bumpVersion);
