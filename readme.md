# Distributed Computing - Practice 1
 
 ## Colaborating Application
 
 This program is a simplification of bingo game using multicast
 communication.
 
 **Features:**
 
 - Client application must generate 6 numbers between 1 
 and 49 (the bingo card) and show them.
 - Server application must generate one random number
  per second (between 1 and 49 too).
 - When a client completes his bingo card, he must send a
 multicast message with his numbers to the server. The server
 must check if is the real winner.
 
 **Execution:**
 
 *Note: firsly, run the clients and then, the server.* 
 
 - To run the client:
    
        $ cd /out/artifacts/Client_jar
        $ java -jar Client.jar
        
 - To run the server:
     
         $ cd /out/artifacts/Server_jar
         $ java -jar Server.jar