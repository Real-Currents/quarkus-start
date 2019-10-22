const fs = require('fs');
const path = require('path');
//const BrowserSyncPlugin = require('browser-sync-webpack-plugin');
const ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = {
    entry: {
        app: [ './main.ts' ]
    },
    cache: false,
    devServer: {
        inline: false
//        contentBase: path.join(__dirname, ''),
//        compress: true,
//        lazy: true,
//        port: 9000
    },
//    devtool: 'inline-source-map',
    module: {
        rules: [
            {
                test: /\.js?$/,
                exclude: /(node_modules|bower_components)/,
                loader: 'babel-loader'
            },
            {
                test: /.json$/,
                loader: 'json-loader'
            },
            {
                test: /\.ts$/,
                exclude: /node_modules/,
                loader: 'ts-loader'
            },
            {
                test: /\.css$/,
                include: path.join(__dirname, ''),
                loader: 'style-loader!css-loader'
                // loader: 'typings-for-css-modules-loader?modules&namedExport'
            },
            {
                test: /\.scss$/,
                // Query parameters are passed to node-sass
                loader: 'style!css!compass?outputStyle=expanded&' +
                'includePaths[]=' +
                (path.resolve(__dirname, './node_modules'))
            },
            {
                test: /\.svg$/,
                loader: 'svg-inline-loader'
            }
        ]
    },
    output: {
        filename: './scripts/[name].js',
        path: path.resolve(__dirname, '')
    },
    plugins: [
//        new BrowserSyncPlugin(
//            {
//                browser: [
//                    (fs.existsSync('/Applications/Firefox Developer Edition.app/Contents/MacOS/firefox')) ?
//                        '/Applications/Firefox Developer Edition.app/Contents/MacOS/firefox' :
//                        (fs.existsSync('C:\\Program Files\\Firefox Developer Edition\\firefox.exe')) ?
//                            'C:\\Program Files\\Firefox Developer Edition\\firefox.exe' :
//                            'firefox',
//                ],
//                files: [{
//                    match: [ './modules/*.js' ],
//                    fn: function(event, file) {
//                        if (event === "change") {
//                            const bs = require('browser-sync').get('bs-webpack-plugin');
//                            bs.reload();
//                        }
//                    }
//                }],
//                injectChanges: true,
//                notify: true,
//                host: 'localhost',
//                port: 3000,
//                proxy: "localhost:8080",
////                server: {
////                    baseDir: [ path.resolve(__dirname, '') ]
////                },
//                startPath: '/'
//            },
//            // plugin options
//            {
//                // prevent BrowserSync from reloading the page
//                // and let Webpack Dev Server take care of this
//                reload: false
//            }
//        ),
        new ExtractTextPlugin('styles/mapscripts.css')
    ],
    resolve: {
        extensions: [ '.css', '.scss', '.ts', '.js' ]
    }
};
