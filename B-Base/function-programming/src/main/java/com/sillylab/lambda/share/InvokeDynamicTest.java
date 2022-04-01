package com.sillylab.lambda.share;

import java.lang.invoke.*;

import static java.lang.invoke.MethodHandles.lookup;

/**
 * invokedynamic指令
 *  每一个invokedynamic指令的实例叫做一个动态调用点(dynamic call site), 动态调用点最开始是未链接状态(unlinked:
 *  表示还未指定该调用点要调用的方法),  动态调用点依靠引导方法来链接到具体的方法.
 *  引导方法是由编译器生成, 在运行期当JVM第一次遇到invokedynamic指令时, 会调用引导方法来
 *  将invokedynamic指令所指定的名字(方法名,方法签名)和具体的执行代码(目标方法)链接起来, 引导方法的返回值永久的决定了调用点的行为.
 *  引导方法的返回值类型是java.lang.invoke.CallSite, 一个invokedynamic指令关联一个CallSite,
 *  将所有的调用委托到CallSite当前的target(MethodHandle)
 *
 *
 * 每一处含有invokedynamic指令的位置都称做“动态调用点”（Dynamic Call Site）,
 * 这条指令的第一个参数不再是代表方法符号引用的CONSTANT_Methodref_info常量,而是变为JDK 1.7新加入的CONSTANT_InvokeDynamic_info常量,
 * 从这个新常量中可以得到3项信息：引导方法（Bootstrap Method,此方法存放在新增的BootstrapMethods属性中）、方法类型（MethodType）和名称。
 * 引导方法是有固定的参数,并且返回值是java.lang.invoke.CallSite对象,这个代表真正要执行的目标方法调用。
 * 根据CONSTANT_InvokeDynamic_info常量中提供的信息,虚拟机可以找到并且执行引导方法,从而获得一个CallSite对象,最终调用要执行的目标方法。
 *
 * BootstrapMethod（）,所有逻辑就是调用MethodHandles$Lookup的findStatic（）方法,
 * 产生testMethod（）方法的MethodHandle,然后用它创建一个ConstantCallSite对象。
 * 最后,这个对象返回给invokedynamic指令实现对testMethod（）方法的调用,invokedynamic指令的调用过程到此就宣告完成了。
 */
public class InvokeDynamicTest {
    public static void main(String[]args)throws Throwable{
        INDY_BootstrapMethod().invokeExact("icyfenix");
    }
    public static void testMethod(String s){
        System.out.println("hello String: "+s);
    }
    public static CallSite BootstrapMethod(
        MethodHandles.Lookup lookup,String name,MethodType mt)throws Throwable{
        return new ConstantCallSite(lookup.findStatic(InvokeDynamicTest.class,name,mt));
    }
    private static MethodType MT_BootstrapMethod(){
        return MethodType.fromMethodDescriptorString(
                "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;",null);
    }
    private static MethodHandle MH_BootstrapMethod()throws Throwable{
        return lookup().findStatic(InvokeDynamicTest.class,"BootstrapMethod",MT_BootstrapMethod());
    }
    private static MethodHandle INDY_BootstrapMethod()throws Throwable{
        CallSite cs=(CallSite)MH_BootstrapMethod().invokeWithArguments(lookup(),"testMethod",
                MethodType.fromMethodDescriptorString("(Ljava/lang/String;)V",null));
        return cs.dynamicInvoker();
    }
}
