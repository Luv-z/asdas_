import java.util.ArrayList;

public class Semantics_parser {
    public int index=0,m=0,nextgoto,whileline,temp=1;
    public String syn;
    public int i = 0,line = 1;
    public int right;
    public ArrayList<String>myList;
    ArrayList<Quaternion> quaters = new ArrayList<Quaternion>();   //存储四元式
    ArrayList<String> variable = new ArrayList<>();  //存储已定义的变量

    public int parser(){
        int s = 0;
        right = 1;
        if(i<myList.size())
            syn = myList.get(i);
        if(syn.equals("0") || syn.equals("1") || syn.equals("2")){   //修饰符
            syn = myList.get(i+2);    //读取下一个单词符号；
            i = i+2;
            if(syn.equals("3") || syn.equals("7")){    //返回类型
                syn = myList.get(i+2);
                i = i+2;
                if(syn.equals("20")){      //函数名
                    syn = myList.get(i+2);
                    i = i+2;
                    if(syn.equals("36") && myList.get(i+2).equals("37")){
                        syn = myList.get(i+4);
                        i = i+4;
                        if(syn.equals("44")){
                            if(statementBlock("void") == -1){
                                right = -1;
                            }
                        }
                    }
                }
                else
                {
                    System.out.println("函数头定义错误！");
                    return -1;
                }
            }
            else
            {
                System.out.println("函数头定义错误！");
                return -1;
            }
            if(right == 1){
                System.out.println("语法正确");
            }
        }
        else {
            System.out.println("函数头定义错误！");
            right = -1;
        }
        return s;
    }
    //处理代码段
    public int codeSegment() {
        int s = 1;
        s = statement();
        while(syn.equals("43")&&(s != -1)){    // ';'
            if(i+2<myList.size()){
                syn = myList.get(i+2);  //读取下一个单词符号；
                i = i+2;
                s = statement();
            }
            else
                break;
        }
        return s;
    }
    public boolean checkVariable(String str){
        boolean existence = false;
        for(int i = 0; i < variable.size(); i++){
            if(str.equals(variable.get(i))){
                existence = true;
                break;
            }
        }
        return existence;
    }
    //处理语句
    public int statement(){
        String temp, eplace;
        int s = 1;
        if(syn.equals("7")){  //如果是变量类型
            nextsyn();
            variable.add(myList.get(i+1));  //存储定义的变量
            if(syn.equals("20")){
                temp = myList.get(i + 1); //获得当前变量
                if(checkVariable(temp)){
                    nextsyn();
                }
                else{
                    System.out.println("使用了未定义的变量");
                    return -1;
                }
            }
            else{
                System.out.println("变量定义错误！");
                return -1;
            }
            if (syn.equals("38")) { // '='
                nextsyn(); //读取下一个单词符号;
                eplace = expression();
                emit("=" , eplace, "", temp);
                s = 1;
            } else {
                System.out.println("缺少赋值号'='错误");
                return -1;
            }
            return s;
        }
        if(syn.equals("20")) { //如果是是变量名
            temp = myList.get(i + 1); //获得当前变量
            if(checkVariable(temp)){
                nextsyn();
            }
            else{
                System.out.println("使用了未定义的变量");
                return -1;
            }
            if (syn.equals("38")) { // '='
                nextsyn(); //读取下一个单词符号;
                eplace = expression();
                emit("=" , eplace, "", temp);
            } else {
                System.out.println("缺少赋值号'='错误");
                return -1;
            }
            return s;
        }
        if(syn.equals("4")){    //'if'
            nextsyn();
            if(syn.equals("36")){  //'('
                nextsyn();
                if((condition("if") == -1)){
                    return -1;
                }
                if(syn.equals("37")){  //')'
                    nextsyn();
                    if(statementBlock("if") == -1){
                        return -1;
                    }
                    bp(line,nextgoto );
                    return s;
                }
                else{
                    System.out.println("if语句输出')'错误");
                    return -1;
                }
            }
            else {
                System.out.println("if语句输出'('错误");
                return  -1;
            }
        }
        if(syn.equals("5")){    //'while'
            nextsyn();
            if(syn.equals("36")){  //'('
                nextsyn();
                whileline = index;
                if(condition("while") == -1){
                    return -1;
                }
                if(syn.equals("37")){  //')'
                    nextsyn();
                    if(statementBlock("while") ==  -1){
                        return -1;
                    }
                    return s;
                }
                else{
                    System.out.println("while语句输出')'错误");
                    return -1;
                }
            }
            else {
                System.out.println("while语句输出'('错误");
                return -1;
            }
        }
        return s;
    }
    //语句块处理
    private int statementBlock(String str) {
        int r = 1;
        if(syn.equals("44")){   // '{'
            nextsyn();
            r = codeSegment();
            if(r == -1){
                return -1;
            }
            if(syn.equals("45")){  //'}'
                nextsyn();
                nextgoto = index;
                if(str.equals("if"))
                    bp(line,nextgoto);
                if(str.equals("while")){
                    nextgoto = nextgoto+1;
                    bp(line,nextgoto);
                    emit("j",""," ",String.valueOf(whileline));
                }
                r = statement();
                if(r == -1){
                    return -1;
                }
            }
            else{
                System.out.println("输出‘}’错误");
                return -1;
            }
        }
        else{
            System.out.println("输出‘{’错误");
            return -1;
        }
        return r;
    }
    //地址回填
    private void bp(int line,int nextgoto) {
        quaters.get(line).setResult(String.valueOf(nextgoto));
    }
    //处理布尔表达式
    private int condition(String str) {
        String tp,eplace2,eplace,temp = null;
        int r = 1;
        if((eplace = expression()) == null){
            return -1;
        }
        if((Integer.parseInt(syn)>=39)&&(Integer.parseInt(syn)<=42)){    //比较运算符
            if(i+1<myList.size())
                temp = myList.get(i+1);
            nextsyn();
            if((eplace2 = expression()) == null){
                return -1;
            }
            tp = newtemp();
            emit(temp,eplace,eplace2,tp);
            m = index+2;
            if(str.equals("if") || str.equals("while"))   //真转
                emit("jt ",tp,"  ",String.valueOf(m));
            line = index;  //记录要回填地址的无条件转的行数
            emit("j",""," ","");    //无条件转
        }
        else{
            System.out.println("error");
            return -1;
        }

        return r;
    }
    //处理表达式
    public String expression() {
        String tp,eplace2,eplace,temp = null;
        eplace = expressionMD();
        if(eplace == null){
            return null;
        }
        while(syn.equals("30") || syn.equals("31")){   //'+' '-'
            if(i+1<myList.size())
                temp = myList.get(i+1); //获取当前运算符
            nextsyn();//读取下一个单词符号;
            if((eplace2 = expressionMD()) == null){
                return null;
            }
            tp = newtemp();
            emit(temp,eplace,eplace2,tp);
            eplace = tp;
        }
        return eplace;
    }
    //优先处理*，/
    public String expressionMD() {
        String tp,eplace3,eplace,tt = null;
        eplace = expressionB();
        if(eplace == null){
            return null;
        }
        while(syn.equals("32")||syn.equals("33")){    //'*' '/'
            if(i+1<myList.size())
                tt = myList.get(i+1);
            nextsyn();//读取下一个单词符号;
            if((eplace3 = expressionB()) == null){
                return null;
            }
            tp = newtemp();  //生成新的变量名
            emit(tt,eplace,eplace3,tp);
            eplace = tp;
        }
        return eplace;
    }
    //优先处理（）
    public String expressionB(){
        String fplace;
        fplace = " ";
        if(syn.equals("20")){     //变量名
            if(i+1<myList.size())
                fplace = myList.get(i+1);
            if(checkVariable(myList.get(i+1))){
                nextsyn();
            }
            else{
                System.out.println("使用了未定义的变量");
                return null;
            }
        }
        else if(syn.equals("50")){   //数字
            if(i+1<myList.size())
                fplace = myList.get(i+1);
            nextsyn();//读取下一个单词符号；
        }
        else if(syn.equals("36")){     //'('
            nextsyn();//读取下一个单词符号；
            if((fplace = expression()) == null){
                return null;
            }
            if(syn.equals("37")){      //')'
                nextsyn();//读取下一个单词符号；
            }
            else {
                System.out.println("输出')'错误");
                return null;
            }
        }
        else if((Integer.parseInt(syn)>=39)&&(Integer.parseInt(syn)<=42)){
            System.out.println("表达式错误！");
            return null;
        }
        else {
            System.out.println(syn + "输出'('错误");
            return null;
        }
        return fplace;
    }
    //生成新的变量名
    public String newtemp(){
        char c = 'T';
        String eplace = String.valueOf(c)+String.valueOf(temp);
        temp++;
        return eplace;
    }
    //生成四元表达式
    public void emit(String op,String ag1,String ag2,String result){
        Quaternion q = new Quaternion();
        q.setResult(result);
        q.setArgv1(ag1);
        q.setOp(op);
        q.setArgv2(ag2);
        quaters.add(q);
        index++;
    }
    //打印四元式
    public void printEmit() {
        for(int j=0;j<quaters.size();j++){
            System.out.println("(" + j + ")"+":("
                    + quaters.get(j).getOp() + ","
                    + quaters.get(j).getArgv1() + ","
                    + quaters.get(j).getArgv2() + ","
                    + quaters.get(j).getResult() + ")");
        }
    }
    //将syn置为下一个单词符号
    public void nextsyn(){
        if(i+2<myList.size())
            syn = myList.get(i+2);//读取下一个单词符号;
        i = i+2;
    }

    public static void main(String[] args) {
        Analyzer analyzer = new Analyzer();
        Semantics_parser parser = new Semantics_parser();
        analyzer.scanner();
        parser.myList = analyzer.wordList;
        parser.parser();
        parser.printEmit();
    }
}
