package com.sillylab.lambda.share;

import java.util.function.IntBinaryOperator;

/**
 *  作用:
 *       每一个invokedynamic指令的实例叫做一个动态调用点(dynamic call site), 动态调用点最开始是未链接状态(unlinked:表示还未指定该调用点要调用的方法),
 *       动态调用点依靠引导方法来链接到具体的方法.  引导方法是由编译器生成, 在运行期当JVM第一次遇到invokedynamic指令时, 会调用引导方法来
 *       将invokedynamic指令所指定的名字(方法名,方法签名)和具体的执行代码(目标方法)链接起来, 引导方法的返回值永久的决定了调用点的行为.
 *       引导方法的返回值类型是java.lang.invoke.CallSite, 一个invokedynamic指令关联一个CallSite, 将所有的调用委托到CallSite当前的target(MethodHandle)
 *    参数:(说明来自api)
 *       LambdaMetafactory.metafactory(Lookup, String, MethodType, MethodType, MethodHandle, MethodType)有六个参数, 按顺序描述如下
 *      1. MethodHandles.Lookup caller : 代表查找上下文与调用者的访问权限, 使用invokedynamic指令时, JVM会自动自动填充这个参数, 这里JVM为我们填充
 *          为Lookup(com.sillylab.lambda.share.LambdaExample.class, (PUBLIC | PRIVATE | PROTECTED | PACKAGE)) 意思是这个Lookup实例可以访问Main类的所有成员.
 *      2. String invokedName : 要实现的方法的名字, 使用invokedynamic时, JVM自动帮我们填充(填充内容来自常量池InvokeDynamic.NameAndType.Name),
 *          在这里JVM为我们填充为 "applyAsInt", IntBinaryOperator.applyAsInt方法名.
 *      3. MethodType invokedType : 调用点期望的方法参数的类型和返回值的类型(方法signature). 使用invokedynamic指令时, JVM会自动自动填充这个参数
 *          (填充内容来自常量池InvokeDynamic.NameAndType.Type), 在这里参数为int,int, 返回值类型为IntBinaryOperator, 表示这个调用点的目标方法的参数为int,
 *          然后invokedynamic执行完后会返回一个IntBinaryOperator实例 .
 *      4. MethodType samMethodType :  函数对象将要实现的接口方法类型, 这里运行时, 值为 int 即 IntBinaryOperator.applyAsInt方法的类型(泛型信息被擦除).
 *      5. MethodHandle implMethod : 一个直接方法句柄(DirectMethodHandle), 描述在调用时将被执行的具体实现方法 (包含适当的参数适配, 返回类型适配,
 *         和在调用参数前附加上捕获的参数), 在这里为 com.sillylab.lambda.share.LambdaExample.lambda$0(int,int)int 方法的方法句柄.
 *      6. MethodType instantiatedMethodType : 函数接口方法替换泛型为具体类型后的方法类型, 通常和 samMethodType 一样, 不同的情况为:
 *          比如函数接口方法定义为 T applyAsInt(int, int)  , 这个时候方法类型为(int, int)int, 这时samMethodType就是 (Object)Object
 *      第4, 5, 6 三个参数来自class文件中的. 如上面引导方法字节码中Method arguments后面的三个参数就是将应用于4, 5, 6的参数.
 */
public class LambdaExample {
    public static void main(String[] args) {
        IntBinaryOperator f = (x, y) -> x + y;
        f.applyAsInt(1, 2);

        //new Thread(() -> System.out.println("Hello world"));
    }
}
