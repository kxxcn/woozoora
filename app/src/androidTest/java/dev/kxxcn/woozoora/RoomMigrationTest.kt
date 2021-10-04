package dev.kxxcn.woozoora

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.kxxcn.woozoora.data.source.local.LocalDatabase
import dev.kxxcn.woozoora.di.ApplicationModule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoomMigrationTest {

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        LocalDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrateAll() {
        val name = "woozoora"
        helper.createDatabase(name, 1).apply { close() }
        Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            LocalDatabase::class.java,
            name
        ).addCallback(ApplicationModule.CALLBACK).addMigrations(*ApplicationModule.ALL_MIGRATIONS)
            .build().apply {
            openHelper.writableDatabase
            close()
        }
    }
}