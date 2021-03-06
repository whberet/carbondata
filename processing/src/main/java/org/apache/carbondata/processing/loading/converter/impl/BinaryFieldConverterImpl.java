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
package org.apache.carbondata.processing.loading.converter.impl;

import java.nio.charset.Charset;

import org.apache.carbondata.common.logging.LogServiceFactory;
import org.apache.carbondata.core.constants.CarbonCommonConstants;
import org.apache.carbondata.core.datastore.row.CarbonRow;
import org.apache.carbondata.core.metadata.datatype.DataType;
import org.apache.carbondata.core.metadata.schema.table.column.CarbonDimension;
import org.apache.carbondata.processing.loading.DataField;
import org.apache.carbondata.processing.loading.converter.BadRecordLogHolder;
import org.apache.carbondata.processing.loading.converter.FieldConverter;
import org.apache.carbondata.processing.loading.exception.CarbonDataLoadingException;

import org.apache.log4j.Logger;

/**
 * Converter for binary
 */
public class BinaryFieldConverterImpl implements FieldConverter {
  private static final Logger LOGGER =
      LogServiceFactory.getLogService(BinaryFieldConverterImpl.class.getName());

  private int index;
  private DataType dataType;
  private CarbonDimension dimension;
  private String nullformat;
  private boolean isEmptyBadRecord;
  private DataField dataField;
  public BinaryFieldConverterImpl(DataField dataField, String nullformat, int index,
      boolean isEmptyBadRecord) {
    this.dataType = dataField.getColumn().getDataType();
    this.dimension = (CarbonDimension) dataField.getColumn();
    this.nullformat = nullformat;
    this.index = index;
    this.isEmptyBadRecord = isEmptyBadRecord;
    this.dataField = dataField;
  }

  @Override
  public void convert(CarbonRow row, BadRecordLogHolder logHolder)
      throws CarbonDataLoadingException {
    if (row.getObject(index) instanceof String) {
      row.update((((String) row.getObject(index)))
          .getBytes(Charset.forName(CarbonCommonConstants.DEFAULT_CHARSET)), index);
    } else if (row.getObject(index) instanceof byte[]) {
      row.update(row.getObject(index), index);
    } else {
      throw new CarbonDataLoadingException("Binary only support String and byte[] data type");
    }
  }

  @Override
  public Object convert(Object value, BadRecordLogHolder logHolder)
      throws RuntimeException {
    return null;
  }

  @Override
  public void clear() {
  }
}