
                     Spring Security

        <dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>

There are a lot to know in Security but we are mainlly focusing on authentication 

1. Implement UserDetails interface :- Create a custom class and implement UserDetails interface. it is provided by Spring Security. 

2. Implement UserDetailsService interface :- Create a custom class as service and implement UserDetailsService interface. 
        it contains only one method to override 
           UserDetails loadUserByUsername(String userName)
                Get User using userName from database and store it inside CustomUserDetails class
           last return this CustomUserDetails

