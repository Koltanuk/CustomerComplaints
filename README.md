At this task I use MySQL DB, if you will use the same for testing please add:
"spring.datasource.url=jdbc:mysql://localhost:.../
spring.datasource.username=
spring.datasource.password= " 
in application.properties.

For DB creation you can use this script:
      CREATE TABLE complaints (
          id BINARY(16) NOT NULL PRIMARY KEY,
          user_id BINARY(16) NOT NULL,
          subject VARCHAR(255) NOT NULL,
          complaint VARCHAR(1000) NOT NULL,
          purchase_id BINARY(16)
      );

When conducting the tests, please be aware that I have used specific IDs that are present in my database. Make sure to take this into account during the testing process.      

In this code I assume that complaint is made by user, so we have correct userID always (and we will get the data from user url) so I didn't handle null situation in this case (not like in purchase, where null is possible).      
