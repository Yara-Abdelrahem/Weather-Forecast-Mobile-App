//package com.example.weathery
//
//import androidx.room.Database
//import androidx.room.RoomDatabase
//
//@Database(entities = [CurrentWeatherEntity::class, HourlyForecastEntity::class, DailyForecastEntity::class], version = 1)
//abstract class WeatherDatabase : RoomDatabase() {
//    abstract fun currentWeatherDao(): CurrentWeatherDao
//    abstract fun hourlyForecastDao(): HourlyForecastDao
//    abstract fun dailyForecastDao(): DailyForecastDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: WeatherDatabase? = null
//
//        fun getDatabase(context: android.content.Context): WeatherDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = androidx.room.Room.databaseBuilder(
//                    context.applicationContext,
//                    WeatherDatabase::class.java,
//                    "weather_database"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}

package com.example.weathery

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weathery.AlarmAlert.Model.AlertItem
import com.example.weathery.Favorite.Model.FavoriteCity
import com.example.weathery.Home.Model.ForecastItemEntity
import com.example.weathery.Model.WeatherDao

@Database(entities = [ForecastItemEntity::class, FavoriteCity::class, AlertItem::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase? = null

        fun getDatabase(context: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "weather_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}