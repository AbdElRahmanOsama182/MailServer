module.exports = {
  "transpileDependencies": [
    "vuetify"
  ],
  chainWebpack: config => config.resolve.extensions.prepend('.mjs').prepend('.wasm')
  
}