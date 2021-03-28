import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Desc
 *
 * @author huqingchao &lt;huqingchao@360.cn&gt;
 * @date 2021/3/28
 */
public class Tools {
    private static final String[] KEY_LIST = new String[]{
            "kPH+bIxk5D2deZiIxcaaaA==", "4AvVhmFLUs0KTA3Kprsdag==", "Z3VucwAAAAAAAAAAAAAAAA==", "fCq+/xW488hMTCD+cmJ3aQ==", "0AvVhmFLUs0KTA3Kprsdag==", "1AvVhdsgUs0FSA3SDFAdag==", "1QWLxg+NYmxraMoxAXu/Iw==", "25BsmdYwjnfcWmnhAciDDg==", "2AvVhdsgUs0FSA3SDFAdag==", "3AvVhmFLUs0KTA3Kprsdag==",
            "3JvYhmBLUs0ETA5Kprsdag==", "r0e3c16IdVkouZgk1TKVMg==", "5aaC5qKm5oqA5pyvAAAAAA==", "5AvVhmFLUs0KTA3Kprsdag==", "6AvVhmFLUs0KTA3Kprsdag==", "6NfXkC7YVCV5DASIrEm1Rg==", "6ZmI6I2j5Y+R5aSn5ZOlAA==", "cmVtZW1iZXJNZQAAAAAAAA==", "7AvVhmFLUs0KTA3Kprsdag==", "8AvVhmFLUs0KTA3Kprsdag==",
            "8BvVhmFLUs0KTA3Kprsdag==", "9AvVhmFLUs0KTA3Kprsdag==", "OUHYQzxQ/W9e/UjiAGu6rg==", "a3dvbmcAAAAAAAAAAAAAAA==", "aU1pcmFjbGVpTWlyYWNsZQ==", "bWljcm9zAAAAAAAAAAAAAA==", "bWluZS1hc3NldC1rZXk6QQ==", "bXRvbnMAAAAAAAAAAAAAAA==", "ZUdsaGJuSmxibVI2ZHc9PQ==", "wGiHplamyXlVB11UXWol8g==",
            "U3ByaW5nQmxhZGUAAAAAAA==", "MTIzNDU2Nzg5MGFiY2RlZg==", "L7RioUULEFhRyxM7a2R/Yg==", "a2VlcE9uR29pbmdBbmRGaQ==", "WcfHGU25gNnTxTlmJMeSpw==", "OY//C4rhfwNxCQAQCrQQ1Q==", "5J7bIJIV0LQSN3c9LPitBQ==", "f/SY5TIve5WWzT4aQlABJA==", "bya2HkYo57u6fWh5theAWw==", "WuB+y2gcHRnY2Lg9+Aqmqg==",
            "kPv59vyqzj00x11LXJZTjJ2UHW48jzHN", "3qDVdLawoIr1xFd6ietnwg==", "ZWvohmPdUsAWT3=KpPqda", "YI1+nBV//m7ELrIyDHm6DQ==", "6Zm+6I2j5Y+R5aS+5ZOlAA==", "2A2V+RFLUs+eTA3Kpr+dag==", "6ZmI6I2j3Y+R1aSn5BOlAA==", "SkZpbmFsQmxhZGUAAAAAAA==", "2cVtiE83c4lIrELJwKGJUw==", "fsHspZw/92PrS3XrPW+vxw==",
            "XTx6CKLo/SdSgub+OPHSrw==", "sHdIjUN6tzhl8xZMG3ULCQ==", "O4pdf+7e+mZe8NyxMTPJmQ==", "HWrBltGvEZc14h9VpMvZWw==", "rPNqM6uKFCyaL10AK51UkQ==", "Y1JxNSPXVwMkyvES/kJGeQ==", "lT2UvDUmQwewm6mMoiw4Ig==", "MPdCMZ9urzEA50JDlDYYDg==", "xVmmoltfpb8tTceuT5R7Bw==", "c+3hFGPjbgzGdrC+MHgoRQ==",
            "ClLk69oNcA3m+s0jIMIkpg==", "Bf7MfkNR0axGGptozrebag==", "1tC/xrDYs8ey+sa3emtiYw==", "ZmFsYWRvLnh5ei5zaGlybw==", "cGhyYWNrY3RmREUhfiMkZA==", "IduElDUpDDXE677ZkhhKnQ==", "yeAAo1E8BOeAYfBlm4NG9Q==", "cGljYXMAAAAAAAAAAAAAAA==", "2itfW92XazYRi5ltW0M2yA==", "XgGkgqGqYrix9lI6vxcrRw==",
            "ertVhmFLUs0KTA3Kprsdag==", "5AvVhmFLUS0ATA4Kprsdag==", "s0KTA3mFLUprK4AvVhsdag==", "hBlzKg78ajaZuTE0VLzDDg==", "9FvVhtFLUs0KnA3Kprsdyg==", "d2ViUmVtZW1iZXJNZUtleQ==", "yNeUgSzL/CfiWw1GALg6Ag==", "NGk/3cQ6F5/UNPRh8LpMIg==", "4BvVhmFLUs0KTA3Kprsdag==", "MzVeSkYyWTI2OFVLZjRzZg==",
            "CrownKey==a12d/dakdad", "empodDEyMwAAAAAAAAAAAA==", "A7UzJgh1+EWj5oBFi+mSgw==", "YTM0NZomIzI2OTsmIzM0NTueYQ==", "c2hpcm9fYmF0aXMzMgAAAA==", "i45FVt72K2kLgvFrJtoZRw==", "U3BAbW5nQmxhZGUAAAAAAA==", "ZnJlc2h6Y24xMjM0NTY3OA==", "Jt3C93kMR9D5e8QzwfsiMw==", "MTIzNDU2NzgxMjM0NTY3OA==",
            "vXP33AonIp9bFwGl7aT7rA==", "V2hhdCBUaGUgSGVsbAAAAA==", "Z3h6eWd4enklMjElMjElMjE=", "Q01TX0JGTFlLRVlfMjAxOQ==", "ZAvph3dsQs0FSL3SDFAdag==", "Is9zJ3pzNh2cgTHB4ua3+Q==", "NsZXjXVklWPZwOfkvk6kUA==", "GAevYnznvgNCURavBhCr1w==", "66v1O8keKNV3TTcGPK1wzg==", "SDKOLKn2J1j/2BHjeZwAoQ=="};

