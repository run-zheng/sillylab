# transaction-template模块 

为了解决传统开发过程中，在服务层做事务控制和远程调用混编时，多个持久化有关的服务需要抽取出来作为一个innerService进行事务控制的问题。 

如： 
{
    serviceA.a();  
    serviceB.b();   
    remoteService.c();  
    ... 
}
一般会抽出来一个XxxInnerService改成
{
    xxxInnerService.inner();    
    remoteService.c();
    ...
}

//XxxInnerService
@Transactional 
... inner() {
    serviceA.a();  
    serviceB.b(); 
}
用该模块可以写成
transactionExecutorTemplate.executeRequired(() ->{
    serviceA.a()
    serviceB.b()
});
remoteService.c(); 
...  