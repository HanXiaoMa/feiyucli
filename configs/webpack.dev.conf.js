const commonConfig = require('./webpack.common.conf');
const webpackMerge = require('webpack-merge');
const webpack = require('webpack');
const config = require('./config');
const weexConfig = webpackMerge(commonConfig[0], {
    plugins: [
        new webpack.DefinePlugin({
            'process.env': {
                'NODE_ENV': config.dev.env,
                'BASE_API': '"http://192.168.10.65:8878/"',
                'HTTP_URL': '"https://service-api.kood-lease.net/"',
            }
        }),
        ...commonConfig[0].plugins
    ]
});
module.exports = [weexConfig];
