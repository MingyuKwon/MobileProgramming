package com.example.broadcastreciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.broadcastreciever.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var broadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initPermission()
        initLayout()
        //getMessage(intent) // intent는 이 activity가 생성 될 때 넘어온 intent를 의미한다
        checkSettingOverlayWindow(intent)

    }

    override fun onNewIntent(intent: Intent?) { // 본래는 create에서 intent를 받지만, 이미 생성 된 후에 intent를 받을 때는 여기서 받는다
        super.onNewIntent(intent)
        checkSettingOverlayWindow(intent)
    }

    fun getMessage(intent: Intent?)
    {
        if(intent != null)
        {
            if(intent.hasExtra("msgSender") and intent.hasExtra("msgBody")){
                val msgSender = intent.getStringExtra("msgSender")
                val msgBody = intent.getStringExtra("msgBody")
                Toast.makeText(this, "보낸번호 : " + msgSender + "\n" + msgBody, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun checkSettingOverlayWindow(intent: Intent?)
    {
        if(Settings.canDrawOverlays((this))){ // setting 결과가 어떻는지를 확인한다
            getMessage(intent)
        }else{
            val overlayIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            requestSettingLauncher.launch(overlayIntent)
        }
    }

    val requestSettingLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(Settings.canDrawOverlays((this))){ // setting 결과가 어떻는지를 확인한다
                getMessage(this.intent)
            }else{
                Toast.makeText(this, "권한 승인이 거부되었습니다", Toast.LENGTH_SHORT).show()
            }
        }


    // 이게 우리가 직접 권한을 설정 해 줄 수 있는 창을 띄워주는 역할을 한다
    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                initPermission()
            }else{
                Toast.makeText(this, "권한 승인이 거부되었습니다", Toast.LENGTH_SHORT).show()
            }
        }

    private fun initPermission() {
        when{
            // 만약 지금 설정 되어 있는 권한이 granted라면
            (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) -> {
                Toast.makeText(this, "문자 수신 동의함", Toast.LENGTH_SHORT).show()
            }

            // 만약 지금 설정 되어 있는 권한이 granted가 아니지만, 아직 여러번 물어 보진 않았을 때
            ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.RECEIVE_SMS) -> {
                permissionAlertDlg() // 다시 한번 생각해 달라는 창을 띄운다
            }

            else->{
                requestPermissionLauncher.launch(android.Manifest.permission.RECEIVE_SMS) // 권한 요청을 직접 할 수 있는 창을 띄운다
            }
        }
    }

    // 이 창은 직접 권한을 설정 할 수 있는 창이 아니라, 권한이 거부가 되었을 떄 다시 한번만 생각해 달라고 말을 할 때 쓰이는 창이다
    private fun permissionAlertDlg() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("반드시 문자 수신 권한이 허용되어야 합니다")
            .setTitle("권한 체크")
            .setPositiveButton("OK"){
                _,_ ->
                requestPermissionLauncher.launch(android.Manifest.permission.RECEIVE_SMS)
            }.setNegativeButton("Cancel"){
                dlg, _ ->
                dlg.dismiss()
            }

        val dlg = builder.create()
        dlg.show()
    }

    private fun initLayout() {
        broadcastReceiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent != null)
                {
                    if(intent.action.equals(Intent.ACTION_POWER_CONNECTED)){
                        Toast.makeText(context, "충전 시작", Toast.LENGTH_SHORT).show()
                    }else if(intent.action.equals(Intent.ACTION_POWER_DISCONNECTED))
                    {
                        Toast.makeText(context, "충전 해제", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(Intent.ACTION_POWER_CONNECTED)
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED)
        registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(broadcastReceiver)
    }
}