import java.io.*;
import java.util.ArrayList;

public class Analyzer {
    public int ch;
    public int code;//保留字状态码
    public StringBuffer strToken = new StringBuffer();  //存放构成单词符号的字符串
    public String[] reservedWord = {"public","private","protect","void","if","while","do","int"};  //保留字0~19
    public String[] operator = {"+","-","*","/","<",">","(",")","=","!=","<=",">=","==",";","{","}","$"}; //运算符30~49
    public String[] number = {"0","1","2","3","4","5","6","7","8","9","10"};  //数字50
    public static ArrayList<String> wordList = new ArrayList<String>();  //存放结果

    public Analyzer(){

    }
    //判断是否是字母
    public boolean isLetter(){
        if( (ch>=65&&ch<=90) || (ch>=97&&ch<=122)){  //小写字母a-z 对应ascii码97-122大写字母A-Z对应ascii码为65-90
            return true;
        }
        return false;
    }
    //判断是否是数字
    public boolean isDigit(){
        if(ch>=48&&ch<=57){    //0-9的ascii码是48-57
            return true;
        }
        return false;
    }
    //判断是否是空格
    public boolean isSpace(int ch){
        if(ch==32){
            return true;
        }
        return false;
    }
    //判断是否是运算符
    public int isOperator(){
        for(int i = 0; i < operator.length; i++){
            if(strToken.toString().equals(operator[i])){
                return i + 30;
            }
        }
        return -1;
    }
    //判断是否为保留字
    public int isReservedWord(){
        for(int i = 0; i < reservedWord.length; i++){
            if(strToken.toString().equals(reservedWord[i])){
                return i;
            }
        }
        return -1;
    }

    public void concat(char ch){ //连接字符串
        strToken.append(ch);
    }
    //获取当前字符串的code
    public int getCode(){
        int code;
        //保留字0~19
        if(isReservedWord() != -1){
            code = isReservedWord();
            return code;
        }
        //运算符30~49
        else if(isOperator() != -1){
            code = isOperator();
            return code;
        }
        //数字 50
        for(int i = 0; i < number.length; i++){
            if(strToken.toString().equals(number[i])){
                return 50;
            }
        }
        return 20;
    }
    //打印结果
    public void getResult(){
        code = getCode();
        if(strToken.length() > 0){
            System.out.println("(" + code + "，'" + strToken + "')");
            wordList.add(String.valueOf(code));
            wordList.add(strToken.toString());
        }
        strToken.delete(0, strToken.length());
    }

    public void scanner(){
        BufferedReader bReader = null;
        try {
            bReader = new BufferedReader(new InputStreamReader(new FileInputStream("E://test.txt")));
            while((ch = bReader.read()) != -1){
                //空格则将strToken输出
                if(isSpace(ch) == false){
                    //如果是字母或数字
                    if(isLetter() == true || isDigit() == true){
                        if(strToken.length()>0){
                            //是运算符则将strToken输出
                            if(isOperator() != -1){
                                getResult();
                                concat((char)ch);
                            }
                            else{
                                concat((char)ch);
                            }
                        }
                        else {
                            concat((char)ch);
                        }
                    }
                    else if(ch == 61){   //看是否为'='
                        if((strToken.length()!=0)&&(strToken.charAt(0)=='=')){   //如果是'=='的情况
                            strToken.append(""+(char)ch);
                            System.out.println("(42，'"+strToken+"')");
                            wordList.add("42");
                            wordList.add(strToken.toString());
                            strToken.delete(0, strToken.length());
                        }
                        else if(((strToken.length()!=0))&&(strToken.charAt(0)=='<')){    //如果是'<='的情况
                            strToken.append((char)ch);
                            System.out.println("(40，'"+strToken+"')");
                            wordList.add("40");
                            wordList.add(strToken.toString());
                            strToken.delete(0, strToken.length());
                        }
                        else if(((strToken.length()!=0))&&(strToken.charAt(0)=='>')){    //如果是'>='的情况
                            strToken.append((char)ch);
                            System.out.println("(41，'"+strToken+"')");
                            wordList.add("41");
                            wordList.add(strToken.toString());
                            strToken.delete(0, strToken.length());
                        }
                        else if(((strToken.length()!=0))&&(strToken.charAt(0)=='!')){    //如果是'!='的情况
                            strToken.append((char)ch);
                            System.out.println("(39，'"+strToken+"')");
                            wordList.add("39");
                            wordList.add(strToken.toString());
                            strToken.delete(0, strToken.length());
                        }
                        else{   //只有一个'='
                            if(strToken.length()==0){
                                strToken.append((char)ch);
                            }
                            else {  //将strToken输出
                                getResult();
                                strToken.append((char)ch);
                            }
                        }
                    }
                    else if(ch == 60){   //看是否为'<'
                        if(strToken.length()==0){
                            strToken.append((char)ch);
                        }
                        else {
                            getResult();
                            strToken.append((char)ch);
                        }
                    }
                    else if(ch == 62){   //看是否为'>'
                        if(strToken.length()==0){
                            strToken.append((char)ch);
                        }
                        else {
                            getResult();
                            strToken.append((char)ch);
                        }
                    }
                    else if(ch == 40){    //看是否为'('
                        if(strToken.length()==0){
                            strToken.append((char)ch);
                        }
                        else {
                            getResult();
                            strToken.append((char)ch);
                        }
                    }
                    else if(ch == 41){    //看是否为')'
                        if(strToken.length()==0){
                            strToken.append((char)ch);
                        }
                        else {
                            getResult();
                            strToken.append((char)ch);
                        }
                    }
                    else if(ch == 123){    //看是否为'{'
                        if(strToken.length()==0){
                            strToken.append((char)ch);
                        }
                        else {
                            getResult();
                            strToken.append((char)ch);
                        }
                    }
                    else if(ch == 125){    //看是否为'}'
                        if(strToken.length()==0){
                            strToken.append((char)ch);
                        }
                        else {
                            getResult();
                            strToken.append((char)ch);
                        }
                    }
                    else if(ch == 59) {    //看是否为';'
                        if (strToken.length() == 0) {
                            strToken.append((char) ch);
                        } else {
                            getResult();
                            strToken.append((char) ch);
                        }
                    }
                    else if(ch == 43) {    //看是否为'+'
                        if (strToken.length() == 0) {
                            strToken.append((char) ch);
                        } else {
                            getResult();
                            strToken.append((char) ch);
                        }
                    }
                    else if(ch == 45) {    //看是否为'-'
                        if (strToken.length() == 0) {
                            strToken.append((char) ch);
                        } else {
                            getResult();
                            strToken.append((char) ch);
                        }
                    }
                    else if(ch == 42) {    //看是否为'*'
                        if (strToken.length() == 0) {
                            strToken.append((char) ch);
                        } else {
                            getResult();
                            strToken.append((char) ch);
                        }
                    }
                    else if(ch == 47) {    //看是否为'/'
                        if (strToken.length() == 0) {
                            strToken.append((char) ch);
                        } else {
                            getResult();
                            strToken.append((char) ch);
                        }
                    }
                    else if(ch == 36){   //看是否为'$'
                        if (strToken.length() == 0) {
                            strToken.append((char) ch);
                        } else {
                            getResult();
                            strToken.append((char) ch);
                            getResult();
                        }
                    }
                }
                else
                {
                    getResult();
                }
            }
        }
        catch (FileNotFoundException  e) {
            e.printStackTrace();
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}