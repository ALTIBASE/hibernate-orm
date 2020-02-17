package org.hibernate.test.dialect.unit.sequence;

import org.hibernate.dialect.AltibaseDialect;
import org.hibernate.dialect.Dialect;
import org.hibernate.tool.schema.extract.internal.SequenceInformationExtractorAltibaseDatabaseImpl;
import org.hibernate.tool.schema.extract.spi.SequenceInformationExtractor;

public class AltibaseDialectSequenceInformationExtractorTest extends AbstractSequenceInformationExtractorTest
{
    @Override
    public Dialect getDialect()
    {
        return new AltibaseDialect();
    }

    @Override
    public String expectedQuerySequencesString()
    {
         return "SELECT a.user_name USER_NAME, b.table_name SEQUENCE_NAME, c.current_seq CURRENT_VALUE, "
                + "c.start_seq START_VALUE, c.min_seq MIN_VALUE, c.max_seq MAX_VALUE, c.increment_seq INCREMENT_BY, "
                + "c.flag CYCLE_, c.sync_interval CACHE_SIZE "
                + "FROM system_.sys_users_ a, system_.sys_tables_ b, x$seq c "
                + "WHERE a.user_id = b.user_id AND b.table_oid = c.seq_oid AND a.user_name <> 'SYSTEM_' AND b.table_type = 'S' "
                + "ORDER BY 1,2";
    }

    @Override
    public Class<? extends SequenceInformationExtractor> expectedSequenceInformationExtractor()
    {
        return SequenceInformationExtractorAltibaseDatabaseImpl.class;
    }
}
