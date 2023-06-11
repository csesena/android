package csesena.entretenimiento.sinosoy;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class Informacion extends Activity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informacion);
    }

    public void volver(View v) {
		Informacion.this.finish();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

      if (keyCode == KeyEvent.KEYCODE_BACK) {
    	  Informacion.this.finish();
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
    
}
