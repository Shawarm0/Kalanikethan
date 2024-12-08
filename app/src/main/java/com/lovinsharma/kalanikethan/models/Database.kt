package com.lovinsharma.kalanikethan.models

import android.app.Application
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class Database: Application() {

    companion object {
        lateinit var realm: Realm
    }

    override fun onCreate() {
        super.onCreate()
        realm  = Realm.open(
            configuration = RealmConfiguration.create(
                schema = setOf(
                    Family::class,
                    Student::class,
                    SignInEvent::class,
                    Parent::class,
                    Payment::class,
                )
            )
        )
    }

}