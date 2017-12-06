import socket
import sys
import struct
import binascii

sock = socket.socket( socket.AF_INET, socket.SOCK_STREAM )

server_name = 'localhost' 
server_address = ( server_name, 2272 )

print 'TCP Server binded at %s:%s' % server_address

sock.bind( server_address )
sock.listen( 1 )

acuState = [ 0x01, 0x02, 0xd1, 0x07, 0x00, 0x00, 0x00, 0x00, 0x02, 0x05, 0x00, 0x00, 0x76, 0x5e, 0x05, 0x0f, 0xc0, 0xa8, 0x00, 0x64, 0xc0, 0xa8, 0x00, 0x63, 0x3d, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xc0, 0xa8, 0x00, 0x61, 0x07, 0xd1, 0x01, 0x00, 0x00, 0x00 ]
acuName = [0] * 51
acuResult = acuState + acuName
#acuResultBin = struct.pack( 'B' * len( acuResult ), *acuResult )
acuResultBin = ''
while True :
	connection, client_address = sock.accept()
	try :
		print 'client connected:', client_address
		while True :
			data = connection.recv( 32 )
			print binascii.b2a_hex( data ) 
			if data :
				connection.sendall( acuResultBin )
			else :
				break
	finally :
		connection.close()
			

