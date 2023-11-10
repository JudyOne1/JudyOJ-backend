package com.JudyOJ.common;

import cn.hutool.core.io.FileUtil;
import com.JudyOJ.utils.CodeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author Judy
 * @create 2023-10-11-16:43
 */
@SpringBootTest
class FonctionTest {


    @Test
    void executeCode() {
        String code =
                "class Solution {\n" +
                        "public static int solute(int[] gifts, int k) {\n" +
                        "        System.out.println(Arrays.toString(gifts));\n" +
                        "        System.out.println(k);\n" +
                        "        return 10086;\n" +
                        "    }\n" +
                        "}";
        String helpCode =
                "    public static void main(String[] args) {\n" +
                "\n" +
                "        //输入参数：gifts = [5,1,4,null,null,3,6], k = 4\n" +
                "        String input = \"gifts = [5,1,4,3,6], k = 10\";\n" +
                "        //核心代码模式测试 -> 使用args传入字符串参数\n" +
                "\n" +
                "        //获取参数名、参数类型、参数值 gifts 和 k\n" +
                "//        ArrayList<String> params = getParams(input);\n" +
                "\n" +
                "        ArrayList<ParamsInfo> params = getParamsByClass(input);\n" +
                "        //获取目标方法的参数信息\n" +
                "        Parameter[] ParameterInfos = reflectMethod(\"solute\", params);\n" +
                "        if (ParameterInfos == null) {\n" +
                "            throw new RuntimeException(\"出现未知错误\");\n" +
                "        }\n" +
                "        if (params.size() != ParameterInfos.length) {\n" +
                "            throw new RuntimeException(\"系统出现未知错误\");\n" +
                "        }\n" +
                "//-------------------------------------------------------------\n" +
                "//        int[] gifts;\n" +
                "//        int k;\n" +
                "        int i = 0;\n" +
                "//        gifts = JAVAparseStringTo1ArrayNoNull(params.get(i++).getValue());\n" +
                "//        k = Integer.parseInt(params.get(i).getValue());\n" +
                "        System.out.println(solute(JAVAparseStringTo1ArrayNoNull(params.get(i++).getValue()), Integer.parseInt(params.get(i).getValue())));\n" +
                "//--------------------------------------------------------------\n" +
                "    }";
        String finalCode = CodeUtils.dealWithCCMCode(helpCode, code);
        File file = saveCodeToFile(finalCode);
    }

