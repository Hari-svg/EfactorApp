package com.sravan.efactorapp.utils.Crypto;

import java.math.BigInteger;
import java.security.AlgorithmParameterGenerator;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

public class CryptoDH {
    private static final String FIXG = "2";
    private static final String FIXP = "cf5cf5c38419a724957ff5dd323b9c45c3cdd261eb740f69aa94b8bb1a5c96409153bd76b24222d03274e4725a5406092e9e82e9135c643cae98132b0d95f7d65347c68afc1e677da90e51bbab5f5cf429c291b4ba39c6b2dc5e8c7231e46aa7728e87664532cdf547be20c9a3fa8342be6e34371a27c06f7dc0edddd2f86373";
    private BigInteger mG;
    private final int mLength;
    private BigInteger mP;
    private DHPrivateKey mPrivateKey;
    private DHPublicKey mPublicKey;
    private byte[] mSecretKey;

    public CryptoDH(int len) {
        this(new BigInteger(FIXP, 16), new BigInteger(FIXG), len);
    }

    public CryptoDH(BigInteger paramP, BigInteger paramG, int len) {
        this.mP = paramP;
        this.mG = paramG;
        this.mLength = len;
        generateKeys();
    }

    public BigInteger getP() {
        return this.mP;
    }

    public BigInteger getG() {
        return this.mG;
    }

    public DHPrivateKey getPriveteKey() {
        return this.mPrivateKey;
    }

    public DHPublicKey getPublicKey() {
        return this.mPublicKey;
    }

    public byte[] getSecretKey() {
        return this.mSecretKey;
    }

    public void generateSecretKey(BigInteger paramBigInteger) {
        try {
            PublicKey localPublicKey=null;
            //   PublicKey localPublicKey = KeyFactory.getInstance("DH").generatePublic(new DHPublicKeySpec(paramBigInteger, this.mP, this.mG));
            KeyAgreement localKeyAgreement = KeyAgreement.getInstance("DH");
            localKeyAgreement.init(this.mPrivateKey);
//            localKeyAgreement.doPhase(localPublicKey, true);
            this.mSecretKey = localKeyAgreement.generateSecret();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e2) {
            e2.printStackTrace();
        }
    }

    private BigInteger[] generatePG() {
        try {
            AlgorithmParameterGenerator localAlgorithmParameterGenerator = AlgorithmParameterGenerator.getInstance("DH");
            localAlgorithmParameterGenerator.init(this.mLength, new SecureRandom());
            DHParameterSpec localDhParameterSpec = (DHParameterSpec) localAlgorithmParameterGenerator.generateParameters().getParameterSpec(DHParameterSpec.class);
            return new BigInteger[]{localDhParameterSpec.getP(), localDhParameterSpec.getG()};
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidParameterSpecException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private boolean generateKeys() {
        try {
            KeyPairGenerator localKeyPairGenerator = KeyPairGenerator.getInstance("DH");
            localKeyPairGenerator.initialize(new DHParameterSpec(this.mP, this.mG, this.mLength));
            KeyPair localKeyPair = localKeyPairGenerator.generateKeyPair();
            this.mPrivateKey = (DHPrivateKey) localKeyPair.getPrivate();
            this.mPublicKey = (DHPublicKey) localKeyPair.getPublic();
            return true;
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
            return false;
        }
    }
}
