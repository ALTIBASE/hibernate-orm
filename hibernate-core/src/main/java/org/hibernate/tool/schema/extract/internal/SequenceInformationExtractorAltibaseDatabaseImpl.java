/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.tool.schema.extract.internal;

/**
 * An SequenceInfomation for Altibase
 *
 * @author YounJung Park
 */
public class SequenceInformationExtractorAltibaseDatabaseImpl extends SequenceInformationExtractorLegacyImpl {
	/**
	 * Singleton access
	 */
	public static final SequenceInformationExtractorAltibaseDatabaseImpl INSTANCE = new SequenceInformationExtractorAltibaseDatabaseImpl();

	@Override
	protected String sequenceNameColumn() {
		return "SEQUENCE_NAME";
	}

	@Override
	protected String sequenceCatalogColumn() {
		return null;
	}

	@Override
	protected String sequenceSchemaColumn() {
		return null;
	}

	@Override
	protected String sequenceStartValueColumn() {
		return "START_VALUE";
	}

	@Override
	protected String sequenceMinValueColumn() {
		return "MIN_VALUE";
	}

	@Override
	protected String sequenceMaxValueColumn() {
		return "MAX_VALUE";
	}

	@Override
	protected String sequenceIncrementColumn() {
		return "INCREMENT_BY";
	}
}
