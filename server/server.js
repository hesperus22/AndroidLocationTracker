var http = require('http');
var server = http.createServer(function (req, res) {
	console.log("something comes")
	var requestData='';
	req.setEncoding('utf-8');
	req.on('data', function(data){
		requestData += data;
	});
	req.on('end', function(){
		res.writeHead(200);
		res.end();
		var parsed = JSON.parse(requestData);
		console.log(parsed);	
	});

});

server.listen(1337, '192.168.43.50');
console.log('Server running at http://192.168.43.50:1337/');
