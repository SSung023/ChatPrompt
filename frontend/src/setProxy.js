const { createProxyMiddleware } = require('http-proxy-middleware');

// 13.125.187.231
module.exports = function(app) {
    app.use(
        '/api',
        createProxyMiddleware({
            target: 'http://13.125.187.231:8080',	// 서버 URL or localhost:설정한포트번호
            changeOrigin: true,
        })
    );
};