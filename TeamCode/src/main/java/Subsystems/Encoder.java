package Subsystems;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Base64;

@RequiresApi(api = Build.VERSION_CODES.O)
class Encoder extends Base64.Encoder {

    public Encoder(DcMotor leftEncoder) {
        super();
    }
    public static void encode (byte[] input,
                               int offset,
                               int len,
                               int flags){}
}
//'Encoder(boolean, byte[], int, boolean)' is not public in 'java.util.Base64.Encoder'.
//Cannot be accessed from outside package.
//what does that mean
//whatever who knows
 