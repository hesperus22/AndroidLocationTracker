var http = require('http');
var server = http.createServer(function (req, res) {
	console.log("something comes")
	req.on('data', function(data){
		console.log(data);
	});
});

server.listen(1337, '192.168.43.50');
console.log('Server running at http://192.168.43.50:1337/');
