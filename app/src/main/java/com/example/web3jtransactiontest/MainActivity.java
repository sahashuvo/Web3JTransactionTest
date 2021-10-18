package com.example.web3jtransactiontest;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;

import com.samsung.android.sdk.blockchain.CoinType;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.SBlockchain;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.android.sdk.blockchain.coinservice.CoinNetworkInfo;
import com.samsung.android.sdk.blockchain.coinservice.CoinService;
import com.samsung.android.sdk.blockchain.coinservice.CoinServiceFactory;
import com.samsung.android.sdk.blockchain.exception.SsdkUnsupportedException;
import com.samsung.android.sdk.blockchain.network.EthereumNetworkType;
import com.samsung.android.sdk.blockchain.wallet.HardwareWallet;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    String LOG_TAG = "AAA";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = findViewById(R.id.tv1);
//        try {
//            getBalanceUsingExtensionkitSDK(tv);
//        } catch (SsdkUnsupportedException e) {
//            e.printStackTrace();
//        }
        getFromAddressUsingWeb3J(tv);
    }

    private void getBalanceUsingExtensionkitSDK(TextView tv) throws SsdkUnsupportedException {
        SBlockchain sBlockchain = new SBlockchain();
        sBlockchain.initialize(this);
        CoinNetworkInfo coinNetworkInfo = new CoinNetworkInfo(CoinType.ETH, EthereumNetworkType.ROPSTEN,
                "https://ropsten.infura.io/v3/71cf9f88c5b54962a394d80890e41d5b");
        CoinService ethereumCoinService = CoinServiceFactory.getCoinService(MainActivity.this, coinNetworkInfo);
        EthereumAccount ethereumAccount = new EthereumAccount(EthereumNetworkType.ROPSTEN, "0xE8Ac2B52BB224CA9c83E558e30F9A61E001d8AAC",
                "","", null);
        ethereumCoinService.getBalance(ethereumAccount).setCallback(new ListenableFutureTask.Callback<BigInteger>() {
            @Override
            public void onSuccess(BigInteger bigInteger) {
                Log.d(LOG_TAG,"balance: success");
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      tv.setText(bigInteger.toString());
                                  }
                              }
                );
            }

            @Override
            public void onFailure(@NonNull ExecutionException e) {
                Log.e(LOG_TAG,"balance: fail");
                Log.e(LOG_TAG, e.toString());
            }

            @Override
            public void onCancelled(@NonNull InterruptedException e) {
                Log.e(LOG_TAG,"balance: cancel");
                Log.e(LOG_TAG, e.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getFromAddressUsingWeb3J(TextView tv){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Web3j web3j = Web3j.build(new HttpService("https://ropsten.infura.io/v3/71cf9f88c5b54962a394d80890e41d5b"));
        try{
            BigInteger balance = web3j.ethGetBalance("0xE8Ac2B52BB224CA9c83E558e30F9A61E001d8AAC", DefaultBlockParameterName.LATEST).
                    sendAsync().get().getBalance();
            tv.setText(balance.toString());
            Log.d("AAA","Balance using web3j directly: "+balance.toString());
        }catch (Exception e){
            tv.setText(e.toString());
            Log.e("AAA",e.toString());
        }
    }
}