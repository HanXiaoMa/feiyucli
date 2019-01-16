//基础打包模块
const commonConfig = require('./webpack.common.conf');
const webpackMerge = require('webpack-merge');
const os = require('os');
const path = require('path');
const webpack = require('webpack');
const UglifyJsparallelPlugin = require('webpack-uglify-parallel');
const config = require('./config');
//weex 打包模块
const weexConfig = webpackMerge(commonConfig[0], {
    output: {
        path: path.join(__dirname, '../dist-demo'),  //输出到demo 文件夹
        filename: '[name].weex'
        //修改成weex 后  文件不压缩  --- 需要在哪里进行配置
    },
    // 引入的插件  --   可进行注入特定的环境变量等
    plugins: [
        //打包线上地址
        new webpack.DefinePlugin({
            'process.env': {
                'NODE_ENV': config.dev.env,
                'BASE_API': '"http://192.168.10.65:8878/"',
                'HTTP_URL': '"https://service-api.kood-lease.net/"',
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
