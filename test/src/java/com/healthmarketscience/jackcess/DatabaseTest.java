// Copyright (c) 2004 Health Market Science, Inc.

package com.healthmarketscience.jackcess;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.healthmarketscience.jackcess.Column;
import com.healthmarketscience.jackcess.DataTypes;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.Table;

import junit.framework.TestCase;

/**
 * @author Tim McCune
 */
public class DatabaseTest extends TestCase {
  
  public DatabaseTest(String name) throws Exception {
    super(name);
  }
   
  private Database open() throws Exception {
    return Database.open(new File("test/data/test.mdb"));
  }
  
  private Database create() throws Exception {
    File tmp = File.createTempFile("databaseTest", ".mdb");
    tmp.deleteOnExit();
    return Database.create(tmp);
  }
  
  public void testGetColumns() throws Exception {
    List columns = open().getTable("Table1").getColumns();
    assertEquals(9, columns.size());
    checkColumn(columns, 0, "A", DataTypes.TEXT);
    checkColumn(columns, 1, "B", DataTypes.TEXT);
    checkColumn(columns, 2, "C", DataTypes.BYTE);
    checkColumn(columns, 3, "D", DataTypes.INT);
    checkColumn(columns, 4, "E", DataTypes.LONG);
    checkColumn(columns, 5, "F", DataTypes.DOUBLE);
    checkColumn(columns, 6, "G", DataTypes.SHORT_DATE_TIME);
    checkColumn(columns, 7, "H", DataTypes.MONEY);
    checkColumn(columns, 8, "I", DataTypes.BOOLEAN);
  }
  
  private void checkColumn(List columns, int columnNumber, String name, byte dataType)
  throws Exception {
    Column column = (Column) columns.get(columnNumber);
    assertEquals(name, column.getName());
    assertEquals(dataType, column.getType());
  }
  
  public void testGetNextRow() throws Exception {
    Database db = open();
    assertEquals(1, db.getTableNames().size());
    Table table = db.getTable("Table1");
    
    Map row = table.getNextRow();
    assertEquals("abcdefg", row.get("A"));
    assertEquals("hijklmnop", row.get("B"));
    assertEquals(new Byte((byte) 2), row.get("C"));
    assertEquals(new Short((short) 222), row.get("D"));
    assertEquals(new Integer(333333333), row.get("E"));
    assertEquals(new Double(444.555d), row.get("F"));
    Calendar cal = Calendar.getInstance();
    cal.setTime((Date) row.get("G"));
    assertEquals(Calendar.SEPTEMBER, cal.get(Calendar.MONTH));
    assertEquals(21, cal.get(Calendar.DAY_OF_MONTH));
    assertEquals(1974, cal.get(Calendar.YEAR));
    assertEquals(Boolean.TRUE, row.get("I"));
    
    row = table.getNextRow();
    assertEquals("a", row.get("A"));
    assertEquals("b", row.get("B"));
    assertEquals(new Byte((byte) 0), row.get("C"));
    assertEquals(new Short((short) 0), row.get("D"));
    assertEquals(new Integer(0), row.get("E"));
    assertEquals(new Double(0d), row.get("F"));
    cal = Calendar.getInstance();
    cal.setTime((Date) row.get("G"));
    assertEquals(Calendar.DECEMBER, cal.get(Calendar.MONTH));
    assertEquals(12, cal.get(Calendar.DAY_OF_MONTH));
    assertEquals(1981, cal.get(Calendar.YEAR));
    assertEquals(Boolean.FALSE, row.get("I"));
  }
  
  public void testCreate() throws Exception {
    Database db = create();
    assertEquals(0, db.getTableNames().size());
  }
  
  public void testWriteAndRead() throws Exception {
    Database db = create();
    createTestTable(db);
    Object[] row = new Object[9];
    row[0] = "Tim";
    row[1] = "R";
    row[2] = "McCune";
    row[3] = new Integer(1234);
    row[4] = new Byte((byte) 0xad);
    row[5] = new Double(555.66d);
    row[6] = new Float(777.88d);
    row[7] = new Short((short) 999);
    row[8] = new Date();
    Table table = db.getTable("Test");
    int count = 1000;
    for (int i = 0; i < count; i++) { 
      table.addRow(row);
    }
    for (int i = 0; i < count; i++) {
      Map readRow = table.getNextRow();
      assertEquals(row[0], readRow.get("A"));
      assertEquals(row[1], readRow.get("B"));
      assertEquals(row[2], readRow.get("C"));
      assertEquals(row[3], readRow.get("D"));
      assertEquals(row[4], readRow.get("E"));
      assertEquals(row[5], readRow.get("F"));
      assertEquals(row[6], readRow.get("G"));
      assertEquals(row[7], readRow.get("H"));
    }
  }
  
  public void testWriteAndReadInBatch() throws Exception {
    Database db = create();
    createTestTable(db);
    int count = 1000;
    List rows = new ArrayList(count);
    Object[] row = new Object[9];
    row[0] = "Tim";
    row[1] = "R";
    row[2] = "McCune";
    row[3] = new Integer(1234);
    row[4] = new Byte((byte) 0xad);
    row[5] = new Double(555.66d);
    row[6] = new Float(777.88d);
    row[7] = new Short((short) 999);
    row[8] = new Date();
    for (int i = 0; i < count; i++) {
      rows.add(row);
    }
    Table table = db.getTable("Test");
    table.addRows(rows);
    for (int i = 0; i < count; i++) {
      Map readRow = table.getNextRow();
      assertEquals(row[0], readRow.get("A"));
      assertEquals(row[1], readRow.get("B"));
      assertEquals(row[2], readRow.get("C"));
      assertEquals(row[3], readRow.get("D"));
      assertEquals(row[4], readRow.get("E"));
      assertEquals(row[5], readRow.get("F"));
      assertEquals(row[6], readRow.get("G"));
      assertEquals(row[7], readRow.get("H"));
    }
  }
  
  private void createTestTable(Database db) throws Exception {
    List columns = new ArrayList();
    Column col = new Column();
    col.setName("A");
    col.setType(DataTypes.TEXT);
    columns.add(col);
    col = new Column();
    col.setName("B");
    col.setType(DataTypes.TEXT);
    columns.add(col);
    col = new Column();
    col.setName("C");
    col.setType(DataTypes.TEXT);
    columns.add(col);
    col = new Column();
    col.setName("D");
    col.setType(DataTypes.LONG);
    columns.add(col);
    col = new Column();
    col.setName("E");
    col.setType(DataTypes.BYTE);
    columns.add(col);
    col = new Column();
    col.setName("F");
    col.setType(DataTypes.DOUBLE);
    columns.add(col);
    col = new Column();
    col.setName("G");
    col.setType(DataTypes.FLOAT);
    columns.add(col);
    col = new Column();
    col.setName("H");
    col.setType(DataTypes.INT);
    columns.add(col);
    col = new Column();
    col.setName("I");
    col.setType(DataTypes.SHORT_DATE_TIME);
    columns.add(col);
    db.createTable("test", columns);
  }
  
}
