const { createProxyMiddleware } = require('http-proxy-middleware');

// 3.36.115.5
module.exports = function(app) {
    app.use(
        '/api',
        createProxyMiddleware({
            target: 'http://3.36.115.5:8080',	// 서버 URL or localhost:설정한포트번호
            changeOrigin: true,
        })
    );
};