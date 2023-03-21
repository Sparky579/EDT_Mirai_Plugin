package org.example.test;



import java.util.Arrays;
import java.util.Stack;

/**
 * @author tian
 * @Package example.project.expression
 * @date 2022/7/23 16:27
 * @description 中缀表达式
 */
public class NifixExpression {
    //Java使用的就是中缀表达式-" ( 10 + 20 / 2 * 3 ) /  2 + 8"
    private static final String nifix="5*12-3*12";

    public static void main(String[] args) {
        int result = evaluateExpression(nifix);
        System.out.println("计算结果为："+result);
    }
    /**
     * 1.格式化表达式
     *
     */
    private static String formatExpression(String expression){
        //拼接字符串
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            //获取字符
            char c = expression.charAt(i);
            //如果为操作符，则两边添加空格并添加进stringBuilder
            if(c == '(' || c == ')' || c == '+' || c == '-' || c == '*' || c == '/' || c == '（' || c == '）'){
                stringBuilder.append(' ');
                stringBuilder.append(c);
                stringBuilder.append(' ');
            }else {
                //否则添加数加两边空格
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();

    }
    /**
     * 2.栈实现中缀表达式计算需要建立两个栈；
     * 符号栈    +     数字栈
     * :
     2.1 符号栈如果为空，或者栈顶为左括号，则可以直接进栈
     2.2 如果栈顶符号优先级比将要入栈的符号优先级低，则可以
     直接进栈
     2.3 如果栈顶符号优先级与将要入栈的优先级相等，则先弹出栈顶
     ，同时弹出数字栈的两个数字使用弹出的符号进行计算，运算结果
     再压入数字栈，最后符号入栈
     2.4 如果将要入栈的符号为右括号，则弹出栈顶并使用弹出的符号对数字栈
     弹出的两个数字进行计算，重复过程直到符号栈栈顶元素为左括号为止
     此时为处理完了括号的内容。
     2.5 最后对最后一个符号和两个数字进行特殊操作
     */
    public static int evaluateExpression(String expression){
        /*
          stack继承了vecter,自己独有的方法只有下面5个：
          1.Object push（Object element）：将元素推送到堆栈顶部。
          2.Object pop（）：移除并返回堆栈的顶部元素。如果我们在调用堆栈为空时调用pop（）,则抛出’EmptyStackException’异常。
          3.Object peek（）：返回堆栈顶部的元素，但不删除它。
          4.boolean empty（）：如果堆栈顶部没有任何内容，则返回true。否则，返回false。
          5.int search（Object element）：确定对象是否存在于堆栈中。如果找到该元素，它将从堆栈顶部返回元素的位置。否则，它返回-1。
         */
        //符号栈
        Stack<Character> operatorStack = new Stack<Character>();
        //数字栈
        Stack<Integer> numberStack = new Stack<Integer>();
        //1.格式化表达式
        String formatExpression = formatExpression(expression);
        System.out.println(formatExpression);
        //切割格式化字符串
        String[] strings = formatExpression.split(" ");
        System.out.println(Arrays.toString(strings));
        //遍历表达式
        for (String string : strings) {
            //1.如果遇到空字符，则直接跳过
            if(string.length() == 0){
                continue;
            }
            //如果字符串长度大于1.必然是数字，比如10、20
            //如果字符串长度等于1，一种是符号、一种是数字
            //跳过取出的字符串的第一个字符是符号，证明改字符串就是符号
            //string.chatAt(0)为符号
            //2.string 读入字符串的第一个字符是+或-的情况
            else if(string.charAt(0) == '+' || string.charAt(0) == '-'){
                //operatorStack.peek()符号栈的栈顶
                while(!operatorStack.isEmpty() && (operatorStack.peek() == '+'
                        || operatorStack.peek() == '-'
                        || operatorStack.peek() == '*'
                        || operatorStack.peek() == '/' )){
                    //计算处理
                    processAnOperator(operatorStack,numberStack);
                }
                //栈内元素处理完时，将待进栈元素进栈
                operatorStack.push(string.charAt(0));
            }
            //3.读入字符为 * 或 /时
            else if(string.charAt(0) == '*' || string.charAt(0) == '/'){
                while(!operatorStack.isEmpty() && (operatorStack.peek() == '*' || operatorStack.peek() == '/' )){
                    //计算处理
                    processAnOperator(operatorStack,numberStack);
                }
                operatorStack.push(string.charAt(0));
            }
            //4.读入字符为左括号时直接进栈
            else if(string.charAt(0) == '(' || string.charAt(0) == '（'){
                operatorStack.push(string.charAt(0));
            }
            //5.读入字符为右括号时，此时要对左括号和右括号之间的数和符号进行处理
            else if(string.charAt(0) == ')' || string.charAt(0) == '）'){
                //只要符号栈栈顶不是左括号，进行循环处理
                while(operatorStack.peek() != '(' && operatorStack.peek() != '（'){
                    processAnOperator(operatorStack,numberStack);
                }
                //处理完括号内容时，直接弹出左括号
                operatorStack.pop();
            }
            //6.当读入字符为数字时
            else {
                if (!FirstModule.isInteger(string)) return -1;
                numberStack.push(Integer.parseInt(string));
            }
        }
        //最后一步，对最后一组运算进行处理
        while (!operatorStack.isEmpty()){
            processAnOperator(operatorStack,numberStack);
        }
        //将压入栈顶元素弹出就是最后的结果
        return numberStack.pop();
    }

    /**
     * 计算处理-处理函数
     * @param operatorStack 符号栈
     * @param numberStack 数字栈
     */
    private static void processAnOperator(Stack<Character> operatorStack, Stack<Integer> numberStack) {
        //运算数1
        int num1 = numberStack.pop();
        //运算数2
        int num2 = numberStack.pop();
        //运算数3
        char c = operatorStack.pop();
        switch (c){
            case '+':{
                numberStack.push(num2 + num1);
                break;
            }
            case '-':{
                numberStack.push(num2 - num1);
                break;
            }
            case '*':{
                numberStack.push(num2 * num1);
                break;
            }
            case '/':{
                if (num2 % num1 != 0) numberStack.push(77);
                else numberStack.push(num2 / num1);
                break;
            }
        }
    }
}

