package org.example.sm2;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.example.utils.StringUtils;
import org.springframework.util.Base64Utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;

public class Sm2Util {

    /**
     * 获取SM2加密工具对象
     *
     * @param privateKey 加密私钥
     * @param publicKey  加密公钥
     * @return 处理结果
     */
    private static SM2 getSM2(String privateKey, String publicKey) {
        ECPrivateKeyParameters ecPrivateKeyParameters = null;
        ECPublicKeyParameters ecPublicKeyParameters = null;
        if (StringUtils.isNotBlank(privateKey)) {
            ecPrivateKeyParameters = BCUtil.toSm2Params(privateKey);
        }
        if (StringUtils.isNotBlank(publicKey)) {
            if (publicKey.length() == 130) {
                //这里需要去掉开始第一个字节 第一个字节表示标记
                publicKey = publicKey.substring(2);
            }
            String xhex = publicKey.substring(0, 64);
            String yhex = publicKey.substring(64, 128);
            ecPublicKeyParameters = BCUtil.toSm2Params(xhex, yhex);
        }
        //创建sm2 对象
        SM2 sm2 = new SM2(ecPrivateKeyParameters, ecPublicKeyParameters);
        sm2.usePlainEncoding();
        sm2.setMode(SM2Engine.Mode.C1C3C2);
        return sm2;
    }
    /**
     * SM2加密
     *
     * @return 处理结果
     */
    public static String encrypt2Data(String data, String publicKey) {
        SM2 sm2 = getSM2(null, publicKey);
        return sm2.encryptBcd(data, KeyType.PublicKey);
    }

    public static void main(String[] args) {
        /* 生成对钥
        SM2 sm2 = SmUtil.sm2();
        String publicKey = HexUtil.encodeHexStr(((BCECPublicKey) sm2.getPublicKey()).getQ().getEncoded(false));
        String privateKey = HexUtil.encodeHexStr(BCUtil.encodeECPrivateKey(sm2.getPrivateKey()));
        System.out.println("==========公钥==========");
        System.out.println(publicKey);
        System.out.println("==========私钥==========");
        System.out.println(privateKey);*/
        //公钥：042767b689f2955b04408b35e7c2e45b4f9f9158f64eccbe8de3d80c95338056e9bb6d1a424b20a70d2b5917033b08168ab9362dd3cde3cd00f97cb5f51f3a4d3e
        //私钥：00b39b3c7b6ff9860ac212912d808f00b186d06332043ba2efb5248a967efc2f8e
        System.out.println("==========密文==========");
        String ciphertext = encrypt2Data("{\n" +
                "    \"mobile\":\"12345678901\",\n" +
                "    \"name\":\"里斯\",\n" +
                "    \"passwd\":\"chaihcb\",\n" +
                "    \"sex\":\"0\",\n" +
                "    \"birthday\":\"1998-01-14\",\n" +
                "    \"updatedTime\":\"2023\"\n" +
                "}", "042767b689f2955b04408b35e7c2e45b4f9f9158f64eccbe8de3d80c95338056e9bb6d1a424b20a70d2b5917033b08168ab9362dd3cde3cd00f97cb5f51f3a4d3e");
        System.out.println(ciphertext);
        //解密
        System.out.println("==========解密==========");
        System.out.println(SmUtil.sm2("00b39b3c7b6ff9860ac212912d808f00b186d06332043ba2efb5248a967efc2f8e", null).decryptStr(ciphertext, KeyType.PrivateKey));
    }
}
