var http = require('http');
var server = http.createServer(function (req, res) {
	console.log("something comes")
	req.on('data', function(data){
		req.setEncoding('utf-8');
		console.log(data);
		var parsed = JSON.parse(data);
		console.log(parsed);
	});
});

server.listen(1337, '192.168.43.50');
console.log('Server running at http://192.168.43.50:1337/');
