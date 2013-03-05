/*
Copyright (c) 2007 Health Market Science, Inc.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

You can contact Health Market Science at info@healthmarketscience.com
or at the following address:

Health Market Science
2700 Horizon Drive
Suite 200
King of Prussia, PA 19406
 */

package com.healthmarketscience.jackcess;

import java.io.IOException;
import java.util.List;

/**
 * Interface which allows customization of the behavior of the
 * <code>Database</code> export methods.
 * 
 * @author James Ahlborn
 */
public interface ExportFilter {

  /**
   * The columns that should be used to create the exported file.
   * 
   * @param columns
   *          the columns as determined by the export code, may be directly
   *          modified and returned
   * @return the columns to use when creating the export file
   */
  public List<ColumnImpl> filterColumns(List<ColumnImpl> columns) throws IOException;

  /**
   * The desired values for the row.
   * 
   * @param row
   *          the row data as determined by the import code, may be directly
   *          modified
   * @return the row data as it should be written to the import table.  if
   *         {@code null}, the row will be skipped
   */
  public Object[] filterRow(Object[] row) throws IOException;

}
