package com.sravan.efactorapp.utils.Crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoAES {
    private static final String TRANSFORMATION_DEFAULT = "AES/CFB/NoPadding";
    private Cipher mDecryptCipher;
    private Cipher mEncryptCipher;
    private final byte[] mIV;
    private final byte[] mKey;
    private final String mTransformation;

    public CryptoAES(byte[] paramKey, byte[] paramIV) {
        this(paramKey, paramIV, TRANSFORMATION_DEFAULT);
    }

    public CryptoAES(byte[] paramKey, byte[] paramIV, String paramString) {
        this.mKey = paramKey;
        this.mIV = paramIV;
        this.mTransformation = paramString;
        this.mEncryptCipher = createEncryptCipher();
        this.mDecryptCipher = createDecryptCipher();
    }

    private Cipher createEncryptCipher() {
        try {
            Cipher localCipher = Cipher.getInstance(this.mTransformation);
            localCipher.init(1, new SecretKeySpec(this.mKey, "AES"), new IvParameterSpec(this.mIV));
            return localCipher;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidAlgorithmParameterException e2) {
            e2.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e3) {
            e3.printStackTrace();
            return null;
        } catch (InvalidKeyException e4) {
            e4.printStackTrace();
            return null;
        }
    }

    private Cipher createDecryptCipher() {
        try {
            Cipher localCipher = Cipher.getInstance(this.mTransformation);
            localCipher.init(2, new SecretKeySpec(this.mKey, "AES"), new IvParameterSpec(this.mIV));
            return localCipher;
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException localNoSuchAlgorithmException) {
            localNoSuchAlgorithmException.printStackTrace();
            return null;
        }
    }

    public byte[] encrypt(byte[] paramArrayOfByte) {
        try {
            return this.mEncryptCipher.doFinal(paramArrayOfByte);
        } catch (BadPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalBlockSizeException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public byte[] decrypt(byte[] paramArrayOfByte) {
        try {
            return this.mDecryptCipher.doFinal(paramArrayOfByte);
        } catch (BadPaddingException | IllegalBlockSizeException localBadPaddingException) {
            localBadPaddingException.printStackTrace();
            return null;
        }
    }
}