    @Test
    void stitchingCMcode() {
        String helpCode = "\tpublic static int minLight3(String road) {\n" +
                "\t\tchar[] str = road.toCharArray();\n" +
                "\t\tint cur = 0;\n" +
                "\t\tint light = 0;\n" +
                "\t\tfor (char c : str) {\n" +
                "\t\t\tif (c == 'X') {\n" +
                "\t\t\t\tlight += (cur + 2) / 3;\n" +
                "\t\t\t\tcur = 0;\n" +
                "\t\t\t} else {\n" +
                "\t\t\t\tcur++;\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t\tlight += (cur + 2) / 3;\n" +
                "\t\treturn light;\n" +
                "\t}" +
                "\t// for test\n" +
                "\tpublic static String randomString(int len) {\n" +
                "\t\tchar[] res = new char[(int) (Math.random() * len) + 1];\n" +
                "\t\tfor (int i = 0; i < res.length; i++) {\n" +
                "\t\t\tres[i] = Math.random() < 0.5 ? 'X' : '.';\n" +
                "\t\t}\n" +
                "\t\treturn String.valueOf(res);\n" +
                "\t}\n" +
                "\n" +
                "\tpublic static void main(String[] args) {\n" +
                "\t\tint len = 20;\n" +
                "\t\tint testTime = 100000;\n" +
                "\t\tfor (int i = 0; i < testTime; i++) {\n" +
                "\t\t\tString test = randomString(len);\n" +
                "\t\t\tint ans2 = solution(test);\n" +
                "\t\t\tint ans3 = minLight3(test);\n" +
                "\t\t\tif (ans2 != ans3) {\n" +
                "\t\t\t\tSystem.out.println(\"oops!\");\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t\tSystem.out.println(\"finish!\");\n" +
                "\t}";
        String code =
                "class Solution {\n" +
                        "\tpublic static int solution(String road) {\n" +
                        "\t\tchar[] str = road.toCharArray();\n" +
                        "\t\tint i = 0;\n" +
                        "\t\tint light = 0;\n" +
                        "\t\twhile (i < str.length) {\n" +
                        "\t\t\tif (str[i] == 'X') {\n" +
                        "\t\t\t\ti++;\n" +
                        "\t\t\t} else {\n" +
                        "\t\t\t\tlight++;\n" +
                        "\t\t\t\tif (i + 1 == str.length) {\n" +
                        "\t\t\t\t\tbreak;\n" +
                        "\t\t\t\t} else { // 有i位置 i+ 1 X .\n" +
                        "\t\t\t\t\tif (str[i + 1] == 'X') {\n" +
                        "\t\t\t\t\t\ti = i + 2;\n" +
                        "\t\t\t\t\t} else {\n" +
                        "\t\t\t\t\t\ti = i + 3;\n" +
                        "\t\t\t\t\t}\n" +
                        "\t\t\t\t}\n" +
                        "\t\t\t}\n" +
                        "\t\t}\n" +
                        "\t\treturn light;\n" +
                        "\t}" +
                        "}";
//        StringBuilder stringBuilder = new StringBuilder(code);
//        int lastIndexOf = stringBuilder.lastIndexOf("}");
//        stringBuilder.replace(lastIndexOf, lastIndexOf + 1, helpCode);
//        stringBuilder.append("}");
//        System.out.println(stringBuilder);
        String finalCode = CodeUtils.dealWithCMCode(helpCode, code);

        File file = saveCodeToFile(finalCode);

    }


