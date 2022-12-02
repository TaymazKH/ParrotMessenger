public class CalculatorBot {
    public static String getFields() {
        return null;
    }
    public static void setFields(String data) {
        // do nothing
    }

    public static String[] answerMessage(String text, long sender) {
        try{
            return new String[]{text+" equals "+wtf(new StringBuilder(text)).toString(),null};
        } catch(Exception e){
            return new String[]{"you are a donkey dear TA",null};
        }
    }
    public static String[] answerGroupMessage(String text, long sender, long group, long owner) {
        return null;
    }
    public static String[] sendMessage(long receiver) {
        return null;
    }
    public static String[] sendGroupMessage(long group) {
        return null;
    }
    public static String[] sendComment(long user, String text, long tweet) {
        return null;
    }
    public static String[] sendTweet() {
        return null;
    }

    public static boolean invokeAnswerMessage() {
        return true;
    }
    public static boolean invokeAnswerGroupMessage() {
        return false;
    }
    public static boolean invokeSendMessage() {
        return false;
    }
    public static boolean invokeSendGroupMessage() {
        return false;
    }
    public static boolean invokeSendComment() {
        return false;
    }
    public static boolean invokeSendTweet() {
        return false;
    }
    public static boolean acceptInvite() {
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public static int[] PM(StringBuilder s){
        //converts StringBuilder to int[]

        int[] output = new int[1000];
        int i = s.length()-1;
        if(s.length()>0){
            while(!(s.charAt(i)=='+' || s.charAt(i)=='-' || i==0)){
                i--;
            }
            StringBuilder a = new StringBuilder(s.substring(i));
            boolean xFound=false; boolean powFound=false;
            int factor=0; int power=0;
            int index;

            for(int j=0;j<a.length();j++){
                if(a.charAt(j)=='x') xFound=true;
                else if(a.charAt(j)=='^') powFound=true;
                if(xFound && powFound) break;
            }

            if(!xFound && !powFound){
                factor=Integer.parseInt(String.valueOf(a));
                power=0;
            }
            else if(!xFound && powFound){
                index=a.indexOf("^");
                if(a.charAt(0)=='-')
                    factor=(int) (-1*Math.pow(Integer.parseInt(a.substring(1,index)),Integer.parseInt(a.substring(index+1))));
                else
                    factor=(int) Math.pow(Integer.parseInt(a.substring(0,index)),Integer.parseInt(a.substring(index+1)));
                power=0;
            }
            else if(xFound){
                if(a.indexOf("x^")==-1) power=1;
                else power=Integer.parseInt(a.substring(a.indexOf("x^")+2));
                if(a.indexOf("x")==0) factor=1;
                else{
                    StringBuilder b = new StringBuilder(a.subSequence(0,a.indexOf("x")));
                    powFound=false;
                    for(int j=0;j<b.length();j++){
                        if(b.charAt(j)=='^'){ powFound=true; break; }
                    }
                    if(!powFound){
                        if(String.valueOf(b).equals("+")) factor=1;
                        else if(String.valueOf(b).equals("-")) factor=-1;
                        else factor=Integer.parseInt(String.valueOf(b));
                    }
                    else{
                        index=b.indexOf("^");
                        if(b.charAt(0)=='-')
                            factor=(int) (-1*Math.pow(Integer.parseInt(b.substring(1,index)),Integer.parseInt(b.substring(index+1))));
                        else
                            factor=(int) Math.pow(Integer.parseInt(b.substring(0,index)),Integer.parseInt(b.substring(index+1)));
                    }
                }
            }

            output = PM(new StringBuilder(s.substring(0,i)));
            output[power] += factor;
        }
        return output;
    }

    public static StringBuilder SM(int[] input){
        //converts int[] to StringBuilder

        StringBuilder s = new StringBuilder();
        for(int i=999;i>0;i--){
            if(input[i]==0) continue;
            if(input[i]>1) s.append("+").append(input[i]);
            else if(input[i]==1) s.append("+");
            else if(input[i]<-1) s.append(input[i]);
            else if(input[i]==-1) s.append("-");
            s.append("x");
            if(i>1) s.append("^").append(i);
        }
        if(input[0]>0) s.append("+").append(input[0]);
        else if(input[0]<0) s.append(input[0]);
        if(s.length()==0) s.append("+0");
        //if(s.charAt(0)=='+') s.deleteCharAt(0);
        return s;
    }

    public static StringBuilder simplifier(StringBuilder s){

        StringBuilder a = new StringBuilder(s);
        StringBuilder b;
        int leftOpen=0; int rightClose;
        int i;
        boolean parenthesisFound;
        int c; //parenthesis Counter
        //int[] outputP = new int[1000];

        do{
            parenthesisFound = false;
            s = new StringBuilder(a);
            for(i=0;i<a.length();i++){
                if(a.charAt(i)=='('){ parenthesisFound=true; leftOpen=i; break; }
            }
            if(parenthesisFound){
                i=leftOpen;
                c=1;
                while(c>0){
                    i++;
                    if(a.charAt(i)==')') c--; else if(a.charAt(i)=='(') c++;
                }
                rightClose=i;
                b = simplifier(new StringBuilder(a.subSequence(leftOpen+1,rightClose)));

                if(leftOpen>0 && a.charAt(leftOpen-1)=='-'){
                    for(int j=0;j<b.length();j++){
                        if(b.charAt(j)=='+') b.setCharAt(j,'-');
                        else if(b.charAt(j)=='-') b.setCharAt(j,'+');
                    }
                    a.replace(leftOpen-1,rightClose+1, String.valueOf(b));
                }
                else if(leftOpen>0 && a.charAt(leftOpen-1)=='+'){
                    a.replace(leftOpen-1, rightClose+1, String.valueOf(b));
                }
                else if(leftOpen==0){
                    a.replace(leftOpen,rightClose+1, String.valueOf(b));
                }
                //if(a.charAt(0)=='+') a.deleteCharAt(0);
            }
            else{
                a = new StringBuilder(SM(PM(s)));
                //if(a.charAt(0)=='+') a.deleteCharAt(0);
            }
            //if(a.charAt(0)=='+') a.deleteCharAt(0);
        }
        while(!s.toString().equals(a.toString()));
        return s;
    }

    public static StringBuilder multiplication(StringBuilder s, int index){
        int leftOpen; int rightClose;
        int c=1; //parenthesis Counter
        int i=index-1;
        while(c>0){
            i--;
            if(s.charAt(i)==')') c++; else if(s.charAt(i)=='(') c--;
        }
        leftOpen=i;
        c=1;
        i=index;
        while(c>0){
            i++;
            if(s.charAt(i)==')') c--; else if(s.charAt(i)=='(') c++;
        }
        rightClose=i;
        //.....................................
        StringBuilder left = new StringBuilder(wtf(new StringBuilder(s.subSequence(leftOpen+1,index-1))));
        StringBuilder right = new StringBuilder(wtf(new StringBuilder(s.subSequence(index+1,rightClose))));
        StringBuilder output = new StringBuilder("(");
        int[] leftP = PM(left); int[] rightP = PM(right);
        int[] outputP = new int[1000];
        for(i=999;i>=0;i--){
            for(int j=999;j>=0;j--){
                if(i+j<=999) outputP[i+j] += leftP[i] * rightP[j];
            }
        }
        output.append(SM(outputP)).append(")");
        //....................................................
        if(leftOpen>0 && s.charAt(leftOpen-1)=='-'){
            for(i=0;i<output.length();i++){
                if(output.charAt(i)=='+') output.setCharAt(i,'-');
                else if(output.charAt(i)=='-') output.setCharAt(i,'+');
            }
            output.insert(0,"+");
            if(output.charAt(2)=='+') output.deleteCharAt(2);
            s.replace(leftOpen-1,rightClose+1, String.valueOf(output));
        }
        else{
            if(output.charAt(1)=='+') output.deleteCharAt(1);
            s.replace(leftOpen, rightClose + 1, String.valueOf(output));
        }
        return s;
    }

    public static StringBuilder hash(StringBuilder s, int index){

        int leftOpen; int rightClose;
        int c=1; //parenthesis Counter
        int i=index-1;
        while(c>0){
            i--;
            if(s.charAt(i)==')') c++; else if(s.charAt(i)=='(') c--;
        }
        leftOpen=i;
        c=1;
        i=index+1;
        while(c>0){
            i++;
            if(s.charAt(i)==')') c--; else if(s.charAt(i)=='(') c++;
        }
        rightClose=i;
        //.....................................................................................................
        StringBuilder left = new StringBuilder(wtf(new StringBuilder(s.subSequence(leftOpen+1,index-1))));
        StringBuilder right = new StringBuilder(wtf(new StringBuilder(s.subSequence(index+2,rightClose))));
        StringBuilder output = new StringBuilder("(");
        int[] leftP = PM(left); int[] rightP = PM(right);
        int[] outputP = new int[1000];

        for(i=999;i>=0;i--) outputP[i]=leftP[i]*rightP[i];
        output.append(SM(outputP)).append(")");
        //....................................................................................................
        if(leftOpen>0 && s.charAt(leftOpen-1)=='-'){
            for(i=0;i<output.length();i++){
                if(output.charAt(i)=='+') output.setCharAt(i,'-');
                else if(output.charAt(i)=='-') output.setCharAt(i,'+');
            }
            output.insert(0,"+");
            if(output.charAt(2)=='+') output.deleteCharAt(2);
            s.replace(leftOpen-1,rightClose+1, String.valueOf(output));
        }
        else{
            if(output.charAt(1)=='+') output.deleteCharAt(1);
            s.replace(leftOpen, rightClose + 1, String.valueOf(output));
        }
        //...................................
        return s;
    }

    public static StringBuilder wtf(StringBuilder s){

        String whatToCall;
        StringBuilder a;
        StringBuilder b = new StringBuilder(s);
        int index=0;

        for(int j=0;j<100;j++) {
            do {
                a = new StringBuilder(b);
                b = new StringBuilder();
                whatToCall = "+";
                for (int i = 1; i < a.length(); i++) {
                    if (a.charAt(i) == '#') {
                        whatToCall = "#";
                        index = i;
                        break;
                    } else if (a.charAt(i - 1) == ')' && a.charAt(i) == '(') {
                        whatToCall = ".";
                        index = i;
                    }
                }
                if (whatToCall.equals("#")) b.append(hash(a, index));
                else if (whatToCall.equals(".")) b.append(multiplication(a, index));
                else b.append(simplifier(a));
            }
            while (!a.toString().equals(b.toString()));
        }
        //.................................................
//        for(int i=0;i<b.length();i++){
//            if(b.charAt(i)=='(' || b.charAt(i)==')') b.deleteCharAt(i);
//        }
//        for(int i=0;i<b.length()-1;i++){
//            if(b.charAt(i)=='+' && b.charAt(i+1)=='-') b.deleteCharAt(i);
//            else if(b.charAt(i)=='+' && b.charAt(i+1)=='+') b.deleteCharAt(i);
//            else if(b.charAt(i)=='-' && b.charAt(i+1)=='+') b.deleteCharAt(i+1);
//            else if(b.charAt(i)=='-' && b.charAt(i+1)=='-') b.replace(i,i+2,"+");
//        }
        if(b.charAt(0)=='+') b.deleteCharAt(0);
        if(b.length()==0) b.append("0");
        return b;
    }
}
