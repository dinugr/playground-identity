package com.example.identity.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordFragment {
    private String param;
    private String salt;
    private String digest;

    public static PasswordFragment parseArgon2id(String encodedString) {
        // so, what is going on?
        // here is example hashed data :
        //   $argon2id$v=19$m=4096,t=3,p=1$t4cTBFWxG13H3esijD7bbw$S04XhoOk1ZRokGhKhLUmx7gzCVptPNsGatJawfLNxpY
        //                PARAM           ^        SALT          ^                HASH
        // we want to split string above and store separately.

        String[] s = encodedString.split("\\$");

        var fragment = new PasswordFragment();
        fragment.setParam(String.join("$", Arrays.copyOfRange(s, 1, 4)));
        fragment.setSalt(s[4]);
        fragment.setDigest(s[5]);

        return fragment;
    }

    @Override
    public String toString() {
        return "$" + param + "$" + salt + "$" + digest;
    }
}
