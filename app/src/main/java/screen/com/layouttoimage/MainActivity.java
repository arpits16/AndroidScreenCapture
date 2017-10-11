package screen.com.layouttoimage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Method to request storage permission and call method to Draw Screen
     */
    private void requestPermissionForStorage() {
        // Check for permissions first
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.WRITE_STORAGE_REQUEST_CODE);
            return;
        }
        callDrawScreen();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Check permission's grant status. Note we need to update second condition for new permission
        if (permissions.length > 0 && (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permissions[0])) &&
                grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            switch (requestCode) {

                case Constants.WRITE_STORAGE_REQUEST_CODE:
                    callDrawScreen();
                    break;
                default:
                    Toast.makeText(this, "Please give permission", Toast.LENGTH_LONG);
            }
        } else {
            Log.d(TAG, "Permission request failed");
        }

    }

    /**
     * This method is used to generate the bitmap and then save it in form of image.
     */
    private void callDrawScreen() {
        GenerateScreen generateScreen = new GenerateScreen();
        generateScreen.drawScreen(this);
    }

    public void capture(View view) {
        requestPermissionForStorage();
    }
}
