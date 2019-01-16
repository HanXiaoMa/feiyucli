//基础打包模块
const commonConfig = require('./webpack.common.conf');
const webpackMerge = require('webpack-merge'); // used to merge webpack configs
const os = require('os');
const webpack = require('webpack');
const UglifyJsparallelPlugin = require('webpack-uglify-parallel');
const config = require('./config');
//weex 打包模块
const weexConfig = webpackMerge(commonConfig[0], {
    // 引入的插件  --   可进行注入特定的环境变量等
    plugins: [
        //打包线上地址
        new webpack.DefinePlugin({
            'process.env': {
                'NODE_ENV': config.dev.env,
                'BASE_API': '"https://service-wx.zu.koodsoft.com/"',
                'HTTP_URL': '"https://service.zu.koodapi.com/"',
            }
        }),
        new UglifyJsparallelPlugin({
            workers: os.cpus().length,
            mangle: true,
            compressor: {
                warnings: false,
                drop_console: true,
                drop_debugger: true
            }
        }),
        ...commonConfig[0].plugins
    ]
});
module.exports = [weexConfig];
