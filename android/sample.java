import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
 
public class Sample extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
 
        TextView text = new TextView(this);
        text.setText("Hello World, Android");
        setContentView(text);
        
    }
}
