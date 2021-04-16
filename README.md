# api

### URLs
*/createTestUser*    
*/userReference (user handling)*     
*/feeds*  
*/contents*   
*/ratings*   
### Spring Boot Actuator
Add the dependency to the pom.xml
```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```
Rebuild and start project, following urls should work out of the box  
*curl http://localhost:8080/actuator*  
*curl http://localhost:8080/actuator/info*  
*curl http://localhost:8080/actuator/health*  
Info will be empty at the beginning, define entry in the applications property, for example
```
info.name= Test Spring Service
info.more.detail= This is a demo for Spring Actuator
```
