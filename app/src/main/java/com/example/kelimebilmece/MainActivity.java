package com.example.kelimebilmece;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    private RewardedAd mRewardedAd;
    private AdView mAdView;
    private final String TAG = "MainActivity";

    private SQLiteDatabase database;
    private Cursor cursor;
    private SQLiteStatement statement;
    private String sqlSorgusu;

    private TextView txtUserHeartCount, txtViewUserName;
    private Button btnHakKazan;
    private ConstraintLayout constra;
    private ImageView imgHeart;
    private int heartIndex, nameIndex, heartCount, imgHeartDuration = 2500, sonCanDurumu;
    private Bitmap imgHeartBitmap;
    private ConstraintLayout.LayoutParams heartParams;
    private ImageView imgHeartDesign;
    private Float imgHeartXPos, imgHeartYPos;
    private ObjectAnimator objectAnimatorHeartX, objectAnimatorHeartY;
    private AnimatorSet imgHeartAnimatorSet;

    private WindowManager.LayoutParams params;

    //Settings Dialog
    private Dialog settingsDialog;
    private ImageView settingsImgClose;
    private Button settingsBtnChangeName, settingsBtnChangeProfileImg;
    private RadioButton settingsRadioOpen, settingsRadioClose;

    //Change Name Dialog
    private Dialog changeNameDialog;
    private ImageView changeNameImgClose;
    private Button changeNameDialogBtn;
    private EditText changeNameEditTxtName;
    private String getChangeName;

    private int izinVerme = 0, izinVerildi = 1;
    private Intent resimDegistirIntent;
    private Uri resimUri;
    private Bitmap resimBitmap;
    private CircleImageView userProfileImage;
    private ImageDecoder.Source resimDosyasi;
    private AlertDialog.Builder alertBuilder;
    private byte[] resimByte;
    private int resimIndex;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private boolean muzikDurumu;

    private Dialog getHeartDialog;
    private ImageView getHeartImgClose, getHeartShowAndGet, getHeartBuyAndGet;

    private Dialog shopDialog;
    private ImageView shopDialogImgClose;
    private RecyclerView shopDialogRecyclerView;
    private ShopAdapter adapter;
    private GridLayoutManager manager;

    private BillingClient mBillingClient;
    private ArrayList<String> skuList;
    private ArrayList<Integer> heartList;
    private int heartPos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtUserHeartCount = (TextView) findViewById(R.id.main_activity_textViewUserHeartCount);
        txtViewUserName = (TextView) findViewById(R.id.main_activity_textViewUserName);
        btnHakKazan = (Button) findViewById(R.id.main_activty_btnHakKazan);
        constra = (ConstraintLayout) findViewById(R.id.main_activty_constra);
        mAdView = (AdView) findViewById(R.id.main_activity_adView);
        imgHeartDesign = (ImageView) findViewById(R.id.main_activity_imageViewHeartDesign);
        userProfileImage = (CircleImageView)findViewById(R.id.main_activity_circleImageViewProfile);

        btnHakKazan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                canKazanmaMenusu();
            }
        });

        //Banner
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        mAdView = new AdView(this);

        mAdView.setAdSize(AdSize.FULL_BANNER);

        mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        mAdView = findViewById(R.id.main_activity_adView);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest1);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.


            }
        });


        //Rewarded
        AdRequest adRequest2 = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917", adRequest2, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Toast.makeText(MainActivity.this, "Ad Failed To Load", Toast.LENGTH_SHORT).show();
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Toast.makeText(MainActivity.this, "Ad was loaded.", Toast.LENGTH_SHORT).show();
                    }
                });

        preferences = this.getSharedPreferences("com.example.kelimebilmece", MODE_PRIVATE);
        muzikDurumu = preferences.getBoolean("muzikDurumu", true);

        skuList = new ArrayList<>();
        heartList = new ArrayList<>();

        skuList.add("buy_heart1");
        skuList.add("buy_heart2");
        skuList.add("buy_heart3");
        skuList.add("buy_heart4");
        skuList.add("buy_heart5");

        heartList.add(3);
        heartList.add(15);
        heartList.add(50);
        heartList.add(90);
        heartList.add(500);

        mBillingClient = BillingClient.newBuilder(this)
                .setListener(this)
                .enablePendingPurchases()
                .build();

        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(getApplicationContext(), "Ödeme Sistemi Şu anda Geçerli Değil.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {

                if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK){
                    Toast.makeText(getApplicationContext(), "Ödeme Sistemi İçin Google Play Hesabınızı Kontrol Ediniz.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        try {
            database = this.openOrCreateDatabase("KelimeBilmece", MODE_PRIVATE, null);
            cursor = database.rawQuery("SELECT * FROM Ayarlar", null);

            heartIndex = cursor.getColumnIndex("k_heart");
            nameIndex = cursor.getColumnIndex("k_adi");
            resimIndex = cursor.getColumnIndex("k_image");
            cursor.moveToFirst();

            resimByte = cursor.getBlob(resimIndex);

            if (resimByte != null){
                resimBitmap = BitmapFactory.decodeByteArray(resimByte, 0 , resimByte.length);
                userProfileImage.setImageBitmap(resimBitmap);
            }

            heartCount = Integer.valueOf(cursor.getString(heartIndex));
            txtUserHeartCount.setText("+" + heartCount);
            txtViewUserName.setText(cursor.getString(nameIndex));

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        imgHeart = new ImageView(this);
        imgHeartBitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.heart);
        imgHeart.setImageBitmap(imgHeartBitmap);
        heartParams = new ConstraintLayout.LayoutParams(96, 96);
        imgHeart.setLayoutParams(heartParams);
        imgHeart.setX(0);
        imgHeart.setY(0);
        imgHeart.setVisibility(View.INVISIBLE);
        constra.addView(imgHeart);
    }


    public void mainBtnClick(View v) {

        switch (v.getId()) {

            case R.id.main_activity_btnPlay:
                Intent playIntent = new Intent(MainActivity.this, PlayActivity.class);
                finish();
                playIntent.putExtra("heartCount", heartCount);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_out_up, R.anim.slide_in_down);
                break;

            case R.id.main_activity_btnShop:
                marketDiyalog();
                break;

            case R.id.main_activity_btnExit:
                onBackPressed();
                break;
        }
    }

    public void btnAyarlar(View v) {

        ayarlariGoster();
    }

    private void marketDiyalog(){
        shopDialog = new Dialog(this);
        params = new WindowManager.LayoutParams();
        params.copyFrom(shopDialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        shopDialog.setCancelable(false);
        shopDialog.setContentView(R.layout.custom_dialog_shop);

        shopDialogImgClose = (ImageView)shopDialog.findViewById(R.id.custom_dialog_shop_imageViewClose);
        shopDialogRecyclerView = (RecyclerView) shopDialog.findViewById(R.id.custom_dialog_shop_recyclerView);

        shopDialogImgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopDialog.dismiss();
            }
        });

        adapter = new ShopAdapter(Shop.getData(), this);
        shopDialogRecyclerView.setHasFixedSize(true);
        manager= new GridLayoutManager(this, 3);
        shopDialogRecyclerView.setLayoutManager(manager);
        shopDialogRecyclerView.addItemDecoration(new GridItemDecoration(3, 5));
        shopDialogRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ShopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Shop mShop, final int pos) {

                if (mBillingClient.isReady()){

                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(Collections.singletonList(skuList.get(pos))).setType(BillingClient.SkuType.INAPP);

                    mBillingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> list) {
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null){
                                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                        .setSkuDetails(list.get(0))
                                        .build();
                                mBillingClient.launchBillingFlow(MainActivity.this, flowParams);

                                heartPos = pos;
                            }
                        }
                    });

                }
            }
        });

        shopDialog.getWindow().setAttributes(params);
        shopDialog.show();

    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
        if (billingResult.getResponseCode() ==  Purchase.PurchaseState.PURCHASED){
            handlePurchase(list.get(0));
        }
    }

    private void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED){
            if (!purchase.isAcknowledged()){
                AcknowledgePurchaseParams purchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

                AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
                    @Override
                    public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                        Toast.makeText(getApplicationContext(), "Satın Alma İşlemi Başarılı.", Toast.LENGTH_SHORT).show();
                        sonCanDurumu += heartList.get(heartPos);
                        canMiktariniGuncelle(Integer.valueOf(txtUserHeartCount.getText().toString()), sonCanDurumu);
                    }
                };

                mBillingClient.acknowledgePurchase(purchaseParams, acknowledgePurchaseResponseListener);
            }
        }
    }

    private void canKazanmaMenusu(){

        getHeartDialog = new Dialog(this);
        params = new WindowManager.LayoutParams();
        params.copyFrom(getHeartDialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getHeartDialog.setCancelable(false);
        getHeartDialog.setContentView(R.layout.custom_dialog_get_heart);

        getHeartImgClose = (ImageView) getHeartDialog.findViewById(R.id.custom_dialog_get_heart_imageViewClose);
        getHeartShowAndGet = (ImageView) getHeartDialog.findViewById(R.id.custom_dialog_get_heart_imageViewShowAndGet);
        getHeartBuyAndGet= (ImageView) getHeartDialog.findViewById(R.id.custom_dialog_get_heart_imageViewBuyAndGet);

        getHeartImgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHeartDialog.dismiss();
            }
        });
        getHeartShowAndGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAd();
                getHeartDialog.dismiss();
            }
        });

        getHeartBuyAndGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Shop Menu
                marketDiyalog();
                getHeartDialog.dismiss();
            }
        });

        getHeartDialog.getWindow().setAttributes(params);
        getHeartDialog.show();
    }


    private void ayarlariGoster() {

        settingsDialog = new Dialog(this);
        params = new WindowManager.LayoutParams();
        params.copyFrom(settingsDialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        settingsDialog.setCancelable(false);
        settingsDialog.setContentView(R.layout.custom_dialog_settings);

        settingsImgClose = (ImageView) settingsDialog.findViewById(R.id.custom_dialog_settings_imageViewClose);
        settingsBtnChangeName = (Button) settingsDialog.findViewById(R.id.custom_dialog_settings_btnChangeName);
        settingsBtnChangeProfileImg = (Button) settingsDialog.findViewById(R.id.custom_dialog_settings_btnChangeProfileImage);
        settingsRadioClose = (RadioButton) settingsDialog.findViewById(R.id.custom_dialog_settings_radioBtnMusicClose);
        settingsRadioOpen = (RadioButton) settingsDialog.findViewById(R.id.custom_dialog_settings_radioBtnMusicOpen);

        if (muzikDurumu){
            settingsRadioOpen.setChecked(muzikDurumu);
        } else{
            settingsRadioClose.setChecked(!muzikDurumu);
    }


        settingsRadioOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    muzikAcKapaAyari(b);
                }
            }
        });

        settingsRadioClose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b){
                    muzikAcKapaAyari(!b);
                }
            }
        });



        settingsImgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsDialog.dismiss();
            }
        });

        settingsBtnChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsDialog.dismiss();
                isimDegistirDiyalog();
            }
        });

        settingsBtnChangeProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, izinVerme);
                }
                else {
                    resimDegistirIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(resimDegistirIntent, izinVerildi);
                }

            }
        });

        settingsDialog.getWindow().setAttributes(params);
        settingsDialog.show();
    }
    private void muzikAcKapaAyari(boolean b){
        editor = preferences.edit();
        editor.putBoolean("muzikDurumu", b);
        editor.apply();

        Toast.makeText(getApplicationContext(), "Ayar Başarıyla Kayıt Edildi. \nAktif Olması İçin Uygulamayı Yeniden Açın. ", Toast.LENGTH_SHORT).show();
        
        

    }

    private void isimDegistirDiyalog() {
        changeNameDialog = new Dialog(this);
        params = new WindowManager.LayoutParams();
        params.copyFrom(changeNameDialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        changeNameDialog.setCancelable(false);
        changeNameDialog.setContentView(R.layout.custom_dialog_change_name);

        changeNameImgClose = (ImageView) changeNameDialog.findViewById(R.id.custom_dialog_change_name_imageViewClose);
        changeNameEditTxtName = (EditText) changeNameDialog.findViewById(R.id.custom_dialog_change_name_editTextName);
        changeNameDialogBtn = (Button) changeNameDialog.findViewById(R.id.custom_dialog_change_name_btnChangeName);

        changeNameImgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeNameDialog.dismiss();
            }
        });

        changeNameDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChangeName = changeNameEditTxtName.getText().toString();

                if (!TextUtils.isEmpty(getChangeName)) {

                    if (!(getChangeName.matches(txtViewUserName.getText().toString()))) {

                        ismiGuncelle(getChangeName, txtViewUserName.getText().toString());

                    } else {

                        Toast.makeText(getApplicationContext(), "Zaten Bu İsmi Kullanıyorsunuz.", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(getApplicationContext(), "İsim Değeri Boş Olamaz.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        changeNameDialog.getWindow().setAttributes(params);
        changeNameDialog.show();
    }

    private void ismiGuncelle(String yeniDeger, String eskiDeger) {
        try {
            sqlSorgusu = "UPDATE Ayarlar SET k_adi = ? WHERE k_adi = ?";
            statement = database.compileStatement(sqlSorgusu);
            statement.bindString(1, yeniDeger);
            statement.bindString(2, eskiDeger);
            statement.execute();

            txtViewUserName.setText(yeniDeger);
            Toast.makeText(getApplicationContext(), "İsminiz Başarıyla Değiştirildi.", Toast.LENGTH_SHORT).show();

            if (changeNameDialog.isShowing()) {
                changeNameDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        //alertDioalog Açılımı
        AlertDialog.Builder alert= new AlertDialog.Builder(this);
        alert.setTitle("Kelime Bilmece");
        alert.setIcon(R.mipmap.ic_kelimebilmece);
        alert.setMessage("uygulamadan Çıkmak İstediğinize emin Misiniz?");
        alert.setPositiveButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alert.setNegativeButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
        alert.show();
    }

    //Rewarded
    private void loadAd() {

        if (mRewardedAd != null) {
            Activity activityContext = MainActivity.this;
            mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Toast.makeText(MainActivity.this, "The user earned the reward.", Toast.LENGTH_SHORT).show();
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();

                    mAdView.setVisibility(View.VISIBLE);

                    //Video bittikten sonraki animasyon
                    imgHeart.setX(constra.getPivotX());
                    imgHeart.setY(constra.getPivotY());
                    imgHeart.setVisibility(View.VISIBLE);
                    imgHeartXPos = ((imgHeartDesign.getX()) + (imgHeartDesign.getWidth() / 2f - 48));
                    imgHeartYPos = (imgHeartDesign.getY() + (imgHeartDesign.getHeight() / 2f - 48));

                    objectAnimatorHeartX = ObjectAnimator.ofFloat(imgHeart, "x", imgHeartXPos);
                    //Ne kadar süre sonra animasyon başlasın objectAnimatorHeartX.setStartDelay(1000);
                    //Ne kadar tekrar edeceği ile ilgili objectAnimatorHeartX.setRepeatCount();
                    objectAnimatorHeartX.setDuration(imgHeartDuration);

                    objectAnimatorHeartY = ObjectAnimator.ofFloat(imgHeart, "y", imgHeartYPos);
                    objectAnimatorHeartY.setDuration(imgHeartDuration);

                    imgHeartAnimatorSet = new AnimatorSet();
                    imgHeartAnimatorSet.playTogether(objectAnimatorHeartX);
                    imgHeartAnimatorSet.playTogether(objectAnimatorHeartY);
                    imgHeartAnimatorSet.start();
                    objectAnimatorHeartY.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            imgHeart.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, "+1 Can Kazandın.", Toast.LENGTH_SHORT).show();

                            sonCanDurumu = heartCount;
                            heartCount++;
                            canMiktariniGuncelle(sonCanDurumu, heartCount);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });

                }
            });


        } else {
            Toast.makeText(MainActivity.this, "The rewarded ad wasn't ready yet.", Toast.LENGTH_SHORT).show();
        }
    }

    private void canMiktariniGuncelle(int sonCanSayisi, int canSayisi) {

        try {

            sqlSorgusu = "UPDATE Ayarlar SET k_heart = ? WHERE k_heart = ?";
            statement = database.compileStatement(sqlSorgusu);
            statement.bindString(1, String.valueOf(canSayisi));
            statement.bindString(2, String.valueOf(sonCanSayisi));
            statement.execute();

            txtUserHeartCount.setText("+" + canSayisi);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == izinVerme){

            if (grantResults.length > 0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                resimDegistirIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(resimDegistirIntent, izinVerildi);

            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == izinVerildi){
            if (resultCode == RESULT_OK && data != null){
                resimUri = data.getData();

                alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setTitle("Kelime Bilmece");
                alertBuilder.setMessage("Profil Resminizi Değiştirmek İstediğinize Emin Misiniz?");
                alertBuilder.setIcon(R.mipmap.ic_kelimebilmece);
                alertBuilder.setCancelable(false);
                alertBuilder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        try {
                            if (Build.VERSION.SDK_INT >= 28){
                                resimDosyasi = ImageDecoder.createSource(MainActivity.this.getContentResolver(), resimUri);
                                resimBitmap = ImageDecoder.decodeBitmap(resimDosyasi);
                                userProfileImage.setImageBitmap(resimBitmap);
                            }
                            else{
                                resimBitmap = MediaStore.Images.Media.getBitmap(MainActivity.this.getContentResolver(), resimUri);
                                userProfileImage.setImageBitmap(resimBitmap);
                            }

                            profilResminiKaydet(resimBitmap);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                alertBuilder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertBuilder.show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void profilResminiKaydet(Bitmap profilResmi){
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            profilResmi.compress(Bitmap.CompressFormat.PNG, 75, outputStream);
            resimByte = outputStream.toByteArray();

            sqlSorgusu = "UPDATE Ayarlar SET k_image = ? WHERE k_adi = ?";
            statement = database.compileStatement(sqlSorgusu);
            statement.bindBlob(1, resimByte);
            statement.bindString(2, txtViewUserName.getText().toString());
            statement.execute();

            if (settingsDialog.isShowing())
                settingsDialog.dismiss();


            Toast.makeText(getApplicationContext(), "Profil Resminiz Başarıyla Değiştirildi.", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    static class GridItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;

        public GridItemDecoration(int spanCount, int spacing) {
            this.spanCount = spanCount;
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            outRect.left = (column + 1) * spacing / spanCount;
            outRect.right = (column + 1) * spacing / spanCount;
            outRect.bottom = spacing;
        }
    }
}
