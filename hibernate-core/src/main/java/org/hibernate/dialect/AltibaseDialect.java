/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.dialect;

import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.dialect.pagination.AltibaseLimitHandler;
import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.LockTimeoutException;
import org.hibernate.exception.spi.SQLExceptionConversionDelegate;
import org.hibernate.hql.spi.id.IdTableSupportStandardImpl;
import org.hibernate.hql.spi.id.MultiTableBulkIdStrategy;
import org.hibernate.hql.spi.id.global.GlobalTemporaryTableBulkIdStrategy;
import org.hibernate.hql.spi.id.local.AfterUseAction;
import org.hibernate.internal.util.JdbcExceptionHelper;
import org.hibernate.sql.CaseFragment;
import org.hibernate.sql.DecodeCaseFragment;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.dialect.function.NoArgSQLFunction;
import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.hibernate.tool.schema.extract.internal.SequenceInformationExtractorAltibaseDatabaseImpl;
import org.hibernate.type.StandardBasicTypes;

import org.hibernate.tool.schema.extract.spi.SequenceInformationExtractor;

/**
 * An SQL dialect for Altibase
 *
 * @author YounJung Park
 */
public class AltibaseDialect extends Dialect {

	public AltibaseDialect() {
		super();

		registerCharacterTypeMappings();
		registerNumericTypeMappings();
		registerDateTimeTypeMappings();
		registerBooleanTypeMapping();
		registerBinaryTypeMapping();
		registerLargeObjectTypeMappings();
		registerFunctions();
		registerDefaultProperties();
	}

	protected void registerCharacterTypeMappings() {
		registerColumnType( Types.CHAR, "char(1)" );
		registerColumnType( Types.VARCHAR, 32000, "varchar($l)" );
		registerColumnType( Types.VARCHAR, "clob" );
		registerColumnType( Types.LONGVARCHAR, "varchar(32000)" );
	}

	protected void registerNumericTypeMappings() {
		registerColumnType( Types.BIGINT, "bigint" );
		registerColumnType( Types.SMALLINT, "smallint" );
		registerColumnType( Types.TINYINT, "smallint" );
		registerColumnType( Types.INTEGER, "integer" );
		registerColumnType( Types.FLOAT, "float" );
		registerColumnType( Types.DOUBLE, "double" );
		registerColumnType( Types.NUMERIC, "number($p,$s)" );
		registerColumnType( Types.DECIMAL, "number($p,$s)" );
		registerColumnType( Types.REAL, "float" );
	}

	protected void registerDateTimeTypeMappings() {
		registerColumnType( Types.DATE, "date" );
		registerColumnType( Types.TIME, "date" );
		registerColumnType( Types.TIMESTAMP, "date" );
	}

	protected void registerBooleanTypeMapping() {
		registerColumnType( Types.BOOLEAN, "char(1)" );
	}

	private void registerBinaryTypeMapping() {
		registerColumnType( Types.BINARY, 32000, "byte($l)" );
		registerColumnType( Types.BINARY, "blob" );
		registerColumnType( Types.VARBINARY, 32000, "varbyte($l)" );
		registerColumnType( Types.VARBINARY, "blob" );
		registerColumnType( Types.LONGVARBINARY, "blob" );
		registerColumnType( Types.BIT, "bit" );
		registerColumnType( Types.BIT, 64000, "bit($l)" );
	}

	protected void registerLargeObjectTypeMappings() {
		registerColumnType( Types.BLOB, "blob" );
		registerColumnType( Types.CLOB, "clob" );
	}

