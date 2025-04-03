package top.kncweb.sposocapp.util;


import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.manipulation.Ordering;

@RunWith(AndroidJUnit4.class)
public class EncryptedSharedPreferencesHelperTest {
    @Test
    public void testSaveLord(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        EncryptedSharedPreferencesHelper encryptedSharedPreferencesHelper = new EncryptedSharedPreferencesHelper(context);

        String aimString = "asdasd";
        encryptedSharedPreferencesHelper.saveJwtToken(aimString);
        assertEquals(aimString,encryptedSharedPreferencesHelper.getJwtToken());
    }
}
