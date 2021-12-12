package com.example.kelimebilmece;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class SplashScreenActivity extends AppCompatActivity {

    //Sorular için listeler
    private String[] sorularList = {"Mutfakta iş yaparken veya yemek yerken kullanılan alet nedir?",
            "Türkiyeden bir ilimiz?"};

    private String[] sorularKodList = {"mutfakS1", "illerS1"};

    //Kelimeler için listeler
    private String[] kelimelerList = {"Bulaşık Süngeri", "Bulaşık Teli", "Bardak", "Bıçak", "Blender",
            "Bulaşık makinesi", "Buzdolabı", "Cezve", "Çanak", "Çay Makinesi", "Çaydanlık", "Çırpacak",
            "Çömlek", "Düdüklü tencere", "Ekmek Kızartma Makinesi", "Elektrikli Izgara", "Elektrikli Ocak",
            "Elektrikli Tava", "Et Kıyma Makinesi", "Fritöz", "Güveçlik", "Kahve Makinesi",
            "Kaşık", "Katı Meyve Sıkacağı", "Kepçe", "Kevgir", "Konserve açacağı", "Merdane",
            "Mikrodalga Fırın", "Mikser", "Karıştırcı", "Mutfak Robotu", "Mutfak Tartısı", "Oklava",
            "Pasta fırçası", "Peçetelik", "Peşkir", "Rende", "Tepsi", "Sini", "Su Isıtıcısı", "Sürahi",
            "Süzgeç", "Tabak", "Tava", "Tencere", "Termos", "Tost Makinesi", "Tuzluk",


            "Adana", "Adıyaman", "Afyon", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin", "Aydın",
            "Balıkesir", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale", "Çankırı", "Çorum", "Denizli",
            "Diyarbakır", "Edirne", "Elâzığ", "Erzincan", "Erzurum", "Eskişehir", "Gaziantep", "Giresun", "Gümüşhane", "Hakkâri",
            "Hatay", "Isparta", "Mersin", "İstanbul", "İzmir", "Kars", "Kastamonu", "Kayseri", "Kırklareli", "Kırşehir", "Kocaeli",
            "Konya", "Kütahya", "Malatya", "Manisa", "Kahramanmaraş", "Mardin ", "Muğla", "Muş", "Nevşehir", "Niğde",
            "Ordu", "Rize", "Sakarya", "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdağ", "Tokat", "Trabzon", "Tunceli",
            "Şanlıurfa ", "Uşak", "Van", "Yozgat", "Zonguldak", "Aksaray", "Bayburt", "Karaman", "Kırıkkale ",
            "Batman", "Şırnak", "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük", "Kilis", "Osmaniye", "Düzce"};


    private String[] kelimelerKodList = {"mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1",
            "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1",
            "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1",
            "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1",
            "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1",
            "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1", "mutfakS1",

            "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1",
            "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1",
            "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1",
            "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1",
            "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1",
            "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1",
            "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1",
            "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1",
            "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1"};


    private ProgressBar mprogress;
    private TextView mTextView;
    SQLiteDatabase database;
    private Cursor cursor;
    private float maksimumProgress = 100f, artacakProgress, progressMiktari = 0;
    static public HashMap<String, String> sorularHashmap;

    private String sqlSorgusu;
    private SQLiteStatement statement;

    private MediaPlayer gameTheme;

    private SharedPreferences preferences;
    private boolean muzikDurumu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mprogress = (ProgressBar) findViewById(R.id.splash_screen_activity_progressBar);
        mTextView = (TextView) findViewById(R.id.splash_screen_activity_textViewState);
        sorularHashmap = new HashMap<>();
        gameTheme = MediaPlayer.create(this, R.raw.gametheme);
        gameTheme.setLooping(true);


        preferences = this.getSharedPreferences("com.example.kelimebilmece", MODE_PRIVATE);
        muzikDurumu = preferences.getBoolean("muzikDurumu", true);


        try {
            database = this.openOrCreateDatabase("KelimeBilmece", MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS Ayarlar (k_adi VARCHAR, k_heart VARCHAR, k_image BLOB)");
            cursor = database.rawQuery("SELECT * FROM Ayarlar", null);

            if (cursor.getCount() < 1) {
                database.execSQL("INSERT INTO Ayarlar (k_adi, k_heart) VALUES ('Oyuncu', '0')");
            }


            database.execSQL("CREATE TABLE IF NOT EXISTS Sorular (id INTEGER PRIMARY KEY, sKod VARCHAR UNIQUE, soru VARCHAR)");
            database.execSQL("DELETE FROM Sorular");
            sqlSorulariEkle();

            database.execSQL("CREATE TABLE IF NOT EXISTS Kelimeler (kKod VARCHAR, kelime VARCHAR, FOREIGN KEY (kKod) REFERENCES Sorular (sKod))");
            database.execSQL("DELETE FROM Kelimeler");
            sqlKelimeleriEkle();

            cursor = database.rawQuery("SELECT * FROM Sorular", null);
            artacakProgress = maksimumProgress / cursor.getCount();


            int sKodIndex = cursor.getColumnIndex("sKod");
            int soruIndex = cursor.getColumnIndex("soru");

            mTextView.setText("Sorular Yükleniyor...");

            while (cursor.moveToNext()) {
                sorularHashmap.put(cursor.getString(sKodIndex), cursor.getString(soruIndex));
                progressMiktari += artacakProgress;

                mprogress.setProgress((int) progressMiktari);
            }

            mTextView.setText("Sorular Alındı Uygulama Başlatılıyor");
            cursor.close();

            new CountDownTimer(1100, 1000) {

                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(mainIntent);

                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Müzik
    @Override
    protected void onResume() {
        super.onResume();

        if (muzikDurumu){
            gameTheme.start();
        }
    }

    private void sqlSorulariEkle() {

        //Artık buna gerek kalmadı
        /*database.execSQL("INSERT INTO Sorular (sKod, soru) VALUES ('mutfakS1' ,'Mutfakta iş yaparken veya yemek yerken kullanılan aletler nelerdir?')");
        database.execSQL("INSERT INTO Sorular (sKod, soru) VALUES ('illerS1' ,'İç Anadolu Bölgesindekş İller?')");*/


        try {
            for (int s = 0; s < sorularList.length; s++) {
                sqlSorgusu = "INSERT INTO Sorular (sKod, soru) VALUES (?,?)";
                statement = database.compileStatement(sqlSorgusu);
                statement.bindString(1, sorularKodList[s]);
                statement.bindString(2, sorularList[s]);
                statement.execute();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sqlKelimeleriEkle(){

        try {
            for (int k = 0; k < kelimelerList.length; k++){
                sqlSorgusu = "INSERT INTO Kelimeler (kKod, kelime) VALUES (?,?)";
                statement = database.compileStatement(sqlSorgusu);
                statement.bindString(1, kelimelerKodList[k]);
                statement.bindString(2, kelimelerList[k]);
                statement.execute();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}