	protected void registerFunctions() {
		registerFunction( "abs", new StandardSQLFunction("abs", StandardBasicTypes.DOUBLE) );
		registerFunction( "sign", new StandardSQLFunction("sign", StandardBasicTypes.INTEGER) );

		registerFunction( "acos", new StandardSQLFunction("acos", StandardBasicTypes.DOUBLE) );
		registerFunction( "asin", new StandardSQLFunction("asin", StandardBasicTypes.DOUBLE) );
		registerFunction( "atan", new StandardSQLFunction("atan", StandardBasicTypes.DOUBLE) );
		registerFunction( "cos", new StandardSQLFunction("cos", StandardBasicTypes.DOUBLE) );
		registerFunction( "cosh", new StandardSQLFunction("cosh", StandardBasicTypes.DOUBLE) );
		registerFunction( "exp", new StandardSQLFunction("exp", StandardBasicTypes.DOUBLE) );
		registerFunction( "ln", new StandardSQLFunction("ln", StandardBasicTypes.DOUBLE) );
		registerFunction( "sin", new StandardSQLFunction("sin", StandardBasicTypes.DOUBLE) );
		registerFunction( "sinh", new StandardSQLFunction("sinh", StandardBasicTypes.DOUBLE) );
		registerFunction( "stddev", new StandardSQLFunction("stddev", StandardBasicTypes.DOUBLE) );
		registerFunction( "sqrt", new StandardSQLFunction("sqrt", StandardBasicTypes.DOUBLE) );
		registerFunction( "tan", new StandardSQLFunction("tan", StandardBasicTypes.DOUBLE) );
		registerFunction( "tanh", new StandardSQLFunction("tanh", StandardBasicTypes.DOUBLE) );
		registerFunction( "variance", new StandardSQLFunction("variance", StandardBasicTypes.DOUBLE) );

		registerFunction( "round", new StandardSQLFunction("round") );
		registerFunction( "trunc", new StandardSQLFunction("trunc") );
		registerFunction( "ceil", new StandardSQLFunction("ceil") );
		registerFunction( "floor", new StandardSQLFunction("floor") );

		registerFunction( "chr", new StandardSQLFunction("chr", StandardBasicTypes.CHARACTER) );
		registerFunction( "initcap", new StandardSQLFunction("initcap") );
		registerFunction( "lower", new StandardSQLFunction("lower") );
		registerFunction( "ltrim", new StandardSQLFunction("ltrim") );
		registerFunction( "rtrim", new StandardSQLFunction("rtrim") );
		registerFunction( "upper", new StandardSQLFunction("upper") );
		registerFunction( "ascii", new StandardSQLFunction("ascii", StandardBasicTypes.INTEGER) );

		registerFunction( "to_char", new StandardSQLFunction("to_char", StandardBasicTypes.STRING) );
		registerFunction( "to_date", new StandardSQLFunction("to_date", StandardBasicTypes.TIMESTAMP) );

		registerFunction( "current_date", new NoArgSQLFunction("current_date", StandardBasicTypes.DATE, false) );
		registerFunction( "current_time", new NoArgSQLFunction("current_timestamp", StandardBasicTypes.TIME, false) );
		registerFunction( "current_timestamp", new NoArgSQLFunction("current_timestamp", StandardBasicTypes.TIMESTAMP, false) );

		registerFunction( "last_day", new StandardSQLFunction("last_day", StandardBasicTypes.DATE) );
		registerFunction( "sysdate", new NoArgSQLFunction("sysdate", StandardBasicTypes.DATE, false) );
		registerFunction( "uid", new NoArgSQLFunction("user_id", StandardBasicTypes.INTEGER, false) );
		registerFunction( "user", new NoArgSQLFunction("user_name", StandardBasicTypes.STRING, false) );

		registerFunction( "rownum", new NoArgSQLFunction("rownum", StandardBasicTypes.LONG, false) );

		registerFunction( "concat", new VarArgsSQLFunction(StandardBasicTypes.STRING, "", "||", "") );
		registerFunction( "instr", new StandardSQLFunction("instr", StandardBasicTypes.INTEGER) );
		registerFunction( "instrb", new StandardSQLFunction("instrb", StandardBasicTypes.INTEGER) );
		registerFunction( "lpad", new StandardSQLFunction("lpad", StandardBasicTypes.STRING) );
		registerFunction( "replace", new StandardSQLFunction("replace", StandardBasicTypes.STRING) );
		registerFunction( "rpad", new StandardSQLFunction("rpad", StandardBasicTypes.STRING) );
		registerFunction( "substr", new StandardSQLFunction("substr", StandardBasicTypes.STRING) );
		registerFunction( "substrb", new StandardSQLFunction("substrb", StandardBasicTypes.STRING) );
		registerFunction( "translate", new StandardSQLFunction("translate", StandardBasicTypes.STRING) );

		registerFunction( "substring", new StandardSQLFunction("substr", StandardBasicTypes.STRING) );

		registerFunction( "atan2", new StandardSQLFunction("atan2", StandardBasicTypes.FLOAT) );
		registerFunction( "log", new StandardSQLFunction("log", StandardBasicTypes.INTEGER) );
		registerFunction( "mod", new StandardSQLFunction("mod", StandardBasicTypes.INTEGER) );
		registerFunction( "nvl", new StandardSQLFunction("nvl") );
		registerFunction( "nvl2", new StandardSQLFunction("nvl2") );
		registerFunction( "power", new StandardSQLFunction("power", StandardBasicTypes.FLOAT) );

		registerFunction( "add_months", new StandardSQLFunction("add_months", StandardBasicTypes.DATE) );
		registerFunction( "months_between", new StandardSQLFunction("months_between", StandardBasicTypes.FLOAT) );
		registerFunction( "next_day", new StandardSQLFunction("next_day", StandardBasicTypes.DATE) );

		registerFunction( "str", new StandardSQLFunction("to_char", StandardBasicTypes.STRING) );
	}

