import SocketServer


class Handler( SocketServer.BaseRequestHandler ) :
	i = 0;
	def handle( self ) :
		data = self.request[0].strip()
		socket = self.request[1]
		self.i = self.i + 1;
		print "[RECV-%d]::{}", self.i,  data

if __name__ == "__main__" :
	print "dummy udp server binded at localhost:1004"
	server = SocketServer.UDPServer( ("localhost", 1004 ), Handler )
	server.serve_forever()
