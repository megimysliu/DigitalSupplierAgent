package co.almotech.digitalsupplieragent.di;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import co.almotech.digitalsupplieragent.MyApplication;
import co.almotech.digitalsupplieragent.data.db.AppDatabase;
import co.almotech.digitalsupplieragent.data.db.dao.CartDAO;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Module
@InstallIn(ApplicationComponent.class)
public class DatabaseModule {

    private static final String DB_NAME = "CartDb";

    @Singleton
    @Provides
    AppDatabase provideDatabase(@ApplicationContext Context context){

        return Room.databaseBuilder(context,AppDatabase.class,"CartDb")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    CartDAO provideCartDAO(AppDatabase database){

        return database.cartDAO();

    }
}