    private static final Pattern REMEMBER_ME_PATTERN = Pattern.compile(".*rememberMe=([^;~]+).*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    public static void main(String[] args) {
        if (args == null || args.length == 0 || args[0].equals("-h")) {
            printHelp();
        }

        if (args[0].equals("-s")) {
            System.out.println("===================内置密钥=====================");
            for (String s : KEY_LIST) {
                System.out.println(s);
            }
            System.out.println("===================内置密钥=====================");

            return;
        }

        if (args.length < 2) {
            printHelp();
            System.exit(0);
        }

        String inputFilePath = null;
        String keyFilePath = null;
        for (int i = 0; i < args.length; i = i + 2) {
            String argName = args[i];
            String argVal = args[i + 1];

            if (argName.equals("-f")) {
                inputFilePath = argVal;
            } else if (argName.equals("-k")) {
                keyFilePath = argVal;
            }
        }

        if (inputFilePath == null) {
            System.out.println("请指定需要解密的文件");
            System.exit(0);
        }

        File file = new File(inputFilePath);
        String outputPath = null;
        if (!file.exists()) {
            System.out.println("输入的解密文件路径不存在");
            System.exit(0);
        } else {
            outputPath = file.getParent() + File.separator + file.getName().substring(0, file.getName().lastIndexOf("."));
            File dir = new File(outputPath);
            dir.mkdir();
        }

        List<String> keyList = null;
        if (keyFilePath != null && keyFilePath.trim().length() > 0) {
            File keyFile = new File(keyFilePath);
            if (!keyFile.exists()) {
                System.out.println("输入的密钥文件路径不存在");
                System.exit(0);
            }

            try {
                keyList = Files.readAllLines(Paths.get(keyFilePath));
            } catch (IOException e) {

            }
            if (keyList == null || keyList.size() == 0) {
                System.out.println("输入的密钥文件中无内容");
                System.exit(0);
            }
        } else {
            keyList = Arrays.asList(KEY_LIST);
        }

        System.out.println("\n\r开始解密文件：" + inputFilePath + ", 解密密钥: " + (keyFilePath == null ? "默认密码" : keyFilePath));

        try {
            List<String> lineList = Files.readAllLines(Paths.get(inputFilePath));
            for (int i = 0; i < lineList.size(); i++) {
                String cookie = lineList.get(i).replace("\\r\\n", "~~");
                Matcher m = REMEMBER_ME_PATTERN.matcher(cookie);
                if (!m.find()) {
                    continue;
                }

                String payload = m.group(1);
                byte[] payloads = payload.getBytes();

                AesCipherService aes = new AesCipherService();
                for (String key : keyList) {
                    ByteSource ciphertext;
                    byte[] keyBytes = Base64.decode(CodecSupport.toBytes(key));
                    try {
                        ciphertext = aes.decrypt(Base64.decode(payloads), keyBytes);
                        FileOutputStream fos = new FileOutputStream(new File(outputPath + File.separator + (i + 1) + ".bin"));
                        fos.write(ciphertext.getBytes());
                        fos.close();

                        break;
                    } catch (Exception e) {
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("解密完成，结果存储路径：" + outputPath);

    }

    private static void printHelp() {
        System.out.println("==========================Shiro解密工具使用帮助=======================================");
        System.out.println(" 工具支持文件和文本两种解密方式：");
        System.out.println(" \t文件输入会自动提取每行文本中rememberMe的内容, 提取正则：\".*rememberMe=([^;~]+).*\"");
        System.out.println(" \t解密结果会输出到输入文件所在目录中, 文件名称为行数");
        System.out.println(" 使用方法如下：");
        System.out.println(" \t命令行：java -jar util-shiro.jar -f 文件路径");
        System.out.println(" 示例：");
        System.out.println(" \t命令行：java -jar util-shiro.jar d:/text.log");

        System.out.println();
        System.out.println(" *****自定义解密密钥*****");
        System.out.println(" \t命令行：java -jar util-shiro.jar 文件路径 -k 密钥文件路径");
        System.out.println(" \t命令行：java -jar util-shiro.jar d:/text.log -k d:/pwd.txt");

        System.out.println();
        System.out.println(" *****查看内置密钥列表*****");
        System.out.println(" \t命令行：java -jar util-shiro.jar -s");


        System.out.println("==========================Shiro解密工具使用帮助=======================================");
    }
}
