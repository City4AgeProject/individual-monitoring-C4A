package eu.city4age.dashboard.api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 *
 * @author GGiotis
 */
public enum JwtIssuer {

    INSTANCE;

    static String issuer, pkAlgorithm, privateKeyStr, publicKeyStr;

    static Algorithm algorithmRS;

    static RSAPublicKey publicKey;

    static RSAPrivateKey privateKey;

    static {
        issuer = "City4Age";

        pkAlgorithm = "RSA";

        privateKeyStr = "MIIJRAIBADANBgkqhkiG9w0BAQEFAASCCS4wggkqAgEAAoICAQCwHcRMZvRmARkA"
                + "eYIMONJJ7Apd6qi8MwvgAKk80mJhe0yFf8BqS5arl56z2Z8+H6Wj4ecROgGYnRdj"
                + "zMTyO2AJ83Cxg/6plh5pTSySaakXUHj8jCrM2Wun3l8yWiU0XUAZiiTe5Y8FSre1"
                + "cO69fshNxLse32yABpILFZ1nsPSgLmLMXiAExC4QLNOv/IIDU7blSiiK/lEixxEy"
                + "IaNXrigsCaTj/ecmbwRjjEvCG71psWMNDApJyJMEBfFpnLrLP2PhWC3vyS+HmxH/"
                + "aG9K9a73e5lUy+nc/qk8LjQvBw0fAN15PC5vJLZq/GzlZvGdinhYtjPMwE/yawqA"
                + "SQ+ujn0vLjbXXIMgtHkCoP9nLyQbCO5eVPZnRpxzGStMukQDx0en3FozOcmN61IK"
                + "MBARoJUAeszffRh6Mqg8XiTBbg/9zkbjE7q5fXLVX51+rK5f9Zz56MiZotLCWkcH"
                + "Z2pCG8ARSagq+qehId0mG937w4KsJo0E+vzPJun2AACLFPn506Mvldhy2tS97Syi"
                + "v6p6an01JHzKuimiwG/tYmY2yvYuUNGi/Z3lDIfdWfpSrHOy5syj8N4v5a7m0xYj"
                + "+HiAGw5A4tiE4n6HW9D4O5cP+VCD2hPB5/GlJQg9F9sFeh7vFe4/vYvtIC0clWWb"
                + "+94Awmtni5+S/Oie9FRTjSDdQS8DAQIDAQABAoICAQCOjrnLwnbIbtEHIlQTgpcu"
                + "ASPvG7iUBpnxK3fSZNX/5GdvlJZzNFZ09VkgaeLNzZ4txvlcZ2DNYTOCtEkjbs5x"
                + "n520A41LRtm9ERwoM+EMnzYf73HhuYi8LHNQdcmx1l3BjqIDzzvy46VK+Gw39X07"
                + "/igdeGsCQs9Kq6drPMD3PDT7CqHOWeKTc0WzKakv+j6M4Xdp8yxIA2028CWhHrcj"
                + "1najmm3WHX0m+3SDXiL3gAAWmo1lZjcixZDBlX1acihuSwt8EpUnpm1uGeIkgjSr"
                + "3V+jVSAro/PnY9sXyhRj6TZdIUrQ6p3Cgiu/kZG4nVKHNI0/itfr+ZtZdkp8WJiZ"
                + "FXEz5+SspyeTTczyBJpSYWb2AHm0kt52Zn52WENaM16Cj3iYJtFkcuF+xdjFgWui"
                + "APcwMR1QL8LsbyMpX7MgRTdlqhj2vI8tYF8c6SfHhCK0i/MNA/xeRaqqfYFoZVcr"
                + "l+di5ez4JNunORO7lnJddYkc2MZcTvk9GmRb84VQTzk/fwqxRgEjv/X5JY95UxVZ"
                + "o3CYsMPC4EzeH4q8VHsOf41XDBmesR+OjV+UNCOtERs6kcIrL7TdzBRRLST2X6LF"
                + "zo2H5NSmEWzcmKLH3Yq49VkHXB0LXn/m60CfLalKI4dD1gVeowoeCCjrMlpuAXMe"
                + "YELXc0RUbPhUeAv5Rpo8AQKCAQEA2PbsmBjRhuBFE2oMasld9+qgpks+rh7kM5N/"
                + "A7GpXor0dohr7r2xm7NldBHfSVsJ9m4OReRBZ4KNZi0EVYwhD0oUjr9tfj4sCoeN"
                + "5fWhtpFosDGaBEnVSYZTt46mGYGglrkuEieZAawK9rBT5wBA1EQNssHlSpAnc6+G"
                + "ZKC3KLbXcohwsToQNEI3D0wBDYOfd8ts7FCnoOzZGr4kScp+xXHR/dB1YhzMbX5a"
                + "t/mRgQOfq4BpwlIWbbe6xnSiCT5Rwp2SyK7+GRUd2D5/GHtczMdnpwgyZyMM6X34"
                + "BcG0a5JYEeyzRNcMmOc1vRgc9T2Sb1dcE6YshARfxpy5fMG34QKCAQEAz81uCUVR"
                + "RLrmSQNx2v/8akUFfKzrc3A39O4HysiGR/oFiVm8SxeK2fbDUDZo6zToizAuP9bw"
                + "aEM/8CC8ThVENAIYpl9klNoXrzfg5fhAVXRtUb5UD0bHTwxg0ZhR5/cQLx5wtHDZ"
                + "g89u1FfByMg4ECLSUU+nco3CnuXrVwN28m7Xy9wbgP3GrjVw3oO0gmocwKVl4qxZ"
                + "evEFjzkF9OY9INoaLePI/vf1qKODO04Kp63ZcpC5ac2uOG5amGbiUXLKZBGWSiR+"
                + "W+e2yUmh+Wg/eYAyXkUJxlbHayy4h7BfYWbWmvF6M3g5Mller9KB5JQQ+z+6OTfU"
                + "+A5uZTaAefQvIQKCAQEApggP9jTH4tctBTZaWj3O0zGRzrA2PzkUmQN+Z21Flzvu"
                + "5+Zt8jzn9abLNI+Yt6IbdaGy+nk6M+QMIOzCLFUX14NxR+vl9n3NZiFlzVyza+rK"
                + "mkra+f0mYyWQWx+mrE4ufiknwGzdAhKOMUmua1AeTFHnyavns6+tATKJPehfQaDq"
                + "LpOK6ZTtYZBqYiJfi9/14M6jhBQUs2mMmpJ3YqC7co0vjXRA39v59LCE69ToLDqB"
                + "rdSPGk2HLF81ZzlJ5pjyKGQ9N6XyfW9Tb3AkArAky/CsuX3kVFjWQLhNXNpnHwrW"
                + "qqDgbwXCdiA5V4NkZafAr8PCiqbX2g8e2/aDW51DAQKCAQEAsC4vuNR4TplXXVf6"
                + "U/7s9vyF5mqFRV5DUvnsyVSx5JxpzUWzisbvBlVcyskWszQsZLX4wE9vjDZYIzLd"
                + "9CW7qYyNcaE3Gay+n+P0XYXYpZsMhF2lzz3nBXItNAa8irrBkfFxBMsY39Qi1w2A"
                + "UsbOIYAkaKPsCAeAlk/A9AkP9ANeGo0HFJoUtObCRyXLCTcGIZ8ZftE5HC7007U5"
                + "A6Wg9hV0VKvnYaTBNrVLMiC8j+WpkqR5Tub/awDE9qlkAEIerjDrBvlh2HHgW1B+"
                + "b4KfLlguXBaUbwBsGsXTvdcfRul10fAh9zla5QE4ckE7t5yBuUMSlCht+9HgbxeC"
                + "XeNVgQKCAQAcjQfbvpi0mwkSH/wJqRvKADOJ4LuHahTTsom6V/r7JrVOGZ99g5X4"
                + "yyspeGtxHsqnq+8pGj1/SVkC5EsqDGYN9YpraqU7poFA+8E4BXWLI52K2myGeCX4"
                + "hkiGe7/Im8HBy5eKvQXtByUTgH7hGfzqHrylRzqOFIxYrqBCv1BV+rYU65Yv063O"
                + "Wm5AJN7pxDznZkzyML4zMr3XaDkrW4dips2CXPDlLYUIaDviPDlMpnw0ap/kT7CQ"
                + "2VVjXuYUzihCc0zIO3lFsO896bwzxvFWLKRUb9fgMeqVr3YD3iwgsXgpMDoNl7ME"
                + "1JoqnmWC4kk0C6k2cfJ2F0ET/skZzkOs";

        publicKeyStr = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAsB3ETGb0ZgEZAHmCDDjS"
                + "SewKXeqovDML4ACpPNJiYXtMhX/AakuWq5ees9mfPh+lo+HnEToBmJ0XY8zE8jtg"
                + "CfNwsYP+qZYeaU0skmmpF1B4/IwqzNlrp95fMlolNF1AGYok3uWPBUq3tXDuvX7I"
                + "TcS7Ht9sgAaSCxWdZ7D0oC5izF4gBMQuECzTr/yCA1O25Uooiv5RIscRMiGjV64o"
                + "LAmk4/3nJm8EY4xLwhu9abFjDQwKSciTBAXxaZy6yz9j4Vgt78kvh5sR/2hvSvWu"
                + "93uZVMvp3P6pPC40LwcNHwDdeTwubyS2avxs5WbxnYp4WLYzzMBP8msKgEkPro59"
                + "Ly4211yDILR5AqD/Zy8kGwjuXlT2Z0accxkrTLpEA8dHp9xaMznJjetSCjAQEaCV"
                + "AHrM330YejKoPF4kwW4P/c5G4xO6uX1y1V+dfqyuX/Wc+ejImaLSwlpHB2dqQhvA"
                + "EUmoKvqnoSHdJhvd+8OCrCaNBPr8zybp9gAAixT5+dOjL5XYctrUve0sor+qemp9"
                + "NSR8yroposBv7WJmNsr2LlDRov2d5QyH3Vn6UqxzsubMo/DeL+Wu5tMWI/h4gBsO"
                + "QOLYhOJ+h1vQ+DuXD/lQg9oTwefxpSUIPRfbBXoe7xXuP72L7SAtHJVlm/veAMJr"
                + "Z4ufkvzonvRUU40g3UEvAwECAwEAAQ==";

        try {
            publicKey = (RSAPublicKey) getPublicKey(publicKeyStr);
            privateKey = (RSAPrivateKey) getPrivateKey(privateKeyStr);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
        }

        algorithmRS = Algorithm.RSA256(publicKey, privateKey);
    }

