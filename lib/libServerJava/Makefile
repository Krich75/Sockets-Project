SRC_DIR=./src
CLS_DIR=./build/classes

SRC_FILES=$(shell find $(SRC_DIR) -iname "*.java")
CLS_FILES=$(patsubst $(SRC_DIR)/%,$(CLS_DIR)/%,$(patsubst %.java,%.class,$(SRC_FILES)))

.PHONY: all clean mrproper

all:  $(CLS_FILES)


$(MANIFEST_FILE) :
	mkdir -p $(dir $@)
	echo 'Manifest-Version: 1.0' > $@

$(CLS_DIR) :
	mkdir -p $(CLS_DIR)

$(CLS_DIR)/%.class : $(SRC_DIR)/%.java $(CLS_DIR)
	javac -d $(CLS_DIR) -sourcepath $(SRC_DIR) $<

clean : 
	rm -Rf ./build