	protected void registerDefaultProperties() {
		getDefaultProperties().setProperty( Environment.USE_STREAMS_FOR_BINARY, "true" );
		getDefaultProperties().setProperty( Environment.STATEMENT_BATCH_SIZE, DEFAULT_BATCH_SIZE );
		getDefaultProperties().setProperty( Environment.USE_GET_GENERATED_KEYS, "false" );
	}

	@Override
	public String getCrossJoinSeparator() {
		return ", ";
	}

	@Override
	public CaseFragment createCaseFragment() {
		return new DecodeCaseFragment();
	}

	@Override
	public String getSelectClauseNullString(int sqlType) {
		switch (sqlType) {
			case Types.VARCHAR:
			case Types.CHAR:
				return "to_char(null)";
			case Types.DATE:
			case Types.TIMESTAMP:
			case Types.TIME:
				return "to_date(null)";
			default:
				return "to_number(null)";
		}
	}

	@Override
	public String getCurrentTimestampSelectString() {
		return "select sysdate from dual";
	}

	@Override
	public String getCurrentTimestampSQLFunctionName() {
		return "sysdate";
	}

	@Override
	public String getAddColumnString() {
		return "add (";
	}

	@Override
	public String getAddColumnSuffixString() {
		return ")";
	}

	@Override
	public String getSequenceNextValString(String sequenceName) {
		return "select " + getSelectSequenceNextValString( sequenceName ) + " from dual";
	}

	@Override
	public String getSelectSequenceNextValString(String sequenceName) {
		return sequenceName + ".nextval";
	}

	@Override
	public String getCreateSequenceString(String sequenceName) {
		return "create sequence " + sequenceName;
	}

	@Override
	public String getDropSequenceString(String sequenceName) {
		return "drop sequence " + sequenceName;
	}

	@Override
	public String getCascadeConstraintsString() {
		return " cascade constraints";
	}

	@Override
	public boolean dropConstraints() {
		return false;
	}

	@Override
	public String getForUpdateNowaitString() {
		return " for update nowait";
	}

	@Override
	public boolean supportsSequences() {
		return true;
	}

	@Override
	public boolean supportsPooledSequences() {
		return true;
	}

	@Override
	public String getForUpdateString(String aliases) {
		return getForUpdateString();
	}

	@Override
	public String getForUpdateNowaitString(String aliases) {
		return getForUpdateString();
	}

