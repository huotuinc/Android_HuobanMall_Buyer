package com.jxd.jlibrary;

import org.junit.Test;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.digest.*;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void TestMd5(){
        try {
            String m = "jxdong";
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update( m.getBytes("utf-8"));
            byte[] s1 = messageDigest.digest();
            //byte[] s2 = DigestUtils.md5( m );
        }catch (NoSuchAlgorithmException ex) {
        }catch (UnsupportedEncodingException exx){
        }
    }
}