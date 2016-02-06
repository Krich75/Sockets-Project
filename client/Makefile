NAME=client

MAIN_CLASS=launch/Main

LIB_SSL_HELPERS=../lib/libSSLHelpersJava

RSC_DIR=./resources
SRC_DIR=./src
CLS_DIR=./build/classes
MANIFEST_FILE=build/META-INF.MF
JAR_FILE=./build/$(NAME).jar

SRC_FILES=$(shell find $(SRC_DIR) -iname "*.java")
CLS_FILES=$(patsubst $(SRC_DIR)/%,$(CLS_DIR)/%,$(patsubst %.java,%.class,$(SRC_FILES)))

.PHONY: jar clean mrproper

jar : $(JAR_FILE)

lib_ssl_helpers: $(CLS_DIR)
	$(MAKE) -C $(LIB_SSL_HELPERS)
	cp -r $(LIB_SSL_HELPERS)/build/classes/* ./build/classes

$(JAR_FILE) : $(MANIFEST_FILE) lib_ssl_helpers $(CLS_FILES) ressources
	jar cmf $< $@ -C $(CLS_DIR) .


$(MANIFEST_FILE) :
	mkdir -p $(dir $@)
	echo Main-Class: $(MAIN_CLASS) > $@

$(CLS_DIR) :
	mkdir -p $(CLS_DIR)

$(CLS_DIR)/%.class : $(SRC_DIR)/%.java $(CLS_DIR)
	javac -d $(CLS_DIR) -cp $(CLS_DIR) -sourcepath $(SRC_DIR) $<

ressources: $(CLS_DIR)
	cp -r $(RSC_DIR)/* $(CLS_DIR) 

clean : 
	rm -Rf ./build
