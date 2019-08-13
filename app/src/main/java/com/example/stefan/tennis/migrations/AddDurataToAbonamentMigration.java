package com.example.stefan.tennis.migrations;

import com.example.stefan.tennis.database.TenisDatabase;
import com.example.stefan.tennis.models.Abonament;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;


@Migration(database = TenisDatabase.class, version = 3)
public class AddDurataToAbonamentMigration extends AlterTableMigration<Abonament> {

    public AddDurataToAbonamentMigration(Class<Abonament> table) {
        super(table);
    }

    @Override
    public void onPreMigrate() {
        super.onPreMigrate();
        addColumn(SQLiteType.INTEGER, "durata");
    }
}
