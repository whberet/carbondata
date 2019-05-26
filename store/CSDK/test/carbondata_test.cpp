/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <iostream>

#include <boost/shared_ptr.hpp>

#include <thrift/transport/TBufferTransports.h>
#include <thrift/protocol/TCompactProtocol.h>

#include "carbondata_index_types.h"

using namespace std;
using namespace apache::thrift::transport;
using namespace apache::thrift::protocol;
using namespace carbondata::format;

void print_usage();

int main(int argc, char const *argv[])
{
	if (argc < 2)
	{
		print_usage();
		return 0;
	}
	
	const char* file_path = argv[1];

	FILE *fp = fopen(file_path, "r");
	fseek(fp, 0L, SEEK_END);
	long file_size = ftell(fp);
	fseek(fp, 0L, SEEK_SET);

	unsigned char *buffer = new unsigned char[file_size];

	size_t read_len = fread(buffer, 1, file_size, fp);

	TCompactProtocolFactory factory;

	boost::shared_ptr<TMemoryBuffer> trans(new TMemoryBuffer(const_cast<uint8_t*>(buffer), read_len));
	boost::shared_ptr<TProtocol> protocol = factory.getProtocol(trans);

	// read IndexHeader
	IndexHeader indexheader;
	indexheader.read(protocol.get());

	cout << indexheader.version << endl;

	// read BlockIndex
	BlockIndex blockindex;
	while (trans->peek())
	{
		blockindex.read(protocol.get());
		cout << blockindex.file_name << endl;
	}

    return 0;
}

void print_usage()
{
	printf("Usage:\n  carbondata_test <CARBONDATA_INDEXFILE>\n");
}