    /**
     *
     * @param username
     * @param roleId
     * @param pilot
     * @return
     */
    public String createAndSign(String username, Short roleId, String pilot) {

        String token = null;
        try {
            token = JWT.create()
                    .withIssuer(issuer)
                    .withClaim("usr", username)
                    .withClaim("rol", (int) roleId)
                    .withClaim("plt", pilot)
                    .sign(algorithmRS);
        } catch (JWTCreationException e) {
        }

        return token;
    }

    /**
     *
     * @param token
     * @return
     * @throws JWTVerificationException
     */
    public DecodedJWT verify(String token) throws JWTVerificationException {

        Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
        return verifier.verify(token);
    }

    /**
     *
     * @param keyStr
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static PrivateKey getPrivateKey(String keyStr) throws InvalidKeySpecException, NoSuchAlgorithmException {

        PKCS8EncodedKeySpec pubKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(keyStr));
        KeyFactory keyFactory = KeyFactory.getInstance(pkAlgorithm);
        PrivateKey prvKey = keyFactory.generatePrivate(pubKeySpec);

        return prvKey;

    }

    /**
     *
     * @param keyStr
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PublicKey getPublicKey(String keyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {

        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(keyStr));
        KeyFactory keyFactory = KeyFactory.getInstance(pkAlgorithm);
        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

        return pubKey;
    }

//    public static void main(String... args) {
//
//        boolean isVerified = true;
//        String token;
//
//        // Create and sign JWT
//        token = createAndSign("", (short) 1, "");
//        System.out.println("JWT token: " + token);
//
//        // Verify JWT signature
//        try {
//            verify(token);
//        } catch (JWTVerificationException e) {
//            isVerified = false;
//        }
//        System.out.println(isVerified ? "Verified" : "Not verified");
//    }
}
