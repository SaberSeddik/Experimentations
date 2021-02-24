const https = require('https');
const fs = require('fs');
require('debug')

const hostname = 'localhost';
const port = 3000;

const options = {
    ca: fs.readFileSync('certificates/ca.crt'),
    cert: fs.readFileSync('certificates/server.crt'),
    key: fs.readFileSync('certificates/server.key'),
    rejectUnauthorized: true,
    requestCert: true,
};

const server = https.createServer(options, (req, res) => {
  console.log(`request received`);
  res.statusCode = 200;
  res.setHeader('Content-Type', 'text/plain');
  res.end('Hello World');
});

server.listen(port, hostname, () => {
  console.log(`Server running at http://${hostname}:${port}/`);
});