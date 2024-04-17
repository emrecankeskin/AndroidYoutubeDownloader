package com.emre.youtubedownloader.Parsers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Decrypter {
    public String baseJs = null;
    public String youtubeUrlSource;
    public String[] steps = null;
    public final Map<String, Integer> functionType = new HashMap<>();


    //regex for encode functions
    private Pattern pattern = Pattern.compile("function\\(\\w+\\)\\{[\\w=.(\")]+;([\\w=.,(\");]*);return \\w+\\.join[(\")]*\\}\\;");
    public final Pattern patDecryptionJsFileWithoutSlash = Pattern.compile("/s/player/([^\"]+?).js");
    public Matcher matcher;

    public Decrypter(String youtubeUrlSource){
        this.youtubeUrlSource = youtubeUrlSource;
        //findFunctionTypes(this.baseJs);
    }

    public void findFunctionTypes(String baseJs){

        baseJs = baseJs.replace("$","");
        matcher = pattern.matcher(baseJs);

        if (matcher.find()) {
            steps = matcher.group(1).split(";");
        }

        String fnMapName = steps[0].split("\\.")[0];
        pattern = Pattern.compile("\\w+ " + fnMapName + "=\\{(.*\\n*.*\\n*.*\\})\\}\\;");
        matcher = pattern.matcher(baseJs);
        String functions = null;

        if (matcher.find()) {
            functions = matcher.group(1).replace("\n", "");
        }

        /*
         * encoding functions finding
         *
         * */
        pattern = Pattern.compile("(\\w+):function\\(\\w*,*\\w*\\)\\{([\\w,.() =;%\\[\\]]*)\\}");
        matcher = pattern.matcher(functions);
        while (matcher.find()) {
            String fct = matcher.group(2);
            int type;
            if (fct.contains("splice"))
                type = DecipherFunctionType.SLICE;
            else if (fct.contains("reverse"))
                type = DecipherFunctionType.REVERSE;
            else if (fct.contains("var") && fct.contains("="))
                type = DecipherFunctionType.SWAP;
            else {
                type = 4;
            }
            functionType.put(matcher.group(1), type);
        }
    }



    /**
     * Decoding signatureCipher's signature with some algorithm
     * to do -> learn how this decoding algorithm work
     *
     * */
    public String decode(String signature) {
        //signature
        for (String i : steps) {

            String funName = i.split("\\.")[1];
            funName = funName.substring(0, funName.indexOf("("));

            String valueString = i.split(",")[1];
            int value = Integer.parseInt(valueString.substring(0, valueString.indexOf(")")));
            int type = functionType.get(funName);

            if (type == DecipherFunctionType.REVERSE) {

                StringBuilder builder = new StringBuilder(signature);
                signature = builder.reverse().toString();

            } else if (type == DecipherFunctionType.SLICE) {

                signature = signature.substring(value);

            } else if (type == DecipherFunctionType.SWAP) {

                char c = signature.charAt(0);
                value = value % signature.length();
                StringBuilder builder = new StringBuilder(signature);
                builder.setCharAt(0, signature.charAt(value));
                builder.setCharAt(value, c);
                signature = builder.toString();

            }
        }
        return signature;
    }

    public String unicodeDecode(String input) {
        String[] arr = input.split(Pattern.quote("\\u"));

        StringBuilder text = new StringBuilder();
        text.append(arr[0]);

        for (int i = 1; i < arr.length; i++) {
            int hexVal = Integer.parseInt(arr[i].substring(0, 4), 16);
            text.append((char) hexVal).append(arr[i].substring(4));
        }
        return text.toString();
    }


    public String urlDecode(String input) {
        try {
            return URLDecoder.decode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
    public String getBaseJs(){
        matcher = patDecryptionJsFileWithoutSlash.matcher(youtubeUrlSource);
        if(matcher.find()){
            baseJs = "https://www.youtube.com"+matcher.group(0);
        }
        return baseJs;
    }


}
