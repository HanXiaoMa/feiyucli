//打包vue配置说明
let webpackConfig;
module.exports = env => {
    console.log("----测试webpack打包流程>>>");
    switch (env.NODE_ENV) {
        case 'prod':
            console.log("----prod>>>");
        case 'production':
            console.log("----production>>>");
            webpackConfig = require('./configs/webpack.prod.conf');
            break;
        case 'demo':
            console.log("----demo>>>");
            webpackConfig = require('./configs/webpack.demo.conf');
            break;
        case 'common':
            console.log("----common>>>");
            webpackConfig = require('./configs/webpack.common.conf');
            break;
        case 'release':
            console.log("----release>>>");
            webpackConfig = require('./configs/webpack.release.conf');
            break;
        case 'dev':
            console.log("----dev>>>");
            webpackConfig = require('./configs/webpack.dev.conf');
            break;
    }
    return webpackConfig;
};
