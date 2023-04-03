const { createProxyMiddleware } = require('http-proxy-middleware');

// 43.200.176.184
module.exports = function(app) {
    app.use(
        '/api',
        createProxyMiddleware({
            target: 'http://43.200.176.184:8080',	// 서버 URL or localhost:설정한포트번호
            changeOrigin: true,
        })
    );
};