    @Test
    void stitchingCCMcode() {
        String code =
                "class Solution {\n" +
                        "public static int solute(int[] gifts, int k) {\n" +
                        "        System.out.println(Arrays.toString(gifts));\n" +
                        "        System.out.println(k);\n" +
                        "        return 10086;\n" +
                        "    }\n" +
                        "}";
        StringBuilder stringBuilder = new StringBuilder(code);
        //1. import代码的写入
        String importCode =
                "import java.lang.reflect.Method;\n" +
                        "import java.lang.reflect.Parameter;\n" +
                        "import java.util.ArrayList;\n" +
                        "import java.util.Arrays;";
        stringBuilder.insert(0, importCode);
        System.out.println(stringBuilder);
        //2. 辅助代码
        int lastIndexOf = stringBuilder.lastIndexOf("}");
        //main函数
        String helpCode = "    public static void main(String[] args) {\n" +
                "\n" +
                "        //输入参数：gifts = [5,1,4,null,null,3,6], k = 4\n" +
                "        String input = \"gifts = [5,1,4,3,6], k = 10\";\n" +
                "        //核心代码模式测试 -> 使用args传入字符串参数\n" +
                "\n" +
                "        //获取参数名、参数类型、参数值 gifts 和 k\n" +
                "//        ArrayList<String> params = getParams(input);\n" +
                "\n" +
                "        ArrayList<ParamsInfo> params = getParamsByClass(input);\n" +
                "        //获取目标方法的参数信息\n" +
                "        Parameter[] ParameterInfos = reflectMethod(\"solute\", params);\n" +
                "        if (ParameterInfos == null) {\n" +
                "            throw new RuntimeException(\"出现未知错误\");\n" +
                "        }\n" +
                "        if (params.size() != ParameterInfos.length) {\n" +
                "            throw new RuntimeException(\"系统出现未知错误\");\n" +
                "        }\n" +
                "//-------------------------------------------------------------\n" +
                "//        int[] gifts;\n" +
                "//        int k;\n" +
                "        int i = 0;\n" +
                "//        gifts = JAVAparseStringTo1ArrayNoNull(params.get(i++).getValue());\n" +
                "//        k = Integer.parseInt(params.get(i).getValue());\n" +
                "        System.out.println(solute(JAVAparseStringTo1ArrayNoNull(params.get(i++).getValue()), Integer.parseInt(params.get(i).getValue())));\n" +
                "//--------------------------------------------------------------\n" +
                "    }";
        stringBuilder.replace(lastIndexOf, lastIndexOf + 1, helpCode);
        System.out.println(stringBuilder);
        String helper =
                "    static class ParamsInfo {\n" +
                        "        String name;\n" +
                        "        String value;\n" +
                        "        Class className;\n" +
                        "\n" +
                        "        public ParamsInfo(String name, String value, Class className) {\n" +
                        "            this.name = name;\n" +
                        "            this.value = value;\n" +
                        "            this.className = className;\n" +
                        "        }\n" +
                        "\n" +
                        "        public String getName() {\n" +
                        "            return name;\n" +
                        "        }\n" +
                        "\n" +
                        "        public void setName(String name) {\n" +
                        "            this.name = name;\n" +
                        "        }\n" +
                        "\n" +
                        "        public String getValue() {\n" +
                        "            return value;\n" +
                        "        }\n" +
                        "\n" +
                        "        public void setValue(String value) {\n" +
                        "            this.value = value;\n" +
                        "        }\n" +
                        "\n" +
                        "        public Class getClassName() {\n" +
                        "            return className;\n" +
                        "        }\n" +
                        "\n" +
                        "        public void setClassName(Class className) {\n" +
                        "            this.className = className;\n" +
                        "        }\n" +
                        "    }\n" +
                        "\n" +
                        "    public static ArrayList<String> getParams(String input) {\n" +
                        "\n" +
                        "        ArrayList<String> paramsInfos = new ArrayList<>();\n" +
                        "        String[] totalSplit = input.split(\", \");\n" +
                        "\n" +
                        "        if (totalSplit.length > 0) {\n" +
                        "            for (int i = 0; i < totalSplit.length; i++) {\n" +
                        "                String name;\n" +
                        "                String[] subSplit = totalSplit[i].split(\" = \");\n" +
                        "                name = subSplit[0];\n" +
                        "                StringBuilder stringBuilder = new StringBuilder(subSplit[1]);\n" +
                        "                int i1 = stringBuilder.indexOf(\"[\");\n" +
                        "                int i2 = stringBuilder.lastIndexOf(\"]\");\n" +
                        "\n" +
                        "                if (i2 != -1 && i1 != -1) {\n" +
                        "                    String arr = stringBuilder.substring(i1, i2 + 1).toString();\n" +
                        "                    stringBuilder = new StringBuilder(arr);\n" +
                        "                    i1 = stringBuilder.indexOf(\"[\");\n" +
                        "                    i2 = stringBuilder.lastIndexOf(\"]\");\n" +
                        "                    String subArr = stringBuilder.substring(1, arr.length() - 1).toString();\n" +
                        "                    stringBuilder = new StringBuilder(subArr);\n" +
                        "                    i1 = stringBuilder.indexOf(\"[\");\n" +
                        "                    i2 = stringBuilder.lastIndexOf(\"]\");\n" +
                        "                    if (i2 != -1 && i1 != -1) {\n" +
                        "                        int[][] arr2 = JAVAparseStringTo2ArrayNoNull(arr);\n" +
                        "                        paramsInfos.add(Arrays.deepToString(arr2));\n" +
                        "                    } else {\n" +
                        "                        int[] arr1 = JAVAparseStringTo1ArrayNoNull(arr);\n" +
                        "                        paramsInfos.add(Arrays.toString(arr1));\n" +
                        "                    }\n" +
                        "                } else {\n" +
                        "                    String value = subSplit[1];\n" +
                        "                    stringBuilder = new StringBuilder(input);\n" +
                        "                    i1 = stringBuilder.indexOf(\"\\\"\");\n" +
                        "                    i2 = stringBuilder.lastIndexOf(\"\\\"\");\n" +
                        "                    if (i2 == -1 && i1 == -1) {\n" +
                        "                        Integer valueInt = Integer.parseInt(value);\n" +
                        "                        paramsInfos.add(String.valueOf(valueInt));\n" +
                        "                    } else {\n" +
                        "                        paramsInfos.add(value);\n" +
                        "                    }\n" +
                        "                }\n" +
                        "            }\n" +
                        "        } else {\n" +
                        "            String name;\n" +
                        "            String[] nameSplit = input.split(\" = \");\n" +
                        "            name = nameSplit[0];\n" +
                        "            StringBuilder stringBuilder = new StringBuilder(input);\n" +
                        "            int i1 = stringBuilder.indexOf(\"[\");\n" +
                        "            int i2 = stringBuilder.lastIndexOf(\"]\");\n" +
                        "\n" +
                        "            if (i2 != -1 && i1 != -1) {\n" +
                        "                String arr = stringBuilder.substring(i1, i2 + 1).toString();\n" +
                        "                stringBuilder = new StringBuilder(input);\n" +
                        "                i1 = stringBuilder.indexOf(\"[\");\n" +
                        "                i2 = stringBuilder.lastIndexOf(\"]\");\n" +
                        "                if (i2 != -1 && i1 != -1) {\n" +
                        "                    int[][] arr2 = JAVAparseStringTo2ArrayNoNull(arr);\n" +
                        "                    paramsInfos.add(Arrays.deepToString(arr2));\n" +
                        "                } else {\n" +
                        "                    int[] arr1 = JAVAparseStringTo1ArrayNoNull(arr);\n" +
                        "                    paramsInfos.add(Arrays.toString(arr1));\n" +
                        "                }\n" +
                        "            } else {\n" +
                        "                String value = nameSplit[1];\n" +
                        "                stringBuilder = new StringBuilder(input);\n" +
                        "                i1 = stringBuilder.indexOf(\"\\\"\");\n" +
                        "                i2 = stringBuilder.lastIndexOf(\"\\\"\");\n" +
                        "                if (i2 == -1 && i1 == -1) {\n" +
                        "                    Integer valueInt = Integer.parseInt(value);\n" +
                        "                    paramsInfos.add(String.valueOf(valueInt));\n" +
                        "                } else {\n" +
                        "                    paramsInfos.add(value);\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }\n" +
                        "        return paramsInfos;\n" +
                        "    }\n" +
                        "\n" +
                        "    public static Parameter[] reflectMethod(String methodName, ArrayList<ParamsInfo> params) {\n" +
                        "        try {\n" +
                        "            Class<?> targetClass = Solution.class;\n" +
                        "\n" +
                        "            Class<?>[] parameterTypes = new Class<?>[params.size()];\n" +
                        "            for (int i = 0; i < params.size(); i++) {\n" +
                        "                if (params.get(i).getClassName() == Integer[].class) {\n" +
                        "                    parameterTypes[i] = int[].class;\n" +
                        "                } else if (params.get(i).getClassName() == Integer[][].class) {\n" +
                        "                    parameterTypes[i] = int[][].class;\n" +
                        "                } else if (params.get(i).getClassName() == Integer.class) {\n" +
                        "                    parameterTypes[i] = int.class;\n" +
                        "                } else {\n" +
                        "                    parameterTypes[i] = params.get(i).getClassName();\n" +
                        "                }\n" +
                        "            }\n" +
                        "\n" +
                        "            Method targetMethod = targetClass.getDeclaredMethod(methodName, parameterTypes);\n" +
                        "\n" +
                        "            Parameter[] parameters = targetMethod.getParameters();\n" +
                        "\n" +
                        "            return parameters;\n" +
                        "        } catch (NoSuchMethodException e) {\n" +
                        "            e.printStackTrace();\n" +
                        "        }\n" +
                        "        return null;\n" +
                        "    }\n" +
                        "\n" +
                        "    public static int[] JAVAparseStringTo1ArrayNoNull(String json) {\n" +
                        "        String[] arrStr = json.replace(\"[\", \"\").replace(\"]\", \"\").split(\",\");\n" +
                        "\n" +
                        "        int[] arrInt = new int[arrStr.length];\n" +
                        "\n" +
                        "        for (int i = 0; i < arrStr.length; i++) {\n" +
                        "            arrInt[i] = Integer.parseInt(arrStr[i].trim());\n" +
                        "        }\n" +
                        "\n" +
                        "        return arrInt;\n" +
                        "    }\n" +
                        "\n" +
                        "    public static int[][] JAVAparseStringTo2ArrayNoNull(String json) {\n" +
                        "        String[] arrStr1 = json.replace(\"[[\", \"\").replace(\"]]\", \"\").split(\"\\\\],\\\\[\");\n" +
                        "\n" +
                        "        int[][] arrInt = new int[arrStr1.length][];\n" +
                        "\n" +
                        "        for (int i = 0; i < arrStr1.length; i++) {\n" +
                        "            String[] arrStr2 = arrStr1[i].split(\",\");\n" +
                        "            arrInt[i] = new int[arrStr2.length];\n" +
                        "            for (int j = 0; j < arrStr2.length; j++) {\n" +
                        "                arrInt[i][j] = Integer.parseInt(arrStr2[j].trim());\n" +
                        "            }\n" +
                        "        }\n" +
                        "\n" +
                        "        return arrInt;\n" +
                        "    }\n" +
                        "\n" +
                        "\n" +
                        "    public static ArrayList<ParamsInfo> getParamsByClass(String input) {\n" +
                        "\n" +
                        "        ArrayList<ParamsInfo> paramsInfos = new ArrayList<>();\n" +
                        "        String[] totalSplit = input.split(\", \");\n" +
                        "\n" +
                        "        if (totalSplit.length > 0) {\n" +
                        "            for (int i = 0; i < totalSplit.length; i++) {\n" +
                        "                String name;\n" +
                        "                String[] subSplit = totalSplit[i].split(\" = \");\n" +
                        "                name = subSplit[0];\n" +
                        "                StringBuilder stringBuilder = new StringBuilder(subSplit[1]);\n" +
                        "                int i1 = stringBuilder.indexOf(\"[\");\n" +
                        "                int i2 = stringBuilder.lastIndexOf(\"]\");\n" +
                        "\n" +
                        "                if (i2 != -1 && i1 != -1) {\n" +
                        "                    String arr = stringBuilder.substring(i1, i2 + 1).toString();\n" +
                        "                    stringBuilder = new StringBuilder(arr);\n" +
                        "                    i1 = stringBuilder.indexOf(\"[\");\n" +
                        "                    i2 = stringBuilder.lastIndexOf(\"]\");\n" +
                        "                    String subArr = stringBuilder.substring(1, arr.length() - 1).toString();\n" +
                        "                    stringBuilder = new StringBuilder(subArr);\n" +
                        "                    i1 = stringBuilder.indexOf(\"[\");\n" +
                        "                    i2 = stringBuilder.lastIndexOf(\"]\");\n" +
                        "                    if (i2 != -1 && i1 != -1) {\n" +
                        "                        int[][] arr2 = JAVAparseStringTo2ArrayNoNull(arr);\n" +
                        "                        ParamsInfo paramsInfo = new ParamsInfo(name, Arrays.deepToString(arr2), arr2.getClass());\n" +
                        "                        paramsInfos.add(paramsInfo);\n" +
                        "                    } else {\n" +
                        "                        int[] arr1 = JAVAparseStringTo1ArrayNoNull(arr);\n" +
                        "                        ParamsInfo paramsInfo = new ParamsInfo(name, Arrays.toString(arr1), arr1.getClass());\n" +
                        "                        paramsInfos.add(paramsInfo);\n" +
                        "                    }\n" +
                        "                } else {\n" +
                        "                    String value = subSplit[1];\n" +
                        "                    stringBuilder = new StringBuilder(input);\n" +
                        "                    i1 = stringBuilder.indexOf(\"\\\"\");\n" +
                        "                    i2 = stringBuilder.lastIndexOf(\"\\\"\");\n" +
                        "                    if (i2 == -1 && i1 == -1) {\n" +
                        "                        Integer valueInt = Integer.parseInt(value);\n" +
                        "                        ParamsInfo paramsInfo = new ParamsInfo(name, valueInt.toString(), valueInt.getClass());\n" +
                        "                        paramsInfos.add(paramsInfo);\n" +
                        "                    } else {\n" +
                        "                        ParamsInfo paramsInfo = new ParamsInfo(name, value, value.getClass());\n" +
                        "                        paramsInfos.add(paramsInfo);\n" +
                        "                    }\n" +
                        "                }\n" +
                        "            }\n" +
                        "        } else {\n" +
                        "            String name;\n" +
                        "            String[] nameSplit = input.split(\" = \");\n" +
                        "            name = nameSplit[0];\n" +
                        "            StringBuilder stringBuilder = new StringBuilder(input);\n" +
                        "            int i1 = stringBuilder.indexOf(\"[\");\n" +
                        "            int i2 = stringBuilder.lastIndexOf(\"]\");\n" +
                        "\n" +
                        "            if (i2 != -1 && i1 != -1) {\n" +
                        "                String arr = stringBuilder.substring(i1, i2 + 1).toString();\n" +
                        "                stringBuilder = new StringBuilder(input);\n" +
                        "                i1 = stringBuilder.indexOf(\"[\");\n" +
                        "                i2 = stringBuilder.lastIndexOf(\"]\");\n" +
                        "                if (i2 != -1 && i1 != -1) {\n" +
                        "                    int[][] arr2 = JAVAparseStringTo2ArrayNoNull(arr);\n" +
                        "                    ParamsInfo paramsInfo = new ParamsInfo(name, Arrays.deepToString(arr2), arr2.getClass());\n" +
                        "                    paramsInfos.add(paramsInfo);\n" +
                        "                } else {\n" +
                        "                    int[] arr1 = JAVAparseStringTo1ArrayNoNull(arr);\n" +
                        "                    ParamsInfo paramsInfo = new ParamsInfo(name, Arrays.toString(arr1), arr1.getClass());\n" +
                        "                    paramsInfos.add(paramsInfo);\n" +
                        "                }\n" +
                        "            } else {\n" +
                        "                String value = nameSplit[1];\n" +
                        "                stringBuilder = new StringBuilder(input);\n" +
                        "                i1 = stringBuilder.indexOf(\"\\\"\");\n" +
                        "                i2 = stringBuilder.lastIndexOf(\"\\\"\");\n" +
                        "                if (i2 == -1 && i1 == -1) {\n" +
                        "                    Integer valueInt = Integer.parseInt(value);\n" +
                        "                    ParamsInfo paramsInfo = new ParamsInfo(name, valueInt.toString(), valueInt.getClass());\n" +
                        "                    paramsInfos.add(paramsInfo);\n" +
                        "                } else {\n" +
                        "                    ParamsInfo paramsInfo = new ParamsInfo(name, value, value.getClass());\n" +
                        "                    paramsInfos.add(paramsInfo);\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }\n" +
                        "        return paramsInfos;\n" +
                        "    }\n";
        stringBuilder.append(helper);
        stringBuilder.append("}");
        System.out.println(stringBuilder);

        //3. 辅助代码的相关方法与类
        File file = saveCodeToFile(stringBuilder.toString());
    }

    public File saveCodeToFile(String code) {
        String userDir = System.getProperty("user.dir");
        String globalCodePathName = userDir + File.separator + "tmpCode";
        // 判断全局代码目录是否存在，没有则新建目录
        if (!FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }

        // 把用户的代码隔离存放
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + "Solution.java";
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
        return userCodeFile;
    }

    public static void main(String[] args) {
        String userCode = "import AAA;   public class Main{}";
        StringBuilder stringBuilder = new StringBuilder(userCode);
        String[] split = userCode.split("public");
        System.out.println(split[0]);
        System.out.println(split[1]);
    }


}