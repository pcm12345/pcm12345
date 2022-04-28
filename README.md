- 👋 Hi, I’m @pcm12345
- 👀 I’m interested in ...
- 🌱 I’m currently learning ...
- 💞️ I’m looking to collaborate on ...
- 📫 How to reach me ...

<-- Template -->
Main -> java -> com.carrot

  TemplateApplication
  <- commons ->
    CommonController.java
    <common>
      Commons.java
      Cryptos.java
    <excel>
      ExcelTemplate.java
      ExcelType.java
    <model>
      CommonModel.java
      ResponseBuilder.java
    <redis>
      AuthComponent.java
      RedisComponent.java
    <reum>
      ReumController.java
      ReumService.java
    <storage>
      ObjectStorage.java
      ObjectStorageController.java
      ObjectStorageService.java

      
  <- config ->
    ChainedTxConfiguration.java
    DatabaseConfiguration.java
    InterceptorConfiguration.java
    RedisSessionConfiguration.java
    SwaggerConfiguration.java
    WebConfiguration.java
   
    
    
  <- exception ->
    ErrorHandleObject.java
    GlobalExceptionHandler.java
    
    
  <- interceptor ->
    SessionCheckInterceptor.java
    
    
  <- mapper.main ->
    CommonMapper.interface
    ReumMapper.interface
   
  <- service ->
    CarrotService.java(abstract class)
    LoginController.java
    LoginService.java
    
    
  <- user ->
    UserController.java
    UserService.java
    UserVO.java
    

Main -> resources
    
  <- mapper.xml ->
    commonMapper.xml
    reumMapper.xml
    
  application.yml
  log4jdbc-log4j2.properties
  logback-spring.xml

    
    
    **build.gradle**
    
