package com.example.managementuser.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.managementuser.data.product.ProductDao
import com.example.managementuser.data.product.ProductEntity
import com.example.managementuser.data.user.UserDao
import com.example.managementuser.data.user.UserEntity

@Database(entities = [UserEntity::class, ProductEntity::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DataBaseApplication : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao

    companion object {
//        val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                // Ví dụ: thêm cột mới
//                database.execSQL("ALTER TABLE users ADD COLUMN age INTEGER DEFAULT 0")
//            }
//        }

        fun getInstance(context: Context): DataBaseApplication {
            return Room.databaseBuilder(
                context = context.applicationContext,
                DataBaseApplication::class.java,
                "app_db"
            )
//                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration() // Tự động reset DB nếu version thay đổi
                .build()
        }
    }
}