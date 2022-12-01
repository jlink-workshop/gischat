// Karma configuration
// Generated on Thu Oct 21 2021 10:16:59 GMT+0200 (Mitteleuropäische Sommerzeit)

// eslint-disable-next-line no-undef
module.exports = function(config) {
  config.set({

    // base path that will be used to resolve all patterns (eg. files, exclude)
    basePath: '',


    // frameworks to use
    // available frameworks: https://www.npmjs.com/search?q=keywords:karma-adapter
    frameworks: ['jasmine'],


    // list of files / patterns to load in the browser
    files: [
      { pattern: 'src/js/*.js', included: false },
      { pattern: 'test/**/*-tests.js', type: 'module' },
      //{ pattern: 'node_modules/**/*.js', included: false },
    ],


    // list of files / patterns to exclude
    exclude: [
    ],


    // preprocess matching files before serving them to the browser
    // available preprocessors: https://www.npmjs.com/search?q=keywords:karma-preprocessor
    preprocessors: {
    },


    // test results reporter to use
    // possible values: 'dots', 'progress'
    // available reporters: https://www.npmjs.com/search?q=keywords:karma-reporter
    reporters: ['progress', 'junit'],

    junitReporter: {
          outputDir: 'build/test-results/test', // results will be saved as $outputDir/$browserName.xml
          outputFile: undefined, // if included, results will be saved as $outputDir/$browserName/$outputFile
          suite: '', // suite will become the package name attribute in xml testsuite element
          useBrowserName: false, // add browser name to report and classes names
          nameFormatter: undefined, // function (browser, result) to customize the name attribute in xml testcase element
          classNameFormatter: undefined, // function (browser, result) to customize the classname attribute in xml testcase element
          properties: {}, // key value pair of properties to add to the <properties> section of the report
          xmlVersion: null // use '1' if reporting to be per SonarQube 6.2 XML format
    },

    // web server port
    port: 9876,


    // enable / disable colors in the output (reporters and logs)
    colors: true,


    // level of logging
    // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
    logLevel: config.LOG_INFO,


    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,


    // start these browsers
    // available browser launchers: https://www.npmjs.com/search?q=keywords:karma-launcher
    browsers: ['ChromeHeadless'],


    // Continuous Integration mode
    // if true, Karma captures browsers, runs the tests and exits
    singleRun: false,

    // Concurrency level
    // how many browser instances should be started simultaneously
    concurrency: Infinity
  })
}
