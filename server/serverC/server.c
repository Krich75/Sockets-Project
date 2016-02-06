#include <stdio.h>
#include <netinet/in.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <errno.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <unistd.h>
#include <ctype.h>

#define MAX_SIZE 1024

const char *VOYELLES = "aeiouy";

const char *CONSONNES = "bcdfghjklmnpqrstvwxz";

static pthread_mutex_t mutex_sock = PTHREAD_MUTEX_INITIALIZER;

int isSomething(char c, const char *smthg){
	unsigned int i;
	c = tolower(c);
	for (i=0; i<strlen(smthg); i++)
		if (c == smthg[i])
			return 1;
	return 0;
}

int isVoyelle(char c){
	return isSomething(c, VOYELLES);
}

int isConsonne(char c){
	return isSomething(c, CONSONNES);
}

int count(char *message, int messageLength, int (*method) (char)){
	int i, result = 0;
	for (i=0; i<messageLength; i++)
		result += (int) method(message[i]);
	return result;
}

void *threadClient(void *arg){
	
	int sock = *((int *) arg);

	pthread_mutex_unlock(&mutex_sock);

	char buffer[MAX_SIZE+1];
	int freeBuff = MAX_SIZE, tmp, result;
	
	puts("Un client s'est connecté.");

	do {
		
		if (freeBuff == 0) {
			puts("Client sent a too long message.");
			write(sock, "-1 Message too long\r\n", 21);
			close(sock);
			pthread_exit(NULL);
		}

		tmp = read(sock, buffer + (MAX_SIZE - freeBuff), freeBuff);
		
		if (tmp == -1){
			perror("read");
			pthread_exit(&errno);
		}
		
		freeBuff -= tmp;
		buffer[MAX_SIZE - freeBuff] = 0x00;	

	} while (buffer[MAX_SIZE-freeBuff-2] != '\r'
		&& buffer[MAX_SIZE-freeBuff-1] != '\n');
	
	printf("Client sent : %s", buffer);
	
	if (strncmp(buffer, "voy", 3) == 0){
		result = count(buffer+4, MAX_SIZE-freeBuff-2, isVoyelle);
	} else if (strncmp(buffer, "con", 3) == 0){
		result = count(buffer+4, MAX_SIZE-freeBuff-2, isConsonne);
	} else {
		write(sock, "-1 Protocol error\r\n", 19);
		close(sock);
		pthread_exit(NULL);
	}

	tmp = snprintf(buffer, MAX_SIZE+1, "%d\r\n", result);
	write(sock, buffer, tmp);
	close(sock);

	pthread_exit(NULL);
}

int main(int argc, char **argv) {
	
	pthread_t thread;
	
	char *endptr, *str;
	int loop = 1;
	int port = 8888;
	int sock, sock_client, sin_client_len;
	struct sockaddr_in sin_server, sin_client;

	if (argc > 1){
		str = argv[1];
		port = strtol(str, &endptr, 10);

		/* Vérification de plusieurs erreurs possibles */

		if (errno == ERANGE) {
			printf("Invalid port number.\nUsage: %s [port]\n", argv[0]);
			exit(1);
		}

		if (endptr == str) {
			printf("Invalid port number.\nUsage: %s [port]\n", argv[0]);
			exit(1);
		}

		if (port < 0 || port > 65535){
			printf("Invalid port number.\nUsage: %s [port]\n", argv[0]);
			exit(1);
		}
	}

	sin_server.sin_addr.s_addr = htonl(INADDR_ANY);   
	sin_server.sin_family = AF_INET;
	sin_server.sin_port = htons(port);
	
	sock = socket(AF_INET, SOCK_STREAM, 0);
	
	if (sock == -1){
		perror("socket");
		exit(errno);
	}
	
	/** Avoid error "Address already in use".
	int yes = 1;
	if ( setsockopt(sock, SOL_SOCKET, SO_REUSEADDR, &yes, sizeof(int)) == -1 )
	{
		perror("setsockopt");
		exit(errno);
	}**/

	if (bind(sock, (struct sockaddr*) &sin_server, sizeof(struct sockaddr_in)) == -1){
		perror("bind");
		exit(errno);
	}
	
	if (listen(sock, 20) == -1){
		perror("listen");
		exit(errno);
	}
	
	printf("Listening on %d...\n", ntohs(sin_server.sin_port));

	while (loop){
		
		pthread_mutex_lock(&mutex_sock);	

		sock_client = accept(sock, (struct sockaddr*) &sin_client, (socklen_t *) &sin_client_len);
		
		if (sock_client == -1){
			perror("accept");
			exit(errno);
		}
		
		if (pthread_create(&thread, NULL, threadClient, &sock_client) != 0){
			puts("Impossible de créer un thread pour traiter le client.");
			exit(1);
		}
	}

	return 0;
}
