----JAVA----

The goal of this little poeject is to use the socket and try ro replicate a TCP comunication.
	The server is always listening and waiting for a CLIENT_HELLO
	Handshake:	
		The client send the CLIENT_HELLO
		The server catch the CLIENT_HELLO, if it corrispond to his CLIENT_HELLO it's sent the SERVER_HELLO
		They exchange their ACK
	Calculation:
		The client listen to the server request 
		The server handle all the mathematical calculations.

The comunication is based on sending and reciving Object 	
