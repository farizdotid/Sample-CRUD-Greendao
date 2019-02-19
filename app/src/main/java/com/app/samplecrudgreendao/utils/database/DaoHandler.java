package com.app.samplecrudgreendao.utils.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Fariz Ramadhan.
 * website : https://farizdotid.com/
 * github : https://github.com/farizdotid
 * linkedin : https://www.linkedin.com/in/farizramadhan/
 */
public class DaoHandler {
    /*
    Fungsi untuk konfigurasi Greendao. Dibuat singleton class agar tinggal panggil saja.
     */
    public static DaoSession getInstance(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "catatpengeluaran_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();

        DaoMaster daoMaster = new DaoMaster(db);
        return daoMaster.newSession();
    }
}