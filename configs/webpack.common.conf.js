const path = require('path');
const fs = require('fs-extra');
const webpack = require('webpack');
const config = require('./config');
const helper = require('./helper');
const glob = require('glob');
const vueLoaderConfig = require('./vue-loader.conf');
const vueWebTemp = helper.rootNode(config.templateDir);
const isWin = /^win/.test(process.platform);
const weexEntry = {};

const getNativeEntryFileContent = (entryPath, vueFilePath) => {
  let relativeVuePath = path.relative(path.join(entryPath, '../'), vueFilePath);
  let contents = '';
  if (isWin) {
    relativeVuePath = relativeVuePath.replace(/\\/g, '\\\\');
  }
  contents += `import App from '${relativeVuePath}'
  App.el = '#root'
  new Vue(App)
  `;
  return contents;
};

// const getEntryFile = (dir) => {
//   dir = dir || config.sourceDir;
//   const entries = glob.sync(`${dir}/${config.entryFilter}`, config.entryFilterOptions);
//   console.log("--->>entries>>" +JSON.stringify(entries));
//   entries.forEach(entry => {
//     const extname = path.extname(entry);
//     const basename = entry.replace(`${dir}/`, '').replace(extname, '');
//     const templatePathForNative = path.join(vueWebTemp, basename + '.js');
//       console.log("--->>templatePathForNative>>" +JSON.stringify(templatePathForNative));
//     fs.outputFileSync(templatePathForNative, getNativeEntryFileContent(templatePathForNative, entry));
//     console.log("---需要编译的文件>>>  "+ basename+ '  ==   '+JSON.stringify(templatePathForNative));
//     weexEntry[basename] = templatePathForNative;
//   })
// };
// getEntryFile();

function getEntries() {
    const entryFiles = glob.sync('./src/entry/**', {'nodir': true})
    for (let i = 0; i < entryFiles.length; i++) {
        let filePath = entryFiles[i];
        let filename = filePath.split('entry/')[1];
        filename = filename.substr(0, filename.lastIndexOf('.'));
        weexEntry[filename] = filePath;
    }
}
getEntries();

const plugins = [
  new webpack.BannerPlugin({
    banner: '// { "framework": "Vue"} \n',
    raw: true,
    exclude: 'Vue'
  })
];  // web的 和  weex 都已经 使用配置


const weexConfig = {
  entry: weexEntry, //入口配置
  //输出位置
  output: {
    path: path.join(__dirname, '../dist'),
    filename: '[name].weex'
      //修改成weex 后  文件不压缩  --- 需要在哪里进行配置
  },
  resolve: {
    extensions: ['.js', '.vue', '.json'],
    alias: {
      '@': helper.resolve('src')
    }
  },
    //添加的模块
  module: {
      loaders: [
          {
              test: /\.js$/,
              loader: 'babel',
              exclude: /node_modules/
          }, {
              test: /\.vue(\?[^?]+)?$/,
              loaders: []
          }, {
              test: /\.scss$/,
              loader: 'style!css!sass'
          }
      ],
    rules: [
      {
        test: /\.js$/,
        use: [{
          loader: 'babel-loader'
        }],
        exclude: config.excludeModuleReg
      },
      {
        test: /\.vue(\?[^?]+)?$/,
        use: [{
          loader: 'weex-loader',
          options: vueLoaderConfig({useVue: false})
        }],
        exclude: config.excludeModuleReg
      }
    ]
  },
    //添加的插件
  plugins: plugins,
   //node 编译配置
  node: config.nodeConfiguration
};
module.exports = [ weexConfig];
