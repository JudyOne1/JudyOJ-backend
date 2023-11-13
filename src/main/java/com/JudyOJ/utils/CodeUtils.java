package com.JudyOJ.utils;

/**
 * 处理代码拼接的工具类
 *
 * @author Judy
 * @create 2023-11-10-20:38
 */
public class CodeUtils {

    private static final String HELPER =
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
                    "        String[] totalSplit = input.split(\",(?=[^\\\\]]*$)\");"+
                    "\n" +
                    "        if (totalSplit.length > 0) {\n" +
                    "            for (int i = 0; i < totalSplit.length; i++) {\n" +
                    "                String name;\n" +
                    "                String[] subSplit = totalSplit[i].split(\"=\");\n" +
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
                    "            String[] nameSplit = input.split(\"=\");\n" +
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
                    "        String[] totalSplit = input.split(\",(?=[^\\\\]]*$)\");"+
                    "\n" +
                    "        if (totalSplit.length > 0) {\n" +
                    "            for (int i = 0; i < totalSplit.length; i++) {\n" +
                    "                String name;\n" +
                    "                String[] subSplit = totalSplit[i].split(\"=\");\n" +
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
                    "                        String valueWithoutQuotes = value.replaceAll(\"^\\\"|\\\"$\", \"\");\n" +
                    "                        ParamsInfo paramsInfo = new ParamsInfo(name, valueWithoutQuotes, value.getClass());"+
                    "                        paramsInfos.add(paramsInfo);\n" +
                    "                    }\n" +
                    "                }\n" +
                    "            }\n" +
                    "        } else {\n" +
                    "            String name;\n" +
                    "            String[] nameSplit = input.split(\"=\");\n" +
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
                    "                    String valueWithoutQuotes = value.replaceAll(\"^\\\"|\\\"$\", \"\");\n" +
                    "                    ParamsInfo paramsInfo = new ParamsInfo(name, valueWithoutQuotes, value.getClass());"+
                    "                    paramsInfos.add(paramsInfo);\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "        return paramsInfos;\n" +
                    "    }\n";

    /**
     * 处理计数器模式的代码
     * @param helpCode
     * @param code
     * @return
     */
    public static String dealWithCMCode(String helpCode, String code) {
        StringBuilder stringBuilder = new StringBuilder(code);
        int lastIndexOf = stringBuilder.lastIndexOf("}");
        stringBuilder.replace(lastIndexOf, lastIndexOf + 1, helpCode);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    /**
     * 处理核心代码模式的代码
     * @param helpCode 数据库中保存的辅助code
     * @param code 用户输入的code
     * @return
     */
    public static String dealWithCCMCode(String helpCode, String code) {
        StringBuilder stringBuilder = new StringBuilder(code);
        //1. import代码的写入
        String importCode =
                "import java.lang.reflect.Method;\n" +
                        "import java.lang.reflect.Parameter;\n" +
                        "import java.util.ArrayList;\n" +
                        "import java.util.Arrays;";
        String helper = HELPER;
        stringBuilder.insert(0, importCode);
        int lastIndexOf = stringBuilder.lastIndexOf("}");
        stringBuilder.replace(lastIndexOf, lastIndexOf + 1, helpCode);
        stringBuilder.append(helper);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }


}
