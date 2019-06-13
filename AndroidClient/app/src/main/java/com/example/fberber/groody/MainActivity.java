package com.example.fberber.groody;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.*;
import android.widget.*;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class MainActivity extends AppCompatActivity implements GooeyMenu.GooeyMenuInterface,MainActivityInterface  {

    private GooeyMenu mGooeyMenu;
    private Toast mToast;

    public TextView tvCoord; /*my*/
    public TextView tvGesture; /*my*/
    GestureDetector mGestureDetector; /*my*/

    KeyboardListener mKeyboardListener;
    KeyboardView kvKeyboard;

    static final String LOG_TAG = "TrackPad_Activity";

    private int brightness=100;
    private ContentResolver cResolver;
    private Window window;

    private Button connect ;
    private Button backBut ;
    private TextView port;
    private String tempIP ="";

    private Queue<byte[]> outgoing = new ConcurrentLinkedQueue<>();
    private Queue<byte[]> incoming = new ConcurrentLinkedQueue<>();
    private Thread connection = null;

    private static SeekBar seek_bar;
    private static TextView text_view;
    private static Switch controlSwitch;

    private boolean loopControl = true ;
    private boolean loopControl2 = true ;
    private boolean notfound = false ;
    private int keyfromComputer ;
    private int brightfromComputer ;
    public static String ComputerIp ;
    public static final int systemPort = 7070 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mKeyboardListener = new KeyboardListener(this, this);
        port=(TextView)findViewById(R.id.port);
        connect = (Button)findViewById(R.id.connectbutton);
        backBut = (Button)findViewById(R.id.backButton);
        controlSwitch = (Switch) findViewById(R.id.myswitch);

    }

    public void backButton(View v){
        setContentView(R.layout.activity_main2);
        mGooeyMenu = (GooeyMenu) findViewById(R.id.gooey_menu);
        mGooeyMenu.setOnMenuListener(this);
        mGooeyMenu.setActivity(this);
    }

    public void connectButton(View v) throws InterruptedException {

        if (loopControl == true) {

            System.err.println("Your Device IP Address: >> " + GetDeviceipWiFiData());
            String[] parts = GetDeviceipWiFiData().split("\\.");
            tempIP += parts[0] + "." + parts[1] + "." + parts[2] + "." + 1;
            System.err.println("Your TEMPPPPPP IP Address: >> " + tempIP);

            IpFinder myfinderobj = new IpFinder(tempIP, systemPort);
            for (int i = 50; Mediator.ipflag == 0; i++) {
                String[] parts2 = tempIP.split("\\.");
                tempIP = parts2[0] + "." + parts2[1] + "." + parts2[2] + "." + i;
                myfinderobj.connect(tempIP, systemPort);
                Thread.sleep(150);
                if (i == 255) {
                    System.err.println("------Ip is not found-------");
                    Toast.makeText(MainActivity.this, "Ip is not found", Toast.LENGTH_LONG).show();
                    notfound=true ;
                    break;
                }

            }

            //myfinderobj.disconnect();
            System.err.println("<<<<<<<<<<<<<<" + Mediator.ipcontrol);
            ComputerIp = Mediator.ipcontrol;
            Mediator.port=systemPort;
            //ComputerIp="192.168.43.213" ;
            loopControl=false;
        }
        if(notfound==false) {
            if (controlSwitch.isChecked() == true) {
                System.err.println("-----This is autoconnect-------");
                setContentView(R.layout.activity_main2);
                mGooeyMenu = (GooeyMenu) findViewById(R.id.gooey_menu);
                mGooeyMenu.setOnMenuListener(this);
                mGooeyMenu.setActivity(this);
                findAllViews();

                connection = new Thread(new ServerConnection(ComputerIp, systemPort, incoming, 2));
                connection.start();
                Thread.sleep(2000);
                System.out.println("----------  Bright from Computer  : " + Mediator.med);
                brightfromComputer = Mediator.med;

                //MousePos();
            } else {

                if (loopControl2 == true) {
                    connection = new Thread(new ServerConnection(ComputerIp, systemPort, incoming, 2));
                    connection.start();
                    Thread.sleep(2000);

                    System.out.println("----------  Key from Computer  : " + Mediator.med);
                    keyfromComputer = Mediator.med;
                    loopControl2 = false;
                }

                int key = Integer.parseInt(port.getText().toString());

                if (key == keyfromComputer) {
                    setContentView(R.layout.activity_main2);
                    mGooeyMenu = (GooeyMenu) findViewById(R.id.gooey_menu);
                    mGooeyMenu.setOnMenuListener(this);
                    mGooeyMenu.setActivity(this);
                    findAllViews();
                    //MousePos();
                } else {
                    Toast.makeText(MainActivity.this, "Wrong port", Toast.LENGTH_LONG).show();
                }
            }
        }
        System.out.println("-------------end of the connect button -----------------");
    }


    public String GetDeviceipWiFiData() {

        WifiManager wm = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        @SuppressWarnings("deprecation")
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return ip;
    }

    private void findAllViews(){
        tvCoord = (TextView)findViewById(R.id.tvCoord);
        tvGesture = (TextView)findViewById(R.id.tvGesture);
        //kvKeyboard = (KeyboardView)findViewById(R.id.kvKeyboard);
    }

    public void MousePos(){
        String data = "mouse#";
        byte[] byteData = data.getBytes();
        outgoing = new ConcurrentLinkedQueue<>();
        outgoing.add(byteData) ;
        connection = new Thread(new ServerConnection(ComputerIp, systemPort,outgoing,1));
        connection.start();
    }

    public void ShowDialog(int menuvalue){
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        final SeekBar seek = new SeekBar(this);
        seek.setMax(200);
        seek.setKeyProgressIncrement(1);
        window = getWindow();
        cResolver = getContentResolver();

        //popDialog.setIcon(android.R.drawable.btn_star_big_on);
        if(menuvalue==1){
            popDialog.setTitle("Brightness ");
            /*-------------first ayar----------*/
            String data = "100#";
            byte[] byteData = data.getBytes();
            outgoing = new ConcurrentLinkedQueue<>();
            outgoing.add(byteData) ;
            connection = new Thread(new ServerConnection(ComputerIp, systemPort,outgoing,1));
            connection.start();
            /*--------------------------------*/
        }
        else{
            popDialog.setTitle("Voice ");
            brightness=10;
        }

        popDialog.setView(seek);
        //Set the progress of the seek bar based on the system's brightness
        seek.setProgress(brightness);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){

                System.err.println("Value of : " + progress) ;

                String data = "";
                data+=progress;
                data+="#";
                byte[] byteData = data.getBytes();

                switch (progress){
                    case 0 :
                        outgoing = new ConcurrentLinkedQueue<>();
                        outgoing.add(byteData) ;
                        connection = new Thread(new ServerConnection(ComputerIp, systemPort,outgoing,1));
                        connection.start();
                        break;
                    case 25 :
                        outgoing = new ConcurrentLinkedQueue<>();
                        outgoing.add(byteData) ;
                        connection = new Thread(new ServerConnection(ComputerIp, systemPort,outgoing,1));
                        connection.start();
                        break;
                    case 50 :
                        outgoing = new ConcurrentLinkedQueue<>();
                        outgoing.add(byteData) ;
                        connection = new Thread(new ServerConnection(ComputerIp, systemPort,outgoing,1));
                        connection.start();
                        break;
                    case 75 :
                        outgoing = new ConcurrentLinkedQueue<>();
                        outgoing.add(byteData) ;
                        connection = new Thread(new ServerConnection(ComputerIp, systemPort,outgoing,1));
                        connection.start();
                        break;
                    case 100 :
                        outgoing = new ConcurrentLinkedQueue<>();
                        outgoing.add(byteData) ;
                        connection = new Thread(new ServerConnection(ComputerIp, systemPort,outgoing,1));
                        connection.start();
                        break;
                    case 125 :
                        outgoing = new ConcurrentLinkedQueue<>();
                        outgoing.add(byteData) ;
                        connection = new Thread(new ServerConnection(ComputerIp, systemPort,outgoing,1));
                        connection.start();
                        break;
                    case 150 :
                        outgoing = new ConcurrentLinkedQueue<>();
                        outgoing.add(byteData) ;
                        connection = new Thread(new ServerConnection(ComputerIp, systemPort,outgoing,1));
                        connection.start();break;
                    case 175 :
                        outgoing = new ConcurrentLinkedQueue<>();
                        outgoing.add(byteData) ;
                        connection = new Thread(new ServerConnection(ComputerIp, systemPort,outgoing,1));
                        connection.start();
                        break;
                    case 200 :
                        outgoing = new ConcurrentLinkedQueue<>();
                        outgoing.add(byteData) ;
                        connection = new Thread(new ServerConnection(ComputerIp, systemPort,outgoing,1));
                        connection.start();
                        break;
                        default: break;
                }

            }
            public void onStartTrackingTouch(SeekBar arg0) {

            }
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Button OK
        popDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        popDialog.create();
        popDialog.show();
    }

    public void seebbarr(int control_activity){

        seek_bar = (SeekBar)findViewById(R.id.seekBar);
        text_view =(TextView)findViewById(R.id.textView);
        if(control_activity==1){
            text_view.setText("Covered Bright: " + brightfromComputer + " / " +seek_bar.getMax());

        }

        else
            text_view.setText("Covered Voice : " + seek_bar.getProgress() + " / " +seek_bar.getMax());


        seek_bar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int flag=0;
                    int progress_value;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = progress;
                        text_view.setText("Covered : " + progress + " / " +seek_bar.getMax());
                        //Toast.makeText(MainActivity.this,"SeekBar in progress",Toast.LENGTH_LONG).show();

                        String data = "";
                        data+=progress*10;
                        data+="#";
                        byte[] byteData = data.getBytes();

                        outgoing = new ConcurrentLinkedQueue<>();
                        outgoing.add(byteData) ;
                        connection = new Thread(new ServerConnection(ComputerIp, systemPort,outgoing,1));
                        connection.start();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        Toast.makeText(MainActivity.this,"SeekBar in StartTracking",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        text_view.setText("Covered : " + progress_value + " / " +seek_bar.getMax());
                        Toast.makeText(MainActivity.this,"SeekBar in StopTracking",Toast.LENGTH_LONG).show();
                    }
                }
        );

    }

    /*--------------IMPLEMENTS  GooeyMenuInterface----------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void menuOpen() {
        //showToast("Menu Open");
    }

    @Override
    public void menuClose() {
        //showToast( "Menu Close");
    }

    @Override
    public void menuItemClicked(int menuNumber) {

        if(menuNumber==1){
            String data ="keyboard#";
            byte[] byteData = data.getBytes();
            outgoing = new ConcurrentLinkedQueue<>();
            outgoing.add(byteData) ;
            connection = new Thread(new ServerConnection(ComputerIp, systemPort,outgoing,1));
            connection.start();

            setContentView(R.layout.activity_keyboard);
            kvKeyboard = (KeyboardView)findViewById(R.id.kvKeyboard);
            createKeyboard();
        }
        else if(menuNumber==2){
            String data ="bright#";
            byte[] byteData = data.getBytes();
            outgoing = new ConcurrentLinkedQueue<>();
            outgoing.add(byteData) ;
            connection = new Thread(new ServerConnection(ComputerIp, systemPort,outgoing,1));
            connection.start();

            //ShowDialog(1);
            setContentView(R.layout.bright_main);
            seebbarr(1);
        }
        else if(menuNumber==3){
            String data ="voice#";
            byte[] byteData = data.getBytes();
            outgoing = new ConcurrentLinkedQueue<>();
            outgoing.add(byteData) ;
            connection = new Thread(new ServerConnection(ComputerIp, systemPort,outgoing,1));
            connection.start();
            //ShowDialog(2);
            setContentView(R.layout.voice_main);
            seebbarr(2);
        }
        else if(menuNumber==4){
            /*String data ="mouse#";
            byte[] byteData = data.getBytes();
            outgoing = new ConcurrentLinkedQueue<>();
            outgoing.add(byteData) ;
            connection = new Thread(new ServerConnection(ComputerIp, systemPort,outgoing,1));
            connection.start();*/
        }
    }

    private void showToast(String msg){
        if(mToast!=null){
            mToast.cancel();
        }
       mToast= Toast.makeText(this,msg,Toast.LENGTH_SHORT);
       mToast.setGravity(Gravity.CENTER,0,0);
       mToast.show();
    }


    /*--------------IMPLEMENTS  MainActivityInterface----------------------*/
    @Override
    public void setTextCoord(String coord) {

    }

    @Override
    public void setTextGesture(String gesture) {

    }

    @Override
    public int getPointerCount() {
        return 0;
    }

    @Override
    public void sendData(byte[] data) {

    }

    @Override
    public KeyboardView getKeyboardView() {
        return null;
    }

    @Override
    public void onSocketStateChanged(int state) {

    }
    @Override
    public int ResolveAction(TPAction action) {
        return 0;
    }
    public void createKeyboard(){
        KeyboardManager.kbdv = kvKeyboard;
        KeyboardManager.kbd_fn = new Keyboard(this, R.xml.tpkeyboard_portrait_fn);
        KeyboardManager.kbd_qwert = new Keyboard(this, R.xml.tpkeyboard_portrait_qwert);
        KeyboardManager.kbd_qwert_shift = new Keyboard(this, R.xml.tpkeyboard_portrait_qwert_shift);
        KeyboardManager.kbd_numpad = new Keyboard(this, R.xml.tpkeyboard_portrait_numpad);
        KeyboardManager.kbd_numpad_shift = new Keyboard(this, R.xml.tpkeyboard_portrait_numpad_shift);
        KeyboardManager.kbd_number_punctuation = new Keyboard(this, R.xml.tpkeyboard_portrait_number_punctuation);
        KeyboardManager.kbd_number_punctuation_shift = new Keyboard(this, R.xml.tpkeyboard_portrait_number_punctuation_shift);
        KeyboardManager.kbd_media = new Keyboard(this, R.xml.tpkeyboard_portrait_media);
        KeyboardManager.kbd_hidden = new Keyboard(this, R.xml.tpkeyboard_portrait_hidden);
        KeyboardManager.cacheKeys();

        kvKeyboard.setKeyboard(KeyboardManager.kbd_qwert);
        kvKeyboard.setOnKeyboardActionListener(mKeyboardListener);
    }
}
