package csesena.entretenimiento.sinosoy;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class SiNoSoy extends Activity implements OnInitListener{
	//Declaracion de variables globales
	boolean sonido=true;
	private TextToSpeech myTTS;
	private int MY_DATA_CHECK_CODE = 0;
	Random generator = new Random();
	int colorSel=0;
	//String fraseSel= "x";
	ArrayList<String> frasesSel= new ArrayList<String>();
	String frase;
	String[] frases= {"Si no soy de Burgos, �por qu� tengo este pedazo de morcilla?", "Si no soy Curro Jim�nez, �por qu� tengo este trabuco?",
			"Si no soy un beb� grande y gordote, �por qu� tengo este cipote?", "Si no tengo un huerto, �por qu� tengo este nabo?",
			"Si no soy un toro, �por qu� tengo este rabo?", "Si no soy un b�falo, �por qu� tengo este falo?", "Si mi puerta no se atranca, �por qu� tengo esta tranca?",
			"Si no me gusta la fruta, �por qu� tengo esta banana?", "Si no soy frutero, �por qu� tengo este pepino?", "Si no soy matem�tico ni de color fucsia, �por qu� tengo esta pilila?",
			"Si no soy Beethoven, �por qu� tengo este instrumento?", "Si no tengo suerte, �por qu� tengo esta chorra?", "Si no soy marinero, �por qu� tengo este m�stil?",
			"Si no soy obrero, �por qu� tengo esta tuneladora?", "Si no soy soldado, �por qu� tengo este ca��n?", "Si no soy ornit�logo, �por qu� tengo este pajarraco?",
			"Si no soy cartero, �por qu� tengo este paquete?", "Si no soy le�ador, �por qu� tengo este tronco?", "Si no soy una abeja, �por qu� tengo este aguij�n?",
			"Si no soy bombero, �por qu� tengo esta manguera?", "Si no soy motorista, �por qu� tengo este bultaco?", "Si no soy antidisturbios, �por qu� tengo esta porra?", "Si no juego al b�isbol, �por qu� tengo este bate?",
			"Si no soy un tr�pode, �por qu� tengo tres piernas?", "Si no soy militar, �por qu� tengo este misil?", "Si no soy un submarino, �por qu� tengo este torpedo?", "Si no soy una porter�a, �por qu� tengo este poste?",
			"Si no soy jinete, �qu� tengo entre las piernas?", "Si no soy panadero, �por qu� tengo esta barra de pan?", "Si no soy fontanero, �qu� hago con esta tuber�a?",
			"Si no soy alem�n, �por qu� tengo este frankfurt?", "Si no soy franc�s, �por qu� tengo esta baguette?", "Si no soy Marsupilami, �por qu� tengo esta cola?"};
	int[] colors= {R.color.blue,R.color.green,R.color.white,R.color.yellow,R.color.red,R.color.purple,R.color.orange};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ImageButton share= (ImageButton) findViewById(R.id.share);
    	share.setEnabled(false);
    	ImageButton repeat= (ImageButton) findViewById(R.id.repeat);
    	repeat.setEnabled(false);
        
      //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
    }
    
    //Crear menu
    public boolean onCreateOptionsMenu(Menu menue) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity, menue);
        return true;
    }
    
    //Listener para el menu
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
            	setResult(RESULT_OK);
            	Intent i = new Intent(getApplicationContext(), Informacion.class);
            	startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    //Ir cambiando la frase del TextView
    public void mostrarFrase(View v) {
    	TextView vista= (TextView) findViewById(R.id.tv);
    	ImageButton share= (ImageButton) findViewById(R.id.share);
    	ImageButton repeat= (ImageButton) findViewById(R.id.repeat);
    	share.setEnabled(true);
    	//share.setBackgroundResource(R.drawable.share_normal);
    	repeat.setEnabled(true);
    	frase =aleatorioString(frases);
    	vista.setText(frase);
    	frasesSel.add(frase);
    	vista.setTextColor(getResources().getColor(aleatorioInt(colors)));
    	if (sonido==true) { speakWords(frase); }
    }
    
    //Coger un valor aleatorio del array de Strings
    public String aleatorioString (String[] array) {
        int rnd = generator.nextInt(array.length);
        String devolver= array[rnd];
        if (frases.length==frasesSel.size()) {
        	frasesSel.clear();
        	devolver=aleatorioString(array);
        } else {
        	if (frasesSel.contains(devolver)) { devolver=aleatorioString(array); }
        }
        return devolver;
    }
    
    //Coger un valor aleatorio del array de Long
    public int aleatorioInt (int[] array) {
        int rnd = generator.nextInt(array.length);
        if (colorSel==array[rnd]) { array[rnd]=aleatorioInt(array); }
        colorSel=array[rnd];
        return array[rnd];
    }
    
    //M�todo para salir de la aplicaci�n
    public void salir(View v) {
    	// Exiting
		new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Salir")
        .setMessage("�Est�s seguro?")
        .setNegativeButton(android.R.string.cancel, null) //sin listener
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
          @Override
          public void onClick(DialogInterface dialog, int which){
            //Salir
        	  setResult(RESULT_OK);
            SiNoSoy.this.finish();
          }
        })
        .show();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

      if (keyCode == KeyEvent.KEYCODE_BACK) {

    	// Exiting
  		new AlertDialog.Builder(this)
          .setIcon(android.R.drawable.ic_dialog_alert)
          .setTitle("Salir")
          .setMessage("�Est�s seguro?")
          .setNegativeButton(android.R.string.cancel, null) //sin listener
          .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
            @Override
            public void onClick(DialogInterface dialog, int which){
              //Salir
            	setResult(RESULT_OK);
              SiNoSoy.this.finish();
            }
          })
          .show();
          
        // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
        return true;
      }
      
      /*if (keyCode == KeyEvent.KEYCODE_MENU) {

    	  MisPedidos.this.finish();
          
        // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
        return true;
      }*/
      
    //para las demas cosas, se reenvia el evento al listener habitual
      return super.onKeyDown(keyCode, event);
    }

  //M�todo para encender o silenciar el sonido
    public void mutearSiNo(View v) {
    	ImageButton imb= (ImageButton) findViewById(R.id.ib);
    	if (sonido==true) {
    		if (myTTS.isSpeaking()) {
    			myTTS.stop();
    		}
	    	imb.setBackgroundResource(R.drawable.mute_pressed);
	    	sonido=false;
    	} else {
    		imb.setBackgroundResource(R.drawable.mute_normal);
        	sonido=true;
    	}
    }
    
   //M�todo para compartir strings
    public void compartir(View v) {
    	Intent intent=new Intent(android.content.Intent.ACTION_SEND);
    	intent.setType("text/plain");
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
    	
    	// A�adimos la informaci�n a compartir en el Intent
    	String subject="http://goo.gl/uFjWG";
    	intent.putExtra(Intent.EXTRA_SUBJECT, subject);
    	intent.putExtra(Intent.EXTRA_TEXT, frase);
    	
    	startActivity(Intent.createChooser(intent, "Compartir frase"));
    }
    
  //M�todo para repetir la frase
    public void repetir(View v) {
    	if (sonido==true) { speakWords(frase); }
    }
    
    //Funcion para que hable
    private void speakWords(String speech) {
            //speak straight away
            myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }
 
        //act on result of TTS data check
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
            myTTS = new TextToSpeech(this, this);
            }
            else {
                    //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }
 
        //setup TTS
    public void onInit(int initStatus) {
 
            //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            	Locale loc = null;
            	loc = new Locale("spa","ES");
                //myTTS.setLanguage(Locale.ITALIAN);
				myTTS.setLanguage(loc);
            	myTTS.setPitch((float) 0.5);
        }
        /*else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }*/
    }
}