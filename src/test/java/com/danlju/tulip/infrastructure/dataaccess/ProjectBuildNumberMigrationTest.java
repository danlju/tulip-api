package com.danlju.tulip.infrastructure.dataaccess;
import org.junit.jupiter.api.Test;
import java.sql.*;
import javax.sql.DataSource;
import static org.mockito.Mockito.*;
class ProjectBuildNumberMigrationTest {
    @Test void replacesLegacyGlobalIndexBeforeDroppingIt() throws Exception {
        var connection = mock(Connection.class);
        var metadata = mock(DatabaseMetaData.class);
        var rows = mock(ResultSet.class);
        var statement = mock(Statement.class);
        when(connection.getMetaData()).thenReturn(metadata);
        when(metadata.getIndexInfo(null, null, "builds", true, false)).thenReturn(rows);
        when(rows.next()).thenReturn(true, false);
        when(rows.getString("INDEX_NAME")).thenReturn("generated_number_key");
        when(rows.getString("COLUMN_NAME")).thenReturn("number");
        when(connection.createStatement()).thenReturn(statement);
        new ProjectBuildNumberMigration(mock(DataSource.class)).migrate(connection);
        var order = inOrder(statement);
        order.verify(statement).execute("ALTER TABLE builds ADD CONSTRAINT uq_build_project_number UNIQUE (project_id, number)");
        order.verify(statement).execute("ALTER TABLE builds DROP INDEX `generated_number_key`");
    }
    @Test void alreadyMigratedSchemaNeedsNoDdl() throws Exception {
        var connection = mock(Connection.class);
        var metadata = mock(DatabaseMetaData.class);
        var rows = mock(ResultSet.class);
        when(connection.getMetaData()).thenReturn(metadata);
        when(metadata.getIndexInfo(null, null, "builds", true, false)).thenReturn(rows);
        when(rows.next()).thenReturn(false);
        new ProjectBuildNumberMigration(mock(DataSource.class)).migrate(connection);
        verify(connection, never()).createStatement();
    }
}
