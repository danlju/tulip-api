package com.danlju.tulip.infrastructure.dataaccess;
import javax.sql.DataSource;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.DependsOn;
import org.springframework.beans.factory.InitializingBean;
import java.util.*;
/** Repairs legacy Hibernate-generated unique index names without assuming a name. */
@Component
@DependsOn("entityManagerFactory")
public class ProjectBuildNumberMigration implements InitializingBean {

    private final DataSource dataSource;
    public ProjectBuildNumberMigration(DataSource dataSource) { this.dataSource = dataSource; }

    @Override public void afterPropertiesSet() throws Exception {
        try (var connection = dataSource.getConnection()) { migrate(connection); }
    }

    public void migrate(java.sql.Connection connection) throws Exception {
        var indexes = new HashMap<String, List<String>>();
        try (var rows = connection.getMetaData().getIndexInfo(connection.getCatalog(), null, "builds", true, false)) {
            while (rows.next()) {
                String name = rows.getString("INDEX_NAME"), column = rows.getString("COLUMN_NAME");
                if (name != null && column != null) indexes.computeIfAbsent(name, key -> new ArrayList<>()).add(column);
            }
        }

        // Add the replacement before removing the old constraint. New databases
        // receive the same constraint from the entity mapping when Hibernate starts.
        boolean legacy = indexes.values().stream().anyMatch(cols -> cols.size() == 1 && cols.get(0).equalsIgnoreCase("number"));
        if (!legacy) return;
        boolean composite = indexes.values().stream().anyMatch(cols -> cols.size() == 2 && cols.stream().anyMatch(c -> c.equalsIgnoreCase("number")) && cols.stream().anyMatch(c -> c.equalsIgnoreCase("project_id")));
        try (var statement = connection.createStatement()) {
            if (!composite) statement.execute("ALTER TABLE builds ADD CONSTRAINT uq_build_project_number UNIQUE (project_id, number)");
            for (var entry : indexes.entrySet()) {
                if (entry.getValue().size() == 1 && entry.getValue().get(0).equalsIgnoreCase("number"))
                    statement.execute("ALTER TABLE builds DROP INDEX `" + entry.getKey().replace("`", "``") + "`");
            }
        }
    }
}
