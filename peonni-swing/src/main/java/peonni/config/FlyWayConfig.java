package peonni.config;

import org.flywaydb.core.Flyway;

public class FlyWayConfig {

    public static void migrate() {
        Flyway flyway = Flyway.configure()
                .dataSource(
                        "jdbc:postgresql://localhost:5432/peonni",
                        "postgres",
                        "1234")
                .baselineOnMigrate(true)
                .load();

        flyway.migrate();
    }
}
