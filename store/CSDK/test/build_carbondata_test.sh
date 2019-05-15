export CFLAGS=`pkg-config --cflags thrift`" -g3 -O0"
export LDFLAGS=`pkg-config --libs-only-L thrift`
export LIBS=`pkg-config --libs-only-l thrift`

export FORMAT_FILE_PATH=../../../format/src/main/thrift

thrift --gen cpp -o . $FORMAT_FILE_PATH/carbondata_index_merge.thrift 
thrift --gen cpp -o . $FORMAT_FILE_PATH/carbondata_index.thrift
thrift --gen cpp -o . $FORMAT_FILE_PATH/carbondata.thrift
thrift --gen cpp -o . $FORMAT_FILE_PATH/dictionary_metadata.thrift
thrift --gen cpp -o . $FORMAT_FILE_PATH/dictionary.thrift
thrift --gen cpp -o . $FORMAT_FILE_PATH/schema.thrift
thrift --gen cpp -o . $FORMAT_FILE_PATH/sort_index.thrift

g++ gen-cpp/carbondata_constants.cpp -c $CFLAGS
g++ gen-cpp/carbondata_index_constants.cpp -c $CFLAGS
g++ gen-cpp/carbondata_index_merge_constants.cpp -c $CFLAGS
g++ gen-cpp/carbondata_index_merge_types.cpp -c $CFLAGS
g++ gen-cpp/carbondata_index_types.cpp -c $CFLAGS
g++ gen-cpp/carbondata_types.cpp -c $CFLAGS
g++ gen-cpp/dictionary_constants.cpp -c $CFLAGS
g++ gen-cpp/dictionary_metadata_constants.cpp -c $CFLAGS
g++ gen-cpp/dictionary_metadata_types.cpp -c $CFLAGS
g++ gen-cpp/dictionary_types.cpp -c $CFLAGS
g++ gen-cpp/schema_constants.cpp -c $CFLAGS
g++ gen-cpp/schema_types.cpp -c $CFLAGS
g++ gen-cpp/sort_index_constants.cpp -c $CFLAGS
g++ gen-cpp/sort_index_types.cpp -c $CFLAGS

g++ carbondata_test.cpp -c $CFLAGS -I./gen-cpp/

g++ carbondata_constants.o carbondata_index_constants.o carbondata_index_merge_constants.o \
carbondata_index_merge_types.o carbondata_index_types.o carbondata_test.o carbondata_types.o \
dictionary_constants.o dictionary_metadata_constants.o dictionary_metadata_types.o dictionary_types.o \
schema_constants.o schema_types.o sort_index_constants.o sort_index_types.o -o carbondata_test \
$LDFLAGS $LIBS