	@Override
	public boolean forUpdateOfColumns() {
		return true;
	}

	@Override
	public String getQuerySequencesString() {
		return "SELECT a.user_name USER_NAME, b.table_name SEQUENCE_NAME, c.current_seq CURRENT_VALUE, "
				+ "c.start_seq START_VALUE, c.min_seq MIN_VALUE, c.max_seq MAX_VALUE, c.increment_seq INCREMENT_BY, "
				+ "c.flag CYCLE_, c.sync_interval CACHE_SIZE "
				+ "FROM system_.sys_users_ a, system_.sys_tables_ b, x$seq c "
				+ "WHERE a.user_id = b.user_id AND b.table_oid = c.seq_oid AND a.user_name <> 'SYSTEM_' AND b.table_type = 'S' "
				+ "ORDER BY 1,2";
	}

	@Override
	public SequenceInformationExtractor getSequenceInformationExtractor() {
		return SequenceInformationExtractorAltibaseDatabaseImpl.INSTANCE;
	}

	@Override
	public LimitHandler getLimitHandler() {
		return AltibaseLimitHandler.INSTANCE;
	}

	@Override
	public int registerResultSetOutParameter(CallableStatement statement, int col) throws SQLException {
		return col;
	}

	@Override
	public ResultSet getResultSet(CallableStatement ps) throws SQLException {
		ps.execute();
		return (ResultSet)ps.getObject( 1 );
	}

	@Override
	public SQLExceptionConversionDelegate buildSQLExceptionConversionDelegate() {
		return (sqlException, message, sql) -> {

			final int errorCode = JdbcExceptionHelper.extractErrorCode(sqlException );

			if ( errorCode == 334393 || errorCode == 4164) {
				// 334393 - response timeout , 4164 - query timeout.
				return new LockTimeoutException(message, sqlException, sql );
			}

			// 200820 - Cannot insert NULL or update to NULL
			// 69720 - Unique constraint violated
			// 200823 - foreign key constraint violation
			// 200822 - failed on update or delete by foreign key constraint violation
			if ( errorCode == 200820 || errorCode == 69720 || errorCode == 200823 || errorCode == 200822 ) {
				final String constraintName = getViolatedConstraintNameExtracter()
						.extractConstraintName( sqlException );

				return new ConstraintViolationException(message, sqlException, sql, constraintName );
			}

			return null;
		};
	}

	@Override
	public MultiTableBulkIdStrategy getDefaultMultiTableBulkIdStrategy() {
		return new GlobalTemporaryTableBulkIdStrategy(
				new IdTableSupportStandardImpl(), AfterUseAction.CLEAN
		);
	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * Altibase in fact does require that parameters appearing in the select clause be wrapped in cast() calls
	 * to tell the DB parser the type of the select value.
	 */
	@Override
	public boolean requiresCastingOfParametersInSelectClause() {
		return true;
	}

	@Override
	public boolean supportsUnboundedLobLocatorMaterialization() {
		return false;
	}

	@Override
	public boolean supportsPartitionBy() {
		return true;
	}

	@Override
	public boolean supportsJdbcConnectionLobCreation(DatabaseMetaData databaseMetaData) {
		return false;
	}

	@Override
	public boolean canCreateSchema() {
		return false;
	}

	@Override
	public boolean supportsColumnCheck() {
		return false;
	}

	@Override
	public boolean supportsUnionAll() {
		return true;
	}

	@Override
	public boolean supportsCommentOn() {
		return true;
	}

	@Override
	public boolean supportsCurrentTimestampSelection() {
		return true;
	}

	@Override
	public boolean isCurrentTimestampSelectStringCallable() {
		return false;
	}

	@Override
	public boolean supportsTupleDistinctCounts() {
		return false;
	}

	@Override
	public boolean supportsEmptyInList() {
		return false;
	}

	@Override
	public boolean supportsExistsInSelect() {
		return false;
	}
}
