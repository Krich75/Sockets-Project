CLIENT=client
LIB_SERVER=lib/libServerJava
LIB_SSL_HELPERS=lib/libSSLHelpersJava
SERVER_C=server/serverC
SERVER_JAVA=server/serverJAVA

.PHONY: all run clean mrproper

all: CLIENT SERVER_C LIB_SERVER LIB_SSL_HELPERS SERVER_JAVA

CLIENT: LIB_SSL_HELPERS
	$(MAKE) -C $(CLIENT)

SERVER_C:
	$(MAKE) -C $(SERVER_C)

LIB_SERVER:
	$(MAKE) -C $(LIB_SERVER)

LIB_SSL_HELPERS:
	$(MAKE) -C $(LIB_SSL_HELPERS)

SERVER_JAVA: LIB_SERVER LIB_SSL_HELPERS
	$(MAKE) -C $(SERVER_JAVA)

clean_server_c:
	$(MAKE) -C $(SERVER_C) clean

clean_lib_server:
	$(MAKE) -C $(LIB_SERVER) clean

clean_lib_ssl_helpers:
	$(MAKE) -C $(LIB_SSL_HELPERS) clean

clean_client:
	$(MAKE) -C $(CLIENT) clean

clean_server_java:
	$(MAKE) -C $(SERVER_JAVA) clean

clean : clean_server_java clean_client clean_lib_server clean_lib_ssl_helpers clean_server_c
