package com.example.sleep;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Button;
import android.content.Intent;
import android.widget.ListView;
import android.widget.Toast;


public class Subactivity extends AppCompatActivity{

    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    Button btn7;

    // 블루투스 송수신 장치
    private BluetoothAdapter bluetoothAdapter;
    private static BluetoothManager bluetoothManager;

    // Bluetooth Request Code
    private int REQUEST_ENABLE_BT = 4;

    // 접근 권한 Code
    private final int PERMISSION_REQUEST_ACCESS_COARSE_LOCATION = 100;

    // Scan에 필요한 변수
    private BluetoothLeScanner bluetoothLeScanner;
    private boolean mScanning = false;
    private BluetoothDevice device = null;
    private Handler handler;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    // Device List의 Adapter
    private LeDeviceListAdapter leDeviceListAdapter;

    // Dialog
    private ListView DeviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subactivity);

        // BLE가 기기에서 지원되는지 확인(?)하고, 지원되는 경우 활성화
        // 지원되지 않는 경우, BLE 비활성화
        // 지원되는 경우, 앱을 사용하는동안 활성화하도록 설정
        // 1. BluetoothAdapter 가져오기
        // Initializes Bluetooth adapter.
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        // 2. Bluetooth 활성화
        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth
        if(bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        // Bluetooth 기기를 탐색할 수 있게 위치 권한이 있는지 확인 후, 없으면 권한 요청
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_ACCESS_COARSE_LOCATION
            );
        }



        btn1 = (Button)findViewById(R.id.Sleep_apnea_Button);
        btn2 = (Button)findViewById(R.id.Record_Button);
        btn3 = (Button)findViewById(R.id.logoutbutton);
        btn4 = (Button)findViewById(R.id.psqi_Button);

        btn7 = (Button)findViewById(R.id.alarm);

        handler = new Handler();

        this.SetListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        // 앱을 사용하는 장치에서 BLE를 지원하지않는 다면 앱 종료
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE not supported", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void SetListener()
    {
        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(),Sleepinfo.class);
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                showAlertDialog();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                SaveSharedPreference.clearUserName(Subactivity.this);
                startActivity(intent);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),psqi_main.class);
                startActivity(intent);
            }
        });

        btn7.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),alarm.class);
                SaveSharedPreference.clearUserName(Subactivity.this);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Subactivity.this);
        View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        builder.setView(view);

        DeviceList = (ListView) view.findViewById(R.id.Device_List);

        // BLE 기기 찾기
        // 찾은 기기는 DeviceList의 Item으로 저장
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        leDeviceListAdapter = new LeDeviceListAdapter();
        DeviceList.setAdapter(leDeviceListAdapter);

        if(mScanning == false) {
            scanLeDevice(true);
        }else {
            scanLeDevice(false);
        }

        // 특정 Bluetooth 기기를 클릭하면 해당 기기와 연결 시도
        DeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                device = leDeviceListAdapter.getDevice(position);
                Toast.makeText(Subactivity.this, "Device : " + device.getName() + "\nAddress : " + device.getAddress(), Toast.LENGTH_LONG).show();
            }
        });

        builder.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(device != null) {
                    Intent intent = new Intent(getApplicationContext(),Recording.class);
                    intent.putExtra("Bluetooth_Device", device);
                    startActivity(intent);
                }else {
                    Toast.makeText(Subactivity.this, "Device not found", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                device = null;
            }
        });
        builder.show();
        /*
        AlertDialog dialog = builder.create();
        dialog.show();
         */
    }

    private void scanLeDevice(final boolean enable) {
        if(bluetoothLeScanner != null) {
            if(enable) {
                // Stops scanning after a pre-defined scan period.
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScanning = false;
                        bluetoothLeScanner.stopScan(leScanCallback);
                    }
                }, SCAN_PERIOD);

                mScanning = true;
                bluetoothLeScanner.startScan(leScanCallback);
            }else {
                mScanning = false;
                bluetoothLeScanner.stopScan(leScanCallback);
            }
        }
    }

    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, final ScanResult result) {
            super.onScanResult(callbackType, result);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    leDeviceListAdapter.addDevice(result.getDevice());
                    leDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Toast.makeText(Subactivity.this, "Scan Failed: Please try again...", Toast.LENGTH_LONG).show();
        }
    };

    // StartActivityForResult를 실행하면, onActivityResult로 결과 반환
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // resultCode 거부 : 0 (RESULT_CANCELED)
        // resultCode 허용 : -1 (RESULT_OK)
        Toast.makeText(getApplicationContext(), "requestCode:" + requestCode + ", resultCode:" + resultCode, Toast.LENGTH_LONG).show();

        if(requestCode == REQUEST_ENABLE_BT) {
            // 사용자가 눌렀을 때, 결과 값
            if(resultCode == RESULT_OK) {
                // 허용 -> 블루투스 활성화
                Toast.makeText(this, "Bluetooth activate", Toast.LENGTH_SHORT).show();
            }else if(resultCode == RESULT_CANCELED) {
                // 거부 -> 앱 종료
                Toast.makeText(this, "Bluetooth not activate", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}