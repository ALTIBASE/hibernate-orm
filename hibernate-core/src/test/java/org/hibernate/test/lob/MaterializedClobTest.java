/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.test.lob;

import org.hibernate.dialect.SybaseASE15Dialect;

import org.hibernate.testing.DialectChecks;
import org.hibernate.testing.RequiresDialectFeature;
import org.hibernate.testing.SkipForDialect;

/**
 * Tests eager materialization and mutation of data mapped by
 * {@link org.hibernate.type.MaterializedClobType}.
 *
 * @author Gail Badner
 */
@RequiresDialectFeature( DialectChecks.SupportsExpectedLobUsagePattern.class )
@SkipForDialect( value = SybaseASE15Dialect.class, comment = "jTDS driver doesn't implement character stream handling")
public class MaterializedClobTest extends LongStringTest {
	public String[] getMappings() {
		return new String[] { "lob/MaterializedClobMappings.hbm.xml" };
	}
}
