/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.dialect;

import java.sql.Types;
import java.util.Locale;
import org.hibernate.engine.spi.RowSelection;
import org.hibernate.testing.junit4.BaseUnitTestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testing of AltibaseDialect limit and offset queries
 *
 * @author YounJung Park
 */
public class AltibaseDialectTestCase extends BaseUnitTestCase {
	private Dialect dialect;

	public AltibaseDialectTestCase() {
		dialect = new AltibaseDialect();
	}

	@Test
	public void testSupportLimits() {
		assertTrue(dialect.getLimitHandler().supportsLimit());
	}

	@Test
	public void testSelectWithSingleLimit() {
		assertEquals( "select c1, c2 from t1 order by c1, c2 desc limit ?",
				dialect.getLimitHandler().processSql("select c1, c2 from t1 order by c1, c2 desc",
						toRowSelection( 0, 15 ) ).toLowerCase(Locale.ROOT));
	}

	@Test
	public void testSelectWithOffsetLimit() {
		assertEquals( "select c1, c2 from t1 order by c1, c2 desc limit ? offset ? + 1",
				dialect.getLimitHandler().processSql("select c1, c2 from t1 order by c1, c2 desc",
						toRowSelection( 5, 15 ) ).toLowerCase(Locale.ROOT));
	}

	@Test
	public void testSelectWithNoLimit() {
		assertEquals( "select c1, c2 from t1 order by c1, c2 desc",
				dialect.getLimitHandler().processSql("select c1, c2 from t1 order by c1, c2 desc",
						null ).toLowerCase(Locale.ROOT));
	}

	@Test
	public void testGetBinaryTypeName() {
		assertEquals("blob", dialect.getTypeName(Types.BINARY ));
		assertEquals("byte(1)", dialect.getTypeName(Types.BINARY, 1, 0, 0));
		assertEquals("byte(32000)", dialect.getTypeName(Types.BINARY, 32000, 0, 0));
		assertEquals("blob", dialect.getTypeName(Types.BINARY, 32001, 0, 0));

		assertEquals("blob", dialect.getTypeName(Types.VARBINARY ));
		assertEquals("varbyte(1)", dialect.getTypeName(Types.VARBINARY, 1, 0, 0));
		assertEquals("varbyte(32000)", dialect.getTypeName(Types.VARBINARY, 32000, 0, 0));
		assertEquals("blob", dialect.getTypeName(Types.VARBINARY, 32001, 0, 0));

		assertEquals("blob", dialect.getTypeName(Types.LONGVARBINARY ));
		assertEquals("blob", dialect.getTypeName(Types.LONGVARBINARY, 1, 0, 0));
		assertEquals("blob", dialect.getTypeName(Types.LONGVARBINARY, 32000, 0, 0));
		assertEquals("blob", dialect.getTypeName(Types.LONGVARBINARY, 32001, 0, 0));

		assertEquals("bit", dialect.getTypeName(Types.BIT ));
	}

	private RowSelection toRowSelection( int firstRow, int maxRows ) {
		RowSelection selection = new RowSelection();
		selection.setFirstRow( firstRow );
		selection.setMaxRows( maxRows );
		return selection;
	}
}